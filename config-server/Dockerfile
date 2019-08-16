FROM java:8-jdk-alpine

COPY ./target/config-server-0.0.1-SNAPSHOT.jar /usr/app/

RUN mkdir /usr/app/shared

COPY  ./src/main/resources/shared/auth-server.yml /usr/app/shared/
COPY  ./src/main/resources/shared/eureka-server.yml /usr/app/shared/
COPY  ./src/main/resources/shared/gateway-service.yml /usr/app/shared/
COPY  ./src/main/resources/shared/wallet-service.yml /usr/app/shared/

COPY  ./src/main/resources/application.yml /usr/app/

WORKDIR /usr/app

RUN sh -c 'touch config-server-0.0.1-SNAPSHOT.jar'

ENTRYPOINT ["java","-jar","config-server-0.0.1-SNAPSHOT.jar"]