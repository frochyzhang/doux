#!/bin/bash


LOG_FILE="deploy.log"

# 通用函数
function log() {
    local message=$1
    echo "$(date +"%Y-%m-%d %H:%M:%S") : $message" >> $LOG_FILE
}

function checkFileExists() {
    local file_path=$1
    if [ ! -f "$file_path" ]; then
        echo "错误：文件 ${file_path} 不存在！"
        log "错误：文件 ${file_path} 不存在！"
        exit 1
    fi
}

function validateIpAddress() {
    local ip=$1
    if [[ ! $ip =~ ^[0-9]+\.[0-9]+\.[0-9]+\.[0-9]+$ ]]; then
        echo "错误：无效的IP地址 ${ip}"
        log "错误：无效的IP地址 ${ip}"
        exit 1
    fi
}

function validatePort() {
    local port=$1
    if [[ ! $port =~ ^[0-9]+$ ]] || [ $port -le 0 ] || [ $port -gt 65535 ]; then
        echo "错误：无效的端口号 ${port}"
        log "错误：无效的端口号 ${port}"
        exit 1
    fi
}

# 环境依赖检查
function checkDependencies() {
    local dependencies=("ssh" "scp" "curl" "awk")
    for cmd in "${dependencies[@]}"; do
        if ! command -v $cmd &> /dev/null; then
            echo "错误：命令 ${cmd} 不存在，请安装后重新开始"
            log "错误：命令 ${cmd} 不存在"
            exit 1
        fi
    done
}

checkDependencies

current_dir="$(dirname "$(readlink -f "$0")")"

# 读取app-info.lst文件并提取唯一应用程序名
function getAppNames() {
    script_dir="$(dirname "$(readlink -f "$0")")"
    declare -A app_names  # 声明一个关联数组
    app_name_list=()

    while read -r user ip app_name node; do
        if [[ -z "${app_names[$app_name]}" ]]; then
            app_names[$app_name]=1
            app_name_list+=("$app_name")
        fi
    done < "$script_dir/lst/app-info.lst"

    # 如果你想在函数外使用 app_name_list，需要显式地返回它
    echo "${app_name_list[@]}"
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
