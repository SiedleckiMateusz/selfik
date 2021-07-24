package com.siedlecki.mateusz.gacek.core.reader;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.File;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ExcelFile {
    private final File file;
}
