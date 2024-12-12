# USC CSCI 201, Fall 2024, Final Project

This repo contains our final project for CSCI 201: a revised version of USC's course registration portal.

Mo Jiang (mojiang@usc.edu), Breeze Pickford (bpickfor@usc.edu), Luke Albert (lpalbert@usc.edu),
Jasmita Yechuri (yechuri@usc.edu), Advay Iyer (advayiye@usc.edu), Samuel Wu (samuelsw@usc.edu),
Satwika Vemuri (vemurina@usc.edu), Edward Shao (shaoe@usc.edu).

### changes needed for authentication to work:
1. add the jar files (GSON and JDBC connector)
2. change the dbProperties.txt to your username, password, and schema
3. make sure you have a users table like this; else drop it and run JDBC.createUserTable()
```sql
CREATE TABLE users (
    id INT PRIMARY KEY auto_increment,
    username VARCHAR(100),
    email VARCHAR(100),
    password VARCHAR(100)
);
```