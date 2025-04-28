package at.htl.bibliotheksverwaltung.model;

public class Customer {
    private int id;
    private String firstName;
    private String lastName;
    private String birthDay;
    private String birthMonth;
    private String birthYear;
    private String street;
    private String plz;
    private String region;

    public Customer(int id, String firstName, String lastName, String birthDay, String birthMonth, String birthYear, String street, String plz, String region) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDay = birthDay;
        this.birthMonth = birthMonth;
        this.birthYear = birthYear;
        this.street = street;
        this.plz = plz;
        this.region = region;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public String getBirthMonth() {
        return birthMonth;
    }

    public String getBirthYear() {
        return birthYear;
    }

    public String getStreet() {
        return street;
    }

    public String getPlz() {
        return plz;
    }

    public String getRegion() {
        return region;
    }
}
