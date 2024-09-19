#!/bin/bash
source "$(dirname "$(realpath "$0")")/common.sh"

function stopAllByNode() {
    local node_number="$1"
    script_dir="$(dirname "$(realpath "$0")")"
    exec 3< "$script_dir/lst/app-info.lst"
    while read -u 3 -r user ip app_name node; do
        if [[ -z "$node_number" || "$node" == "$node_number" ]]; then
            echo "正在停止节点 $node_number 上的所有进程：$user@$ip $app_name"
            ssh -t $user@$ip "~/bin/${app_name}/${app_name}-shutdown.sh"
        fi
    done
    exec 3<&-
}

function checkAllByNode() {
    local node_number="$1"
    script_dir="$(dirname "$(realpath "$0")")"
    exec 3< "$script_dir/lst/app-info.lst"
    problem_count=0
    declare -a problem_apps  # 用于存储有问题的进程名

    while read -u 3 -r user ip app_name node; do
        if [[ -z "$node_number" || "$node" == "$node_number" ]]; then
            echo "正在检查节点 $node_number 上的所有进程：$user@$ip $app_name"
            output=$(ssh -t $user@$ip "~/bin/${app_name}/${app_name}-check.sh")

            # 检查输出中是否包含“有问题啦”或“进程数居然不为1”
            if [[ $output == *"有问题啦"* || $output == *"进程数居然不为1"* ]]; then
                ((problem_count++))
                problem_apps+=("$app_name")  # 将有问题的进程名添加到数组中
            else
                echo "$output"
            fi
        fi
    done
    exec 3<&-

    echo "检查完成，有 $problem_count 个进程检查存在问题。"
    if [ $problem_count -gt 0 ]; then
        echo "以下进程未启动成功："
        for app in "${problem_apps[@]}"; do
            echo "- $app"
        done
    fi

    read -p "请确认上述信息是否正确，OK后按回车键继续。"
}


function startAllByNode() {
    local node_number="$1"
    script_dir="$(dirname "$(realpath "$0")")"
    exec 3< "$script_dir/lst/app-info.lst"
    while read -u 3 -r user ip app_name node; do
        if [[ -z "$node_number" || "$node" == "$node_number" ]]; then
            echo "正在启动节点 $node_number 上的所有进程：$user@$ip $app_name"
            # 执行 startup 脚本
            startup_params=""
            # 判断是否有参数，必须要输入启动脚本参数，用while循环
            while [ -z "$startup_params" ]; do
                read -p "请输入 $app_name-startup.sh 在 $ip 上的参数: " startup_params
            done
            ssh $user@$ip "source /etc/profile; source ~/.bash_profile;sh ~/bin/${app_name}/${app_name}-startup.sh $startup_params"
        fi
    done
    exec 3<&-
    read -p "请确认上述信息是否正确，OK后按回车键继续。"
}

function restart() {
    echo "开始执行换包重启操作"

    # 提供应用名选项并选择
    selectAppName

    script_dir="$(dirname "$(realpath "$0")")"

    exec 3< "$script_dir/lst/app-info.lst"

    while read -u 3 -r user ip app_name node; do
        if [[ "$app_name" != "$app_name_input" ]]; then
            continue
        fi
        # 查找最新的应用包命令
        FIND_LATEST_PACKAGE_CMD="ls -t ~/${app_name}*.tar.gz | head -n 1"

        # 获取最新的应用包的全路径
        LATEST_PACKAGE_PATH=$(ssh -t $user@$ip "$FIND_LATEST_PACKAGE_CMD")

        if [[ -z "$LATEST_PACKAGE_PATH" ]]; then
            log "没有找到 $app_name 的应用包在 $ip 的用户主目录下"
            echo "没有找到 $app_name 的应用包在 $ip 的用户主目录下"
            exit 1
        fi

        # 提取包名
        LATEST_PACKAGE=$(basename "$LATEST_PACKAGE_PATH")

        echo "在 $ip 上找到最新的包：$LATEST_PACKAGE"

        # 询问用户是否需要备份
        read -p "你是否需要备份 $app_name 的当前版本？(y/d): " backup_answer

        # 执行解压命令
        ssh -t $user@$ip "sh ~/uncompress.sh $LATEST_PACKAGE $backup_answer"

        # 判断是否有额外操作
        while true; do
            read -p "你是否有其他命令需要在 $ip 上执行？(yes/no): " answer
            if [[ "$answer" == "yes" ]]; then
                read -p "请输入命令: " extra_command
                ssh -t $user@$ip "$extra_command"
            elif [[ "$answer" == "no" ]]; then
                break
            else
                echo "请输入 yes 或 no。"
            fi
        done

        # 执行 shutdown 脚本
        ssh -t $user@$ip "~/bin/${app_name}/${app_name}-shutdown.sh"

        # 执行 startup 脚本
        startup_params=""
        # 判断是否有参数，必须要输入启动脚本参数，用while循环
        while [ -z "$startup_params" ]; do
            read -p "请输入 $app_name-startup.sh 在 $ip 上的参数: " startup_params
        done
        ssh $user@$ip "source /etc/profile; source ~/.bash_profile;sh ~/bin/${app_name}/${app_name}-startup.sh $startup_params"
        # 询问用户是否需要查看日志
        while true; do
            read -p "你是否需要查看日志文件？(yes/no): " log_answer
            if [[ "$log_answer" == "yes" ]]; then
                echo "按 Ctrl+C 停止查看日志"
                ssh -t $user@$ip "tail -f ~/logs/${app_name}/${app_name}.log"
                break
            elif [[ "$log_answer" == "no" ]]; then
                break
            else
                echo "请输入 yes 或 no。"
            fi
        done
    # 检查启动脚本启动的进程信息
    echo "正在检查启动脚本启动的进程信息..."
    ssh -t $user@$ip "~/bin/${app_name}/${app_name}-check.sh"
    read -p "请确认上述信息是否正确，OK后按回车键继续。"
    done
}