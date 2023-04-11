package com.siedlecki.mateusz.gacek.controller;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ProcessFlags {
    private boolean SLM0003IsOkFlag;
    private boolean prenotIsOkFlag;

    private boolean prenotProcessFlag;
    private boolean morningProcessFlag;

    public void reset(){
        SLM0003IsOkFlag = false;
        prenotIsOkFlag = false;

        prenotProcessFlag = false;
        morningProcessFlag = false;
    }

    public boolean isActiveAnyProcess(){
        return prenotProcessFlag || morningProcessFlag;
    }
}
