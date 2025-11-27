# Campus-Placements
# Software Architecture
<img width="700" height="1522" alt="image" src="https://github.com/user-attachments/assets/bf591dd4-d58d-48fa-b6e1-6ec7a0d60a55" />

# Description

## Overview
  This diagram shows the architecture of a web application using the React and Spring Boot framework. 
  It is divided into different layers. Each layer has a different role in how the application works.
  
  Client: Renders pages and calls backend REST endpoints.
  Presentaion (MVC Controller): Parse and validate requests, delegate to services and return JSON.
  Application: Encapsulate business rules and transactions. Coordinate multiple repositories.
  Persistance: Repositories to work with the db.
  Database: MySQL DB

# Presentation Layer
  The Presentation Layer is the entry point for HTTP requests. It contains Spring MVC controllers.
  It is responsible for converting the JSON field’s parameter to Java Objects and vice-versa. 
  Once it performs the authentication of the request it passes it to the next layer. i.e., the Application layer.

# Application layer
  The Application Layer contains service interfaces and their implementations. This layer is contains the core business
  logic of the appication. It talks to repositories and other services as necessary.

# Persistence Layer
  The Persistence Layer contains entities and Spring Data JPA repositories. This layer map's Java entities to database tables and back.
  Provide CRUD and queries for services. Keep controller and service code free from SQL or driver concerns.

# Database Layer
  The Database Layer is MySQL. This layer can contain multiple databases. This layer is responsible for executing CRUD operations issued
  by repositories. Enforce relational constraints and indexing for performance and integrity.


# Full Example
  For example, the user goes to the web application and searches a company. The web application sends this request to the controller. 
  The Controller receives the request and the user wants to search for a company. Then, it calls the Service that contains the logic for 
  searching a company. The Service checks the logic for searching a company. To get the list of related companies, the service, with the 
  help of the Repository Interface, sends a query to the database layer. The Database Layer retrieves the list of the companies and 
  sends it back to the Service .


 
