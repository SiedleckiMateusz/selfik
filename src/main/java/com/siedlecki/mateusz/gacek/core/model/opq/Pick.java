package com.siedlecki.mateusz.gacek.core.model.opq;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@AllArgsConstructor
@Getter
public class Pick {
    private final int pickqty;
    private final LocalDate cutOffDate;
    private final LocalTime cutOffTime;
}
