package com.mits.dto;

import java.util.List;

public class SportAdminRequestLogin {

    private String name;
    private String email;
    private String password;
    
    // ✅ NEW: List of Sport IDs to assign to this admin
    private List<Long> sportIds;

    public SportAdminRequestLogin() {
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    // ✅ NEW: Getters & Setters for sportIds
    public List<Long> getSportIds() { return sportIds; }
    public void setSportIds(List<Long> sportIds) { this.sportIds = sportIds; }
}