package com.example.iphoneStore.dto;


import lombok.Data;
import lombok.NonNull;

@Data
public class AddedGoods {

    @NonNull
    private final String name;
    @NonNull
    private final Double price;
    @NonNull
    private final Integer quantity;
}
