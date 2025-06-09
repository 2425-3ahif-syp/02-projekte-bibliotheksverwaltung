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

    /**
     * Legt die Tabellen an (inklusive customer_id in book) und befüllt sie mit Dummy-Daten.
     */
    public void initializeDatabase() throws SQLException {
        String[] createTableStatements = new String[] {
                """
            CREATE TABLE IF NOT EXISTS book (
                id IDENTITY PRIMARY KEY,
                title VARCHAR(255),
                rating INT,
                borrowed BOOLEAN,
                due_date VARCHAR(20),
                customer_id BIGINT
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

        try (Statement stmt = connection.createStatement()) {
            for (String sql : createTableStatements) {
                stmt.execute(sql);
            }
        }

        insertDummyData();
    }

    public void insertDummyData() {
        try (Statement stmt = connection.createStatement()) {
            // Alte Daten löschen
            stmt.execute("TRUNCATE TABLE book RESTART IDENTITY");
            stmt.execute("TRUNCATE TABLE customer RESTART IDENTITY");

            // Dummy-Kunden
            stmt.execute("""
                INSERT INTO customer (first_name, last_name, birth_day, birth_month, birth_year, street, plz, region) VALUES
                ('Max', 'Mustermann', '01', '01', '2000', 'Musterstraße 1', '4020', 'Linz'),
                ('Anna', 'Musterfrau',  '12', '02', '2001', 'Beispielweg 2', '4030', 'Linz')
            """);

            // Dummy-Bücher
            stmt.execute("""
                INSERT INTO book (title, rating, borrowed, due_date, customer_id) VALUES
                  ('Harry Potter und der Stein der Weisen',    5, TRUE,  '25.03.2025', 1),
                  ('Harry Potter und die Kammer des Schreckens',4, FALSE, '',          NULL),
                  ('Harry Potter und der Gefangene von Askaban',5, FALSE, '',          NULL),
                  ('Don Quijote',                              5, FALSE, '',          NULL),
                  ('Herr der Ringe 1',                         4, TRUE,  '01.04.2025', 2),
                  ('Harry Potter und die Heiligtümer des Todes',3, FALSE, '',          NULL),
                  ('Der Verdacht',                             2, FALSE, '',          NULL),
                  ('Don Carlos',                               1, FALSE, '',          NULL)
            """);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // CRUD: Books
    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM book";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int    id          = rs.getInt("id");
                String  title       = rs.getString("title");
                int     rating      = rs.getInt("rating");
                boolean borrowed    = rs.getBoolean("borrowed");
                String  dueDate     = rs.getString("due_date");
                int     cid        = rs.getInt("customer_id");
                if (rs.wasNull()) {
                    cid = 0;
                }

                books.add(new Book(
                        title,
                        rating,
                        borrowed,
                        dueDate,
                        id,
                        cid
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    public void addBook(String title, int rating) {
        String sql = "INSERT INTO book (title, rating, borrowed, due_date, customer_id) VALUES (?, ?, false, '', NULL)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, title);
            stmt.setInt(2, rating);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateBook(Book book) {
        String sql = """
            UPDATE book
               SET borrowed    = ?,
                   due_date    = ?,
                   customer_id = ?
             WHERE id = ?
            """;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setBoolean(1, book.isBorrowed());
            stmt.setString(2, book.getDueDate());
            if (book.getCustomerId() != 0) {
                stmt.setInt(3, book.getCustomerId());
            } else {
                stmt.setNull(3, Types.BIGINT);
            }
            stmt.setInt(4, book.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteBook(Book book) {
        String sql = "DELETE FROM book WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, book.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // CRUD: Customers
    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM customer";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

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

    public void printAllCustomerIDs() {
        String sql = "SELECT id FROM customer";
        try (var stmt = connection.prepareStatement(sql);
             var rs = stmt.executeQuery()) {

            System.out.println("Customer IDs:");
            while (rs.next()) {
                int id = rs.getInt("id");
                System.out.println(id);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addCustomer(String first, String last, String day, String month, String year,
                            String street, String plz, String region) {
        String sql = """
            INSERT INTO customer (
                first_name, last_name,
                birth_day, birth_month, birth_year,
                street, plz, region
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
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

    public boolean customerExists(int id) {
        String sql = "SELECT COUNT(*) FROM customer WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Customer getCustomerById(int id) {
        String sql = "SELECT * FROM customer WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Customer(
                            rs.getInt("id"),
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getString("birth_day"),
                            rs.getString("birth_month"),
                            rs.getString("birth_year"),
                            rs.getString("street"),
                            rs.getString("plz"),
                            rs.getString("region")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateCustomer(int id, String firstName, String lastName,
                                  int birthDay, int birthMonth, int birthYear,
                                  String street, String plz, String region) {
        String sql = """
            UPDATE customer SET
                first_name = ?, last_name = ?,
                birth_day  = ?, birth_month = ?, birth_year = ?,
                street     = ?, plz = ?, region = ?
             WHERE id = ?
            """;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            stmt.setInt(3, birthDay);
            stmt.setInt(4, birthMonth);
            stmt.setInt(5, birthYear);
            stmt.setString(6, street);
            stmt.setString(7, plz);
            stmt.setString(8, region);
            stmt.setInt(9, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
