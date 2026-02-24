package org.breaze.data.entities;

public class Patient {

    private String id; // Documento
    private String name;
    private String lastName;
    private int age;
    private String email;
    private String gender;
    private String city;
    private String country;

    public Patient(String id, String name, String lastName, int age, String email, String gender, String city, String country) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.age = age;
        this.email = email;
        this.gender = gender;
        this.city = city;
        this.country = country;
    }

    // Getters y Setters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getLastName() { return lastName; }
    public int getAge() { return age; }
    public String getEmail() { return email; }
    public String getGender() { return gender; }
    public String getCity() { return city; }
    public String getCountry() { return country; }

    @Override
    public String toString() {
        return String.format("%s, %s %s (%d años) - %s", id, name, lastName, age, city);
    }
}