# BeyondSpockEndpointProject

Overview

  This server provides login, bid storing and retrieving capabilities.
	Refer to "BeyondSpockEndpointProject.pdf" for details

Start application:
	
	- Jave 8

	- C:>java -jar BeyondSpockEndpoint.jar

Kill application

	Press <Ctrl>+c
	
Typical Requests

	- http://localhost:8081/4711/login (GET)

	- http://localhost:8081/2/bid?sessionkey=UICSNDK (POST)

	- http://localhost:8081/2/topBidList (GET)
