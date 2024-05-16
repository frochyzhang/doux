#!/bin/bash

# Base Folder Path like "/folder/packages"
CURRENT_DIR=$(readlink -f "$0")
BASE_PACKAGE="${CURRENT_DIR%/bin/*}"
# Shell Script file name after removing path like "start-yaml-validator.sh"
SHELL_SCRIPT_FILE_NAME=$(basename -- "$0")
APP_NAME=${SHELL_SCRIPT_FILE_NAME%-check.sh}

PID_PATH="$BASE_PACKAGE/pid/$APP_NAME/$APP_NAME.pid"
if [ -e "${PID_PATH}" ]; then
    PID=$(pgrep -F "${PID_PATH}")
    if [ -z "${PID}" ]; then
      echo "!!!Warning!!!"
      echo "${APP_NAME}有问题啦，进程数居然不为1，当前进程数0"
      exit 1
    else
      PINF=$(ps -eo pid,lstart,cmd | grep "${PID}" | grep "${USER}" | grep "${APP_NAME}" | grep -v $0 | grep -v grep)

      PTM=$(echo "${PINF}" | awk '{print $2,$3,$4,$5,$6}')

      PCNT=$(echo "${PID}" | wc -w)

      PHM=$(echo "${PINF}" | awk '{print $8,$9}')

      ACTIVE_PROFILE=$(echo "${PINF}" | awk '{print $10}')

      AP=${ACTIVE_PROFILE#*=}

      echo
      echo "+===================================================================================================================+"
      printf '+ %-20s\t|%-5s\t|%-18s\t|%-14s\t|%-12s\t|%-33s+\n' "进程名" "进程号" "堆内存" "ACTIVE_PROFILE" "进程数量" "进程启动时间"
      echo "+-----------------------+-------+-----------------------+---------------+---------------+---------------------------+"
      printf '+ %-20s\t|%-5s\t|%-18s\t|%-14s\t|%-12s\t|%-27s+\n' "${APP_NAME}" "${PID}" "${PHM}" "${AP}" "${PCNT}" "${PTM}"
      echo "+===================================================================================================================+"
      echo
    fi
else
  echo "Process Id FilePath($PID_PATH) Not found"
  echo "!!!Warning!!!"
  echo "${APP_NAME}有问题啦，进程数居然不为1，当前进程数0"
fi