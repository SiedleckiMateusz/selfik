package com.siedlecki.mateusz.gacek.core.model.opq;

import lombok.Getter;

public enum PickArea {
    SELF("Self Serve"),
    FULL("Full Serve internal"),
    MARKETHALL("Markethall"),
    NONE("");

    @Getter
    private final String tabName;

    PickArea(String tabName) {
        this.tabName = tabName;
    }
}
