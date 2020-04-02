#!/bin/bash

mvn clean install
docker cp target/weatherHasher-1.0-SNAPSHOT-jar-with-dependencies.jar sandbox-hdp:/tmp/weatherHasher.jar

