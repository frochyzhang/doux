#!/bin/bash
source "$(dirname "$(readlink -f "$0")")/common.sh"

function addSSHKey() {
    echo "开始设置SSH免密登录..."

    if [ -f ~/.ssh/id_rsa ] || [ -f ~/.ssh/id_rsa.pub ]; then
        echo "本地已存在SSH密钥对，无需重新生成。"
    else
        ssh-keygen -t rsa -N "" -f ~/.ssh/id_rsa
        log "生成新的SSH密钥对"
    fi

    read -p "请输入服务器列表文件路径(请确保每行末尾都有换行符)：" server_list_file
    server_list_file=${server_list_file:-$current_dir/lst/ssh-copy.lst}
    checkFileExists "$server_list_file"

    success_count=0

    while IFS= read -r line; do
        read -r target_ip target_user <<<"$line"
        validateIpAddress "$target_ip"
        ssh-copy-id -i ~/.ssh/id_rsa.pub "${target_user}@${target_ip}"
        log "设置免密登录：${target_user}@${target_ip}"

        echo "正在验证免密登录..."
        ssh -o PasswordAuthentication=no "${target_user}@${target_ip}" exit
        if [ $? -eq 0 ]; then
            echo "免密登录验证成功！"
            ((success_count++))
        else
            log "免密登录验证失败，请检查设置。"
            echo "免密登录验证失败，请检查设置。"
        fi
    done <"$server_list_file"

    total_count=$(wc -l < "$server_list_file")

    echo "[**确认信息**]文件中共有 $((total_count)) 个服务器。成功设置免密登录的服务器数量为 $success_count"
    log "[**确认信息**]文件中共有 $((total_count)) 个服务器。成功设置免密登录的服务器数量为 $success_count"
    read -p "请确认上述信息是否正确，OK后按回车键继续。"
}
