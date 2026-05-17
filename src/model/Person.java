package model;

/**
 * Person - Abstract base class.
 * Parent class for Driver and Rider.
 * Demonstrates abstraction and polymorphism.
 */
public abstract class Person {

    private String id;
    private String name;
    private String email;
    private String phone;

    public Person(String id, String name, String email, String phone) {
        this.id    = id;
        this.name  = name;
        this.email = email;
        this.phone = phone;
    }

    /**
     * Abstract method - forces every subclass to
     * implement their own version (polymorphism)
     */
    public abstract String getDetails();

    // Getters
    public String getId()    { return id; }
    public String getName()  { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }

    // Setters
    public void setName(String name)   { this.name  = name; }
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }
}