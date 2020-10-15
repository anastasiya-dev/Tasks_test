# Test Task Labinvent

Test Task Labinvent is a simple app for sensors info management.

## Installation

Use Apache Maven (http://maven.apache.org) to build the project
and Apache Tomcat (http://tomcat.apache.org) to start the app.

Database used for info storage - MySQL5.7.

Initial info population is performed via:
src\main\resources\liquibase-changelog.xml

## Usage

There are two user roles: Administrator and Viewer.
Add/edit/delete functions are available for Administrator only.

Please note that test users user1.1, user1.2, user2 are assigned passwords
password1.1(Viewer), password1.2(Viewer) and password2(Administrator) respectively.

*** src\main\java ***

### \
Application configuration files

### \controller
Spring MVC controllers

SensorEditController is secured only for users with the role "Administrator": add/edit/delete functions.
Deletion is carried out by changing sensor status without actual deletion from MySQL database.

SensorViewController manages filtering and displaying of the sensors list
and is available for all authenticated users.

### \pojo
Includes objects used for the app
Sensor fields values requirements are stipulated via validation constraints.

### \security
Spring Security configuration, password encoder - BCrypt

### \repository
Repositories are implemented via JpaRepository interface

### \service
Intermediary classes to manage interaction between controllers and repositories

*** src\webapp ***
Web UI is created with JSP (HTML+CSS).
Available at http://localhost:8080/test-labinvent/