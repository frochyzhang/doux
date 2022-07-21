# 输入参数：${spring.applicaiton.name}-${version} Y/y表示是否要备份，D/d表示是否要删除apps下的应用文件夹
APP_NAME_INCLUDE_VERSION=$1
if [ ! -f $APP_NAME_INCLUDE_VERSION-assembly.tar.gz ]; then
    echo "Package does not exist!" 1>&2
    exit 1
fi
APP_NAME="${APP_NAME_INCLUDE_VERSION%-*-*}"

# 备份，当入参包含Y/y时备份，防止备份覆盖原稳定版本的备份可不选择备份
if [ "Y" == "$2" -o "y" == "$2" ]; then
    mkdir -p $HOME/bak
    tar -zcvf $HOME/bak/$APP_NAME_INCLUDE_VERSION-$(date "+%Y%m%d")-bak.tar.gz bin/$APP_NAME apps/$APP_NAME
    rm -rf $HOME/apps/$APP_NAME $HOME/bin/$APP_NAME
fi

# 仅删除apps和bin对应应用的文件夹
if [ "D" == "$2" -o "d" == "$2" ]; then
    rm -rf $HOME/apps/$APP_NAME $HOME/bin/$APP_NAME
fi

# 换包
tar -zxvf $APP_NAME_INCLUDE_VERSION-assembly.tar.gz -C $HOME
mkdir -p $HOME/bin
mkdir -p $HOME/apps
mv $HOME/$APP_NAME_INCLUDE_VERSION/bin/* $HOME/bin/
mv $HOME/$APP_NAME_INCLUDE_VERSION/apps/* $HOME/apps/
rm -rf $APP_NAME_INCLUDE_VERSION

echo "$APP_NAME_INCLUDE_VERSION解压完成"