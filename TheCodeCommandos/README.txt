Trello - by TheCodeCommandos

About:

This project is a task management application developed using JavaFX, inspired by Trello. It allows users to create, manage, and organize tasks in a visually intuitive manner.

Features:

User Authentication: Sign up, log in, and log out functionality.
Task Management: Create, edit, delete, and organize tasks.
Boards and Lists: Create multiple boards and use lists to categorize tasks.
Move tasks between lists: Move tasks within "To-do", "Doing", and "Done" through button.
Persistence: Save tasks and boards locally using SQLite database.
Responsive UI: User-friendly and responsive interface.

Installation:

Download JDK 17 or later for your operating system. Make sure JAVA_HOME is properly set to the JDK installation directory.
For the first time only:
Install e(fx)clipse 3.8.0 and Maven Integration for Eclipse m2e plugin from Eclipse Marketplace.
Clone the sample, open it with Eclipse, and make sure the paths for JDK and JavaFX match those on your machine.
Click Run -> Run As -> Maven Build -> New launch configuration to create a new configuration. Name it TheCodeCommandos, and add the required goals:
clean javafx:run
Run the project Run -> Run As -> Maven Build -> TheCodeCommandos -> Run.
Login using username "@yPeng10" and password"123".


Database:
This application uses SQLite for data storage. The database file is included in the project directory.
Database Schema:
The database consists of the following tables:
Members: Stores members information (ID, FirstName, LastName, Username, Password, Date).
Boards: Stores board information (ID, BoardTitle, Description, Date).
Cards: Stores task information (ID, CardName, Status, Date, Board).

Usage:
After starting the application, you can sign up for a new account or log in with an existing account. Once logged in, you can create new boards, and start adding tasks. Use button to move tasks between lists and organize your workflow.


Contact:
TheCodeCommandos - peng.yu3@northeasten.edu
GitHub - https://github.com/YuPeng1995/TheCodeCommandos

Acknowledgements:
JavaFX - The toolkit used for building the UI.
SQLite - The database engine used for data storage.
Trello - Inspiration for the project.

