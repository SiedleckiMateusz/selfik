package com.siedlecki.mateusz.gacek.core.model;

import lombok.Getter;

public enum ProductStatus {
    DO_PRZERZUCENIA("Przygotować na dostawę"),
    WRACA_PO_NIEDOSTEPNOSCI("Wraca po niedostępności");

    @Getter
    private final String opis;

    ProductStatus(String opis) {
        this.opis = opis;
    }
}
