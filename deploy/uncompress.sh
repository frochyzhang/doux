# 输入参数：${spring.application.name}-${version}-assembly.tar.gz Y/y表示是否要备份，D/d表示是否要删除apps下的应用文件夹
APP_FULL_NAME=$1

# Base Folder Path like "/folder/packages"
CURRENT_DIR=$(readlink -f "$0")
BASE_PACKAGE="${CURRENT_DIR%/*}"
# Shell Script file name after removing path like "start-yaml-validator.sh"
SHELL_SCRIPT_FILE_NAME=$(basename -- "$0")
# Shell Script file name after removing extension like "start-yaml-validator"
#SHELL_SCRIPT_FILE_NAME_WITHOUT_EXT="${SHELL_SCRIPT_FILE_NAME%.sh}"
# App name after removing start/stop strings like "yaml-validator"
APP_NAME=${SHELL_SCRIPT_FILE_NAME%-startup.sh}

APP_NAME_INCLUDE_VERSION=${APP_FULL_NAME%-assembly.tar.gz}
if [ ! -f "${BASE_PACKAGE}"/"${APP_FULL_NAME}" ]; then
    echo "Package does not exist!" 1>&2
    exit 1
fi
APP_NAME="${APP_NAME_INCLUDE_VERSION%-*-*}"
# 备份，当入参包含Y/y时备份，防止备份覆盖原稳定版本的备份可不选择备份
if [ "Y" == "$2" ] || [ "y" == "$2" ]; then
    BAK_APP_NAME_INCLUDE_JAR=$(cd "${BASE_PACKAGE}"/apps/"${APP_NAME}" || exit;ls "${APP_NAME}"*.jar)
    BAK_APP_NAME="${BAK_APP_NAME_INCLUDE_JAR%.jar}"
    mkdir -p "${BASE_PACKAGE}"/bak
    tar -zcvf "${BASE_PACKAGE}"/bak/"${BAK_APP_NAME}"-"$(date "+%Y%m%d")"-bak.tar.gz "${BASE_PACKAGE}"/bin/"${APP_NAME}" "${BASE_PACKAGE}"/apps/"${APP_NAME}"
    rm -rf "${BASE_PACKAGE}"/apps/"${APP_NAME}" "${BASE_PACKAGE:?}"/bin/"${APP_NAME}"
fi

# 仅删除apps和bin对应应用的文件夹
if [ "D" == "$2" ] || [ "d" == "$2" ]; then
    rm -rf "${BASE_PACKAGE}"/apps/"${APP_NAME}" "${BASE_PACKAGE:?}"/bin/"${APP_NAME}"
fi

# 换包
tar -zxvf "${BASE_PACKAGE}"/"${APP_FULL_NAME}" -C "${BASE_PACKAGE}"
mkdir -p "${BASE_PACKAGE}"/bin
mkdir -p "${BASE_PACKAGE}"/apps
mv "${BASE_PACKAGE}"/"${APP_NAME_INCLUDE_VERSION}"/bin/* "${BASE_PACKAGE}"/bin/
mv "${BASE_PACKAGE}"/"${APP_NAME_INCLUDE_VERSION}"/apps/* "${BASE_PACKAGE}"/apps/
rm -rf "${BASE_PACKAGE:?}"/"${APP_NAME_INCLUDE_VERSION}"

echo "${APP_NAME_INCLUDE_VERSION}解压完成"