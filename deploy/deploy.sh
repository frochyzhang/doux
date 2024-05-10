#!/bin/bash

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
    esac
}

function db2Cmd() {
    echo "请输入sql脚本路径：\c"
    read -r sqlPath
    if [ ! -e "${sqlPath}" ]; then
        echo "sql脚本不存在，请重新输入"
    else
        echo "开始执行sql脚本"
        # 创建远程数据库映射
        db2 connect to "${database}" user "${user}" using "${password}"
        db2 -tvf "${sqlPath}"
        db2 quit
    fi
}

function mysqlCmd() {
    echo "请输入sql脚本路径：\c"
    read -r sqlPath
    if [ ! -e "${sqlPath}" ]; then
        echo "sql脚本不存在，请重新输入"
    else
      echo "开始执行sql脚本"
      mysql -h "${host}" -u "${user}" -P "${port}" -p "${password}" -D "${database}" < "${sqlPath}"
    fi
}

function nacosChange() {
    echo "开始执行nacos变更操作"
    read -p "请输入Nacos服务器地址：" nacos_server
    read -p "请输入命名空间ID（如果没有，请直接按回车）：" namespace_id
    read -p "请输入配置信息文件路径：" content_file

    nacos_server=${nacos_server:-10.100.79.102:8848}
    content_file=${content_file:-/Users/zhangyong/Desktop/dev/deploy/abiip-param-config}

    if [ ! -f "$content_file" ]; then
        echo "错误：配置信息文件不存在！"
        return 1
    fi

    read -p "请输入Data ID（如果文件名和dataId保持一致可不填写）：" data_id
    read -p "请输入Group（默认为DEFAULT_GROUP）：" group
    group=${group:-DEFAULT_GROUP}

    dataId=${data_id:-$(basename "$path")}

    # 读取YAML文件内容
    content=$(cat "$content_file")
    # 对内容进行处理，将换行符替换为 URL 编码的换行符
    encoded_content=$(echo "$content" | awk '{printf "%s\\n", $0}' | sed 's/ /%20/g')

    curl_cmd="curl -X POST 'http://${nacos_server}/nacos/v1/cs/configs?dataId=${data_id}&group=${group}&content=${content}'"
    if [ -n "$namespace_id" ]; then
        curl_cmd="${curl_cmd}&tenant=${namespace_id}"
    fi

    echo "执行Nacos配置更新操作..."
    echo "使用命令：$curl_cmd"
    eval "$curl_cmd"
}

function restart() {
 echo "开始执行换包重启操作"
}

option=0
while [ $option != 5 ]; do
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
    echo "*  3. 执行换包重启操作                                  *"
    echo "*  exit. 退出                                         *"
    echo "*                                                    *"
    echo "*  请输入选项编号：                                     *"
    echo "*                                                     *"
    echo "*******************************************************"
    read -r inputOption
    option=${inputOption}
    clear
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
      echo "执行换包重启操作"
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

curl -X POST http://10.100.79.102:8848/nacos/v1/auth/users/login -d "message=true&username=nacos&password=nacos"
curl --location --request POST http://10.100.79.102:8848/nacos/v1/cs/configs?tenant=iasp-dev&accessToken=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJuYWNvcyIsImV4cCI6MTcxNTM4NzYwOH0.YrrqMJH3nLOOVtsa2yNNiz1Hcg-OZ1yGmCCe23TBPxY
&username=nacos&import=true&namespace=iasp-dev&policy=REPLACE&dataId=abiip-ddd' --form 'file=@"/Users/zhangyong/Downloads/nacos_config_export_20240511033018.zip"
