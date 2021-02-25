# spring-task-mgt

# Installation

configure data source in application.properties

spring.datasource.url= jdbc:mysql://localhost:3306/{db_name}?useSSL=false&allowPublicKeyRetrieval=true&createDatabaseIfNotExist=true
spring.datasource.username= ****
spring.datasource.password= ********



# Request Response Format
Postman Client requests are attached here
https://www.getpostman.com/collections/52a0e6a3c88238aa4bfc

# Check Initial Data

INSERT INTO roles(name) VALUES('ROLE_USER');
INSERT INTO roles(name) VALUES('ROLE_ADMIN');

# API Test 
Fresh database have no user, so we have to create user.
/api/auth/signup
then login to get access token. Now put this access-token in every single request in the header section
Authorization:<access token>
api/auth/signin

/api/project/create#    //create new project

/api/project/all        //get all projects

/api/task/create

api/task/all

/api/task/edit

/api/task/get/{taskid}

/api/task/get/pId={projectid}

task/get/status={status : OPEN, CLOSE, IN_PROGRESS}

/task/get/dDate

/api/task/getAllTaskByUser/{userid}

/api/project/getAllProjectsByUser/{userid}




