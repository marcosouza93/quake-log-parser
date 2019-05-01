# Loads a docker image that contains Alpine Linux with OpenJDK JRE
FROM openjdk:8-jre-alpine

# Sets environment variables
ENV APP_VERSION 0.0.1-SNAPSHOT
ENV APP_NAME quake-log-parser-${APP_VERSION}.jar
ENV PROFILE default
ENV APP_HOME /opt/labs

# Sets working directory
WORKDIR ${APP_HOME}

# Copies jar into image
COPY target/${APP_NAME} ${APP_HOME}

# Starts the project
ENTRYPOINT java -jar -Dspring.profiles.active=${PROFILE} ${APP_NAME}