# 输入参数：${spring.applicaiton.name}-${version} Y/y或其他任何字符
APP_NAME_INCLUDE_VERSION=$1
APP_NAME="${APP_NAME_INCLUDE_VERSION%-*-*}"

# 备份，当入参包含Y/y时备份
if [ "Y" == "$2" -o "y" == "$2" ]; then
    tar -zcvf $APP_NAME_INCLUDE_VERSION-$(date "+%Y%m%d")-bak.tar.gz $HOME/bin $HOME/apps
    rm -rf ./apps ./bin
fi

# 换包
tar -zxvf $APP_NAME_INCLUDE_VERSION-assembly.tar.gz -C $HOME
mkdir -p $HOME/bin/
mkdir -p $HOME/apps/
mv $HOME/$APP_NAME_INCLUDE_VERSION/bin/* $HOME/bin/
mv $HOME/$APP_NAME_INCLUDE_VERSION/apps/* $HOME/apps/
rm -rf $APP_NAME_INCLUDE_VERSION

echo "$APP_NAME_INCLUDE_VERSION解压完成"