# 输入参数：备份包全名 ${spring.application.name}-${version}-yyyyMMdd-bak.tar.gz
BAK_PACKAGE_FULL_NAME=$1
if [ ! -f $HOME/bak/$BAK_PACKAGE_FULL_NAME ]; then
    echo "Bak package does not exist!" 1>&2
    exit 1
fi
APP_NAME_INCLUDE_VERSION_DATE="${BAK_PACKAGE_FULL_NAME%-bak.tar.gz}"
# ${spring.application.name}
APP_NAME="${APP_NAME_INCLUDE_VERSION_DATE%-*-*-*}"

# 解压备份包
mkdir -p $HOME/bak/$APP_NAME_INCLUDE_VERSION_DATE
tar -zxvf $HOME/bak/$BAK_PACKAGE_FULL_NAME -C $HOME/bak/$APP_NAME_INCLUDE_VERSION_DATE

# 删除有问题的启停脚本和jar包、配置文件
rm -r $HOME/bin/$APP_NAME $HOME/apps/$APP_NAME

# 移动jar包、配置文件以及启停脚本
mv $HOME/bak/$APP_NAME_INCLUDE_VERSION_DATE/bin/* $HOME/bin
mv $HOME/bak/$APP_NAME_INCLUDE_VERSION_DATE/apps/* $HOME/apps

# 删除解压的目录
rm -rf $HOME/bak/$APP_NAME_INCLUDE_VERSION_DATE