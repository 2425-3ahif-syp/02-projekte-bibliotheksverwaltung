package at.htl.bibliotheksverwaltung.model;

public class Book {
    private String title;
    private int rating;         // z.B. 1-5 Sterne
    private boolean borrowed;
    private String dueDate;     // z.B. "25.03.2025"
    private int id;
    private int customerId;

    public Book(String title, int rating, boolean borrowed, String dueDate, int id, int customerId) {
        this.title = title;
        this.rating = rating;
        this.borrowed = borrowed;
        this.dueDate = dueDate;
        this.id = id;
        this.customerId = customerId;
    }

    public String getTitle() {
        return title;
    }

    public int getRating() {
        return rating;
    }

    public boolean isBorrowed() {
        return borrowed;
    }

    public void setBorrowed(boolean borrowed) {
        this.borrowed = borrowed;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public int getId() {
        return id;
    }

    public int getCustomerId() {return customerId;}

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }


    public void setTitle(String text) {
        this.title = text;
    }
    public void setRating(int rating) {
        this.rating = rating;
    }
}
