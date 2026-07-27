# 🏥 CareSync ERP (Enterprise Resource Planning)

## Enterprise Hospital Management & Real-Time Healthcare Workflow System

CareSync ERP is a modern enterprise-grade Hospital Management System built using Spring Boot and designed to digitize the complete hospital workflow through a centralized, role-based architecture.

Unlike traditional hospital management systems, CareSync ERP provides **independent dashboards and dedicated user interfaces** for every stakeholder—including **Admin, Doctor, Receptionist, and Patient**—while keeping all operations synchronized in real time.

Every action performed by one user is instantly reflected for the relevant users across the system.

### 🚀 Real-Time Workflow

- A patient books an appointment online.
- The appointment immediately appears on the assigned doctor's dashboard.
- The receptionist can simultaneously view and manage the same appointment.
- The admin gets complete visibility of every booking, cancellation, check-in, and check-out across the hospital.
- Doctors can complete consultations while receptionists manage patient flow without data conflicts.
- Patients can track their appointments and profile updates from their own portal.

All users work on the same centralized database, ensuring real-time consistency and seamless collaboration between departments.

---

## ✨ Key Highlights

- 🔐 JWT-based Authentication & Role-Based Authorization
- 👨‍⚕️ Dedicated Dashboard for Doctors
- 👩‍💻 Independent Receptionist Portal
- 🧑‍🦱 Patient Self-Service Portal
- 👨‍💼 Centralized Admin Control Panel
- 📅 Smart Appointment Scheduling
- ⚡ Real-Time Appointment Synchronization
- 🚶 Walk-in Patient Registration
- ✅ Patient Check-In & Check-Out Workflow
- 📊 Analytics & Reporting Dashboard
- 📈 Pagination, Filtering & Search APIs
- 🔄 Soft Delete Strategy
- 🔑 OTP-Based Password Recovery
- 🛡️ Secure REST APIs with Spring Security & JWT
- 🏗️ Clean Layered Architecture Following Enterprise Standards

---

## 💡 Why CareSync ERP?

CareSync ERP is not just a CRUD-based Hospital Management System.

It is designed as a **real-world healthcare workflow platform** where multiple hospital departments work simultaneously on the same system. Every role has its own responsibilities, permissions, and interface, while the backend ensures secure communication, synchronized data, and a seamless operational flow across the entire hospital.

---

## 📌 Project Overview

CareSync ERP is a backend REST API designed for modern hospitals and clinics. It provides secure authentication, role-based authorization, appointment scheduling, patient management, doctor management, receptionist operations, dashboards, and reporting.

The application supports four different user roles:

- 👨‍💼 Admin
- 👨‍⚕️ Doctor
- 👩‍💻 Receptionist
- 🧑‍🦱 Patient

Each role has its own permissions and dashboard.

---

# 🚀 Tech Stack

| Category | Technology |
|----------|------------|
| Framework | Spring Boot 3.x |
| Language | Java 17 |
| Security | Spring Security + JWT |
| Database | MySQL / PostgreSQL |
| ORM | Hibernate (JPA) |
| Build Tool | Maven |
| Validation | Hibernate Validator |
| Email Service | JavaMailSender |
| Logging | SLF4J + Logback |
| API Style | RESTful APIs |

---

# ✨ Features

## 🔐 Authentication & Security

- JWT Authentication
- Spring Security
- Role Based Authorization
- Password Encryption (BCrypt)
- OTP Based Password Reset
- Secure Login
- Method Level Authorization
- Public Registration APIs

---

## 👨‍💼 Admin Features

- Dashboard
- Manage Doctors
- Manage Patients
- Manage Receptionists
- Manage Appointments
- View Reports
- Monitor Hospital Statistics

---

## 👨‍⚕️ Doctor Features

- Doctor Dashboard
- View Today's Schedule
- View Assigned Appointments
- Manage Patient Visits
- Complete Appointment

---

## 👩‍💻 Receptionist Features

- Walk-in Patient Registration
- Appointment Booking
- Patient Check-In
- Patient Check-Out
- View Daily Appointments

---

## 🧑‍🦱 Patient Features

- Self Registration
- Secure Login
- Book Appointment
- Cancel Appointment
- View Doctors
- View Profile
- Update Profile
- Patient Dashboard

