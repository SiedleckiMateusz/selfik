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
    private List<Pick> pickSet = new ArrayList<>();

    private final LocalDate date = LocalDate.now().plusDays(Constants.OPQ_DAYS_TO_PICK);

    public PickingProduct(String numberId,String pickArea) {
        this.numberId = numberId;
        this.area = setArea(pickArea);
    }

    public PickingProduct(String numberId,String pickArea,Pick pick) {
        this(numberId,pickArea);
        pickSet.add(pick);
    }

    public void addPick(Pick pick){
        pickSet.add(pick);
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


}
