# 输入参数：${spring.applicaiton.name}-${version} Y/y表示是否要备份，D/d表示是否要删除apps下的应用文件夹
APP_NAME_INCLUDE_VERSION=$1
APP_NAME="${APP_NAME_INCLUDE_VERSION%-*-*}"

# 备份，当入参包含Y/y时备份
if [ "Y" == "$2" -o "y" == "$2" ]; then
    tar -zcvf $APP_NAME_INCLUDE_VERSION-$(date "+%Y%m%d")-bak.tar.gz $HOME/bin $HOME/apps
    rm -rf $HOME/apps $HOME/bin
fi

# 删除apps下对应应用的文件夹
if [ "D" == "$2" -o "d" == "$2" ]; then
    rm -rf $HOME/apps/$APP_NAME
fi

# 换包
tar -zxvf $APP_NAME_INCLUDE_VERSION-assembly.tar.gz -C $HOME
mkdir -p $HOME/bin/
mkdir -p $HOME/apps/
mv $HOME/$APP_NAME_INCLUDE_VERSION/bin/* $HOME/bin/
mv $HOME/$APP_NAME_INCLUDE_VERSION/apps/* $HOME/apps/
rm -rf $APP_NAME_INCLUDE_VERSION

echo "$APP_NAME_INCLUDE_VERSION解压完成"
