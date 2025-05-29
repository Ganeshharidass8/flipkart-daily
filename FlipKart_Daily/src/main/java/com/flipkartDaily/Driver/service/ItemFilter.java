package com.flipkartDaily.Driver.service;


import com.flipkartDaily.Driver.entities.Item;

public interface ItemFilter {
    boolean matches(Item item);
}
