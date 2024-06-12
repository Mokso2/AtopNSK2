package com.example.atopnsk2;

public class EmployeeProfile {
    private String email;
    private String password;
    private Boolean director;
    public EmployeeProfile(String email, String password, Boolean director) {
        this.email = email;
        this.password = password;
        this.director = director;
    }

    public EmployeeProfile() {

    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean isDirector() {
        return director;
    }

    public void setDirector(Boolean director) {
        this.director = director;
    }




}
