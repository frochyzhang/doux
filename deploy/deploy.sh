#!/bin/bash

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
    esac
}

function db2Cmd() {
    echo "请输入sql脚本路径：\c"
    read -r sqlPath
    if [ ! -e "${sqlPath}" ]; then
        echo "sql脚本不存在，请重新输入"
    else
        echo "开始执行sql脚本"
        # 创建远程数据库映射
        db2 connect to "${database}" user "${user}" using "${password}"
        db2 -tvf "${sqlPath}"
        db2 quit
    fi
}

function mysqlCmd() {
    echo "请输入sql脚本路径：\c"
    read -r sqlPath
    if [ ! -e "${sqlPath}" ]; then
        echo "sql脚本不存在，请重新输入"
    else
      echo "开始执行sql脚本"
      mysql -h "${host}" -u "${user}" -P "${port}" -p "${password}" -D "${database}" < "${sqlPath}"
    fi
}

function nacosChange() {
    echo "开始执行nacos变更操作"

    read -p "请输入Nacos服务器地址：" nacos_server
    read -p "请输入nacos用户名: " nacos_user
    read -p "请输入nacos密码: " nacos_passwd

    nacos_server=${nacos_server:-10.100.79.102:8848}
    nacos_user=${nacos_user:-nacos}
    nacos_passwd=${nacos_passwd:-nacos}
    # 登录获取accessToken
    access_token=$(curl -X POST http://${nacos_server}/nacos/v1/auth/users/login -d "message=true&username=${nacos_user}&password=${nacos_passwd}" | grep -o '"accessToken":"[^"]*' | awk -F'"' '{print $4}')

    # 判断accessToken是否获取成功
    if [ -z "$access_token" ]; then
        echo "登录失败. Exiting."
        exit 1
    fi

    read -p "请输入命名空间ID（如果没有，请直接按回车）：" namespace_id
    read -p "请输入配置信息文件路径：" content_zip

    content_zip=${content_zip:-/Users/zhangyong/Downloads/MGM_GROUP.zip}

    if [ ! -f "$content_zip" ]; then
        echo "错误：配置信息文件不存在！"
        exit 1
    fi

    # 上传配置到Nacos
    response=`curl -X POST "http://${nacos_server}/nacos/v1/cs/configs?import=true&namespace=${namespace_id}&accessToken=${access_token}&username=${nacos_user}" \
         --form "policy=OVERWRITE" \
         --form "file=@${content_zip}" \
         --insecure`

    # 提取各个键值对并输出
    code=$(echo "$response" | grep -o '"code":[0-9]*' | sed 's/"code"://')
    message=$(echo "$response" | grep -o '"message":"[^"]*"' | sed 's/"message":"//;s/"$//')
    succCount=$(echo "$response" | grep -o '"succCount":[0-9]*' | sed 's/"succCount"://')
    unrecognizedCount=$(echo "$response" | grep -o '"unrecognizedCount":[0-9]*' | sed 's/"unrecognizedCount"://')
    skipCount=$(echo "$response" | grep -o '"skipCount":[0-9]*' | sed 's/"skipCount"://')
    unrecognizedData=$(echo "$response" | grep -o '"unrecognizedData":\[{"itemName":"[^"]*"}\]' | sed 's/"unrecognizedData":\[{"itemName":"//;s/"}\]//')

    # 输出解析结果
    echo "[**确认信息**]Code: $code"
    echo "[**确认信息**]导入结果: $message"
    echo "[**确认信息**]导入成功数: $succCount"
    echo "[**确认信息**]不识别的配置数: $unrecognizedCount"
    echo "[**确认信息**]跳过的配置数（需额外检查）: $skipCount"
    echo "[**确认信息**]不识别的配置文件名: $unrecognizedData"
    # 提示用户进行检查
    read -p "请检查输出结果，确认后按回车键退出。"
}

function addSSHKey() {
    echo "开始设置SSH免密登录..."

    # 检查本地是否已存在SSH密钥对
    if [ -f ~/.ssh/id_rsa ] || [ -f ~/.ssh/id_rsa.pub ]; then
        echo "本地已存在SSH密钥对，无需重新生成。"
    else
        # 生成SSH密钥对
        ssh-keygen -t rsa -N "" -f ~/.ssh/id_rsa
    fi

    # 读取服务器列表文件路径
    read -p "请输入服务器列表文件路径(请确保每行末尾都有换行符)：" server_list_file

    # 检查文件是否存在
    if [ ! -f "$server_list_file" ]; then
        echo "错误：服务器列表文件不存在！"
        exit 1
    fi

    success_count=0

    # 逐行读取服务器列表文件并进行处理
    while IFS= read -r line; do
        # 分割每行，提取服务器IP地址和用户名
        read -r target_ip target_user <<<"$line"

        # 将公钥拷贝到目标服务器的authorized_keys文件中
        ssh-copy-id -i ~/.ssh/id_rsa.pub "${target_user}@${target_ip}"

        # 输出成功信息
        echo "免密登录设置完成：${target_user}@${target_ip}"

        # 验证免密登录是否成功
        echo "正在验证免密登录..."
        ssh -o PasswordAuthentication=no "${target_user}@${target_ip}" exit
        if [ $? -eq 0 ]; then
            echo "免密登录验证成功！"
            ((success_count++))
        else
            echo "免密登录验证失败，请检查设置。"
        fi
    done <"$server_list_file"
    # 比对添加成功的数量和文件行数
    total_count=$(wc -l < "$server_list_file")

    echo "[**确认信息**]文件中共有 $((total_count)) 个服务器。成功设置免密登录的服务器数量为 $success_count"

    # 提示用户确认
    read -p "请确认上述信息是否正确，OK后按回车键继续。"
}

function uploadFiles() {
    echo "开始执行文件上传操作"

    # 读取待上传文件列表文件路径
    read -p "请输入待上传文件列表文件路径：" file_list_file

    # 检查文件是否存在
    if [ ! -f "$file_list_file" ]; then
        echo "错误：文件列表文件不存在！"
        exit 1
    fi

    # 初始化成功上传文件数量
    success_count=0

    # 使用while read循环结合文件描述符遍历文件内容
    while IFS= read -r line; do
        # 分割每行，提取本地文件路径、目标服务器信息和覆盖方式
        read -r local_path target_info overwrite <<<"$line"

        # 分割目标服务器信息，提取用户名、IP和目标路径
        read -r target_user target_ip target_path <<<$(echo "$target_info" | tr '@' ' ')

        # 使用ssh命令连接到目标服务器，检查远程文件是否存在
        ssh "${target_user}@${target_ip}" "[ -f '${target_path}' ]"

        # 检查ssh命令执行结果
        if [ $? -eq 0 ]; then
            # 文件存在，根据覆盖方式判断是否终止上传
            if [ "$overwrite" == "R" ]; then
                echo "目标服务器已存在相同路径的文件，覆盖上传文件：$local_path -> ${target_user}@${target_ip}:${target_path}"
            else
                echo "目标服务器已存在相同路径的文件，终止上传文件：$local_path -> ${target_user}@${target_ip}:${target_path}"
                continue
            fi
        else
            # 文件不存在，直接上传
            echo "开始上传文件：$local_path -> ${target_user}@${target_ip}:${target_path}"
        fi

        # 使用scp命令将文件上传到目标服务器的指定路径
        scp "$local_path" "${target_user}@${target_ip}:${target_path}"

        # 检查命令执行结果
        if [ $? -eq 0 ]; then
            # 输出成功信息
            echo "文件上传成功：$local_path -> ${target_user}@${target_ip}:${target_path}"
            ((success_count++))
        else
            echo "文件上传失败：$local_path -> ${target_user}@${target_ip}:${target_path}"
        fi
    done < "$file_list_file"

    # 输出上传结果
    total_count=$(wc -l < "$file_list_file")
    echo "[**确认信息**]文件中共有 $((total_count)) 个文件，共有 $success_count 个文件上传成功。"

    # 提示用户确认
    read -p "请确认上述信息是否正确，OK后按回车键继续。"
}


function restart() {
 echo "开始执行换包重启操作"
}

option=0
while [ $option != 5 ]; do
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
    echo "*  3. 执行换包重启操作                                  *"
    echo "*  4. 新增免密登录                                     *"
    echo "*  exit. 退出                                         *"
    echo "*                                                    *"
    echo "*  请输入选项编号：                                     *"
    echo "*                                                     *"
    echo "*******************************************************"
    read -r inputOption
    option=${inputOption}
    clear
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
      echo "执行换包重启操作"

      ;;
    4)
      echo "新增免密登录"
      addSSHKey
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
