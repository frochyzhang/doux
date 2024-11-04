#!/bin/bash

source "$(dirname "$(readlink -f "$0")")/common.sh"

function readNacosInfoFromFile() {
    read -p "请输入列表文件路径(请确保每行末尾都有换行符)：" nacos_file
    nacos_file=${nacos_file:-$current_dir/lst/nacos_file.lst}
    checkFileExists "$nacos_file"

    read -r nacos_server nacos_user nacos_passwd content_zip namespace_id < "$nacos_file"
    if [ -z "$nacos_server" ] || [ -z "$nacos_user" ] || [ -z "$nacos_passwd" ] || [ -z "$content_zip" ]; then
        echo "错误：文件格式不正确，必须包含 Nacos 服务器地址、用户名、密码、配置文件路径、命名空间ID"
        exit 1
    fi
}

function uploadContentZip() {
    nacos_server=${nacos_server:-10.100.79.102:8848}
    nacos_user=${nacos_user:-nacos}
    nacos_passwd=${nacos_passwd:-nacos}
    access_token=$(curl -X POST http://"${nacos_server}"/nacos/v1/auth/users/login -d "username=${nacos_user}&password=${nacos_passwd}" | grep -o '"accessToken":"[^"]*' | awk -F'"' '{print $4}')

    if [ -z "$access_token" ]; then
        echo "登录失败. Exiting."
        log "登录失败：Nacos服务器 ${nacos_server}"
        exit 1
    fi

    echo "导入配置文件 $content_zip 到命名空间 $namespace_id"
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
    log "Nacos变更操作：Code: $code，导入结果: $message，成功数: $succCount，不识别数: $unrecognizedCount，跳过数: $skipCount，不识别文件: $unrecognizedData"
}

function nacosChange() {
    echo "开始执行 Nacos 变更操作"

    read -p "请输入列表文件路径(请确保每行末尾都有换行符)：" nacos_file
    nacos_file=${nacos_file:-$current_dir/lst/nacos_file.lst}
    checkFileExists "$nacos_file"
    while IFS= read -r line; do
        read -r nacos_server nacos_user nacos_passwd content_zip namespace_id <<<"$line"
        if [ -z "$nacos_server" ] || [ -z "$nacos_user" ] || [ -z "$nacos_passwd" ] || [ -z "$content_zip" ]; then
            echo "错误：文件格式不正确，必须包含 Nacos 服务器地址、用户名、密码、配置文件路径、命名空间ID"
            exit 1
        fi
        checkFileExists "${content_zip}"
        uploadContentZip
    done <"$nacos_file"

    read -p "请检查输出结果，确认后按回车键退出。"
}