#Active Profile(YAML)
ACTIVE_PROFILE="$1"
if [ -z "$ACTIVE_PROFILE" ]; then
  echo "Usage: sit|uat|prod, otherwise exit!" 1>&2
  exit 1
fi
# JVM Parameters and Spring boot initialization parameters
JVM_PARAM="-Xms512m -Xmx1024m -Dspring.profiles.active=${ACTIVE_PROFILE} -Dcom.webmethods.jms.clientIDSharing=true "
# Base Folder Path like "/folder/packages"
CURRENT_DIR=$(readlink -f "$0")
BASE_PACKAGE="${CURRENT_DIR%/bin/*}"
# Shell Script file name after removing path like "start-yaml-validator.sh"
SHELL_SCRIPT_FILE_NAME=$(basename -- "$0")
# Shell Script file name after removing extension like "start-yaml-validator"
#SHELL_SCRIPT_FILE_NAME_WITHOUT_EXT="${SHELL_SCRIPT_FILE_NAME%.sh}"
# App name after removing start/stop strings like "yaml-validator"
APP_NAME=${SHELL_SCRIPT_FILE_NAME%-startup.sh}

# skywalking相关配置
SKY_WALKING_BACKEND_ADDRESS=""
if [ $ACTIVE_PROFILE == "sit" ]; then
    SKY_WALKING_BACKEND_ADDRESS="10.250.28.142:11800"
    echo "skywalking backend_service_address: $SKY_WALKING_BACKEND_ADDRESS"
elif [ $ACTIVE_PROFILE == "uat" ]; then
    SKY_WALKING_BACKEND_ADDRESS="10.250.28.142:11800"
    echo "skywalking backend_service_address: $SKY_WALKING_BACKEND_ADDRESS"
elif [ $ACTIVE_PROFILE == "prod" ]; then
    SKY_WALKING_BACKEND_ADDRESS="10.250.28.142:11800"
    echo "skywalking backend_service_address: $SKY_WALKING_BACKEND_ADDRESS"
fi
SKY_WALKING_PARAM="-javaagent:$BASE_PACKAGE/agent/skywalking-agent.jar -Dskywalking.agent.service_name=$APP_NAME -Dskywalking.collector.backend_service=$SKY_WALKING_BACKEND_ADDRESS"
SKY_WALKING_PLUGIN_PARAM="-Dskywalking.plugin.toolkit.log.grpc.reporter.server_host=10.250.28.142 -Dskywalking.plugin.toolkit.log.grpc.reporter.server_port=11800"

JVM_PARAM_EXT="--spring.config.location=$BASE_PACKAGE/apps/$APP_NAME/config/"
PIDS=`ps aux |grep [j]ava.*-Dspring.profiles.active=$ACTIVE_PROFILE.*$APP_NAME.*jar | awk {'print $2'}`
if [ -z "$PIDS" ]; then
  echo "No instances of $APP_NAME with profile:$ACTIVE_PROFILE is running..." 1>&2
else
  for PROCESS_ID in $PIDS
  do
        echo "Please stop the process($PROCESS_ID) using the shell script: $APP_NAME-shutdown.sh"
  done
  exit 1
fi

# Preparing the java home path for execution
#JAVA_EXEC='/usr/bin/java'
JAVA_EXEC=${JAVA_HOME}/bin/java
# Java Executable - Jar Path Obtained from latest file in directory
JAVA_APP=$(ls -t $BASE_PACKAGE/apps/$APP_NAME/$APP_NAME*.jar | head -n1)
# To execute the application.
FINAL_EXEC="$JAVA_EXEC $JVM_PARAM -jar $JAVA_APP $JVM_PARAM_EXT $SKY_WALKING_PARAM $SKY_WALKING_PLUGIN_PARAM"
# Making executable command using tilde symbol and running completely detached from terminal
`nohup $FINAL_EXEC  </dev/null >/dev/null 2>&1 &`
#`nohup $FINAL_EXEC >> ../logs/$APP_NAME/$APP_NAME.log 2>&1 &`
echo "$APP_NAME start script is  completed."