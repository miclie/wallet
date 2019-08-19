cd mysql-server

docker build -t my-mysql .

docker run -d -p 3306:3306 --name my-mysql -e MYSQL_ROOT_PASSWORD=root my-mysql

cd ..
cd config-server

mvn install

docker build -t config-server .

docker run -d -p 8888:8888 config-server:latest

cd ..

cd eureka-server

mvn install

docker build -t eureka-server .

docker run -d -p 9091:9091 eureka-server:latest

cd ..

cd auth-server
mvn install

docker build -t auth-server .

docker run -d -p 9999:9999 auth-server:latest

cd ..

cd gateway-service

mvn install

docker build -t gateway-service .

docker run -d -p 9092:9092 gateway-service:latest

cd ..

cd wallet-service

mvn install -DskipTests

docker build -t wallet-service .

docker run -d -p 9093:9093 wallet-service:latest

cd ..
