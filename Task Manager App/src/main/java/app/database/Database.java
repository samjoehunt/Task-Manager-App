package app.database;

import java.sql.*;
import org.mindrot.jbcrypt.BCrypt;

public class Database {
    /**
     * URL which connects to the database containing the user information and user tasks
     */
    private static final String databaseURL = "jdbc:sqlite:taskManager.db"; // Database file

    /**
     * Function to return the connection to the database
     * @return Connection to the database. If this is returned, connection to the database was successful
     */
    public static Connection connect() throws SQLException{
        Connection conn = DriverManager.getConnection(databaseURL);

        if (conn == null) {
            throw new SQLException("connect() method failed to establish a database connection.");
        }

        return conn;
    }

    /**
     * Creates two tables in the database. One to store user info, and one to store task info.
     * These tables are related via user_id.
     */
    public static void createTables() {
    String sql = """
                CREATE TABLE IF NOT EXISTS users (
                    user_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    username TEXT NOT NULL,
                    email TEXT NOT NULL,
                    password TEXT NOT NULL
                );
                CREATE TABLE IF NOT EXISTS tasks (
                    task_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    user_id INTEGER NOT NULL,
                    task_name TEXT NOT NULL,
                    task_description TEXT,
                    due_date DATE,
                    priority TEXT,
                    status TEXT NOT NULL,
                    FOREIGN KEY (user_id) REFERENCES users(user_id)
                );
                """;

        // Try to connect to the database and attach a statement to this connection
        try (Connection conn = connect();
             Statement statement = conn.createStatement()) {
            statement.execute(sql);
            System.out.println("Tables created");
        } catch (SQLException e) {
            System.out.println("Error creating tables: " + e.getMessage());
        }
    }

    /**
     * Deletes table containing user information
     */
    public static void dropUsersTable() {
        String sql = "DROP TABLE IF EXISTS users";

        // Try to connect to the database and attach a statement to this connection
        try (Connection conn = connect();
             Statement statement = conn.createStatement()) {
            statement.execute(sql);
            System.out.println("Users table dropped");
        } catch (SQLException e) {
            System.out.println("Error dropping table: " + e.getMessage());
        }
    }

    /**
     * Deletes table containing task information
     */
    public static void dropTasksTable() {
        String sql = "DROP TABLE IF EXISTS tasks";

        // Try to connect to the database and attach a statement to this connection
        try (Connection conn = connect();
             Statement statement = conn.createStatement()) {
            statement.execute(sql);
            System.out.println("Tasks table dropped");
        } catch (SQLException e) {
            System.out.println("Error dropping table: " + e.getMessage());
        }
    }

    /**
     * Function to identify if a user exists in the users table
     * @param username Username of the user to check
     * @return Boolean value indicating the specified user's existence in users.
     */
    public static boolean userExists(String username) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";

