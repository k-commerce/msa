package com.kcommerce.domain.order.client;

import lombok.Getter;

@Getter
public class Item {

    private Long id;
    private String name;
    private int price;
    private String description;
}
