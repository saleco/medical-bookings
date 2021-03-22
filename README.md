[![CircleCI](https://circleci.com/gh/saleco/medical-bookings.svg?style=svg)](https://circleci.com/gh/saleco/medical-bookings)
[![codecov](https://codecov.io/gh/saleco/medical-bookings/branch/master/graph/badge.svg?token=7FO9XSWVAT)](https://codecov.io/gh/saleco/medical-bookings)
# Medical Bookings API

# User Stories

## As a patient, I must be able to see the availability of the doctors and schedule an appointment for myself.
#### Get Doctors Availability (GET /ap1/v1/agendas/search)
#### Schedule an appointment (POST /ap1/v1/appointments/)

## As a doctor, I must be able to see the appointments that I have for a given time period.
#### Search Appointments for the given criterias (GET /ap1/v1/appointments/search)

## As a doctor, I can set my self as unavailable for a specific time period. blocking any patients from scheduling an appointment for that period. (e.g., doctor can be on vacation, sick, etc…)
#### Update Doctors Availability (PUT /ap1/v1/agenda/search)

# Running Container 
./src/main/docker/local/docker-compose up -d

# Logging
http://localhost:5601/

# Security 
Credentials: admin / secret

# Metrics 
http://localhost:1111/

# API
http://localhost:8080/swagger-ui/index.html

##Possible Improvements
- Java doc
- Eureka for service discovery
- Cloud Config for properties
- Gateway to handle requests / security / loadbalancing
