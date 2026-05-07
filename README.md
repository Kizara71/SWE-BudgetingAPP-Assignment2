# 💰 Java Budgeting Application

A comprehensive desktop application for tracking personal finances, managing budgets, and achieving financial goals. Built with Java Swing and an SQLite database, featuring a modern dark-themed UI.

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white) ![SQLite](https://img.shields.io/badge/sqlite-%2307405e.svg?style=for-the-badge&logo=sqlite&logoColor=white)

---

## ✨ Features

### Core Functionality
- 📊 **Interactive Dashboard:** Get a real-time overview of your finances with visual summaries.
- 💸 **Transaction Management:** Easily log, edit, and track incomes and expenses.
- 🎯 **Budget Tracking:** Set custom limits for different spending categories and monitor your progress.
- 🏆 **Goal Management:** Create financial goals and watch your progress automatically update as you save.
- 🔔 **Smart Notifications:** Receive alerts when approaching or exceeding your budget limits.

### Advanced Features
- 🔐 **User Authentication:** Secure local accounts with signup and login functionality.
- 🎨 **Modern UI:** Sleek, dark-themed interface powered by FlatLaf for a premium experience.
- 💾 **Local Persistence:** All data is safely stored offline in a lightweight SQLite database.
- 📈 **Custom Reporting:** Generate and view detailed financial reports.

---

## 🚀 Installation & Setup

### Prerequisites
- Java Development Kit (JDK) 17 or higher
- Maven (for dependency management)
- Git (optional)

### Setup

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd <project-directory>
   ```

2. **Build the project**
   ```bash
   mvn clean install
   ```

3. **Run the Application**
   ```bash
   mvn exec:java -Dexec.mainClass="com.budgetapp.view.MainFrame"
   ```
   *Alternatively, run `MainFrame.java` directly from your IDE.*

---

## 📁 Project Structure

```text
SWE - Budgeting App/
├── src/main/java/com/budgetapp/
│   ├── controller/          # Business logic & flow control
│   │   ├── AuthController.java
│   │   ├── BudgetController.java
│   │   ├── DashboardController.java
│   │   └── ...
│   ├── model/               # Data structures & Entities
│   │   ├── Budget.java
│   │   ├── Category.java
│   │   ├── Transaction.java
│   │   ├── User.java
│   │   └── ...
│   ├── persistence/         # Database Access
│   │   └── DatabaseManager.java
│   └── view/                # UI Components (Java Swing)
│       ├── MainFrame.java
│       ├── DashboardView.java
│       ├── TransactionView.java
│       └── ...
├── pom.xml                  # Maven dependencies & config
├── budgetapp.db             # Generated SQLite database
└── README.md                # Project documentation
```

---

## 🧠 Architecture Details

### 📂 `model/`
Contains the core business objects.
- `Transaction`, `Income`, `Expense`: Financial record models.
- `User`, `SessionManager`: Handles user data and current session state.

### 📂 `view/`
The presentation layer built with Java Swing.
- `MainFrame`: The primary window that orchestrates different panels.
- `*View.java`: Individual screens like `DashboardView`, `BudgetView`, etc.

### 📂 `controller/`
Acts as the intermediary between the **Model** and **View**.
- Processes user input, updates the database, and refreshes the UI.

### 📂 `persistence/`
- `DatabaseManager`: Initializes the database schema and handles raw SQLite queries.

---

## 🛠️ Built With

- **[Java 17](https://jdk.java.net/17/)** - Core language
- **[Maven](https://maven.apache.org/)** - Build and Dependency Management
- **[SQLite JDBC](https://github.com/xerial/sqlite-jdbc)** - Database driver
- **[FlatLaf](https://www.formdev.com/flatlaf/)** - Modern Swing Look and Feel
- **[JDatePicker](https://github.com/JDatePicker/JDatePicker)** - Date selection UI components

---
