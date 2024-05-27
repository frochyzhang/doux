#!/in/bash
source "$(dirname "$(realpath "$0")")/common.sh"

function restart() {
    echo "开始执行换包重启操作"

    # 提供应用名选项并选择
    selectAppName

    script_dir="$(dirname "$(realpath "$0")")"

    exec 3< "$script_dir/lst/app-info.lst"

    while read -u 3 -r user ip app_name; do
        if [[ "$app_name" != "$app_name_input" ]]; then
            continue
        fi
        # 查找最新的应用包命令
        FIND_LATEST_PACKAGE_CMD="ls -t ~/${app_name}*.tar.gz | head -n 1"

        # 获取最新的应用包的全路径
        LATEST_PACKAGE_PATH=$(ssh $user@$ip "$FIND_LATEST_PACKAGE_CMD")

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
