package com.siedlecki.mateusz.gacek.model;

import lombok.Getter;

public enum ProductStatus {
    DO_PRZYGOTOWANIA("DOSTAWA"),
    SPR_L23("L23"),
    SPR_L23_DO_PRZERZUCENIA("L23->P"),
    WRACA_PO_NIEDOSTEPNOSCI("WRACA");

    @Getter
    private final String opis;

    ProductStatus(String opis) {
        this.opis = opis;
    }
}
