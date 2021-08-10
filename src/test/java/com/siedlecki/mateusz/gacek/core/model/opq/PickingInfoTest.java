package com.siedlecki.mateusz.gacek.core.model.opq;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;


class PickingInfoTest {

    @Test
    @DisplayName("should return noInfo=5, reservationCps=5 and the rest 0 when pickQty = 5 and cut off time and date is null")
    public void test1(){
        //given
        Pick pick = new Pick(5,null,null);
        //when
        PickingInfo pickingInfo = new PickingInfo(pick);
        //then
        assertEquals(5,pickingInfo.getNoInfo());
        assertEquals(0,pickingInfo.getQtyAfterDay());
        assertEquals(0,pickingInfo.getQtyToOrder());
        assertEquals(5,pickingInfo.getQtyReservationInCps());
        assertEquals(0,pickingInfo.getQtyReservationInFps());
    }

    @Test
    @DisplayName("should return toOrder and reservationInCps = 5, the rest 0 when pickQty = 5, cutOffTime=16.00 and date=NOW")
    public void test2(){
        //given
        Pick pick = new Pick(5, LocalDate.now(), LocalTime.of(16,0));
        //when
        PickingInfo pickingInfo = new PickingInfo(pick);
        //then
        assertEquals(0,pickingInfo.getNoInfo());
        assertEquals(0,pickingInfo.getQtyAfterDay());
        assertEquals(5,pickingInfo.getQtyToOrder());
        assertEquals(5,pickingInfo.getQtyReservationInCps());
        assertEquals(0,pickingInfo.getQtyReservationInFps());
    }

    @Test
    @DisplayName("should return afterDay=5 and the rest 0 when pickQty = 5, cutOffTime=12.00 and date=NOW+2days")
    public void test3(){
        //given
        Pick pick = new Pick(5, LocalDate.now().plusDays(2), LocalTime.of(12,0));
        //when
        PickingInfo pickingInfo = new PickingInfo(pick);
        //then
        assertEquals(0,pickingInfo.getNoInfo());
        assertEquals(5,pickingInfo.getQtyAfterDay());
        assertEquals(0,pickingInfo.getQtyToOrder());
        assertEquals(0,pickingInfo.getQtyReservationInCps());
        assertEquals(0,pickingInfo.getQtyReservationInFps());
    }
}