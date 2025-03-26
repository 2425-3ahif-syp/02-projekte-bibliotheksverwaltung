package at.htl.bibliotheksverwaltung.model;

import java.util.ArrayList;
import java.util.List;

public class DataStore {

    public static List<Book> books = new ArrayList<>();
    public static List<Customer> customers = new ArrayList<>();
    private static int nextCustomerId = 1;

    static {
        // Beispiel-Bücher
        books.add(new Book("Harry Potter und der Stein der Weisen", 5, true, "25.03.2025"));
        books.add(new Book("Harry Potter und die Kammer des Schreckens", 4, false, ""));
        books.add(new Book("Harry Potter und der Gefangene von Askaban", 5, false, ""));
        books.add(new Book("Don Quijote", 5, false, ""));
        books.add(new Book("Herr der Ringe 1", 4, true, "01.04.2025"));
        books.add(new Book("Harry Potter und die Heiligtümer des Todes", 3, false, ""));
        books.add(new Book("Der Verdacht", 2, false, ""));
        books.add(new Book("Don Carlos", 1, false, ""));

        // Beispiel-Kunden
        customers.add(new Customer(nextCustomerId++, "Max", "Mustermann", "1", "1", "2000", "Musterstraße 1", "1234", "Musterstadt"));
        customers.add(new Customer(nextCustomerId++, "Erika", "Musterfrau", "2", "2", "2001", "Musterstraße 2", "1234", "Musterstadt"));
    }

    public static void addCustomer(String firstName, String lastName, String birthDay, String birthMonth, String birthYear, String street, String plz, String region) {
        customers.add(new Customer(nextCustomerId++, firstName, lastName, birthDay, birthMonth, birthYear, street, plz, region));
    }

    public static void addBook(String title, int rating) {
        books.add(new Book(title, rating, false, ""));
    }
}
