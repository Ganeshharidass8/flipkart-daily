package com.flipkartDaily.Driver.service;



import com.flipkartDaily.Driver.dao.InventoryDao;
import com.flipkartDaily.Driver.dao.InventoryDaoImpl;
import com.flipkartDaily.Driver.entities.Item;
import com.flipkartDaily.Driver.exception.InvalidInputException;
import com.flipkartDaily.Driver.exception.ItemNotFoundException;

import java.util.List;
import java.util.Map;

public class InventoryService {

    private final InventoryDao inventoryDao;

    public InventoryService() {
        this.inventoryDao = new InventoryDaoImpl();
    }

    // For testing or alternate DAO injection if needed
    public InventoryService(InventoryDao inventoryDao) {
        this.inventoryDao = inventoryDao;
    }

    private void validateBrandCategory(String brand, String category) {
        if (brand == null || brand.trim().isEmpty()) {
            throw new InvalidInputException("Brand cannot be null or empty.");
        }
        if (category == null || category.trim().isEmpty()) {
            throw new InvalidInputException("Category cannot be null or empty.");
        }
    }

    public void addItem(String brand, String category, int price) {
        validateBrandCategory(brand, category);
        if (price <= 0) {
            throw new InvalidInputException("Price must be greater than zero.");
        }

        Item item = new Item(brand, category, price);
        inventoryDao.saveItem(item);
        System.out.println("AddItem(" + brand + ", " + category + ", " + price + ")");
    }

    public void addInventory(String brand, String category, int quantity) {
        validateBrandCategory(brand, category);
        if (quantity <= 0) {
            throw new InvalidInputException("Quantity must be positive.");
        }

        Item item = inventoryDao.getItem(brand, category);
        if (item == null) {
            throw new ItemNotFoundException("Item not found: " + brand + ", " + category);
        }

        item.addQuantity(quantity);
        inventoryDao.updateItem(item);
        System.out.println("AddInventory(" + brand + ", " + category + ", " + quantity + ")");
    }

    public List<Item> searchItems(Map<String, List<String>> filters, int[] priceRange, String orderBy, boolean asc) {
        return inventoryDao.searchItems(filters, priceRange, orderBy, asc);
    }

    // Print inventory summary (can be called from Driver for output)
    public void printInventory() {
        System.out.println("\nInventory Summary:");
        inventoryDao.getAllItems().values().forEach(System.out::println);
    }
}
