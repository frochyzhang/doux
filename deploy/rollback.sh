# 输入参数：备份包全名 ${spring.application.name}-${version}-yyyyMMdd-bak.tar.gz
BAK_PACKAGE_FULL_NAME=$1

# Base Folder Path like "/folder/packages"
CURRENT_DIR=$(readlink -f "$0")
BASE_PACKAGE="${CURRENT_DIR%/*}"

if [ ! -f "${BASE_PACKAGE}"/bak/"${BAK_PACKAGE_FULL_NAME}" ]; then
    echo "Bak package does not exist!" 1>&2
    exit 1
fi
APP_NAME_INCLUDE_VERSION_DATE="${BAK_PACKAGE_FULL_NAME%-bak.tar.gz}"
# ${spring.application.name}
APP_NAME="${APP_NAME_INCLUDE_VERSION_DATE%-*-*-*}"

# 删除有问题的启停脚本和jar包、配置文件
rm -rf "${BASE_PACKAGE:?}"/bin/"${APP_NAME}" "${BASE_PACKAGE}"/apps/"${APP_NAME}"

# 解压备份包
tar -zxvf "${BASE_PACKAGE}"/bak/"${BAK_PACKAGE_FULL_NAME}" -C /
