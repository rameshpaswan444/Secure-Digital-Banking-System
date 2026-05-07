Banking Management System

A secure and scalable Banking Management System built using Java Spring Boot, MySQL, JWT Authentication, and Role-Based Authorization. This project simulates real-world banking operations including account creation, deposits, withdrawals, money transfers, PDF receipt generation, email notifications, transaction history, admin dashboard analytics, and more.

🚀 Features
🔐 Authentication & Security
User Registration & Login
JWT Authentication
Role-Based Authorization (USER / ADMIN)
Secure Password Encryption using BCrypt
Forgot Password System with Email Token Verification
🏦 Banking Features
Bank Account Creation
Deposit Money
Withdraw Money
Money Transfer Between Accounts
Transaction History
Pagination Support for Transactions
Balance Validation
Transaction Logging
📧 Email Notifications
Account Creation Email
Deposit Notification
Withdrawal Notification
Transfer Notification
Forgot Password Email
📄 PDF Features
Download Transaction Receipt as PDF
📊 Admin Dashboard
Total Users
Total Accounts
Total Transactions
Total Bank Balance
Total Deposits
Total Withdrawals
🛠️ Technologies Used
Java 17
Spring Boot
Spring Security
JWT Authentication
Spring Data JPA
Hibernate
MySQL
Maven
Lombok
Java Mail Sender
iText PDF
📂 Project Structure
src/main/java/com/banking
│
├── controller
├── service
├── repository
├── entity
├── dto
├── jwt
├── config
└── exception