---

# 📁 Project Structure

```
src/main/java/com/caresync/erp

├── common
│   ├── constants
│   └── enums
│
├── config
│   ├── SecurityConfig
│   ├── CorsConfig
│   └── AppConfig
│
├── controller
│
├── dto
│
├── exception
│
├── mapper
│
├── model
│
├── repository
│
├── security
│   ├── JwtAuthenticationFilter
│   ├── JwtService
│   ├── JwtAuthenticationEntryPoint
│   └── CustomUserDetailsService
│
├── service
│
├── util
│
└── CareSyncApplication.java
```

---

# 👥 User Roles

| Role | Description |
|-------|-------------|
| ADMIN | Complete System Access |
| DOCTOR | Manage Own Patients & Appointments |
| RECEPTIONIST | Front Desk Operations |
| PATIENT | Self Service Portal |

---

# 🔑 Role Permissions

| Module | Admin | Doctor | Receptionist | Patient |
|---------|:----:|:------:|:------------:|:-------:|
| Dashboard | ✅ | ✅ | ✅ | ✅ |
| Manage Doctors | ✅ | ❌ | ❌ | ❌ |
| Manage Patients | ✅ | View | ✅ | Self |
| Book Appointment | ✅ | ❌ | ✅ | ✅ |
| Cancel Appointment | ✅ | ❌ | ✅ | ✅ |
| Check-In | ❌ | ❌ | ✅ | ❌ |
| Check-Out | ❌ | ❌ | ✅ | ❌ |
| Reports | ✅ | ❌ | ❌ | ❌ |

---

# 🔐 Authentication APIs

## Staff Authentication

| Method | Endpoint | Description |
|---------|----------|-------------|
| POST | `/api/auth/register` | Register Staff |
| POST | `/api/auth/login` | Login |
| POST | `/api/auth/public-register/receptionist` | Receptionist Registration |
| POST | `/api/auth/forgot-password` | Generate OTP |
| POST | `/api/auth/verify-otp` | Verify OTP |
| POST | `/api/auth/reset-password` | Reset Password |

---

## Patient Authentication

| Method | Endpoint |
|---------|----------|
| POST | `/api/patient/register` |
| POST | `/api/patient/login` |

---

# 👨‍⚕️ Doctor APIs

| Method | Endpoint |
|---------|----------|
| POST | `/api/doctors/create` |
| GET | `/api/doctors/{id}` |
| PUT | `/api/doctors/{id}` |
| DELETE | `/api/doctors/{id}` |
| GET | `/api/doctors` |
| POST | `/api/doctors/public-register` |
| GET | `/api/doctors/{doctorId}/available-slots` |

---

# 🧑‍🦱 Patient Management APIs

| Method | Endpoint |
|---------|----------|
| POST | `/api/patients/create` |
| GET | `/api/patients/{id}` |
| PUT | `/api/patients/{id}` |
| DELETE | `/api/patients/{id}` |
| GET | `/api/patients` |

---

# 👤 Patient Self-Service APIs

| Method | Endpoint |
|---------|----------|
| GET | `/api/patient/dashboard/{patientId}` |
| GET | `/api/patient/appointments` |
| POST | `/api/patient/appointments/book` |
| PUT | `/api/patient/appointments/{id}/cancel` |
| GET | `/api/patient/doctors` |
| GET | `/api/patient/profile/{patientId}` |
| PUT | `/api/patient/profile/{patientId}` |

---

# 📅 Appointment APIs

| Method | Endpoint |
|---------|----------|
| POST | `/api/appointments/create` |
| GET | `/api/appointments/{id}` |
| GET | `/api/appointments/doctor/{doctorId}` |
| GET | `/api/appointments/patient/{patientId}` |
| PUT | `/api/appointments/{id}/cancel` |
| PUT | `/api/appointments/{id}/complete` |
| GET | `/api/appointments` |

---

# 👩‍💻 Receptionist APIs

| Method | Endpoint |
|---------|----------|
| POST | `/api/receptionist/appointments/book` |
| GET | `/api/receptionist/appointments/today` |
| GET | `/api/receptionist/appointments` |
| PUT | `/api/receptionist/appointments/{id}/check-in` |
| PUT | `/api/receptionist/appointments/{id}/check-out` |

