package data;

import java.sql.*;
import java.util.ArrayList;

public class TodoLocalDataSource {
    private String url;

    public TodoLocalDataSource() {
        this.url = "jdbc:sqlite:Todo.db";
        createDatabase();
    }

    /**
     * select all rows in the todo table
     */
    public ArrayList<Todo> selectAll() {
        String sql = "SELECT * FROM todo";

        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            ArrayList<Todo> todos = new ArrayList<>();
            while (rs.next()) {
                Todo temp = new Todo(
                        rs.getString("title"),
                        rs.getInt("complete") == 1
                );
                todos.add(temp);
            }
            return todos;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    /**
     * Insert a new row into the todo table
     *
     * @param todo data to be inserted
     */
    public boolean insert(Todo todo) {
        String sql = "INSERT INTO todo(title,complete) VALUES(?,?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, todo.getTitle());
            pstmt.setInt(2, todo.isComplete() ? 1 : 0);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Update data of a Todo specified by the title
     *
     * @param title title of Todo
     * @param isComplete isComplete of Todo
     */
    public boolean updateIsComplete(String title, boolean isComplete) {
        String sql = "UPDATE todo SET complete = ?"
                + "WHERE title = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, isComplete ? 1 : 0);
            pstmt.setString(2,title);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Create a new table in the Todo database
     */
    private void createDatabase() {
        String sql = "CREATE TABLE IF NOT EXISTS todo (\n"
                + "	id integer PRIMARY KEY,\n"
                + "	title text NOT NULL,\n"
                + "	complete integer\n"
                + ");";
        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Delete a completed todo specified by the title
     */
    public boolean deleteCompleted() {
        String sql = "DELETE FROM todo WHERE complete = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, 1);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Connect to the data.Todo.db database
     *
     * @return the Connection object
     */
    private Connection connect() {
        // SQLite connection string
        Connection conn = null;
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(url);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return conn;
    }
}
