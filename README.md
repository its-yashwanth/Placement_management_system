# Placement Management System

This is a Spring Boot MVC starter project for a campus Placement Management System.

## Modules included

- Student dashboard to view drives and apply for jobs
- Company dashboard to post placement drives
- Placement officer dashboard to approve companies
- Admin dashboard to monitor core metrics
- Eligibility auto-check based on branch and CGPA
- Placement lock once a student is selected
- Observer-pattern style notification hook
- Factory-based user creation helper

## Tech stack

- Java
- Spring Boot
- Spring MVC
- Spring Security
- Spring Data JPA
- Thymeleaf
- MySQL

## Project structure

- `controller`: MVC request handlers
- `service`: business logic
- `repository`: JPA repositories
- `model`: entities and enums
- `dto`: form objects
- `templates`: Thymeleaf pages

## Setupor drives
coordinate interviews
reject applications
verify final selections
But only the company can shortlist candida

1. Install Java 21+ and Maven.
2. Create a MySQL database named `pms_db`.
3. Update database username/password in `src/main/resources/application.properties`.
4. Run `mvn spring-boot:run`.
5. Open `http://localhost:8080`.

## Default seeded accounts

- Admin: `admin@pms.edu / admin123`
- Placement Officer: `officer@pms.edu / officer123`
- Student demo: `ananya@student.edu / student123`
- Company demo: `hr@techverse.com / company123`

## Implemented flows

- Student and company registration with login
- Role-based dashboard redirects using Spring Security
- Student profile update and job application flow
- Company drive create, edit, delete, application review, shortlist, interview scheduling, and selection
- Placement officer company approval, rejection, drive management, interview coordination, and final selection verification
- Admin dashboard with student/company management, drive management, approval actions, report screen, and CSV export
- SQL schema and PlantUML source files for submission in `docs/`

## Database tables

- `user_accounts`
- `students`
- `companies`
- `placement_drives`
- `applications`

## UML mapping ideas

### Use case diagram

- Student: register, update profile, view drives, apply, check status
- Company: register, post job, shortlist candidate, schedule interview
- Placement officer: approve company, manage drives, monitor applications
- Admin: manage users, view reports

### Class diagram

Main classes:

- `Student`
- `Company`
- `PlacementDrive`
- `Application`
- `UserAccount`

### Activity diagram

Company registration -> officer approval -> drive creation -> student application -> eligibility check -> shortlist -> interview -> selection -> placement status update

### State diagram

Application states:

- `APPLIED`
- `ELIGIBLE`
- `SHORTLISTED`
- `INTERVIEW_SCHEDULED`
- `SELECTED`
- `REJECTED`

## Design patterns used

- MVC architecture
- Factory Pattern in `UserFactory`
- Observer Pattern in `ApplicationService`
- Singleton Pattern via Spring-managed beans
