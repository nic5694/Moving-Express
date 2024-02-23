# Fullstack Moving Express Web Application

This is a fullstack web application for a moving company, designed to streamline the process of getting quotes, managing shipments, and generating necessary reports. The application is built with a Spring Boot backend, Express.js for the frontend, and utilizes Auth0 for hybrid authentication in the API. It also integrates with Gmail's SMTP service for email notifications.

## Features

- **Authentication & Authorization**:
  - Hybrid authentication with Auth0 in the API layer.
  - Authorization for various user roles:
    - Clients can register, log in, and manage their inventories.
    - Observers can be assigned with three types of permissions: view, edit, and full access.
    - Truck drivers can assign themselves moves, update move details, and generate shipment manifests.

- **Quote & Shipment Estimation**:
  - Clients can visit the application to receive quotes.
  - After registering, clients can create shipments and request estimations.
  - Moving estimators can visit the client's house to estimate the move.

- **Shipment Management**:
  - Once a move is accepted, the shipment is created.
  - Clients can manage their inventories and items within these shipments.
  - Observers with appropriate permissions can also manage these inventories and generate PDF reports of the shipment.

- **Truck Driver Functionality**:
  - Truck drivers can assign themselves to moves by providing their truck VIN.
  - They can update the final weight of the assigned move.
  - Truck drivers have the ability to generate shipment manifests for their assigned moves.

## Installation

To get started with the application, follow these steps:

**Clone the Repository**:
   ```bash
   git clone https://github.com/nic5694/moving-express.git
   cd moving-express
   ```
### **Backend Setup:**
Navigate to the backend directory:
```bash
cd backend
```
Configure your Auth0 credentials, Gmail SMTP Credentials and your hosted mysql database credentials in the application.properties file. Docker files and docker-compose files are in place if you want to use the application locally, MySQL Schema files are in the resource folder in the backend project.
```bash
#Use this command to run the docker-compose running the backend project, you can uncomment the react and mysql servers to have them all run in dockerized components
docker-compose up --build
```
### Frontend Setup
```bash
cd ../frontend
```
- Install dependencies
```bash
npm install
```
- Set the backend API URL in the .env file.
- Start frontend server or run the docker-compose
```bash
docker compose up --build
#or to run locally
npm start
```
- Open a web browser and navigate to http://localhost:3000 to use the application.

## Technologies Used

- **Backend**:
  - Spring Boot
  - Java
  - Auth0 for Authentication & Authorization (with implemented custom login flows to set default roles and add Id token to the http header)
  - SMTP for Gmail integration

- **Frontend**:
  - React with TypeScript
  - Tailwind CSS for styling
  - Toastify library for notifications
  - Typescript
  - HTML/CSS

## Responsive Design

The entire application is built with responsive design in mind, ensuring a seamless experience across all devices and screen sizes.

## Design
The entire domain and application is deisgned from the ground up with use case diagrams, sequence diagrams, integrated class diagrams, domain driven design diagram among many of the diagrams designed by our team.

## CI/CD Pipeline 
The CI/CD Pipeline is tightly integrated with Vercel and Digital Ocean for seamless continuous deployment. Circle CI orchestrates all automated tests for the backend, while Qodana acts as a stringent linter. Additionally, GitHub Actions facilitates pull request labeling, and the frontend undergoes linting using npm with the command `npm ci`. We maintain a rigorous minimum code coverage of 90% enforced by Jacoco to ensure the backend CI pipeline passes, achieving an impressive overall Jacoco coverage of 98% across the entire codebase.
