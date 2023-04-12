package com.siedlecki.mateusz.gacek.core.model;

import com.siedlecki.mateusz.gacek.core.FirstLocationComparator;
import lombok.Getter;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class Result {
    private final List<IkeaProduct> all;
    private List<IkeaProduct> toPrepare;

    public Result(List<IkeaProduct> all, List<IkeaProduct> toPrepare) {
        this.all = all.stream().sorted(Comparator.comparing(o -> o.getMainLocation().getName())).collect(Collectors.toList());
        this.toPrepare = toPrepare.stream().sorted(Comparator.comparing(o -> o.getMainLocation().getName())).collect(Collectors.toList());
    }

    public void addToPrepare(List<IkeaProduct> toPrepare) {
        if (toPrepare != null) {
            this.toPrepare.addAll(toPrepare);
            this.toPrepare = this.toPrepare.stream()
                    .sorted(new FirstLocationComparator())
                    .collect(Collectors.toList());
        }
    }
}
