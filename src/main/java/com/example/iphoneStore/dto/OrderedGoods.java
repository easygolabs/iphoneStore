package com.example.iphoneStore.dto;

import lombok.Data;
import lombok.NonNull;

@Data
public class OrderedGoods {

    @NonNull
    private final Long id;
    @NonNull
    private final Integer quantity;
}
