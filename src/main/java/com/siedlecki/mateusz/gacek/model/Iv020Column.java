package com.siedlecki.mateusz.gacek.model;

import com.siedlecki.mateusz.gacek.settings.Settings;
import lombok.Getter;
import lombok.Setter;

@Getter
public enum Iv020Column {
    NUMBER(Settings.IV020_NUMBER),
    SALESMETHOD(Settings.IV020_SALESMETHOD),
    RESERVED_QTY(Settings.IV020_RESERVED_QTY);

    private final Settings settings;
    @Setter
    private Integer index;

    Iv020Column(Settings settings) {
        this.settings = settings;
    }


}
