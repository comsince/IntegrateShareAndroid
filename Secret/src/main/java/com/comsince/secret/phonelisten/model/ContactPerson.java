package com.comsince.secret.phonelisten.model;

/**
 * Created by liaojinlong on 14-11-12.
 */
public class ContactPerson {
    private String name;
    private String number;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "ContactPerson{" +
                "name='" + name + '\'' +
                ", number='" + number + '\'' +
                '}';
    }
}
