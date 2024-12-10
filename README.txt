Continuing Professional Development:
1. Dynamic Programming
2. Version Control
3. Google SSO Authentication
4. Javascript Blob (file saving)
5. Python Programming
6. Credentials Files

Skills and Tools:
Google Authentication (Tool)
HTML/CSS/JS Programming (Skill)
Apache Tomcat (Tool)
Java Programming (Skill)
MySQL Database (Tool)
USC Schedule of Classes API (Tool)


Outside Courses:
1. Dynamic Programming
2. Version Control
3. Google SSO Authentication
4. Javascript Blob (file saving)
5. Python Programming
6. Credentials Files
7. CSS Navbars

Design and Development Decisions:
Project Design -- Something that worked for us was having a pretty clear overall design that was derived largely based off of the skills we learned through labs/assignments in 201, meaning that all team members were relatively familiar with our tech stack. Something that did not work for us was starting off with Python, which we realized did not lend itself well to our tech stack. We pivoted from this pretty quickly however, and our original Python code helped us write our Java code. 
Teamwork -- Something that worked for us was that we separated tasks when we drafted our technical specifications deliverable, which was nice so that people could work on the parts of the project that interested them the most. Something that did not work as well for us was meeting virtually for most of the project. Having a few more in person meetings might have helped with the collaborative process.

Data Structures:
Map (Schedule of Classes Data)
Used when parsing data returned from USC Schedule of Classes API
Keys: Courses (Ex: CSCI 201)
Values: Sections (Ex: TTh 11-12:20, MW 3:30-4:50)

Set (Department Data)
Keeps track of all possible departments that exist in USC
Set is used since all departments are unique
In order to retrieve classes for all departments, we generate an API call for each item in the set

Vector (Database Results)
When querying the database, we keep track of the results of our query in this vector

List  (Scheduled Classes)
Used in the dynamic programming part of our code when trying to come up with a valid scheduling of classes
This list keeps track of the classes we have scheduled thus far

ArrayList (Section Dates)
Used in our Section class, which keeps track of the data from one specific section of a class
This list keeps track of all the dates that this section occurs on (including lecture, lab, quizzes, etc.)

Multithreading: Web-based project (web server implements multi threading).

Networking: Used in proper interlayer communication between database and server, also frontend and backend connection for Servlets

User Login Functionality: When user is signed-in, their schedule is personalized with their name.
