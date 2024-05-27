#!/bin/bash
source "$(dirname "$(realpath "$0")")/common.sh"

function uploadFiles() {
    echo "开始执行文件上传操作"

    read -p "请输入待上传文件列表文件路径：" file_list_file
    file_list_file=${file_list_file:-$current_dir/lst/file-trans.lst}
    checkFileExists "$file_list_file"

    success_count=0

    while IFS= read -r line; do
        read -r local_path target_info <<<"$line"

        log "开始上传文件：$local_path -> ${target_info}"
        echo "开始上传文件：$local_path -> ${target_info}"

        scp "$local_path" "${target_info}"

        if [ $? -eq 0 ]; then
            echo "文件上传成功：$local_path -> ${target_info}"
            ((success_count++))
        else
            log "文件上传失败：$local_path -> ${target_info}"
            echo "文件上传失败：$local_path -> ${target_info}"
        fi
    done < "$file_list_file"

    total_count=$(wc -l < "$file_list_file")
    echo "[**确认信息**]文件中共有 $((total_count)) 个文件，共有 $success_count 个文件上传成功。"
    log "[**确认信息**]文件中共有 $((total_count)) 个文件，共有 $success_count 个文件上传成功。"
    read -p "请确认上述信息是否正确，OK后按回车键继续。"
}
