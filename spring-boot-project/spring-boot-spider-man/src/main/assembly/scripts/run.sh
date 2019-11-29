#!/bin/sh
##cnf
APP_PMS="-Xms256m -Xmx512m"
APP_MAIN=com.mountain.project.spider.Application

##run
cur=$(dirname $(readlink -f "$0")) && cd $cur/..

APP_CP=$(echo lib/*.jar |tr ' ' ':')
exec java -Dfile.encoding=UTF-8 ${APP_PMS} -server -cp "runtime:${APP_CP}" ${APP_MAIN}
