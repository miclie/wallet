FROM java:8-jdk-alpine

COPY ./target/auth-server-0.0.1-SNAPSHOT.jar /usr/app/

WORKDIR /usr/app

RUN sh -c 'touch auth-server-0.0.1-SNAPSHOT.jar'

ENTRYPOINT ["java","-jar","auth-server-0.0.1-SNAPSHOT.jar"]