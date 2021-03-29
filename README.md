Application shows what revenue is returned for booking premium and standard rooms

To build application please run: mvn clean test

To start application please run: mvn spring-boot:run  

It exposes rest endpoint under address http://localhost:8080/booking/calculate/revenue

Example of call: curl -X POST "http://localhost:8080/booking/calculate/revenue?numberOfPremiumRooms=3&numberOfStandardRooms=3"

