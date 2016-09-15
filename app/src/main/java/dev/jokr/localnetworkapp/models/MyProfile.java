package dev.jokr.localnetworkapp.models;

import java.io.Serializable;

/**
 * Created by JoKr on 9/15/2016.
 */
public class MyProfile implements Serializable {

    private String firstName;
    private String lastName;

    public MyProfile(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }
}
