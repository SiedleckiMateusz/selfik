package com.siedlecki.mateusz.gacek.core;

import com.siedlecki.mateusz.gacek.core.model.IgnoredSlm003Value;
import com.siedlecki.mateusz.gacek.core.model.Slm003Column;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Constants {
    public static String SLM0003_SHEET_NAME = "Sheet1";
    public static int SLM0003_ROW_INDEX = 2;
    public static Set<IgnoredSlm003Value> SLM0003_IGNORED_VALUES = new HashSet<>();

    public static String IV020_SHEET_NAME = "Sheet1";
    public static int IV020_ROW_INDEX = 2;

    public static String PRENOT_SHEET_NAME = "SSFA";
    public static int PRENOT_ROW_INDEX = 1;

    public static LocalTime CPS_CUT_OF_TIME = LocalTime.of(16,0);

    public static String[] EXCEL_COLUMNS_NAMES;

    public static float[] COLUMNS_SIZE_FOR_TO_PREPARE_PDF = new float[]{9,33,43,6,9};
    public static List<String> HEADERS_FOR_TO_PREPARE_PDF = Arrays.asList("NR","NAZWA","SLID","PALET","STATUS");

    public static float[] COLUMNS_SIZE_FOR_L23_PDF = new float[]{61,9,10,10,10};
    public static List<String> HEADERS_FOR_L23_PDF = Arrays.asList("NAZWA","NR","L23[pal]","PRENOT[pal]","RAZEM [szt]");

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

    public static String convertNumber(double num){
        return String.format("%.2f", num);
    }
}
