Simple application to show how big revenue is returned
It exposes rest endpoint under address http://localhost:8080/booking/calculate/revenue

Example of call:

curl -X POST "http://localhost:8080/booking/calculate/revenue?numberOfPremiumRooms=1&numberOfStandardRooms=1"

Endpoint takes as argument 2 parameters: numberOfPremiumRooms, numberOfStandardRooms

