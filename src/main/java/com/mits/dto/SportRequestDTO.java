package com.mits.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class SportRequestDTO {

    @NotBlank(message = "Sport name is required")
    @Size(min = 2, max = 50, message = "Sport name must be between 2 and 50 characters")
    private String sportName;

    @Size(max = 200, message = "Description must not exceed 200 characters")
    private String description;

    public SportRequestDTO() {
    }

    public String getSportName() {
        return sportName;
    }

    public void setSportName(String sportName) {
        this.sportName = sportName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}