package com.siedlecki.mateusz.gacek.core.reader;


import static com.siedlecki.mateusz.gacek.core.Constants.*;

public class SheetReaderFactory {
    private static SheetReader slm0003Reader;
    private static SheetReader prenotReader;

    public static SheetReader getSlm0003Reader(){
        if (slm0003Reader==null) {
            slm0003Reader = new SheetReader(SLM0003_SHEET_NAME, SLM0003_COLUMN_INDEX, SLM0003_COLUMNS);
        }
        return slm0003Reader;
    }

    public static SheetReader getPrenotReader(){
        if (prenotReader==null) {
            prenotReader = new SheetReader(PRENOT_SHEET_NAME, PRENOT_COLUMN_INDEX, PRENOT_COLUMNS);
        }
        return prenotReader;
    }
}
