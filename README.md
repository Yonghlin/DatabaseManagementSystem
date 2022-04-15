DBMS Programming Assignment 2
Created by Brennan Mulligan, Yong Lin, Makaylie Lucas, Mason Weyant
FOR Database Management Systems
Shippensburg University

Instructions:

Helpful Tips

To avoid hard coding a UI for every table you should look up information on Accessing Meta-Data in a MySQL DB.  Also, our MySQL server is not visible through Ship's firewall.  As such if you need your code to access it off campus you'll need to look into VPN access to our network (www.engr.ship.edu->Student Information->FAQ).  Lastly, a reminder that Character is a Keyword in MySQL.  So, if you choose that as a name for a table (not sure if also applies to columns), you'll need to change it.

 

Due: Tuesday last week of classes, accommodations on request.

 

Turn-In: D2L->Assignments (Programming Assignment 2) - Will present project during last week of the semester in class.

 

This assignment will have you build a Java application that will interface with your MySQL database.  You will be using your work from homework 3 and 5, regardless of the grade you received on that assignment.

 

Part I

You will need to create a method or group of methods to build the tables for your database.  This should just use JDBC and the SQL statements you created in Part 2 of Homework 3. 

 

Part II

You will need to create a method or methods to populate the tables created in Part II with at least 5 values each per table.  Using JDBC and your SQL statements from Part 3 of Homework 3 should accomplished most of this.  Depending on your foreign key constraints you may need to remove and then add a constraint back to add in the initial values.

 

Part III

Implement the User Interfaces designed and reviewed from Homework 5.  See D2L for document on using swing to build a simple UI in Java.  Each person in the group is expected to do one of the interfaces from Homework 5, you will only be graded on the interface you worked on. Additionally, each person also needs to implement one stored procedure on the MySQL database as part of their solution – see Zoom video for how to do this.
