package com.siedlecki.mateusz.gacek.core.model;

import lombok.Getter;
import lombok.Setter;

@Getter
public enum Slm003Column {
    DIV("DIV"),
    SPECSHOP("SPSHOP_NAME"),
    RANGE_GROUP("RG_DESC"),
    NUMBER("ART"),
    NAME("ARTNAME_UNICODE"),
    SLID("SLID"),
    FCST("FCST"),
    SSQ("SSQ"),
    ASSQ("ASSQ"),
    AVGSALES("AVGSALES"),
    PAL_QTY("C_PALQ"),
    AVAILABLE_STOCK("AVAILSTOCK"),
    SGF_QTY("QTYSGF"),
    VOLUME("VOL"),
    LOC_TYP("LOCTYP");

    private final String name;
    @Setter
    private Integer index;

    Slm003Column(String name) {
        this.name = name;
    }


}
