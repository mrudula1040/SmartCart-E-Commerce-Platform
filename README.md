# SmartCart – Full Stack E-Commerce Web Application

SmartCart is a full-stack e-commerce web application developed using Java, Spring Boot, React.js, and MySQL. It provides online shopping functionality for customers and management features for administrators.

## Features

### Customer Features
- User registration and login
- JWT-based authentication
- Browse and search products
- Filter products by category
- View product details and prices
- Add, update, and remove cart items
- Manage wishlist
- Place orders and view order history
- Select payment methods: Cash on Delivery (COD), Card, and UPI
- Submit product reviews, ratings, and comments

### Admin Features
- Admin login and role-based access
- Manage products and categories
- Manage orders and order status
- Manage inventory and stock quantities
- View low-stock information
- Manage customer-related information
- Access dashboard and sales-related information

### Backend Features
- RESTful API architecture
- Controller–Service–Repository structure
- Spring Data JPA and Hibernate
- DTO-based request and response handling
- Input validation
- Global exception handling
- BCrypt password hashing
- JWT authentication and role-based authorization

## Technology Stack

| Component | Technologies |
|---|---|
| Backend | Java 21, Spring Boot |
| Security | Spring Security, JWT, BCrypt |
| Database | MySQL 8 |
| ORM | Spring Data JPA, Hibernate |
| Frontend | React.js, Vite, JavaScript, CSS |
| Build Tool | Maven |
| API Testing | Postman |
| Development Tools | IntelliJ IDEA, VS Code, MySQL Workbench |
| Version Control | Git, GitHub |

## Database

The application uses a MySQL database named `smartcart`.

Main tables include:
- `users`
- `categories`
- `products`
- `cart`
- `wishlist`
- `orders`
- `order_items`
- `payment`
- `inventory`
- `review`

The database schema can be created by Hibernate/JPA configuration or imported from the provided SQL script, where applicable.



## Prerequisites

Install the following before running the application:

- Java JDK 21
- MySQL Server 8
- Node.js and npm
- Git
- An IDE such as IntelliJ IDEA or VS Code

## Setup and Installation

### 1. Clone the Repository

```bash
git clone https://github.com/mrudula1040/SmartCart-E-Commerce-Platform.git
cd SmartCart-E-Commerce-Platform
```

### 2. Configure MySQL

1. Start MySQL Server.
2. Create a database named `smartcart`, if it is not created automatically.
3. Open the backend configuration file under `project/src/main/resources/`.
4. Configure the database URL, username, and password for your local MySQL installation.
5. Import the provided database SQL script if you are using one.

Do not publish real database passwords, JWT secrets, or other credentials in the repository.

### 3. Run the Backend

Open Git Bash or a terminal inside the `project` folder.

On Windows, run:

```bash
./mvnw.cmd spring-boot:run
```

Alternatively, use the Maven Wrapper command in Command Prompt:

```cmd
mvnw.cmd spring-boot:run
```

The backend is expected to run at:

`http://localhost:8080`

### 4. Run the Frontend

Open a separate terminal inside the `SmartCart-Frontend` folder.

Install dependencies:

```bash
npm install
```

Start the Vite development server:

```bash
npm run dev
```

Open the local URL printed in the terminal, normally:

`http://localhost:5173`

Ensure that the backend and MySQL database are running before testing frontend features.

## API Testing

The REST APIs can be tested using Postman. Authentication-protected endpoints require a valid bearer token where configured.

Main API modules include:
- Authentication
- Users
- Products
- Categories
- Cart
- Wishlist
- Orders
- Payments
- Inventory
- Reviews

Refer to the project's API documentation for endpoint details and request/response examples.

## Limitations

- The payment workflow is a project-level simulation and is not integrated with a live payment gateway.

## Project Objective

The objective of SmartCart is to demonstrate full-stack development concepts by integrating a React frontend, a Spring Boot REST API backend, and a MySQL relational database, with authentication, data validation, and e-commerce workflows.

## Author

**Mrudula Thange**

GitHub: [mrudula1040](https://github.com/mrudula1040)

---

Developed as a Full Stack Development project.
