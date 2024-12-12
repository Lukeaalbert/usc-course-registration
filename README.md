# USC CSCI 201, Fall 2024, Final Project

This repo contains our final project for CSCI 201: a revised version of USC's course registration portal.

Mo Jiang (mojiang@usc.edu), Breeze Pickford (bpickfor@usc.edu), Luke Albert (lpalbert@usc.edu),
Jasmita Yechuri (yechuri@usc.edu), Advay Iyer (advayiye@usc.edu), Samuel Wu (samuelsw@usc.edu),
Satwika Vemuri (vemurina@usc.edu), Edward Shao (shaoe@usc.edu).

### 1 - pull current semester data into database
1. run the javac files so that the web api can be invoked and semester database pulled into database
2. this is essential for pulling into database

### 2 - drag the src/main folder into Eclipse Tomcat workspace
1. the src/main contains all front end and backend files necessary for running the project

### 3 - changes needed for configuration:
1. add the jar files (GSON and JDBC connector)
2. modify the properties in the `project src code\src\main\webapp\WEB-INF\database_properties.txt` to your username, password, and schema
3. make sure you have a users table like this; else drop it and run JDBC.createUserTable()
```sql
CREATE TABLE users (
    id INT PRIMARY KEY auto_increment,
    username VARCHAR(100),
    email VARCHAR(100),
    password VARCHAR(100)
);
```