# 输入参数：${spring.application.name}-${version}-assembly.tar.gz Y/y表示是否要备份，D/d表示是否要删除apps下的应用文件夹
APP_FULL_NAME=$1
APP_NAME_INCLUDE_VERSION=${APP_FULL_NAME%-assembly.tar.gz}
if [ ! -f "${HOME}/${APP_FULL_NAME}" ]; then
    echo "Package does not exist!" 1>&2
    exit 1
fi
APP_NAME="${APP_NAME_INCLUDE_VERSION%-*-*}"
# 备份，当入参包含Y/y时备份，防止备份覆盖原稳定版本的备份可不选择备份
if [ "Y" == "$2" ] || [ "y" == "$2" ]; then
    BAK_APP_NAME_INCLUDE_JAR=$(cd "${HOME}/apps/${APP_NAME}" || exit;ls "${APP_NAME}*.jar")
    BAK_APP_NAME="${BAK_APP_NAME_INCLUDE_JAR%.jar}"
    mkdir -p "${HOME}/bak"
    tar -zcvf "${HOME}/bak/${BAK_APP_NAME}-$(date "+%Y%m%d")-bak.tar.gz" "${HOME}/bin/${APP_NAME} ${HOME}/apps/${APP_NAME}"
    rm -rf "${HOME}/apps/${APP_NAME}" "${HOME:?}/bin/${APP_NAME}"
fi

# 仅删除apps和bin对应应用的文件夹
if [ "D" == "$2" ] || [ "d" == "$2" ]; then
    rm -rf "${HOME}/apps/${APP_NAME}" "${HOME:?}/bin/${APP_NAME}"
fi

# 换包
tar -zxvf "${HOME}/${APP_FULL_NAME}" -C "${HOME}"
mkdir -p "${HOME}/bin"
mkdir -p "${HOME}/apps"
mv "${HOME}"/"${APP_NAME_INCLUDE_VERSION}"/bin/* "${HOME}/bin/"
mv "${HOME}"/"${APP_NAME_INCLUDE_VERSION}"/apps/* "${HOME}/apps/"
rm -rf "${HOME:?}/${APP_NAME_INCLUDE_VERSION}"

echo "${APP_NAME_INCLUDE_VERSION}解压完成"