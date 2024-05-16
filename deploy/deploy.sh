#!/bin/bash

current_dir=$(dirname "$(realpath "$0")")

# 读取app-info.lst文件并提取唯一应用程序名
function getAppNames() {
    script_dir="$(dirname "$(realpath "$0")")"
    declare -A app_names
    app_name_list=()

    while read -r user ip app_name; do
        if [[ -z "${app_names[$app_name]}" ]]; then
            app_names[$app_name]=1
            app_name_list+=("$app_name")
        fi
    done < "$script_dir/app-info.lst"
}

# 提供选择应用程序名的菜单
function selectAppName() {
    getAppNames

    echo "请选择应用包名称："
    for i in "${!app_name_list[@]}"; do
        echo "$((i + 1)). ${app_name_list[$i]}"
    done

    while true; do
        read -p "请输入序号: " app_index
        if [[ "$app_index" =~ ^[0-9]+$ ]] && ((app_index >= 1 && app_index <= ${#app_name_list[@]})); then
            app_name_input="${app_name_list[$((app_index - 1))]}"
            break
        else
            echo "无效的序号，请重新输入。"
        fi
    done
}

function db2Cmd() {
    read -p "请输入db2脚本执行参数文件: " db2Params
    while IFS= read -r line; do
        read -r database user password sqlPath <<<"$line"
        if [ ! -e "${sqlPath}" ]; then
            echo "sql脚本不存在，${sqlPath}"
        else
            echo "开始执行sql脚本"
            db2 connect to "${database}" user "${user}" using "${password}"
            db2 -tvf "${sqlPath}"
            db2 quit
        fi
    done <"$db2Params"
}

function mysqlCmd() {
    read -p "请输入mysql脚本执行参数文件: " mysqlParams
    while IFS= read -r line; do
        read -r host port database user password sqlPath <<<"$line"
        if [ ! -e "${sqlPath}" ]; then
            echo "sql脚本不存在，${sqlPath}"
        else
          echo "开始执行sql脚本"
          mysql -h "${host}" -u "${user}" -P "${port}" -p"${password}" "${database}" < "${sqlPath}"
        fi
    done <"$mysqlParams"
}

function dbChange() {
    echo "开始执行数据库变更操作"
    case $1 in
    db2)
      if [ -z "$(which db2)" ]; then
          echo "db2命令不存在，请安装后重新开始"
          exit 1
      fi
      db2Cmd
      ;;
    mysql)
      if [ -z "$(which mysql)" ]; then
          echo "mysql命令不存在，请安装后重新开始"
          exit 1
      fi
      mysqlCmd
      ;;
    *)
      echo "无效的数据库类型！"
      exit 1
      ;;
    esac
}

