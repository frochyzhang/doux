#Active Profile(YAML)
ACTIVE_PROFILE="$1"
#Base Folder Path like "/folder/packages"
CURRENT_DIR=$(readlink -f "$0")
BASE_PACKAGE="${CURRENT_DIR%/bin/*}"
# Shell Script file name after removing path like "start-yaml-validator.sh"
SHELL_SCRIPT_FILE_NAME=$(basename -- "$0")
# Shell Script file name after removing extension like "start-yaml-validator"
#SHELL_SCRIPT_FILE_NAME_WITHOUT_EXT="${SHELL_SCRIPT_FILE_NAME%.*}"
# App name after removing start/stop strings like "yaml-validator"
APP_NAME=${SHELL_SCRIPT_FILE_NAME%-shutdown.sh}

# Script to stop the application
PID_PATH="$BASE_PACKAGE"/pid/"$APP_NAME"/"$APP_NAME".pid
if [ -e "${PID_PATH}" ]; then
    PIDS=$(pgrep -f -F "${PID_PATH}" -u "${USER}" "[j]ava.*-Dspring.profiles.active=$ACTIVE_PROFILE.*$APP_NAME.*jar")
    if [ -z "$PIDS" ]; then
      echo "No instances of $APP_NAME with profile:$ACTIVE_PROFILE is running..." 1>&2
    else
      for PROCESS_ID in $PIDS
      do
        kill "$PROCESS_ID";
        echo "Gracefully stopping $APP_NAME with PROCESS_ID:$PROCESS_ID..."
        sleep 5s
      done
      exit
    fi
else
  echo "Process Id FilePath($PID_PATH) Not found"
fi

if [ -n "${ACTIVE_PROFILE}" ]; then
  PIDS=$(pgrep -f -u "${USER}" "[j]ava.*-Dspring.profiles.active=$ACTIVE_PROFILE.*$APP_NAME.*jar")
  if [ -z "$PIDS" ]; then
    echo "All instances of $APP_NAME with profile:$ACTIVE_PROFILE has has been successfully stopped now..." 1>&2
  else
    for PROCESS_ID in $PIDS
    do
      kill "$PROCESS_ID";
      echo "Gracefully stopping $APP_NAME with PROCESS_ID:$PROCESS_ID..."
      sleep 5s
    done
    exit
  fi
fi