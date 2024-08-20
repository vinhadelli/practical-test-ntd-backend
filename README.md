# practical-test-ntd-backend

Challenge created for a role in NTD Software. This repository contains the Backend of the application, in Java.

The Frontend in Vue.js is located in the repository: 

https://github.com/vinhadelli/practical-test-ntd-frontend

## How to run

How to run the application

### Locally

Execute the command `docker-compose -f docker-compose-local.yml up -d`. It will start a mysql container, populate the database with the file `CREATE_AND_POPULATE_NTD_CALCULATOR.sql` and build the image, pointing to the newly created mysql. The API will be listening at the port 8080.

### Pointing to the Cloud

Execute the command `docker-compose -f docker-compose-cloud.yml up -d`. It will build the image of the aplication, pointing to the database hosted at Amazon AWS. The API will be listening at the port 8080.

## Testing the aplication

### Automated Tests

Ensure that the aplication is poiting to a working database and execute the command `mvn test`. Or use the IDE of your choice to execute the Test Class, I used IntelliJ.
By default the aplication is poiting to the database in the cloud