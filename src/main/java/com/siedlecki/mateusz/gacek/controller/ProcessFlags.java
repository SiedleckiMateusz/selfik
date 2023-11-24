package com.siedlecki.mateusz.gacek.controller;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ProcessFlags extends MainController {
    private boolean SLM0003IsOkFlag;
    private boolean iv020IsOkFlag;
    private boolean prenotIsOkFlag;

    private boolean prenotProcessFlag;
    private boolean morningProcessFlag;

    public void reset(){
        SLM0003IsOkFlag = false;
        prenotIsOkFlag = false;
        iv020IsOkFlag = false;

        prenotProcessFlag = false;
        morningProcessFlag = false;
    }

    public boolean isActiveAnyProcess(){
        return prenotProcessFlag || morningProcessFlag;
    }
}
