#!/usr/bin/env bash
export MYSQL_DATA=$PWD/mysqldata
mkdir -p mysqldata
export UPLOAD_DATA=$PWD/uploaddata
mkdir -p uploaddata
export REDIS_DATA=$PWD/redisdata
mkdir -p redisdata
docker-compose up -d