1. Config Server http://localhost:8888

2. Eureka Server http://localhost:9091/

3. Auth Server http://localhost:9999/
4. Gateway Service http://localhost:9092/

5. Wallet Service http://localhost:9093/

Request for token http://localhost:9092/uaa/oauth/token

Swagger http://localhost:9092/wallet-service/swagger-ui.html


For testing Wallet API you may use Postman collection named wallet.postman_collection.json 

You may find dockerization commands in docker.bat and docker.sh files.

During dockerization process a mysql database and necessary tables will be created.


