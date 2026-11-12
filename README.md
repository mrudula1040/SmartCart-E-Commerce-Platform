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

## Project Preview
 This is a Full Stack Java Development E-commerce Project built using Java, Spring Boot, Spring Security, React JS, Vite, MySQL
 
## Admin Dashboard
<img width="1883" height="956" alt="10_Admin_Dashboard" src="https://github.com/user-attachments/assets/241ee315-da55-4a58-b0eb-3624988210c8" />

<img width="1875" height="965" alt="11_Admin_Dashboard" src="https://github.com/user-attachments/assets/a21c251a-559f-4d3e-a2b8-8176abd017ad" />

## Products
<img width="1893" height="957" alt="3_Products png" src="https://github.com/user-attachments/assets/4d88590a-cc90-48d7-8845-55dd33e8bef8" />

## Cart Payment Method
<img width="1815" height="882" alt="7_Cart_Products_Payment_methods" src="https://github.com/user-attachments/assets/e1e7b2c6-db62-4514-9424-7e24ab7e2ab6" />

## Admin Inventory Management
<img width="1885" height="952" alt="14_Admin_Inventory_mangement" src="https://github.com/user-attachments/assets/9ad3d780-9c4a-4439-a991-edcbc239a353" />

## Admin Order
<img width="1764" height="957" alt="15_Admin_order" src="https://github.com/user-attachments/assets/df201e61-e599-46cf-abaf-2eca6645499e" />

## Review
<img width="1870" height="965" alt="5_Product_Review png" src="https://github.com/user-attachments/assets/35aa4da2-d521-4dd4-b028-922dc38590f7" />

## Search Filter
<img width="1874" height="942" alt="4_Search_Filter png" src="https://github.com/user-attachments/assets/b49984fe-7309-4da2-840d-46100bea9eab" />

## More Screenshots $ Full Documentation 


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
