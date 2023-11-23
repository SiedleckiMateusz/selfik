package com.siedlecki.mateusz.gacek.core.model;

import lombok.Getter;
import lombok.Setter;

@Getter
public enum PrenotColumn {
    NUMER("ART NO"),
    DO("TO"),
    ILOSC("QTY");

    private final String name;
    @Setter
    private Integer index;

    PrenotColumn(String name) {
        this.name = name;
    }
}