        try (Connection conn = connect();
             PreparedStatement preparedStatement = conn.prepareStatement(sql);) {
            preparedStatement.setString(1, username);
            ResultSet users = preparedStatement.executeQuery();
            if (users.next()) {
                return (users.getInt(1) > 0);
            }
        } catch (SQLException e) {
            System.out.println("Error finding user: " + e.getMessage());
        }
        return false;
    }

    /**
     * Function to insert a new user into the users table
     * @param username User's username
     * @param email User's email
     * @param password User's password
     */
    public static void insertUser(String username, String email, String password) {
        // Hash the password using the BCrypt hashing algorithm, to ensure that if the database
        // is breached, compromised passwords are effectively unusable
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(12));
        // By structuring the SQL statement in this way, SQL injections are prevented.
        String sql = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";

        // Try to establish a connection to the database and attach the statement to this connection
        try (Connection conn = connect();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, hashedPassword);
            preparedStatement.executeUpdate();
            System.out.println("Addition of user information successful");
        } catch (SQLException e) {
            System.out.println("Error inserting user: " + e.getMessage());
        }
    }

    /**
     * Function to insert a new task into the tasks table
     * @param userID Unique ID of the user who this task belongs to
     * @param taskName Name of the task
     * @param taskDescription Description of the task
     * @param dueDate String in format YYYY-MM-DD representing the task's deadline
     * @param priority String representing the task's priority
     * @param status String representing the task's completion status
     */
    public static void insertTask(String userID, String taskName, String taskDescription, String dueDate, String priority, String status) {
        // By structuring the SQL statement in this way, SQL injections are prevented.
        String sql = "INSERT INTO tasks (user_id, task_name, task_description, due_date, priority, status) VALUES (?, ?, ?, ?, ?, ?)";

        // Try to establish a connection to the database and attach the statement to this connection
        try (Connection conn = connect();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, userID);
            preparedStatement.setString(2, taskName);
            preparedStatement.setString(3, taskDescription);
            preparedStatement.setString(4, dueDate);
            preparedStatement.setString(5, priority);
            preparedStatement.setString(6, status);
            preparedStatement.executeUpdate();
            System.out.println("Addition of task information successful");
        } catch (SQLException e) {
            System.out.println("Error inserting task: " + e.getMessage());
        }
    }

    /**
     * Removes a user from the users table
     * @param username Username of user to be removed
     */
    public static void removeUser(String username) {
        String sql = "DELETE FROM users WHERE username = ?";

        try (Connection conn = connect();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, username);
            int usersDeleted = preparedStatement.executeUpdate();

            // Checks whether any users were found and deleted
            if (usersDeleted > 0) {
                System.out.println("User deleted");
            } else {
                System.out.println("User not found");
            }
        } catch (SQLException e) {
            System.out.println("Error removing user: " + e.getMessage());
        }
    }

    /**
     * Removes a task from the tasks table
     * @param taskID Unique ID of the task to be removed
     */
    public static void removeTask(String taskID) {
        String sql = "DELETE FROM tasks WHERE task_id = ?";

        try (Connection conn = connect();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, taskID);
            int tasksDeleted = preparedStatement.executeUpdate();

            // Checks whether any tasks were found and deleted
            if (tasksDeleted > 0) {
                System.out.println("Task deleted");
            } else {
                System.out.println("Task not found");
            }
        } catch (SQLException e) {
            System.out.println("Error removing task: " + e.getMessage());
        }
    }

    /**
     * Function to change a specific task in tasks table's name
     * @param taskID Unique ID of specified task
     * @param newTaskName New name for the task
     */
    public static void changeTaskName (String taskID, String newTaskName) {
        String sql = "UPDATE tasks SET task_name = ? WHERE task_id = ?";

        try (Connection conn = connect();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, newTaskName);
            preparedStatement.setString(2, taskID);

            int affectedTasks = preparedStatement.executeUpdate();

            // If any task names are changed
            if (affectedTasks > 0) {
                System.out.println("Task name updated successfully");
            } else {
                System.out.println("Task not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error changing task name: " + e.getMessage());
        }
    }

    /**
     * Function to change a specified task's description
     * @param taskID Unique ID of the specified task
     * @param newTaskDescription String containing the task's new description
     */
    public static void changeTaskDescription (String taskID, String newTaskDescription) {
        String sql = "UPDATE tasks SET task_description = ? WHERE task_id = ?";

        try (Connection conn = connect();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, newTaskDescription);
            preparedStatement.setString(2, taskID);

            int affectedTasks = preparedStatement.executeUpdate();

            // If any task descriptions are changed
            if (affectedTasks > 0) {
                System.out.println("Task description updated successfully");
            } else {
                System.out.println("Task not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error changing task description: " + e.getMessage());
        }
    }

    /**
     * Function to change a specified task's due date
     * @param taskID Unique ID of the specified task
     * @param newTaskDueDate New due date of the specified task in YYYY-MM-DD format
     */
    public static void changeTaskDueDate (String taskID, String newTaskDueDate) {
        String sql = "UPDATE tasks SET due_date = ? WHERE task_id = ?";

        try (Connection conn = connect();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, newTaskDueDate);
            preparedStatement.setString(2, taskID);

            int affectedTasks = preparedStatement.executeUpdate();

            // If any task due dates are changed
            if (affectedTasks > 0) {
                System.out.println("Task due date updated successfully");
            } else {
                System.out.println("Task not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error changing task due date: " + e.getMessage());
        }
    }

    /**
     * Function to change the priority of a specified task
     * @param taskID Unique ID of the specified task
     * @param newTaskPriority New priority to be applied to the specified task
     */
    public static void changeTaskPriority (String taskID, String newTaskPriority) {
        String sql = "UPDATE tasks SET priority = ? WHERE task_id = ?";

        try (Connection conn = connect();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, newTaskPriority);
            preparedStatement.setString(2, taskID);

            int affectedTasks = preparedStatement.executeUpdate();

            // If any task descriptions are changed
            if (affectedTasks > 0) {
                System.out.println("Task priority updated successfully");
            } else {
                System.out.println("Task not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error changing task priority: " + e.getMessage());
        }
    }

    /**
     * Function to change the status of a specified task
     * @param taskID Unique ID of the specified task
     * @param newTaskStatus New status to be applied to the specified task
     */
    public static void changeTaskStatus (String taskID, String newTaskStatus) {
        String sql = "UPDATE tasks SET status = ? WHERE task_id = ?";

        try (Connection conn = connect();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, newTaskStatus);
            preparedStatement.setString(2, taskID);

            int affectedTasks = preparedStatement.executeUpdate();

            // If any task statuses are changed
            if (affectedTasks > 0) {
                System.out.println("Task status updated successfully");
            } else {
                System.out.println("Task not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error changing task status: " + e.getMessage());
        }
    }

    /**
     * Function to be called when a user tries to log in
     * @param username Entered username
     * @param password Entered password
     * @return Boolean value indicating whether the user can be authenticated or not
     */
    public static boolean authenticateUser(String username, String password) {
        String sql = "SELECT password FROM users WHERE username = ?";

        try (Connection conn = connect();
             PreparedStatement preparedStatement = conn.prepareStatement(sql);) {

            preparedStatement.setString(1, username);
            ResultSet userInfo = preparedStatement.executeQuery();

            // Checks if the user exists in the database
            if (userInfo.next()) {
                String hashedPassword = userInfo.getString("password"); // Retrieves hashed password from database
                if (BCrypt.checkpw(password, hashedPassword)) { // Checks if the hash of the input password is the same as the hashed password retrieved from the database
                    System.out.println("Login successful");
                    return true;
                } else {
                    System.out.println("Invalid password");
                }
            } else {
                System.out.println("User not found");
            }
        } catch (SQLException e) {
            System.out.println("Error authenticating user: " + e.getMessage());
        }

        return false;
    }

    public static void main(String[] args) {

    }
}
