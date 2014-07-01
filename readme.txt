Programming Assignment

Andrey Nikolov 2014/07/01

Description
===========
The application provides RESTful services with the following functionality:
(1) Authenticate user based on username & password passed in a JSON payload and verified against MySQL database.
(2) Retrieve users from database filtered by 'gender' passed as URL parameter and ordered alphabetically by 'name'.
(3) Check and return the status of system components (MySQL database connection, Web server parameters).
(4) List the files in a given directory specified in the URL request.

Environment
===========
Database server: MySQL 5.6.19
Web server: Apache Tomcat 8.0.9
Java runtime: JDK version 1.8.0-05
Development: Maven Project on Eclipse IDE
Dependencies: junit 4.11, jersey-core/servlet/client/json 1.18.1, mysql-connector-java 5.1.31

Deployment
==========
Database setup
--------------
(1) Create database user 'root'
# mysqladmin -u root password "password"
(2) Create database 'maven-web'
# mysqladmin -u root -p create maven_web
(3) Run SQL script to create table 'user'
# mysql -u root -p
> source create.sql;
(4) Run SQL script to insert sample data
> source insert.sql;

Source build
------------
(1) Validate project
Eclipse > Run As > Maven build > validate
(2) Compile java classes
Eclipse > Run As > Maven build > compile
(3) Run test cases
Eclipse > Run As > Maven build > test
Tests run: 10, Failures: 0, Errors: 0, Skipped: 0
(4) Package into web archive
Eclipse > Run As > Maven build > package
Building war: /Users/Andrey/Workspace/MavenWeb/target/MavenWeb.war

Service deployment
------------------
(1) Start Tomcat server
# cd /usr/share/tomcat/bin
# ./startup.sh
(2) Deploy package 'MavenWeb.war'
http://localhost:8080/manager/html
(3) Make sure the service is accessible online
http://localhost:8080/MavenWeb/v1/system/response

Sample output
=============
Authenticate user
-----------------
Test: http://localhost:8080/MavenWeb/v1/authenticate (Clara/Password)
Success: {"authenticated":false,"message":"Unknown username."}

Test: http://localhost:8080/MavenWeb/v1/authenticate (Andrey/Nikolov)
Success: {"authenticated":false,"message":"Wrong password."}

Test: http://localhost:8080/MavenWeb/v1/authenticate (Andrey/Password)
Success: {"authenticated":true,"message":"Correct credentials."}

Retrieve users
--------------
Test: http://localhost:8080/MavenWeb/v1/users
Success: {"users":[{"name":"Andrey","gender":"Male","age":36},{"name":"Boris","gender":"Male","age":33},{"name":"Carlo","gender":"Male","age":32},{"name":"Maria","gender":"Female","age":31}]}

Test: http://localhost:8080/MavenWeb/v1/users?gender=Male
Success: {"users":[{"name":"Andrey","gender":"Male","age":36},{"name":"Boris","gender":"Male","age":33},{"name":"Carlo","gender":"Male","age":32}]}

Test: http://localhost:8080/MavenWeb/v1/users?gender=Female
Success: {"users":[{"name":"Maria","gender":"Female","age":31}]}

Check system status
-------------------
Test: http://localhost:8080/MavenWeb/v1/system
Success: Checking server status...
  OS name: Mac OS X
  OS version: 10.9.3
  Memory total: 187695104
  Memory free:  121143400
  HTTP server: Success 200
Checking database status...
  URL: jdbc:mysql://localhost/maven_web
  Driver: com.mysql.jdbc.Driver
  Username: root
  Password: password
  Status: Connection success.
  Entries: 4

List files
----------
Test: http://localhost:8080/MavenWeb/v1/files/Users/Andrey/Downloads
Success: .DS_Store
.localized
Apple ProRes White Paper.pdf
mysql-5.6.19-osx10.7-x86_64.dmg

Test: http://localhost:8080/MavenWeb/v1/files/Dummy/Folder
Success: 
