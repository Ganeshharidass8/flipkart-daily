package com.flipkartDaily.Driver.service;

import com.flipkartDaily.Driver.entities.Item;

import java.util.List;


class CompositeFilter implements ItemFilter {
    private final List<ItemFilter> filters;

    public CompositeFilter(List<ItemFilter> filters) {
        this.filters = filters;
    }

    @Override
    public boolean matches(Item item) {
        for (ItemFilter filter : filters) {
            if (!filter.matches(item)) {
                return false;
            }
        }
        return true;
    }
}
