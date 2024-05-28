#!/bin/bash

source "$(dirname "$(realpath "$0")")/common.sh"
source "$(dirname "$(realpath "$0")")/db-change.sh"
source "$(dirname "$(realpath "$0")")/ssh-copy.sh"
source "$(dirname "$(realpath "$0")")/package-upload.sh"
source "$(dirname "$(realpath "$0")")/app-restart.sh"
source "$(dirname "$(realpath "$0")")/nacos-change.sh"


# 添加主菜单逻辑
option=0
while [ "$option" != "exit" ]; do
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
    echo "*  3. 新增免密登录                                     *"
    echo "*  4. 执行文件上传                                     *"
    echo "*  5. 执行换包重启操作                                  *"
    echo "*  exit. 退出                                         *"
    echo "*                                                     *"
    echo "*******************************************************"
    read -p "请输入选项编号： " inputOption
    option=${inputOption}

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
      echo "新增免密登录"
      addSSHKey
      ;;
    4)
      echo "执行文件上传"
      uploadFiles
      ;;
    5)
      echo "执行换包重启操作"
      echo "请选择操作："
      echo "1. 根据节点编号停止所有进程"
      echo "2. 换包重启操作"
      echo "3. 根据节点编号检查所有进程"
      read -p "请输入选项 (1/2/3): " option

      case "$option" in
          1)
              read -p "请输入节点编号: " node_number
              stopAllByNode "$node_number"
              ;;
          2)
              restart
              ;;
          3)
              read -p "请输入节点编号: " node_number
              checkAllByNode "$node_number"
              ;;
          *)
              echo "无效的选项"
              exit 1
              ;;
      esac
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
