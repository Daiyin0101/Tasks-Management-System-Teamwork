package edu.northeastern.course.TheCodeCommandos.Models;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.time.LocalDate;

public class Member implements Comparable<Member> {
    // Data field for Member
    private final String firstName;
    private final String lastName;
    private final String username;
    private final ObjectProperty<LocalDate> dateCreated;

    // Constructor
    public Member(String fName, String lName, String username, LocalDate date) {
        this.firstName = fName;
        this.lastName = lName;
        this.username = username;
        this.dateCreated = new SimpleObjectProperty<>(date);
    }

    // Four getter methods
    public String getFirstName() {return firstName;}

    public String getLastName() {return lastName;}

    public String getUsername() {return username;}

    public ObjectProperty<LocalDate> dateProperty() {return dateCreated;}

    // Delete member
    public void delete() {
        Model.getInstance().getDatabaseDriver().deleteMember(username);
    }

    @Override
    public int compareTo(Member m) {
        return this.dateCreated.get().compareTo(m.dateProperty().get());
    }
}