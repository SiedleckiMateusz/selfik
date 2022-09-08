package com.siedlecki.mateusz.gacek.core;



import com.siedlecki.mateusz.gacek.core.model.IkeaProduct;

import java.util.Comparator;

public class FirstLocationComparator implements Comparator<IkeaProduct> {

    @Override
    public int compare(IkeaProduct o1, IkeaProduct o2) {
        return o1.getMainLocation().getName().compareTo(o2.getMainLocation().getName());
    }
}