---

# 📊 Dashboard APIs

| Method | Endpoint |
|---------|----------|
| GET | `/api/dashboard/admin/stats` |
| GET | `/api/dashboard/doctor/stats/{doctorId}` |
| GET | `/api/dashboard/receptionist/stats` |

---

# 📈 Reports APIs

| Method | Endpoint |
|---------|----------|
| GET | `/api/reports/appointments/daily` |
| GET | `/api/reports/appointments/monthly` |
| GET | `/api/reports/patients/gender` |
| GET | `/api/reports/doctors/performance` |

---

# 🗄️ Database Design

## Users

```sql
id BIGINT PRIMARY KEY

username VARCHAR(100) UNIQUE

password VARCHAR(255)

email VARCHAR(150)

role ENUM

enabled BOOLEAN

account_non_locked BOOLEAN
```

---

## Doctors

```sql
id BIGINT PRIMARY KEY

name

specialization

phone

email

experience_years

active

user_id (FK)
```

---

## Patients

```sql
id BIGINT PRIMARY KEY

name

age

gender

phone

email

address

blood_group

medical_history

insurance_provider

insurance_number

emergency_contact

registration_date

active

user_id (FK)
```

---

## Appointments

```sql
id BIGINT PRIMARY KEY

doctor_id (FK)

patient_id (FK)

appointment_date

appointment_time

status

booking_source

check_in_time

check_out_time

active
```

Unique Constraint

```sql
UNIQUE
(
doctor_id,
appointment_date,
appointment_time
)
```

---

# 🔐 JWT Authentication Flow

```
Client
   │
   │ Login
   ▼

Authentication Controller

   │

Validate Credentials

   │

Generate JWT Token

   │

Return JWT

   │

Client stores JWT

   │

Authorization:
Bearer <JWT>

   │

JwtAuthenticationFilter

   │

Validate Token

   │

Spring Security

   │

Authorized API Access
```

---

# 🌐 Public APIs

No authentication required.

```
/api/auth/**

/api/patient/register

/api/patient/login

/api/doctors/public-register

/api/auth/public-register/receptionist
```

---

# ❌ Error Response Format

```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Doctor not found with id: 5"
}
```

---

# 📋 Business Rules

### ✅ Appointment Booking

- No overlapping appointments for the same doctor.
- One slot can be booked only once.

---

### ✅ Walk-in Patient

- Receptionist can register walk-in patients.
- Email is auto-generated if not available.
- User account is optional.

---

### ✅ Patient Check-In

Allowed only if:

- Appointment is for today.
- Status = **BOOKED**

---

### ✅ Patient Check-Out

Allowed only if:

- Status = **CHECKED_IN**

---

### ✅ Appointment Cancellation

Allowed only when:

- Appointment is in the future.
- Status = **BOOKED**

---

### ✅ Soft Delete

Doctors and Patients are never permanently deleted.

Instead:

```text
active = false
```

---

### ✅ OTP Verification

- OTP expires after **5 minutes**
- Stored temporarily using **ConcurrentHashMap**
- One-time use only

---

# 🔒 Security Highlights

- JWT Authentication
- Stateless Sessions
- BCrypt Password Encryption
- Spring Security Filters
- Role-Based Access Control (RBAC)
- Global Exception Handling
- Input Validation
- CORS Configuration

---

# 📦 Future Enhancements

- Payment Gateway Integration
- Video Consultation
- Prescription Module
- Laboratory Module
- Pharmacy Management
- Inventory Management
- Notification Service (Email/SMS)
- Audit Logging
- Docker Support
- CI/CD Pipeline
- Redis Caching
- Swagger/OpenAPI Documentation

---

# ▶️ Getting Started

## Clone the Repository

```bash
git clone https://github.com/your-username/caresync-erp.git
```

## Navigate to Project

```bash
cd caresync-erp
```

## Configure Database

Update your `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/caresync

spring.datasource.username=root

spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update
```

## Build the Project

```bash
mvn clean install
```

## Run the Application

```bash
mvn spring-boot:run
```

Application will start on:

```
http://localhost:8080
```

---

# 👨‍💻 Author

**Anshuman Dalabehera**

Java Backend Developer

Spring Boot • Java • MySQL • REST APIs • Spring Security • JWT

