package com.siedlecki.mateusz.gacek.core;



import com.siedlecki.mateusz.gacek.core.model.Column;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

public class Constants {
    public static String SLM0003_SHEET_NAME = "Sheet";
    public static int SLM0003_COLUMN_INDEX = 0;
    public static Set<Column> SLM0003_COLUMNS = new HashSet<>();
    public static Set<Column> SLM0003_IGNORED_VALUES = new HashSet<>();

    public static String PRENOT_SHEET_NAME = "SSFA";
    public static int PRENOT_COLUMN_INDEX = 1;
    public static Set<Column> PRENOT_COLUMNS = new HashSet<>();

    public static String RESULT_DIRECTORY_PATH = System.getProperty("user.dir")+"\\WYGENEROWANE PLIKI";
    public static String STATS_DIRECTORY_PATH = System.getProperty("user.dir")+"\\ST";
    public static String STATS_FILE_NAME = "STATS.ST";

    public static DateTimeFormatter SIMPLE_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

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

        PRENOT_COLUMNS.add(Column.builder().index(1).name("NUMER").build());
        PRENOT_COLUMNS.add(Column.builder().index(14).name("DO").build());
        PRENOT_COLUMNS.add(Column.builder().index(15).name("ILOŚĆ").build());

        EXCEL_COLUMNS_NAMES = new String[]{
                "SPECSHOP",
                "RANGE GR",
                "ART NR",
                "ART NAME",
                "LOCATIONS",
                "ASSQ",
                "PAL QTY",
                "AVALIABLE STOCK",
                "ON SALE PLACES",
                "FREE SPACE",
                "FREE SPACE[PQ]",
                "PRENOT +SGF[PQ]",
                "PRENOT ORDER[PQ]",
                "L23 ORDER[PQ]",
                "FREE SPACE AFTER ORDER"
        };
    }

    public static void getLogo(){
        System.out.println("*********************************************************************************************");
        System.out.println("*      ,o888888o.             .8.              ,o888888o.    8 8888888888   8 8888     ,88' *");
        System.out.println("*     8888     `88.          .888.            8888     `88.  8 8888         8 8888    ,88'  *");
        System.out.println("*  ,8 8888       `8.        :88888.        ,8 8888       `8. 8 8888         8 8888   ,88'   *");
        System.out.println("*  88 8888                 . `88888.       88 8888           8 8888         8 8888  ,88'    *");
        System.out.println("*  88 8888                .8. `88888.      88 8888           8 888888888888 8 8888 ,88'     *");
        System.out.println("*  88 8888               .8`8. `88888.     88 8888           8 8888         8 8888 88'      *");
        System.out.println("*  88 8888   8888888    .8' `8. `88888.    88 8888           8 8888         8 888888<       *");
        System.out.println("*  `8 8888       .8'   .8'   `8. `88888.   `8 8888       .8' 8 8888         8 8888 `Y8.     *");
        System.out.println("*     8888     ,88'   .888888888. `88888.     8888     ,88'  8 8888         8 8888   `Y8.   *");
        System.out.println("*      `8888888P'    .8'       `8. `88888.     `8888888P'    8 888888888888 8 8888     `Y8. *");
        System.out.println("*********************************************************************************************");
    }

    public static String logMessage(String message){
        String localDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
        return "["+localDateTime+"] "+message;
    }
}