function nacosChange() {
    echo "开始执行nacos变更操作"

    read -p "请输入Nacos服务器地址：" nacos_server
    read -p "请输入nacos用户名: " nacos_user
    read -p "请输入nacos密码: " nacos_passwd

    nacos_server=${nacos_server:-10.100.79.102:8848}
    nacos_user=${nacos_user:-nacos}
    nacos_passwd=${nacos_passwd:-nacos}
    access_token=$(curl -X POST http://"${nacos_server}"/nacos/v1/auth/users/login -d "username=${nacos_user}&password=${nacos_passwd}" | grep -o '"accessToken":"[^"]*' | awk -F'"' '{print $4}')

    if [ -z "$access_token" ]; then
        echo "登录失败. Exiting."
        exit 1
    fi

    read -p "请输入命名空间ID（如果没有，请直接按回车）：" namespace_id
    read -p "请输入配置信息文件路径：" content_zip

    if [ ! -f "$content_zip" ]; then
        echo "错误：配置信息文件不存在！"
        exit 1
    fi

    response=$(curl -X POST "http://${nacos_server}/nacos/v1/cs/configs?import=true&namespace=${namespace_id}&accessToken=${access_token}&username=${nacos_user}" \
         --form "policy=OVERWRITE" \
         --form "file=@${content_zip}" \
         --insecure)

    code=$(echo "$response" | grep -o '"code":[0-9]*' | sed 's/"code"://')
    message=$(echo "$response" | grep -o '"message":"[^"]*"' | sed 's/"message":"//;s/"$//')
    succCount=$(echo "$response" | grep -o '"succCount":[0-9]*' | sed 's/"succCount"://')
    unrecognizedCount=$(echo "$response" | grep -o '"unrecognizedCount":[0-9]*' | sed 's/"unrecognizedCount"://')
    skipCount=$(echo "$response" | grep -o '"skipCount":[0-9]*' | sed 's/"skipCount"://')
    unrecognizedData=$(echo "$response" | grep -o '"unrecognizedData":\[{"itemName":"[^"]*"}\]' | sed 's/"unrecognizedData":\[{"itemName":"//;s/"}\]//')

    echo "[**确认信息**]Code: $code"
    echo "[**确认信息**]导入结果: $message"
    echo "[**确认信息**]导入成功数: $succCount"
    echo "[**确认信息**]不识别的配置数: $unrecognizedCount"
    echo "[**确认信息**]跳过的配置数（需额外检查）: $skipCount"
    echo "[**确认信息**]不识别的配置文件名: $unrecognizedData"
    read -p "请检查输出结果，确认后按回车键退出。"
}

function addSSHKey() {
    echo "开始设置SSH免密登录..."

    if [ -f ~/.ssh/id_rsa ] || [ -f ~/.ssh/id_rsa.pub ]; then
        echo "本地已存在SSH密钥对，无需重新生成。"
    else
        ssh-keygen -t rsa -N "" -f ~/.ssh/id_rsa
    fi

    read -p "请输入服务器列表文件路径(请确保每行末尾都有换行符)：" server_list_file
    server_list_file=${server_list_file:-$current_dir/ssh-copy.lst}

    if [ ! -f "$server_list_file" ]; then
        echo "错误：服务器列表文件不存在！"
        exit 1
    fi

    success_count=0

    while IFS= read -r line; do
        read -r target_ip target_user <<<"$line"

        ssh-copy-id -i ~/.ssh/id_rsa.pub "${target_user}@${target_ip}"

        echo "免密登录设置完成：${target_user}@${target_ip}"

        echo "正在验证免密登录..."
        ssh -o PasswordAuthentication=no "${target_user}@${target_ip}" exit
        if [ $? -eq 0 ]; then
            echo "免密登录验证成功！"
            ((success_count++))
        else
            echo "免密登录验证失败，请检查设置。"
        fi
    done <"$server_list_file"

    total_count=$(wc -l < "$server_list_file")

    echo "[**确认信息**]文件中共有 $((total_count)) 个服务器。成功设置免密登录的服务器数量为 $success_count"
    read -p "请确认上述信息是否正确，OK后按回车键继续。"
}

function uploadFiles() {
    echo "开始执行文件上传操作"

    read -p "请输入待上传文件列表文件路径：" file_list_file
    file_list_file=${file_list_file:-$current_dir/file-trans.lst}

    if [ ! -f "$file_list_file" ]; then
        echo "错误：文件列表文件不存在！"
        exit 1
    fi

    success_count=0

    while IFS= read -r line; do
        read -r local_path target_info <<<"$line"

        echo "开始上传文件：$local_path -> ${target_info}"

        scp "$local_path" "${target_info}"

        if [ $? -eq 0 ]; then
            echo "文件上传成功：$local_path -> ${target_info}"
            ((success_count++))
        else
            echo "文件上传失败：$local_path -> ${target_info}"
        fi
    done < "$file_list_file"

    total_count=$(wc -l < "$file_list_file")
    echo "[**确认信息**]文件中共有 $((total_count)) 个文件，共有 $success_count 个文件上传成功。"
    read -p "请确认上述信息是否正确，OK后按回车键继续。"
}

function restart() {
    echo "开始执行换包重启操作"

    # 提供应用名选项并选择
    selectAppName

    script_dir="$(dirname "$(realpath "$0")")"

    exec 3< "$script_dir/app-info.lst"

    while read -u 3 -r user ip app_name; do
        if [[ "$app_name" != "$app_name_input" ]]; then
            continue
        fi
        # 查找最新的应用包命令
        FIND_LATEST_PACKAGE_CMD="ls -t ~/${app_name}*.tar.gz | head -n 1"

        # 获取最新的应用包的全路径
        LATEST_PACKAGE_PATH=$(ssh $user@$ip "$FIND_LATEST_PACKAGE_CMD")

        if [[ -z "$LATEST_PACKAGE_PATH" ]]; then
            echo "没有找到 $app_name 的应用包在 $ip 的用户主目录下"
            exit 1
        fi

        # 提取包名
        LATEST_PACKAGE=$(basename "$LATEST_PACKAGE_PATH")

        echo "在 $ip 上找到最新的包：$LATEST_PACKAGE"

        # 询问用户是否需要备份
        read -p "你是否需要备份 $app_name 的当前版本？(y/d): " backup_answer

        # 执行解压命令
        ssh $user@$ip "sh ~/uncompress.sh $LATEST_PACKAGE $backup_answer"

        # 判断是否有额外操作
        while true; do
            read -p "你是否有其他命令需要在 $ip 上执行？(yes/no): " answer
            if [[ "$answer" == "yes" ]]; then
                read -p "请输入命令: " extra_command
                ssh $user@$ip "$extra_command"
            elif [[ "$answer" == "no" ]]; then
                break
            else
                echo "请输入 yes 或 no。"
            fi
        done

        # 执行 shutdown 脚本
        ssh $user@$ip "~/bin/${app_name}/${app_name}-shutdown.sh"

        # 执行 startup 脚本
        startup_params=""
        # 判断是否有参数，必须要输入启动脚本参数，用while循环
        while [ -z "$startup_params" ]; do
            read -p "请输入 $app_name-startup.sh 在 $ip 上的参数: " startup_params
        done
        ssh $user@$ip "~/bin/${app_name}/${app_name}-startup.sh $startup_params"
        # 检查启动脚本启动的进程信息
        echo "正在检查启动脚本启动的进程信息..."
        ssh $user@$ip "~/bin/${app_name}/${app_name}-check.sh"

        # 询问用户是否需要查看日志
        while true; do
            read -p "你是否需要查看日志文件？(yes/no): " log_answer
            if [[ "$log_answer" == "yes" ]]; then
                echo "按 Ctrl+C 停止查看日志"
                ssh $user@$ip "tail -f ~/logs/${app_name}/${app_name}.log"
                break
            elif [[ "$log_answer" == "no" ]]; then
                break
            else
                echo "请输入 yes 或 no。"
            fi
        done
    done
}

# 添加主菜单逻辑
option=0
while [ "$option" != "exit" ]; do
    echo "*******************************************************"
    echo "*                                                     *"
    echo "*             欢迎使用变更步骤选择器                      *"
    echo "*                                                     *"
    echo "*******************************************************"
    echo "*                                                     *"
    echo "*  请选择需要执行的变更步骤：                             *"
    echo "*                                                     *"
    echo "*  1. 执行数据库操作                                    *"
    echo "*  2. 执行nacos配置更新操作                             *"
    echo "*  3. 新增免密登录                                     *"
    echo "*  4. 执行文件上传                                     *"
    echo "*  5. 执行换包重启操作                                  *"
    echo "*  exit. 退出                                         *"
    echo "*                                                     *"
    echo "*******************************************************"
    read -p "请输入选项编号： " inputOption
    option=${inputOption}

    case $inputOption in
    1)
      echo "请输入数据库类型，db2 or mysql: \c"
      read -r dbType
      dbChange "${dbType}"
      ;;
    2)
      echo "执行nacos配置更新操作"
      nacosChange
      ;;
    3)
      echo "新增免密登录"
      addSSHKey
      ;;
    4)
      echo "执行文件上传"
      uploadFiles
      ;;
    5)
      echo "执行换包重启操作"
      restart
      ;;
    exit)
      echo "感谢使用，再见！"
      exit 0
      ;;
    *)
      echo "错误：请输入有效的选项编号！"
      ;;
    esac
done
