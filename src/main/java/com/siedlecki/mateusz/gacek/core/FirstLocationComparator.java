package com.siedlecki.mateusz.gacek.core;



import com.siedlecki.mateusz.gacek.core.model.IkeaProductToWrite;

import java.util.Comparator;

public class FirstLocationComparator implements Comparator<IkeaProductToWrite> {

    @Override
    public int compare(IkeaProductToWrite o1, IkeaProductToWrite o2) {
        return o1.getProduct().getLocations().first().compareTo(o2.getProduct().getLocations().first());
    }
}
