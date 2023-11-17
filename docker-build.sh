#!/usr/bin/env bash
name=tripstore-orders-service
docker rmi "$name"
docker build . -t "$name"
