#!/bin/bash

SHELL_SCRIPT_FILE_NAME=$(basename -- "$0")
APP_NAME=${SHELL_SCRIPT_FILE_NAME%-check.sh}

PINF=`ps -eo pid,lstart,cmd | grep ${USER} | grep ${APP_NAME} | grep -v grep`

PID=`echo ${PINF} | awk '{print $1}'`

PTM=`echo ${PINF} | awk '{print $2,$3,$4,$5,$6}'`

PCNT=`echo ${PID} | wc -w`

if [ "$PCNT" -ne 1 ];then
  echo "!!!Warning!!!"
  echo "${APP_NAME}有问题啦，进程数居然不为1，当前进程数${PCNT}"
else
  echo
  echo "+===========================================================================+"
  printf '+ %-20s\t|%-5s\t|%-12s\t|%-33s+\n' "进程名" "进程号" "进程数量" "进程启动时间"
  echo "+-----------------------+-------+---------------+---------------------------+"
  printf '+ %-20s\t|%-5s\t|%-12s\t|%-27s+\n' "${APP_NAME}" "${PID}" "${PCNT}" "${PTM}"
  echo "+===========================================================================+"
  echo
fi