package com.siedlecki.mateusz.gacek.core.model.opq;

import com.siedlecki.mateusz.gacek.core.Constants;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
public class PickingProduct{
    private final String numberId;
    private final PickArea area;
    private List<Pick> pickList = new ArrayList<>();

    private int qtyToOrder = 0;
    private int qtyAfterDay = 0;
    private int qtyReservationInCps = 0;
    private int qtyReservationInFps = 0;

    private final LocalDate date = LocalDate.now().plusDays(Constants.OPQ_DAYS_TO_PICK);
    private final LocalDate dateToTest = LocalDate.of(2021,7,20).plusDays(Constants.OPQ_DAYS_TO_PICK);

    public PickingProduct(String numberId,String pickArea) {
        this.numberId = numberId;
        this.area = setArea(pickArea);
    }

    public PickingProduct(String numberId,String pickArea,Pick pick) {
        this(numberId,pickArea);
        addPick(pick);
    }

    public void addPick(Pick pick){
        calculateStats(pick);
        pickList.add(pick);
    }

    private void calculateStats(Pick p) {
        if (p.getCutOffTime() != null && p.getCutOffDate() != null) {
            if (p.getCutOffDate().isAfter(dateToTest)) {
                qtyAfterDay+=p.getPickQty();
            } else {
                qtyToOrder+=p.getPickQty();
                if (Constants.CPS_CUT_OFF_TIME.contains(p.getCutOffTime())) {
                    qtyReservationInCps+=p.getPickQty();
                }
                if (Constants.FPS_CUT_OFF_TIME.contains(p.getCutOffTime())) {
                    qtyReservationInFps+=p.getPickQty();
                }
            }
        }
    }

    private PickArea setArea(String pickArea) {

        if (pickArea.equals(PickArea.SELF.getTabName())){
            return PickArea.SELF;
        }
        if (pickArea.equals(PickArea.MARKETHALL.getTabName())){
            return PickArea.MARKETHALL;
        }
        if (pickArea.equals(PickArea.FULL.getTabName())){
            return PickArea.FULL;
        }
        return PickArea.NONE;
    }

    @Override
    public String toString() {
        return "ARTNO: " + numberId + '\'' +
                ", qtyToOrder=" + qtyToOrder +
                ", qtyAfterDay=" + qtyAfterDay +
                ", qtyReservationInCps=" + qtyReservationInCps +
                ", qtyReservationInFps=" + qtyReservationInFps +
                ", date=" + date +
                "\n"+ pickSet(pickList);
    }

    private String pickSet(List<Pick> pickList) {
        StringBuffer sb = new StringBuffer();

        for (Pick p:pickList){
            sb.append(p.toString()).append("\n");
        }

        return sb.toString();
    }
}
