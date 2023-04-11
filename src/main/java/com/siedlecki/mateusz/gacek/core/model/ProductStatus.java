package com.siedlecki.mateusz.gacek.core.model;

import lombok.Getter;

public enum ProductStatus {
    DO_PRZERZUCENIA("DOSTAWA"),
    DO_SPRAWDZENIA_CZY_WEJDZIE("SPR CZY WEJDZIE -> L23"),
    DO_SPR_CZY_WEJDZIE_I_PRZERZUCIC("SPR CZY WEJDZIE -> L23 I PRZYGOTOWAĆ"),
    WRACA_PO_NIEDOSTEPNOSCI("WRACA");

    @Getter
    private final String opis;

    ProductStatus(String opis) {
        this.opis = opis;
    }
}
