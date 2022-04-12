# Employee Reimbursement System

## Project Description

A fullstack web application allowing employees to submit reimbursement requests and finance managers to approve/deny those requests.

## Technologies Used

* Java8
* JUnit/Mockito
* Docker
* Logback
* GCP Compute Engine
* GCP Cloud SQL
* GCP Cloud Storage
* Firebase
* HTML/CSS/JavaScript

## Features

List of features:
* Employee Features:
  * Login and view current status of reimbursements
  * Submit a new reimbursement request
  * Delete unresolved reimbursements ("Pending Requests")
* Manager Features:
  * Login with all Employee Features included
  * Navigate to Manager Page
  * View All Reimbursements
  * Filter Reimbursements by Department, User, and Status
  * Approve/Deny any Pending Reimbursement

## Getting Started
- Initial Setup:
```
git clone https://github.com/java-gcp-220228/CaitlinTalerico/new/main/project-1-reimbursement-system
gradle build
```
- Required Environmental Variables:
  - Database:
    - Username, Password, URL
  - GCP Cloud Storage
    - Google Generate Service Key
- Frontend Server:
  - To run a development server run:
```
live-server --port=8081
```



## Usage
- Examples of the Application in Work:
#### Manager Logged In
![Logged-In-Image](https://storage.googleapis.com/misc-github-images-bucket/Manager_Login.PNG)
#### Manager View
![Manager-View-Image](https://storage.googleapis.com/misc-github-images-bucket/Manager_View.PNG)
#### Employee View
![Employee-View-Image](https://storage.googleapis.com/misc-github-images-bucket/Employee%20View.PNG)
#### Reimbursement Ticket
![Reimbursement-Ticket-Image](https://storage.googleapis.com/misc-github-images-bucket/reimbursement-ticker.PNG)

## Tests
Unit tests have been written for the Backend Service layers. To execute the tests run:
```
gradle test
```

## Questions

If you have any additional questions or comments, please reach out to me. You can find more of my work [here](https://github.com/cait-tal).
