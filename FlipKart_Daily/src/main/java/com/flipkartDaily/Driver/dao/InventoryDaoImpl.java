package com.flipkartDaily.Driver.dao;

import com.flipkartDaily.Driver.entities.Item;
import com.flipkartDaily.Driver.exception.InvalidInputException;
import com.flipkartDaily.Driver.exception.ItemNotFoundException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InventoryDaoImpl implements InventoryDao {

    private final Map<String, Item> inventory = new HashMap<>();

    private String getKey(String brand, String category) {
        return brand.toLowerCase() + "#" + category.toLowerCase();
    }

    @Override
    public void saveItem(Item item) {
        String key = getKey(item.getBrand(), item.getCategory());
        if (inventory.containsKey(key)) {
            throw new InvalidInputException("Item already exists: " + item.getBrand() + ", " + item.getCategory());
        }
        inventory.put(key, item);
    }

    @Override
    public Item getItem(String brand, String category) {
        String key = getKey(brand, category);
        return inventory.get(key);
    }

    @Override
    public Map<String, Item> getAllItems() {
        return inventory;
    }

    @Override
    public void updateItem(Item item) {
        String key = getKey(item.getBrand(), item.getCategory());
        inventory.put(key, item);
    }

    @Override
    public List<Item> searchItems(Map<String, List<String>> filters, int[] priceRange, String orderBy, boolean asc) {
        // Stream and filter items as before
        List<Item> filteredItems = inventory.values().stream()
                .filter(item -> {
                    if (filters != null) {
                        if (filters.containsKey("brand") && !filters.get("brand").isEmpty()) {
                            boolean brandMatch = filters.get("brand").stream()
                                    .anyMatch(b -> b.equalsIgnoreCase(item.getBrand()));
                            if (!brandMatch)
                                return false;
                        }
                        if (filters.containsKey("category") && !filters.get("category").isEmpty()) {
                            boolean categoryMatch = filters.get("category").stream()
                                    .anyMatch(c -> c.equalsIgnoreCase(item.getCategory()));
                            if (!categoryMatch)
                                return false;
                        }
                    }
                    if (priceRange != null) {
                        if (priceRange.length != 2) {
                            throw new InvalidInputException("Price range must have exactly two values: from and to.");
                        }
                        int from = priceRange[0];
                        int to = priceRange[1];
                        if (from != -1 && to != -1 && from > to) {
                            throw new InvalidInputException("Price range 'from' cannot be greater than 'to'.");
                        }
                        if (from != -1 && item.getPrice() < from)
                            return false;
                        if (to != -1 && item.getPrice() > to)
                            return false;
                    }
                    return true;
                })
                .sorted((a, b) -> {
                    int cmp;
                    switch (orderBy == null ? "" : orderBy.toLowerCase()) {
                        case "price":
                            cmp = Integer.compare(a.getPrice(), b.getPrice());
                            break;
                        case "itemqty":
                            cmp = Integer.compare(a.getQuantity(), b.getQuantity());
                            break;
                        default:
                            cmp = Integer.compare(a.getPrice(), b.getPrice());
                    }
                    return asc ? cmp : -cmp;
                })
                .collect(Collectors.toList());

        // Throw exception if filters are applied but no results found
        boolean hasFilters = (filters != null && !filters.isEmpty()) || (priceRange != null);
        if (hasFilters && filteredItems.isEmpty()) {
            throw new ItemNotFoundException("No items found matching the given search criteria.");
        }

        return filteredItems;
    }
}
