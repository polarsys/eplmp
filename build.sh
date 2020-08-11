#!/usr/bin/env sh

IMAGE_NAME=polarsys/eplmp-server
VERSION=$(mvn -q -N org.codehaus.mojo:exec-maven-plugin:3.0.0:exec \
    -Dexec.executable='echo' \
    -Dexec.args='${project.version}')

mvn clean install && \
 docker build -f docker/payara/Dockerfile -t polarsys/eplmp-base:latest .  && \
 docker tag polarsys/eplmp-base:latest polarsys/eplmp-base:$VERSION  && \
 docker build -f docker/Dockerfile -t $IMAGE_NAME:latest .  && \
 docker tag $IMAGE_NAME:latest $IMAGE_NAME:$VERSION
