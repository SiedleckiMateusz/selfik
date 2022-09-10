package com.siedlecki.mateusz.gacek.core.model;

import lombok.Getter;

public enum ProductStatus {
    DO_PRZERZUCENIA("DOSTAWA"),
    WRACA_PO_NIEDOSTEPNOSCI("WRACA");

    @Getter
    private final String opis;

    ProductStatus(String opis) {
        this.opis = opis;
    }
}
