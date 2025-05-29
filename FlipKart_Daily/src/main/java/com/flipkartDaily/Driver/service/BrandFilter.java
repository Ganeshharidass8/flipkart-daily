package com.flipkartDaily.Driver.service;

import com.flipkartDaily.Driver.entities.Item;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;


class BrandFilter implements ItemFilter {
    private final Set<String> allowedBrands;

    public BrandFilter(Collection<String> brands) {
        this.allowedBrands = brands.stream()
                .map(String::toLowerCase)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean matches(Item item) {
        return allowedBrands.isEmpty() || allowedBrands.contains(item.getBrand().toLowerCase());
    }
}