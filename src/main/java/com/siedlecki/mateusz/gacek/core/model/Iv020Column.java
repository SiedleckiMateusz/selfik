package com.siedlecki.mateusz.gacek.core.model;

import lombok.Getter;
import lombok.Setter;

@Getter
public enum Iv020Column {
    NUMBER("ARTNO"),
    SALESMETHOD("SALESMETHOD"),
    RESERVED_QTY("QTYRESERVEDINSTOCK");

    private final String name;
    @Setter
    private Integer index;

    Iv020Column(String name) {
        this.name = name;
    }


}
