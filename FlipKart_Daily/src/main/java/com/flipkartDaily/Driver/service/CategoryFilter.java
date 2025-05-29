package com.flipkartDaily.Driver.service;

import com.flipkartDaily.Driver.entities.Item;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

class CategoryFilter implements ItemFilter {
    private final Set<String> allowedCategories;

    public CategoryFilter(Collection<String> categories) {
        this.allowedCategories = categories.stream()
                .map(String::toLowerCase)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean matches(Item item) {
        return allowedCategories.isEmpty() || allowedCategories.contains(item.category.toLowerCase());
    }
}
