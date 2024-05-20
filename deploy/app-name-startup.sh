while getopts :hs:x:j:w:r:p:v: opt
do
    case $opt in
        h)
                echo "[-s]:set minimum memory size for pile and heap"
                echo "[-x]:set maximum memory size for pile and heap"
                echo "[-j]:set jmx prometheus javaagent port"
                echo "[-w]:set skywalking backend address, eg: 127.0.0.1:11800"
                echo "[-r]:set remote debug port"
                echo "[-p]:set active profile"
                echo "[-v]:set jmx remote port``"
                exit 1;
                ;;
        s)
                echo "Xms=$OPTARG"
                XMS=$OPTARG
                ;;
        x)
                echo "Xmx=$OPTARG"
                XMX=$OPTARG
                ;;
        j)
                JMX_PORT=$OPTARG
                echo "jmx port=${JMX_PORT}"
                JMX_PROMETHEUS_AGENT=$(find "${HOME}" -type f -name "jmx_prometheus_javaagent*.jar" -exec stat --format '%Y %n' {} \; | sort -nr | head -n 1 | awk '{print $2}')
                if [ ! -e "${JMX_PROMETHEUS_AGENT}" ]; then
                  echo "jmx_prometheus_javaagent is not found, please check it out." 1>&2
                  exit 1
                fi
                PROMETHEUS_CONFIG=$(find "${HOME}" -type f -name "prometheus-config.yml" -exec stat --format '%Y %n' {} \; | sort -nr | head -n 1 | awk '{print $2}')
                if [ ! -e "${PROMETHEUS_CONFIG}" ]; then
                  echo "prometheus-config.yml is not found, please check it out." 1>&2
                  exit 1
                fi
                JMX_PARAM="-javaagent:${JMX_PROMETHEUS_AGENT}=${JMX_PORT}:${PROMETHEUS_CONFIG}"
                ;;
        w)
                echo "skywalking backend address=$OPTARG"
                SKYWALKING_BACKEND_ADDRESS=$OPTARG
                SKYWALKING_AGENT=$(find "${HOME}" -type f -name "skywalking-agent.jar" -exec stat --format '%Y %n' {} \; | sort -nr | head -n 1 | awk '{print $2}')
                if [ ! -e "${SKYWALKING_AGENT}" ]; then
                    echo "skywalking agent is not found, please check it out." 1>&2
                    exit 1
                fi
                SKYWALKING_BACKEND_IP=$(awk -F ':' '{print $1}' <<< "${SKYWALKING_BACKEND_ADDRESS}")
                SKYWALKING_BACKEND_PORT=$(awk -F ':' '{print $2}' <<< "${SKYWALKING_BACKEND_ADDRESS}")
                SKYWALKING_PARAM="-javaagent:$SKYWALKING_AGENT -Dskywalking.agent.service_name=$APP_NAME -Dskywalking.collector.backend_service=$SKYWALKING_BACKEND_ADDRESS"
                SKYWALKING_PLUGIN_PARAM="-Dskywalking.plugin.toolkit.log.grpc.reporter.server_host=${SKYWALKING_BACKEND_IP} -Dskywalking.plugin.toolkit.log.grpc.reporter.server_port=${SKYWALKING_BACKEND_PORT}"
                ;;
        r)
                echo "remote debug port=$OPTARG"
                REMOTE_DEBUG_PARAM="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=${OPTARG}"
                ;;
        p)
                echo "active profile=${OPTARG}"
                ACTIVE_PROFILE=${OPTARG}
                ;;
        v)
                echo "jmx remote port=${OPTARG}"
                JMX_REMOTE_PORT=${OPTARG}
                JMX_REMOTE_PARAM="-Dcom.sun.management.jmxremote=true -Dcom.sun.management.jmxremote.port=${JMX_REMOTE_PORT} -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false"
                ;;
        :)
                echo "-$OPTARG needs an argument"
                exit 1
                ;;
        ?)
                echo "wrong option"
                exit 1
                ;;
        *)
                echo "-$opt not recognized"
                exit 1
                ;;
    esac
