package com.siedlecki.mateusz.gacek.model;

import com.siedlecki.mateusz.gacek.settings.Settings;
import lombok.Getter;
import lombok.Setter;

@Getter
public enum Slm003Column {
    DIV(Settings.SLM003_DIV),
    SPECSHOP(Settings.SLM003_SPECSHOP),
    RANGE_GROUP(Settings.SLM003_RANGE_GROUP),
    NUMBER(Settings.SLM003_NUMBER),
    NAME(Settings.SLM003_NAME),
    SLID(Settings.SLM003_SLID),
    FCST(Settings.SLM003_FCST),
    SSQ(Settings.SLM003_SSQ),
    ASSQ(Settings.SLM003_ASSQ),
    AVGSALES(Settings.SLM003_AVGSALES),
    PAL_QTY(Settings.SLM003_PAL_QTY),
    AVAILABLE_STOCK(Settings.SLM003_AVAILABLE_STOCK),
    SGF_QTY(Settings.SLM003_SGF_QTY),
    VOLUME(Settings.SLM003_VOLUME),
    LOC_TYP(Settings.SLM003_LOC_TYP);

    private final Settings settings;
    @Setter
    private Integer index;

    Slm003Column(Settings settings) {
        this.settings = settings;
    }


}
