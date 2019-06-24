#!/usr/bin/env bash
export MYSQL_DATA=$PWD/mysqldata
mkdir -p mysqldata
export UPLOAD_DATA=$PWD/uploaddata
mkdir -p uploaddata
export REDIS_DATA=$PWD/redisdata
mkdir -p redisdata
export LOG_DIR=$PWD/log
mkdir -p log
docker-compose up -d