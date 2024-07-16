FROM openjdk:17-jdk-alpine

# Add Maintainer Info
LABEL maintainer="Leni <lenicoulibaly@gmail.com>"

# Make port 8080 available to the world outside this container
EXPOSE 101

ENV EUREKA_CONTAINER_NAME="eureka-container"
ENV EUREKA_SERVER_URL="http://eureka-container:8761/eureka/"
ENV CONFIG_SERVER_URL="http://config-server-container:8888"
ENV PROFILES="dev"

# Run the jar file
ENTRYPOINT ["java", "-jar", "./target/TypeService.jar", "--eureka.client.service-url.defaultZone=${EUREKA_SERVER_URL}", "--spring.cloud.config.uri=${CONFIG_SERVER_URL}", "--spring.profiles.active=${PROFILES}"]

#docker container run -d --name gateway-container -p 9999:9999 --restart unless-stopped lenicoulibaly/gateway:4.1.4.1
#docker network create --drive brigde bridge-network
#docker network connect bridge-network eureka-container