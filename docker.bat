call cd mysql-server

call docker build -t my-mysql .

call docker run -d -p 3306:3306 --name my-mysql -e MYSQL_ROOT_PASSWORD=root my-mysql

call cd ..

call cd config-server

call mvnw install

call docker build -t config-server .

call docker run -d -p 8888:8888 config-server:latest

call cd ..

call cd eureka-server

call mvnw install

call docker build -t eureka-server .

call docker run -d -p 9091:9091 eureka-server:latest

call cd ..

call cd auth-server
call mvnw install

call docker build -t auth-server .

call docker run -d -p 9999:9999 auth-server:latest

call cd ..

call cd gateway-service

call mvnw install

call docker build -t gateway-service .

call docker run -d -p 9092:9092 gateway-service:latest

call cd ..

call cd wallet-service

call mvnw install -DskipTests

call docker build -t wallet-service .

call docker run -d -p 9093:9093 wallet-service:latest

call cd ..
