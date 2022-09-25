FROM maven:3.6-jdk-8-slim
WORKDIR /homework
ENTRYPOINT [ "mvn" ]