package com.zerobase.challengeproject.type;

public enum Category {

    COTE("COTE"),
    DRINKING("DRINKING"),
    DIET("DIET")
    ;

    private final String description;

    Category(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
