# USC CSCI 201, Fall 2024, Final Project

This repo contains our final project for CSCI 201: a revised version of USC's course registration portal.

Mo Jiang (mojiang@usc.edu), Breeze Pickford (bpickfor@usc.edu), Luke Albert (lpalbert@usc.edu),
Jasmita Yechuri (yechuri@usc.edu), Advay Iyer (advayiye@usc.edu), Samuel Wu (samuelsw@usc.edu),
Satwika Vemuri (vemurina@usc.edu), Edward Shao (shaoe@usc.edu).

### important:
make sure sql has a users table defined as following, for the JDBC to work:
password do not set as NOT NULL: for Google SSO users
as NULL password is prevented on the web interface
```sql
CREATE TABLE users (
    id INT PRIMARY KEY auto_increment,
    username VARCHAR(100),
    email VARCHAR(100),
    password VARCHAR(100)
);
```