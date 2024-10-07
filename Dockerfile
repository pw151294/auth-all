FROM openjdk:8-jdk-alpine

MAINTAINER itsc

ENV BASE_DIR="/home/spring-shiro-service"

copy ./spring-shiro-service/target/spring-shiro-service.zip /usr/tmp/

RUN unzip /usr/tmp/spring-shiro-service.zip -d /home/ \
    && rm -rf /usr/tmp/spring-shiro-service.zip

WORKDIR $BASE_DIR

EXPOSE 8088

ENTRYPOINT ["/bin/sh","-c","$BASE_DIR/run.sh start && tail -f /dev/null"]