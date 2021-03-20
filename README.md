[![CircleCI](https://circleci.com/gh/saleco/medical-bookings.svg?style=svg)](https://circleci.com/gh/saleco/medical-bookings)
# Medical Bookings API

## Agendas's API - Resources related to Agendas's information

#TODO
- Global Exception Handler
- ELK for logging
- Eureka for service discovery
- Cloud Config for properties
- Gateway to handle requests / security (http basic)
- Swagger Auth
- User Roles + Spring AOP to check roles


• As a patient, I must be able to see the availability of the doctors and schedule an
appointment for myself.
#### Get Doctors Availability (GET /ap1/v1/agendas/search)
#### Schedule an appointment (POST /ap1/v1/appointments/)

• As a doctor, I must be able to see the appointments that I have for a given time
period.

• As a doctor, I can set my self as unavailable for a specific time period. blocking any
patients from scheduling an appointment for that period. (e.g., doctor can be on
vacation, sick, etc…)


• Easy configuration

• Easy to run it locally (must be a containerised solution)

• Logging

• Testing

• Metrics - nice to have

• Security