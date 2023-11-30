package com.siedlecki.mateusz.gacek.settings;

import com.siedlecki.mateusz.gacek.model.IgnoredSlm003Value;
import com.siedlecki.mateusz.gacek.model.Slm003Column;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Utils {
    public static String SLM0003_SHEET_NAME = "Sheet1";
    public static int SLM0003_ROW_INDEX = 2;
    public static Set<IgnoredSlm003Value> SLM0003_IGNORED_VALUES = new HashSet<>();

    public static String IV020_SHEET_NAME = "Sheet1";
    public static int IV020_ROW_INDEX = 2;

    public static String PRENOT_SHEET_NAME = "SSFA";
    public static int PRENOT_ROW_INDEX = 1;

    public static String[] EXCEL_COLUMNS_NAMES;

    public static float[] COLUMNS_SIZE_FOR_TO_PREPARE_PDF = new float[]{9, 5, 6, 9, 39, 33};
    public static List<String> HEADERS_FOR_TO_PREPARE_PDF = Arrays.asList("NR", "PQ", "ORDER", "STATUS", "SLID", "NAZWA");

    static {

        SLM0003_IGNORED_VALUES.add(IgnoredSlm003Value.builder().column(Slm003Column.DIV).value("WH").build());
        SLM0003_IGNORED_VALUES.add(IgnoredSlm003Value.builder().column(Slm003Column.DIV).value("MH").build());
        SLM0003_IGNORED_VALUES.add(IgnoredSlm003Value.builder().column(Slm003Column.RANGE_GROUP).value("Activity area 4").build());
        SLM0003_IGNORED_VALUES.add(IgnoredSlm003Value.builder().column(Slm003Column.SPECSHOP).value("Furniture").build());
        SLM0003_IGNORED_VALUES.add(IgnoredSlm003Value.builder().column(Slm003Column.SLID).value("NoSLID").build());
        SLM0003_IGNORED_VALUES.add(IgnoredSlm003Value.builder().column(Slm003Column.SLID).value("REC027").build());
        SLM0003_IGNORED_VALUES.add(IgnoredSlm003Value.builder().column(Slm003Column.SLID).value("BIZNES").build());

        EXCEL_COLUMNS_NAMES = new String[]{
                "SPECSHOP",
                "RANGE GR",
                "ART NR",
                "ART NAME",
                "LOCATIONS",
                "ASSQ",
                "PAL QTY",
                "AVAILABLE STOCK",
                "ON SALE PLACES",
                "FREE SPACE",
                "FREE SPACE[PQ]",
                "PRENOT +SGF[PQ]",
                "PRENOT ORDER[PQ]",
                "L23 ORDER[PQ]",
                "FREE SPACE AFTER ORDER"
        };
    }

    public static String convertNumber(double num) {
        return String.format("%.2f", num);
    }
}
