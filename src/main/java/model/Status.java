package model;

import lombok.Getter;

@Getter
public enum Status {

    PENDING("1"),
    PREPARING("2"),
    READY("3"),
    DELIVERED("4");

    private final String code;

    Status(String code) {
        this.code = code;
    }

}
