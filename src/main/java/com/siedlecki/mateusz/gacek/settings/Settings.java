package com.siedlecki.mateusz.gacek.settings;

import com.siedlecki.mateusz.gacek.model.ValueType;
import com.siedlecki.mateusz.gacek.model.entity.Parameter;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Optional;

@Slf4j
public enum Settings {
    SLM003_SHEET_NAME("slm003.sheetName", "Sheet1", ValueType.TEXT, "Nazwa arkusza w pliku SLM003"),
    SLM003_ROW_INDEX("slm003.rowIndex", "2", ValueType.NUMBER, "Nr wiersza pliku SLM003 w którym są nagłówki, liczony od zera"),
    IV020_SHEET_NAME("iv020.sheetName", "Sheet1", ValueType.TEXT, "Nazwa arkusza w pliku IV020"),
    IV020_ROW_INDEX("iv020.rowIndex", "1", ValueType.NUMBER, "Nr wiersza pliku SLM003 w którym są nagłówki, liczony od zera"),

    SLM003_DIV("slm003.columnName.div", "DIV", ValueType.TEXT, "Nazwa kolumny DIV  w pliku SLM003"),
    SLM003_SPECSHOP("slm003.columnName.specshop", "SPSHOP_NAME", ValueType.TEXT, "Nazwa kolumny SPECSHOP  w pliku SLM003"),
    SLM003_RANGE_GROUP("slm003.columnName.rangeGroup", "RG_DESC", ValueType.TEXT, "Nazwa kolumny RANGE_GROUP  w pliku SLM003"),
    SLM003_NUMBER("slm003.columnName.number", "ARTNO", ValueType.TEXT, "Nazwa kolumny NUMBER  w pliku SLM003"),
    SLM003_NAME("slm003.columnName.artName", "ARTNAME_UNICODE", ValueType.TEXT, "Nazwa kolumny NAME  w pliku SLM003"),
    SLM003_SLID("slm003.columnName.slid", "SLID", ValueType.TEXT, "Nazwa kolumny SLID  w pliku SLM003"),
    SLM003_FCST("slm003.columnName.fcst", "FCST", ValueType.TEXT, "Nazwa kolumny FCST  w pliku SLM003"),
    SLM003_SSQ("slm003.columnName.ssq", "SSQ", ValueType.TEXT, "Nazwa kolumny SSQ  w pliku SLM003"),
    SLM003_ASSQ("slm003.columnName.assq", "ASSQ", ValueType.TEXT, "Nazwa kolumny ASSQ  w pliku SLM003"),
    SLM003_AVGSALES("slm003.columnName.avgSales", "AVGSALES", ValueType.TEXT, "Nazwa kolumny AVGSALES  w pliku SLM003"),
    SLM003_PAL_QTY("slm003.columnName.palQty", "C_PALQ", ValueType.TEXT, "Nazwa kolumny PAL_QTY  w pliku SLM003"),
    SLM003_AVAILABLE_STOCK("slm003.columnName.div", "AVAILSTOCK", ValueType.TEXT, "Nazwa kolumny AVAILABLE_STOCK  w pliku SLM003"),
    SLM003_SGF_QTY("slm003.columnName.sgfQty", "QTYSGF", ValueType.TEXT, "Nazwa kolumny SGF_QTY  w pliku SLM003"),
    SLM003_VOLUME("slm003.columnName.volume", "VOL", ValueType.TEXT, "Nazwa kolumny VOLUME  w pliku SLM003"),
    SLM003_LOC_TYP("slm003.columnName.locTyp", "LOCTYP", ValueType.TEXT, "Nazwa kolumny LOC_TYP  w pliku SLM003"),

    IV020_NUMBER("iv020.columnName.artNo", "ARTNO", ValueType.TEXT, "Nazwa kolumny ARTNO w pliku IV020"),
    IV020_SALESMETHOD("iv020.columnName.salesMethod", "SALESMETHOD", ValueType.TEXT, "Nazwa kolumny SALESMETHOD w pliku IV020"),
    IV020_RESERVED_QTY("iv020.columnName.reservedQty", "QTYRESERVEDINSTOCK", ValueType.TEXT, "Nazwa kolumny QTYRESERVEDINSTOCK w pliku IV020");

    @Getter
    private final String key;
    @Getter
    private final String description;
    @Getter
    private final ValueType valueType;
    private final String defaultValue;
    private String value;

    Settings(String key, String defaultValue, ValueType valueType, String description) {
        this.key = key;
        this.defaultValue = defaultValue;
        this.description = description;
        this.valueType = valueType;
    }

    public static void setValueIfExist(Parameter parameter) {
        Optional<Settings> any = Arrays.stream(values())
                .filter(s -> s.key.equals(parameter.getValue()))
                .findAny();
        if (any.isPresent()) {
            Settings settings = any.get();
            settings.value = parameter.getValue();
        }else {
            log.error("Key {} doesn't exists in the Settings",parameter.getKey());
        }
    }

    public String getValue() {
        return value == null ? defaultValue : value;
    }
}
