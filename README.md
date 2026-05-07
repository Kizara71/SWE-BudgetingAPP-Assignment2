Budgeting App - Project Overview

1. Tools and Technologies Used
------------------------------
This project was developed using the following tools and libraries:
* Java (JDK 17): The core programming language used to build the application logic and UI.
* Maven: Dependency management and build automation tool (configuration in `pom.xml`).
* Java Swing: The native Java GUI toolkit used to build the desktop interface.
* SQLite (JDBC Driver v3.45.1.0): A lightweight, file-based relational database used for local data persistence.
* FlatLaf (v3.4): A modern, custom look-and-feel library used to provide a clean, modern, dark-themed UI over standard Java Swing components.
* JDatePicker (v1.3.4): A custom UI component library used to provide user-friendly date-picker dropdowns for transactions and budgets.

2. Files and Architecture Included
----------------------------------
The application follows the Model-View-Controller (MVC) architectural pattern.

[Project Root Files]
* pom.xml: The Maven build file defining dependencies and compiler settings.
* budgetapp.db: The local SQLite database file that stores user accounts, transactions, budgets, and goals.
* .gitignore: Instructs Git on which files/directories (like /target/) to ignore.
* Sequance Diagram 1.txt - 8.txt: Textual representations of system sequence diagrams detailing control flow.
* State Diagram.txt: Textual representation of the system's state transitions.
* diagram.txt: UML or general structural documentation.

[Source Code: src/main/java/com/budgetapp/]

Model Layer (Business Logic & Entities)
* User.java: Represents user accounts and authentication credentials.
* Transaction.java: Base abstract class for financial records.
* Income.java / Expense.java: Concrete classes extending Transaction to represent incoming/outgoing funds.
* Budget.java: Represents a user's defined budget limits over specific periods.
* Goal.java: Represents financial targets and tracks progress.
* Category.java: Defines the classification system for transactions and budgets.
* Notification.java: Represents system alerts (e.g., nearing a budget limit).
* SessionManager.java: Singleton class that tracks the currently authenticated user session.
* IPersistable.java: Interface defining save/update functionality for models.

Persistence Layer (Database Access)
* DatabaseManager.java: Handles the SQLite connection lifecycle, creates necessary tables, and provides core SQL execution utilities.

Controller Layer (Logic linking Models & Views)
* AuthController.java: Handles login and registration logic.
* DashboardController.java: Aggregates data for the main overview dashboard.
* TransactionController.java: Handles validation and processing of income/expenses.
* BudgetController.java: Processes budget creation and checks for budget limit breaches.
* GoalController.java: Manages goal updates based on financial activity.
* ProfileController.java: Manages user preference and profile updates.
* NotifController.java: Triggers and dismisses user notifications.
* BaseController.java: Provides shared functionality across all controllers.

View Layer (UI/Screens)
* MainFrame.java: The core JFrame application window that swaps out different view panels.
* AuthView.java: The UI panel for user login and signup.
* DashboardView.java: The main landing panel showing financial summaries.
* TransactionView.java: The panel containing forms and tables for managing transactions.
* BudgetView.java: The panel for setting and tracking budget limits.
* GoalView.java: The panel for visual goal tracking.
* ProfileView.java: The panel for editing user settings.
* ReportsView.java: The panel for generating financial reports.
* IDashboardView.java, ITransactionView.java, IBudgetView.java: Interfaces standardizing UI updates for their respective controllers.
