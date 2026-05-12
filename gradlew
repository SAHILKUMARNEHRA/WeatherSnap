#!/bin/sh

app_path=$0

while
    APP_HOME=${app_path%"${app_path##*/}"}
    [ -h "$app_path" ]
do
    ls=$(ls -ld "$app_path")
    link=${ls#*' -> '}
    case $link in
      /*)   app_path=$link ;;
      *)    app_path=$APP_HOME$link ;;
    esac
done

APP_HOME=$(cd "${APP_HOME:-./}" && pwd -P) || exit 1

DEFAULT_JVM_OPTS="-Xmx64m -Xms64m"

CLASSPATH=$APP_HOME/gradle/wrapper/gradle-wrapper.jar:$APP_HOME/gradle/wrapper/gradle-wrapper-shared.jar:$APP_HOME/gradle/wrapper/gradle-cli.jar

if [ -n "$JAVA_HOME" ] ; then
    if [ -x "$JAVA_HOME/jre/sh/java" ] ; then
        JAVACMD=$JAVA_HOME/jre/sh/java
    else
        JAVACMD=$JAVA_HOME/bin/java
    fi
    if [ ! -x "$JAVACMD" ] ; then
        echo "ERROR: JAVA_HOME is set to an invalid directory: $JAVA_HOME" >&2
        exit 1
    fi
else
    JAVACMD=java
    if ! command -v java >/dev/null 2>&1
    then
        echo "ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH." >&2
        exit 1
    fi
fi

save() {
    for i do
        printf '%s\n' "$i" | sed "s/'/'\\\\''/g;1s/^/'/;\$s/\$/' \\\\/" ;
    done
    echo " "
}

APP_ARGS=$(save "$@")

eval "set -- $APP_ARGS"

exec "$JAVACMD" $DEFAULT_JVM_OPTS $JAVA_OPTS $GRADLE_OPTS "-Dorg.gradle.appname=gradlew" -classpath "$CLASSPATH" org.gradle.wrapper.GradleWrapperMain "$@"
