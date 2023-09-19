package com.siedlecki.mateusz.gacek.core;

import com.siedlecki.mateusz.gacek.core.model.Column;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Constants {
    public static String SLM0003_SHEET_NAME = "Sheet";
    public static int SLM0003_ROW_INDEX = 0;
    public static Set<Column> SLM0003_COLUMNS = new HashSet<>();
    public static Set<Column> SLM0003_IGNORED_VALUES = new HashSet<>();

    public static String PRENOT_SHEET_NAME = "SSFA";
    public static int PRENOT_ROW_INDEX = 1;
    public static Set<Column> PRENOT_COLUMNS = new HashSet<>();

    public static String OPQ_SHEET_NAME = "Sheet";
    public static int OPQ_ROW_INDEX = 0;
    public static Set<Column> OPQ_COLUMNS = new HashSet<>();

    public static String FPS_SERVICE_PROVIDER = "32081";
    public static LocalTime CPS_CUT_OF_TIME = LocalTime.of(16,0);

    public static String[] EXCEL_COLUMNS_NAMES;

    static {
        SLM0003_COLUMNS.add(Column.builder().index(0).name("DIV").build());
        SLM0003_COLUMNS.add(Column.builder().index(1).name("SPECSHOP_UNICODE").build());
        SLM0003_COLUMNS.add(Column.builder().index(2).name("RANGEGROUP_UNICODE").build());
        SLM0003_COLUMNS.add(Column.builder().index(5).name("ARTNO").build());
        SLM0003_COLUMNS.add(Column.builder().index(6).name("ARTNAME_UNICODE").build());
        SLM0003_COLUMNS.add(Column.builder().index(7).name("SLID").build());
        SLM0003_COLUMNS.add(Column.builder().index(14).name("FCST").build());
        SLM0003_COLUMNS.add(Column.builder().index(16).name("SSQ").build());
        SLM0003_COLUMNS.add(Column.builder().index(42).name("AVGSALES").build());
        SLM0003_COLUMNS.add(Column.builder().index(31).name("C_PALQ").build());
        SLM0003_COLUMNS.add(Column.builder().index(43).name("AVAILSTOCK").build());
        SLM0003_COLUMNS.add(Column.builder().index(44).name("QTYSGF").build());
        SLM0003_COLUMNS.add(Column.builder().index(46).name("VOL").build());

        SLM0003_IGNORED_VALUES.add(Column.builder().index(0).name("WH").build());
        SLM0003_IGNORED_VALUES.add(Column.builder().index(0).name("MH").build());
        SLM0003_IGNORED_VALUES.add(Column.builder().index(2).name("Activity area 4").build());
        SLM0003_IGNORED_VALUES.add(Column.builder().index(1).name("Furniture").build());
        SLM0003_IGNORED_VALUES.add(Column.builder().index(7).name("NoSLID").build());
        SLM0003_IGNORED_VALUES.add(Column.builder().index(7).name("REC027").build());
        SLM0003_IGNORED_VALUES.add(Column.builder().index(7).name("BIZNES").build());

        PRENOT_COLUMNS.add(Column.builder().index(1).name("ART NO").build());
        PRENOT_COLUMNS.add(Column.builder().index(14).name("TO").build());
        PRENOT_COLUMNS.add(Column.builder().index(15).name("QTY").build());

        OPQ_COLUMNS.add(Column.builder().index(2).name("ARTNO").build());
        OPQ_COLUMNS.add(Column.builder().index(7).name("OPEN_PICK_QTY").build());
        OPQ_COLUMNS.add(Column.builder().index(19).name("CUT_OFF_DATE").build());
        OPQ_COLUMNS.add(Column.builder().index(20).name("CUT_OFF_TIME").build());

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
}
