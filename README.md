# ⚽ Futsal Pro — Futsal Management System

**Futsal Pro** is a dual-interface Java desktop application designed to streamline the management of sports facilities using **custom Data Structures & Algorithms (DSA)** and **modern Object-Oriented Design**.
The program allows facility owners to manage courts and bookings while enabling customers to check availability and request slots in real-time.

---

## 📌 Project Overview

This application simulates a **comprehensive facility management system** where users can:

* Maintain a registry of futsal courts
* Process booking requests via a **FIFO Queue**
* Visualize availability using an interactive grid
* Compute revenue and analyze popularity trends

The project focuses on **manual implementation of logic**, avoiding heavy frameworks to demonstrate mastery of **Java Swing, File I/O, and Core Algorithms**.

---

## ✨ Key Features

### 👨‍💼 Admin Module (Facility Management)

* **Dashboard Analytics:** Real-time tracking of Total Revenue, Total Bookings, and Most Popular Court.
* **Court Registry:** Add, delete, and view courts with attributes like ID, Price, and Location.
* **Request Processing:** Manage incoming booking requests using a strict **First-In-First-Out (FIFO)** queue system.
* **Cancellation Mode:** Specialized interface to cancel active bookings safely.

### 👤 Customer Module (Booking System)

* **Smart Availability Grid:** Visual representation of time slots (24h format) for selected dates.
* **Search & Sort:**
* **Binary Search:** Quickly find courts by ID.
* **Sorting:** Organize courts by **Price (Low-High)** or **Name (A-Z)**.


* **Booking History:** Track status of requests (Pending vs. Confirmed).

### 🔐 Security & Persistence

* **Authentication:** Secure Login and Registration for Admins and Customers.
* **Data Persistence:** Custom file handling (`.csv` / `.txt`) ensures data remains saved after the application closes.

---

## 🧠 Concepts Used

This project demonstrates strong foundations in:

* **Data Structures (Manual Implementation)**
* **Queue:** For handling booking requests.
* **Linked List:** For dynamic data management.
* **ArrayList:** For flexible storage of objects.


* **Algorithms**
* **Binary Search:** Efficient data retrieval.
* **Sorting Algorithms:** Bubble/Selection sort logic.


* **GUI Development**
* `Java Swing`: Custom components, CardLayout navigation.
* `Graphics2D`: Custom rendering for buttons and gradients.


* **Object-Oriented Programming (OOP)**
* Encapsulation, Inheritance, and Polymorphism.



---

## 🛠 Technologies Used

* **Java (JDK 8+)**
* `javax.swing` — Graphical User Interface
* `java.awt` — Custom Graphics & Layouts
* `java.io` — File Handling for Database

No external libraries (like MySQL or Spring) are required.

---

## ▶ How to Run

1. **Compile the program:**
```bash
javac MainGUI.java

```


2. **Run the application:**
```bash
java MainGUI

```


3. **Login:**
* **Admin:** `furqan` / `furqan123` (Hardcoded for demo).
* **Customer:** Register a new account via the GUI.



---

## 📋 Dashboard Tabs

**Admin Interface:**

```
1. Analytics Home (Revenue & Stats)
2. Manage Courts (Add/Remove)
3. Pending Queue (Approve/Reject Requests)
4. All Bookings (Search & Sort)
5. Cancellation Mode

```

**Customer Interface:**

```
1. Find a Court (Search, Filter, Date Picker)
2. My Bookings (History & Status)

```

---

## 🎓 Academic Use

This project is well-suited for:

* **Data Structures & Algorithms** assignments (Queue/Search implementations).
* **Object-Oriented Programming** final projects.
* **Java GUI (Swing)** design demonstrations.
* **File Handling** and data persistence practice.

It combines theoretical algorithmic concepts with a practical, visual application.

---

## 🔮 Possible Enhancements

* Database integration (MySQL/SQLite)
* Email notifications for booking confirmation
* Payment gateway integration
* Network socket implementation (Client-Server)
* Exporting reports to PDF/Excel

---

## 📜 License

This project is open for educational and learning purposes.
You may freely modify or extend it.

---

## 👤 Author

Developed by **Furqan**
A robust Java application blending custom algorithms with modern GUI design.

---

⭐ If this project helped your learning, consider starring the repository.

---

## 📂 Project Structure

```text
FutsalPro/
│
├── main/
│   ├── MainGUI.java             # Program entry point
│   ├── LoginFrame.java          # Authentication UI
│   ├── AdminDashboard.java      # Admin GUI logic
│   ├── CustomerDashboard.java   # Customer GUI logic
│   └── StyleUtils.java          # UI styling helper
│
├── core/
│   ├── FutsalManager.java       # Business logic controller
│   ├── UserManager.java         # User authentication logic
│   └── Algorithms.java          # Search & Sort algorithms
│
├── models/
│   ├── User.java                # User entity
│   ├── FutsalGround.java        # Court entity
│   ├── Booking.java             # Booking entity
│   └── BookingRequest.java      # Queue node entity
│
└── data_structures/
    ├── BookingQueue.java        # Custom Queue implementation
    ├── CustomLinkedList.java    # Custom Linked List
    └── Node.java                # Generic Node class

```
