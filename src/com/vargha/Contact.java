package com.vargha;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Contact {
    private SimpleStringProperty id;
    private SimpleStringProperty name;
    private SimpleStringProperty phone;
    private SimpleStringProperty email;

    Contact(int id, String name, String phone, String email) {
        this.id = new SimpleStringProperty(Integer.toString(id));
        this.name = new SimpleStringProperty(name);
        this.phone = new SimpleStringProperty(phone);
        this.email = new SimpleStringProperty(email);
    }

    public StringProperty nameProperty(){
        return name;
    }

    public StringProperty idProperty(){
        return id;
    }

    public StringProperty phoneProperty(){
        return phone;
    }

    public StringProperty emailProperty(){
        return email;
    }

    public String getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id = new SimpleStringProperty(Integer.toString(id));
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name = new SimpleStringProperty(name);
    }

    public String getPhone() {
        return phone.get();
    }

    public void setPhone(String phone) {
        this.phone = new SimpleStringProperty(phone);
    }

    public String getEmail() {
        return email.get();
    }

    public void setEmail(String email) {
        this.email  = new SimpleStringProperty(email);
    }
}
