#!/bin/bash

mvn clean package

eval $(minikube docker-env)

docker build ./ -t data_joiner

kubectl create -f ./kubernetes/configMaps.yml
kubectl create -f ./kubernetes/appDeploy.yml
kubectl create -f ./kubernetes/appService.yml
kubectl create -f ./kubernetes/appServiceMonitor.yml
kubectl create -f ./kubernetes/appHpa.yml
