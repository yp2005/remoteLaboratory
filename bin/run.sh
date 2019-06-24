#!/usr/bin/env bash
export MYSQL_DATA=$PWD/mysqldata
mkdir -p mysqldata
docker-compose up -d