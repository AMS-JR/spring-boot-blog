# 📝 Spring Boot Blogging Web App

A simple **monolithic** blogging platform built with **Spring Boot** and **Thymeleaf**. This web application provides essential features like user authentication, authorization, post creation/editing, and role-based access control — all packed into a clean and responsive UI.

---

## 🚀 Features

### ✅ Core Functionality
- **User Registration & Login** – Secure authentication using email and password
- **Role-Based Access Control** – Roles for Admin, Editor, and User
- **Create, Edit, Delete Posts** – Full CRUD support for blog content
- **Public Blog Viewing** – Posts are visible based on visibility status
- **Profile Page** – View and update user profile information
- **Post Visibility Control** – Admin/Editor can toggle post visibility (draft/published)

### 🔐 Security & Authorization
- **Spring Security Integration** – Secure routes and user handling
- **Remember-Me Support** – Persistent login with secure cookie
- **Forgot Password Flow** –
    - Request reset with email
    - Token-based secure link with expiry
    - Email delivery of password reset instructions

### 💡 Future Enhancements (Planned)
- 💬 Commenting System
- 📊 Admin Dashboard with metrics and moderation tools
- ⏳ Scheduled Publishing – Set posts to be published manually or automatically
- 👥 User Management – Enhanced controls for admins

### 🎨 Frontend & UX
- **Thymeleaf Templating** – Clean, maintainable dynamic HTML
- **Responsive Design** – Optimized for all screen sizes using Bootstrap
- **Custom Error Pages & Notifications** – Friendly UX for login and form flows

### 🛠 Tech Stack & Integrations
- Java 17, Spring Boot, Spring Security, Thymeleaf, Spring Data JPA
- H2 / MySQL / PostgreSQL database compatibility
- Bootstrap 5 for modern UI

---

## 📸 Screenshots

> _Add screenshots or a GIF preview here if available_

---

## 🔧 Getting Started

### Prerequisites

- Java 17+
- Maven
- A modern browser

### Setup

```bash
# Clone the repository
git clone https://github.com/AMS-JR/spring-boot-blog.git
cd spring-boot-blog

# Build and run the application
./mvnw spring-boot:run
