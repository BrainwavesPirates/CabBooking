# CabBooking


Github REPOSITORY:
--------------------

https://github.com/BrainwavesPirates/CabBooking

Tech Stack :
-----------

-Java 8 with Object Oriented concepts  and other features like :

	- Inheritance, Encapsulation, Interface, Collections, In -Memory caching, Singleton, Exception and Error Framework, Util classes, Spark restful , 
	
-Cache Manager(Singleton) : Collections.synchronizedSet for storing data in memory
-Spark-core for restful API
-Junit Framework for unit tests
-slf4j for logging
-gson
-Maven as a build tool
-Utility classes : JSONUtil, CommonUtil for calculating distance between two points

Workflow : 
------------

Assumptions
-----------
* Cab requests are ordered as per time. Request at time t2 comes after request at time t1 provided t1 < t2
* The price is 2 per kilometer. Pink cars cost an additional 5 
* Distance between Cab location and Pickup location is assumed to be 

	distance = this.getLocation() - request.getPickupLocation() * 2km
	time to Reach = distance * 2 ( Time for Kilometer)
	
	
1. Details about and Classes and Test classes :

Details about classes :
-----------------------

Models : 				Cab, CabRequest
Spark Rest controller : CabBookingController, Main ( to trigger the spark server)

	- methods addCab , requestForNearestCab[ getAvailableCabsBasedOnType() for PINK cab types ], getNumofCabsRunning
	
Interface and Service layer : ICabBookingSystem, CabBookingSystemImpl

	- methods addCab , requestForNearestCab[getAvailableCabsBasedOnType() for PINK cab types],  getNumofCabsRunning
	
Utility classes : 		JSONUtil, CommonUtil( for calculating distance between two points)
Caching 		: 		CacheManager
Exception Handling : 	CabNotAvailableException, Response Error ( for spark response error)
Contants :				CabSystemConstants

Test classes : for testing methods addCabs , requestNearestCabs, getNumOfRunningCabs
----------------

- CabTest: 			 	Cab-Construction , Cab-Request creation
- CabRequest: 			CabRequestConstruction
- CabBookingTest:		Cab-Construction -> checkBooking for nearest cab / nearest PINK cab + CabNotAvailableException handling, 

- Spark Api Test :  	CabBookingControllerIntegrationTest   

						- Cab-Construction 				: testCabConstruction() 			: request("POST", "/cabs?id=DL01HB002&type=red&location=200");
						- NumOfRunningCabs 				: testGetAllRunningCabs() 			: request("GET", "/cabs");
						- NearestAVailableCabBooking 	: testNearestAvailableCabRequest()  : request("GET", "/cabs/?bookingId=BR001&cabType=pink&pickupCode=100&dropCode=156");

2. Functionalities:
---------------------

-> Cab Creation :: Cabs are added using Spark Test testCabConstruction() / CabTest.java TestCases 

	: addCab(Cab cab) service is invoked 
	: Params :  id ,  type,  location
	
-> Cab Request :: Nearest Cabs are assigned using Spark testNearestAvailableCabRequest() /  CabBookingTest.java Testcases checkBooking()

	: requestForNearestCab(CabRequest request) service is invoked

	: Params : bookingId, cabType(PINK/others), pickupCode, dropCode, pickupTime
	
	: Ability to ride around in pink cars : 
		getAvailableCabsBasedOnType(String cabType)
		
-> During Cab Request Following methods are Executed :
 
	- Distance between two points is calculated and nearest cab is fetched 
		
		CommonUtil calc = new CommonUtil(cab.getLocation(), request.getPickupLocation());
		double distanceBetween = calc.getDistance();
		
	- Check for Cabs availability
	
		- selectedCab.addJourney(request)-> addJourneyMinutes() -> this.availableFrom time is specified  : Selected Nearest available cab's Journey time is added  during which it can no longer pick up any other customers
		- cab.gotTimeToServeThis(request) : Checks that the cab is available from what time if there is a spare time of 15 mins , cab is assigned 
			calc timeToReach
			calc currentTime 
			calc pickupTime
			calc spareTime : pickupTime - currentTime >=15 mins
			
		- CabNotAvailableException("No Cab Available") : If there are no taxis available, you reject the customer's request and Exception is thrown
		
		
	- Once Journey finishes and Cab is available based on this.availableFrom time check
	
	- Fare Calculation 
		-selectedCab.getFare(request)
		-The price is 2 per kilometer. Pink cars cost an additional 5 
		
		travelDistance = Math.abs(request.getPickupLocation() - request.getDropLocation());
		cabFare = CabSystemConstants.FARE;													// 2
		if (request.getCabType() == CabSystemConstants.PINK_CAB)
			cabFare = CabSystemConstants.PINK_CAB_FARE;										//5
		costCollected = travelDistance * cabFare;											// Fare


