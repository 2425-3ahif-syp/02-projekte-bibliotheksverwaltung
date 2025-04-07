package at.htl.bibliotheksverwaltung.database;

import at.htl.bibliotheksverwaltung.model.Book;
import at.htl.bibliotheksverwaltung.model.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static final String URL = "jdbc:h2:./librarydb";
    private static final String USERNAME = "test";
    private static final String PASSWORD = "test";

    private static DatabaseManager instance;
    private Connection connection;

    private DatabaseManager() {
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            initializeDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    public void closeConnection() {
        try {
            if (connection != null)
                connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void initializeDatabase() throws SQLException {
        var createTableStatements = new String[]{
                """
            CREATE TABLE IF NOT EXISTS book (
                id IDENTITY PRIMARY KEY,
                title VARCHAR(255),
                rating INT,
                borrowed BOOLEAN,
                due_date VARCHAR(20)
            )
            """,
                """
            CREATE TABLE IF NOT EXISTS customer (
                id IDENTITY PRIMARY KEY,
                first_name VARCHAR(100),
                last_name VARCHAR(100),
                birth_day VARCHAR(2),
                birth_month VARCHAR(2),
                birth_year VARCHAR(4),
                street VARCHAR(255),
                plz VARCHAR(10),
                region VARCHAR(100)
            )
            """
        };

        try (var statement = connection.createStatement()) {
            for (var sql : createTableStatements) {
                statement.execute(sql);
            }
        }

        insertDummyData();
    }

    public void insertDummyData() {
        try (var statement = connection.createStatement()) {
            statement.execute("DELETE FROM book");
            statement.execute("DELETE FROM customer");

            statement.execute("""
                INSERT INTO book (title, rating, borrowed, due_date) VALUES
                ('Harry Potter und der Stein der Weisen', 5, TRUE, '25.03.2025'),
                ('Harry Potter und die Kammer des Schreckens', 4, FALSE, ''),
                ('Harry Potter und der Gefangene von Askaban', 5, FALSE, ''),
                ('Don Quijote', 5, FALSE, ''),
                ('Herr der Ringe 1', 4, TRUE, '01.04.2025'),
                ('Harry Potter und die Heiligtümer des Todes', 3, FALSE, ''),
                ('Der Verdacht', 2, FALSE, ''),
                ('Don Carlos', 1, FALSE, '')
            """);

            statement.execute("""
                INSERT INTO customer (first_name, last_name, birth_day, birth_month, birth_year, street, plz, region) VALUES
                ('Max', 'Mustermann', '01', '01', '2000', 'Musterstraße 1', '4020', 'Linz'),
                ('Anna', 'Musterfrau', '12', '02', '2001', 'Beispielweg 2', '4030', 'Linz')
            """);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // CRUD: Books

    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM book";
        try (var stmt = connection.createStatement();
             var rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                books.add(new Book(
                        rs.getString("title"),
                        rs.getInt("rating"),
                        rs.getBoolean("borrowed"),
                        rs.getString("due_date")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    public void addBook(String title, int rating) {
        String sql = "INSERT INTO book (title, rating, borrowed, due_date) VALUES (?, ?, false, '')";
        try (var stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, title);
            stmt.setInt(2, rating);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateBook(Book book) {
        String sql = "UPDATE book SET borrowed = ?, due_date = ? WHERE title = ?";
        try (var stmt = connection.prepareStatement(sql)) {
            stmt.setBoolean(1, book.isBorrowed());
            stmt.setString(2, book.getDueDate());
            stmt.setString(3, book.getTitle());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteBook(Book book) {
        String sql = "DELETE FROM book WHERE title = ?";
        try (var stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, book.getTitle());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM customer";
        try (var stmt = connection.createStatement();
             var rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                customers.add(new Customer(
                        rs.getInt("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("birth_day"),
                        rs.getString("birth_month"),
                        rs.getString("birth_year"),
                        rs.getString("street"),
                        rs.getString("plz"),
                        rs.getString("region")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }

    public void addCustomer(String first, String last, String day, String month, String year,
                            String street, String plz, String region) {
        String sql = "INSERT INTO customer (first_name, last_name, birth_day, birth_month, birth_year, street, plz, region) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (var stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, first);
            stmt.setString(2, last);
            stmt.setString(3, day);
            stmt.setString(4, month);
            stmt.setString(5, year);
            stmt.setString(6, street);
            stmt.setString(7, plz);
            stmt.setString(8, region);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
