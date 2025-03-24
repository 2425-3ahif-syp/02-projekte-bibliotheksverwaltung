package at.htl.bibliotheksverwaltung.model;

public class Book {
    private String title;
    private int rating;         // z.B. 1-5 Sterne
    private boolean borrowed;
    private String dueDate;     // z.B. "25.03.2025"

    public Book(String title, int rating, boolean borrowed, String dueDate) {
        this.title = title;
        this.rating = rating;
        this.borrowed = borrowed;
        this.dueDate = dueDate;
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
}
