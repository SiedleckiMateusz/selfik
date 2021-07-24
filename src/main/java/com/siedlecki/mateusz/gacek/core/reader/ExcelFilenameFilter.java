package com.siedlecki.mateusz.gacek.core.reader;

import java.io.File;
import java.io.FilenameFilter;

public class ExcelFilenameFilter implements FilenameFilter {
    @Override
    public boolean accept(File dir, String name) {
        return name.endsWith(".xlsx") || name.endsWith(".xls");
    }
}
