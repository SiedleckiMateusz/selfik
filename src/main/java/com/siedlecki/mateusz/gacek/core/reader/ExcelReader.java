package com.siedlecki.mateusz.gacek.core.reader;

import lombok.AllArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class ExcelReader {

    public ExcelFile readFromFile(File file) throws IOException {
        checkingExcelFile(file);
        return new ExcelFile(file);
    }

    public List<ExcelFile> readFromDirectory(File directory) throws IOException {
        if (!directory.exists()){
            throw new IOException(directory.getName() +" nie istnieje");
        }
        if (!directory.isDirectory()){
            throw new IOException(directory.getName() +" nie jest folderem");
        }
        File[] files = directory.listFiles(new ExcelFilenameFilter());

        if (files != null && files.length>0){
            List<File> filesList = Arrays.asList(files);
            return filesList.stream()
                    .sorted((o1, o2) -> Long.compare(o2.lastModified(),o1.lastModified()))
                    .map(ExcelFile::new)
                    .collect(Collectors.toList());
        }
        throw new IOException("Nie znaleziono pliku w folderze "+directory.getName());
    }

    private void checkingExcelFile(File file) throws IOException {
        String name = file.getName();
        if (!file.exists()) {
            throw new IOException("Plik "+ name +" nie istnieje");
        }
        if (!name.endsWith(".xlsx") && !name.endsWith(".xls")) {
            throw new IOException("Plik "+ name +" nie jest w formacie .xls lub .xlsx");
        }
    }

}



