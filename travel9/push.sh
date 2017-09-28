#!/usr/bin/env bash
docker build -t webserg_java .
docker tag webserg_java stor.highloadcup.ru/travels/marked_barracuda
docker push stor.highloadcup.ru/travels/marked_barracuda
