# Hotel Booking Management System (REST API)<br>

## 1. Project Overview<br>

The **Hotel Booking Management System** is a RESTful web application developed using **Core Java, Servlet, JSP, Maven, and <br> MySQL**. The system allows users to register, log in securely, browse hotels and rooms, and create bookings.<br>

The project follows a **layered architecture** to maintain clean separation of concerns and scalability. It is designed as a<br> backend REST API that can later be integrated with web or mobile front-end applications.<br>

This system demonstrates industry-standard backend practices such as:<br>

* RESTful API design<br>
* Layered architecture (Controller → Service → DAO → Model)<br>
* Secure authentication using <br>
* Password encryption using BCrypt<br>
* Database interaction using <br>
* API testing using Postman<br>

---

## 2. Technology Stack<br>

| Layer               | Technology         |<br>
| ------------------- | ------------------ |<br>
| Language            | Java (Core Java)   |<br>
| Web Technology      | Servlet & JSP      |<br>
| Build Tool          | Maven              |<br>
| Database            | MySQL              |<br>
| Server              | Apache Tomcat 10+  |<br>
| Security            | JWT Authentication |<br>
| Password Encryption | BCrypt             |<br>
| API Testing         | Postman            |<br>
| Development IDE     | VS Code            |<br>

---

## 3. Project Architecture<br>

The system follows a **Layered Architecture Pattern**.<br>

```
Controller Layer <br> 
       ↓
Service Layer <br>
       ↓
DAO (Data Access Layer) <br>
       ↓
Database (MySQL) <br>
```

### Layer Responsibilities <br>

**Controller Layer**<br>

* Handles HTTP requests and responses <br>
* Maps REST API endpoints <br>

**Service Layer** <br>

* Contains business logic <br>
* Validates rules before database  <br>

**DAO Layer** <br>

* Handles database operations using JDBC <br>
* Performs CRUD queries <br>

**Model Layer** <br>

* Defines entity classes representing database tables <br>

**Util Layer** <br>

* Database connection <br>
* JWT token generation/validation <br>
* Password encryption <br>

**Filter Layer** <br>

* JWT Authentication filter to secure APIs <br>

## 4. Project Folder Structure <br>

```
HotelBookingSystem <br>
│
├── src/main/java/com/hotel <br>
│
│   ├── controller <br>
│   │       AuthController.java <br>
│   │       UserController.java <br>
│   │       HotelController.java <br>
│   │       RoomController.java <br>
│   │       BookingController.java <br>
│   │
│   ├── service <br>
│   │       UserService.java <br>
│   │       HotelService.java <br>
│   │       RoomService.java <br>
│   │       BookingService.java <br>
│   │
│   ├── dao <br>
│   │       UserDAO.java <br>
│   │       HotelDAO.java <br>
│   │       RoomDAO.java <br>
│   │       BookingDAO.java <br>
│   │
│   ├── model <br>
│   │       User.java <br>
│   │       Hotel.java <br>
│   │       Room.java <br>
│   │       Booking.java <br>
│   │
│   ├── util <br>
│   │       DBConnection. java <br>
│   │       JWTUtil.java <br>
│   │       PasswordUtil. java <br>
│   │
│   ├── filter <br>
│   │       JWTAuthFilter. java <br>
│
├── src/main/webapp <br>
│       WEB-INF/web.xml <br>
│
├── pom.xml <br>
└── README.md <br>
```

## 5. Database Schema <br>

### Users Table <br>

```
users <br>
------
user_id (PK) <br>
name <br>
email <br>
password <br>
phone <br>
role <br>
```

### Hotels Table <br>
 
-------
hotel_id (PK) <br>
name <br>
location <br>
description <br>
```

### Rooms Table <br>
------
room_id (PK) <br>
hotel_id (FK) <br>
room_type <br>
price <br>
capacity <br>
status <br>
```

Status values: <br>

* AVAILABLE <br>
* BOOKED <br>

### Bookings Table <br>

```
bookings <br>
---------
booking_id (PK) <br>
user_id (FK) <br>
room_id (FK) <br>
check_in <br>
check_out <br>
total_price <br>
status <br>
```
Status values: <br>

* CONFIRMED <br>
* CANCELLED <br>
---

## 6. API Endpoints <br>

### Authentication APIs <br>

Register User <br>

```
POST /api/auth/register <br>
```

Login <br>

```
POST /api/auth/login <br>
```

Returns a **JWT token** used for authentication. <br>
---
### User APIs <br>

```
GET     /api/users <br> 
PUT     /api/users <br>
DELETE  /api/users?userId={id} <br>
```
### Hotel APIs <br>

```
POST    /api/hotels <br>
GET     /api/hotels <br>
PUT     /api/hotels <br>
DELETE  /api/hotels?hotelId={id} <br>
```

### Room APIs <br>

```
POST    /api/rooms <br>
GET     /api/rooms <br>
PUT     /api/rooms <br>
DELETE  /api/rooms?roomId={id} <br>
```

### Booking APIs <br>

```
POST    /api/bookings <br>
GET     /api/bookings <br>
GET     /api/bookings?bookingId={id} <br>
PUT     /api/bookings <br>
DELETE  /api/bookings?bookingId={id} <br>
```

## 7. Authentication & Security

The system uses **JWT (JSON Web Token)** authentication.<br>

Workflow: <br>

1. User logs in using email and password. <br>
2. Server verifies credentials. <br>
3. Server generates a JWT token. <br> 
4. Client sends the token in every request.<br>

Header example:<br>
```
Authorization: Bearer <JWT_TOKEN> <br>
```

The **JWTAuthFilter** validates the token before accessing protected APIs.<br>
---

## 8. API Testing Using Postman <br>

Example request to create a booking: <br>

```
POST /api/bookings <br>
```

Request Body: <br>

```
{ <br>
  "userId": 1, <br>
  "roomId": 2, <br>
  "checkIn": "2026-03-15", <br>
  "checkOut": "2026-03-18", <br>
  "totalPrice": 9000, <br>
  "status": "CONFIRMED"<br>
}<br>
```
---
## 9. Key Features <br>

* User Registration and Login <br>
* Secure password encryption <br>
* JWT-based authentication <br>
* Hotel management <br>
* Room management <br>
* Booking management <br>
* RESTful API design <br> 
* Layered architecture <br>
* MySQL relational database <br>
---

## 10. Future Improvements <br>

Possible improvements include: <br>

* Frontend UI using React or Angular <br>
* Room availability checking <br>
* Payment gateway integration <br>
* Booking cancellation policy <br>
* Admin dashboard <br>
* Email notifications <br>
---

## 11. How to Run the Project <br>

1. Clone the repository. <br>
2. Import the project as a **Maven project**. <br>
3. Configure MySQL database. <br>
4. Update database credentials in `DBConnection.java`. <br>
5. Build the project using Maven. <br>
6. Deploy the WAR file to **Apache Tomcat**. <br>
7. Test APIs using Postman. <br>
---

## 12. Purpose of the Project <br>

This project is developed as a **Final Year Computer Science Project** to demonstrate practical implementation of: <br>

* Java Web Development <br>
* REST API Design <br>
* Database Integration <br>
* Secure Authentication <br>
* Software Engineering Architecture <br>
---

### 13.Author
---
👨‍💻 Developer
AJIT KUMAR SAHU
Email-sahuajitkumara@gmail.com
Contact - 9861567673
---

