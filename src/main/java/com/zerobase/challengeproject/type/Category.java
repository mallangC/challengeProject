package com.zerobase.challengeproject.type;

import lombok.Getter;

@Getter
public enum Category {

    COTE("COTE"),
    DRINKING("DRINKING"),
    DIET("DIET")
    ;

    private final String description;

    Category(String description) {
        this.description = description;
    }

}