done
#shift $((OPTIND - 1))

if [ -z "$ACTIVE_PROFILE" ]; then
  echo "Please input active profile, eg: sit|uat|prod, otherwise exit!" 1>&2
  exit 1
fi

if [ -z "$XMS" ]; then
  echo "Usage: jvm param -Xms lost, use default 128m" 1>&2
  XMS=128
fi

if [ -z "$XMX" ]; then
  echo "Usage: jvm param -Xmx lost, use default 128m" 1>&2
  XMX=128
fi

# Base Folder Path like "/folder/packages"
CURRENT_DIR=$(readlink -f "$0")
BASE_PACKAGE="${CURRENT_DIR%/bin/*}"
# Shell Script file name after removing path like "start-yaml-validator.sh"
SHELL_SCRIPT_FILE_NAME=$(basename -- "$0")
# Shell Script file name after removing extension like "start-yaml-validator"
#SHELL_SCRIPT_FILE_NAME_WITHOUT_EXT="${SHELL_SCRIPT_FILE_NAME%.sh}"
# App name after removing start/stop strings like "yaml-validator"
APP_NAME=${SHELL_SCRIPT_FILE_NAME%-startup.sh}
# JVM Parameters and Spring boot initialization parameters
JVM_PARAM="-Xms${XMS}m -Xmx${XMX}m -Dspring.profiles.active=${ACTIVE_PROFILE} -Dcom.webmethods.jms.clientIDSharing=true
-Dspring.config.location=$BASE_PACKAGE/apps/$APP_NAME/config/
-Dlogging.config=$BASE_PACKAGE/apps/$APP_NAME/config/logback-spring.xml"
HEAP_DUMP_PARAM="-XX:+HeapDumpOnOutOfMemoryError -XX:+ExitOnOutOfMemoryError -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:HeapDumpPath=${HOME}/dump/${APP_NAME}-$(date "+%Y%m%d").hprof"

PID_DIR="${BASE_PACKAGE}/pid/${APP_NAME}"
if [ ! -d "${PID_DIR}" ]; then
    echo "Pid file path not exist, create it now..."
    mkdir -p "${PID_DIR}"
fi
PID_PATH="${PID_DIR}/${APP_NAME}.pid"
if [ -e "${PID_PATH}" ]; then
    PIDS=$(pgrep -F "${PID_PATH}")
    if [ -z "$PIDS" ]; then
      echo "No instances of $APP_NAME with profile:$ACTIVE_PROFILE is running..." 1>&2
    else
      for PROCESS_ID in $PIDS
      do
        echo "Please stop the process($PROCESS_ID) using the shell script: $APP_NAME-shutdown.sh"
      done
      exit 1
    fi
else
  echo "No instances of $APP_NAME with profile:$ACTIVE_PROFILE is running..." 1>&2
fi

# Preparing the java home path for execution
JAVA_EXEC=${JAVA_HOME}/bin/java
# Java Executable - Jar Path Obtained from latest file in directory
JAVA_APP=$(find "$BASE_PACKAGE/apps/$APP_NAME" -maxdepth 1 -type f -name "$APP_NAME*.jar" -exec stat --format '%Y %n' {} \; | sort -nr | head -n 1 | awk '{print $2}')
# To execute the application.
FINAL_EXEC="$JAVA_EXEC $JVM_PARAM $HEAP_DUMP_PARAM $SKYWALKING_PARAM $SKYWALKING_PLUGIN_PARAM ${JMX_PARAM} ${REMOTE_DEBUG_PARAM} ${JMX_REMOTE_PARAM} -jar $JAVA_APP"
# Making executable command using tilde symbol and running completely detached from terminal
eval "$(${FINAL_EXEC} </dev/null >/dev/null 2>&1 & echo $! > "${PID_PATH}")"
#`nohup $FINAL_EXEC >> ../logs/$APP_NAME/$APP_NAME.log 2>&1 &`
echo "$APP_NAME start script is completed."
