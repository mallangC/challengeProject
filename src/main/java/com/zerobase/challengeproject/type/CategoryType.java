package com.zerobase.challengeproject.type;

public enum CategoryType {

    COTE("COTE"),
    DRINKING("DRINKING"),
    DIET("DIET")
    ;

    private final String description;

    CategoryType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
