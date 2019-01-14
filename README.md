# SmartPortables
Developed web-applications to purchase/sell portable products.
Technology used: JSP Servlet, CSS, HTML, XML, Bootstrap, Python, Twitter API, AJAX


Steps to deploy and run the project:-

	1. Copy the "csj" folder into the Apache Tomacat's "webapps" folder.

	2. Set classpath of ServletAPI, Gson, MongoDb and MySQL drivers if not there. The required JAR files can be found in "SmartPortables\requiredLibs" folder.

	3. You should have MySQL running on port number 3306. Execute the attach SQL script file "sql.sql" in "SmartPortables\csj\src" to create database/tables required to run the project.

	4. This project also requires that you have a MongoDb Database running on port number 27017 named "CustomerReviews" and collection named "myReviews" in the database.

	5. Install Python2/ Anaconda 2.7. Install modules TwitterAPI and pymysql.
	
	6. Create an account on twitter.com ; Generate authentication tokens ;  Add tokens to the credentials.txt file present in "SmartPortables\csj".

	7. Setup the mysql connection to the database. Run the jupyter notebook and launch the "BestBuyDeals.ipynb" to generate "DealsMatches.txt" both present in "SmartPortables\csj".

	8. Navigate to "csj\WEB-INF\classes\" folder where all the JAVA source code is located. 

	9. Compile all the files in CMD by typing -->  javac *.java

	10. If all the files are compiled properly then you should see .class files in the "classes" folder without any error message on command prompt.

	11. Start a web browser and navigate to http://localhost/home. You should see the webpage of Smartportables portal.

	12. Sample user's login credentials:
		Customer: username : anshika	password: anshika
		manager: username : storemanager password: password
		salesman: username : salesman password: password

	13. "Project Demo"  for the demo in SmartPortables folder.
