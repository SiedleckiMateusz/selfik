package com.siedlecki.mateusz.gacek.core.model;

import lombok.Getter;

@Getter
public enum Specshop {
    PARZYSTA("P"),
    NIEPARZYSTA("NP"),
    OTHER("-");

    private final String shortName;

    Specshop(String shortName) {
        this.shortName = shortName;
    }
}
