# BANKING_MICROSERVICES
## Customer service:
Customer service used to get, create, edit, save and delete customer accounts.

### Customer service actuators:
http://localhost:9633/actuator/info
http://localhost:9633/actuator/loggers

### Swagger information:
http://localhost:9633/swagger-ui/index.html#/


## Account and Account Payment services:
Account service used to get, create, edit, save and delete customer accounts.
Transaction service used to get customer transactions.

### Account service actuators:
http://localhost:9632/actuator/info
http://localhost:9632/actuator/loggers

### Swagger information:
http://localhost:9632/swagger-ui/index.html#/


## Credit card service:
Credit card service used to get, create, edit, save and delete customer credit cards.

### Credit card service actuators:
http://localhost:9634/actuator/info
http://localhost:9634/actuator/loggers

### Swagger information:
http://localhost:9634/swagger-ui/index.html#/

## Loan and Loan Payment services:
Loan service used to get, create, edit, save and delete customer loans.
Loan payments service used to get loan payments.

### Loan service actuators:
http://localhost:9633/actuator/info
http://localhost:9633/actuator/loggers

### Swagger information:
http://localhost:9633/swagger-ui/index.html#/


### Kafka documentation:

to start zookeeper:
.\bin\windows\zookeeper-server-start.bat config\zookeeper.properties

to start kafka
.\bin\windows\kafka-server-start.bat config\server.properties

to read account service topics:
.\bin\windows\kafka-console-consumer.bat --topic AccountTopic --from-beginning --bootstrap-server localhost:9092

### Rabbit MQ:
localhost:15672
