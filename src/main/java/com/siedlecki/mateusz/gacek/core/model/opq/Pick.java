package com.siedlecki.mateusz.gacek.core.model.opq;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Builder
@Getter
public class Pick {
    private final int pickQty;
    private final LocalDate cutOffDate;
    private final LocalTime cutOffTime;
    private final String deliveryMethod;

    public Pick(int pickQty, LocalDate cutOffDate, LocalTime cutOffTime, String deliveryMethod) {
        this.pickQty = pickQty;
        this.cutOffDate = cutOffDate;
        this.cutOffTime = cutOffTime;
        this.deliveryMethod = deliveryMethod;
    }

    public Pick(int pickQty, String cutOffDate, String cutOffTime, String deliveryMethod) {
        this.pickQty = pickQty;
        this.deliveryMethod = deliveryMethod;

        if (cutOffDate!=null && !cutOffDate.isEmpty()){
            this.cutOffDate = LocalDate.parse(cutOffDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }else {
            this.cutOffDate = null;
        }

        if (cutOffDate!=null && !cutOffDate.isEmpty()){
            this.cutOffTime = LocalTime.parse(cutOffTime,DateTimeFormatter.ofPattern("HH:mm"));
        }else {
            this.cutOffTime = null;
        }

    }
}
