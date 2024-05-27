#!/bin/bash

source "$(dirname "$(realpath "$0")")/common.sh"

function addDatabaseInfo() {
    read -p "请输入主机: " host
    validateIpAddress "$host"
    read -p "请输入端口: " port
    validatePort "$port"
    read -p "请输入数据库名: " database
    read -p "请输入用户名: " user
    read -sp "请输入密码: " password
    echo
    read -p "请输入SQL文件路径: " sqlPath
    checkFileExists "$sqlPath"
    echo "$host $port $database $user $password" >> "$mysqlParams"
    echo "数据库信息已添加"
}

function selectDatabase() {
    mapfile -t databases < "$mysqlParams"
    echo "请选择数据库："
    for i in "${!databases[@]}"; do
        echo "$((i + 1)). ${databases[$i]}"
    done
    echo "$((i + 2)). 新增数据库信息"
    read -p "输入选择的编号: " choice
    if [ "$choice" -eq "$((i + 2))" ]; then
        addDatabaseInfo
        selectDatabase
    else
        selectedDb="${databases[$((choice - 1))]}"
    fi
}

function db2Cmd() {
    read -p "请输入db2脚本执行参数文件: " db2Params
    checkFileExists "$db2Params"
    while IFS= read -r line; do
        read -r database user password sqlPath <<<"$line"
        checkFileExists "${sqlPath}"
        echo "开始执行sql脚本"
        log "开始执行db2脚本：数据库 ${database}，用户 ${user}"
        db2 connect to "${database}" user "${user}" using "${password}"
        db2 -tvf "${sqlPath}"
        db2 quit
    done <"$db2Params"
}

function mysqlCmd() {
    selectDatabase
    read -p "你选择的是: $selectedDb，确认执行吗？(y/n): " confirm
    if [[ "$confirm" != "y" ]]; then
        echo "操作取消"
        exit 1
    fi
    read -p "请输入要执行的SQL文件路径: " sqlPath
    checkFileExists "$sqlPath"
    read -r host port database user password <<<"$selectedDb"
    echo "开始执行sql脚本"
    log "开始执行mysql脚本：主机 ${host}，端口 ${port}，数据库 ${database}，用户 ${user}"
    mysql -h "${host}" -u "${user}" -P "${port}" -p"${password}" "${database}" < "${sqlPath}"
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
