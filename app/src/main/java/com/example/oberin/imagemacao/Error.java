package com.example.oberin.imagemacao;

/**
 * Created by GUILHERME ANTONIO CAMELO on 03/06/15.
 */
public enum Error {
    DATABASE(0, "A database error has occured."),
    ERROR_TABLE_WORD_EMPTY(1, "This user already exists.");

    private final int code;
    private final String description;

    Error(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return code + ": " + description;
    }
}
