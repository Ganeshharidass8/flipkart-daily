package com.flipkartDaily.Driver.service;

import com.flipkartDaily.Driver.entities.Item;

class PriceRangeFilter implements ItemFilter {
    private final int priceFrom;
    private final int priceTo;

    public PriceRangeFilter(Integer priceFrom, Integer priceTo) {
        this.priceFrom = priceFrom != null ? priceFrom : Integer.MIN_VALUE;
        this.priceTo = priceTo != null ? priceTo : Integer.MAX_VALUE;
    }

    @Override
    public boolean matches(Item item) {
        return item.getPrice() >= priceFrom && item.getPrice() <= priceTo;
    }
}
