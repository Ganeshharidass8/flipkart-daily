package com.flipkartDaily.Driver.dao;

import com.flipkartDaily.Driver.entities.Item;

import java.util.List;
import java.util.Map;

public interface InventoryDao {
    void saveItem(Item item);
    Item getItem(String brand, String category);
    Map<String, Item> getAllItems();
    void updateItem(Item item);
    List<Item> searchItems(Map<String, List<String>> filters, int[] priceRange, String orderBy, boolean asc);
}
