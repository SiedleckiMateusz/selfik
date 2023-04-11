package com.siedlecki.mateusz.gacek.core.reader;


import static com.siedlecki.mateusz.gacek.core.Constants.*;

public class SheetReaderFactory {
    private static SheetReader slm0003Reader;
    private static SheetReader prenotReader;

    public static SheetReader getSlm0003Reader(){
        if (slm0003Reader==null) {
            slm0003Reader = new Slm003Reader(SLM0003_SHEET_NAME, SLM0003_ROW_INDEX);
        }
        return slm0003Reader;
    }

    public static SheetReader getPrenotReader(){
        if (prenotReader==null) {
            prenotReader = new PrenotReader(PRENOT_SHEET_NAME, PRENOT_ROW_INDEX);
        }
        return prenotReader;
    }
}
