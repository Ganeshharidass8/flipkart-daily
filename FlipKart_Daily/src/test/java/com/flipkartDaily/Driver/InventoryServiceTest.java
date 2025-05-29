package com.flipkartDaily.Driver;


import com.flipkartDaily.Driver.entities.Item;
import com.flipkartDaily.Driver.exception.InvalidInputException;
import com.flipkartDaily.Driver.exception.ItemNotFoundException;
import com.flipkartDaily.Driver.service.InventoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class InventoryServiceTest {

    private InventoryService service;

    @BeforeEach
    void setUp() {
        service = new InventoryService();
        service.addItem("Amul", "Milk", 100);
        service.addItem("Amul", "Curd", 50);
        service.addItem("Nestle", "Milk", 60);
        service.addItem("Nestle", "Curd", 90);

        service.addInventory("Amul", "Milk", 20);
        service.addInventory("Amul", "Curd", 5);
        service.addInventory("Nestle", "Milk", 15);
        service.addInventory("Nestle", "Curd", 10);
    }

    @Test
    void testAddItemDuplicate() {
        try {
            service.addItem("Amul", "Milk", 110);
            System.out.println("Status: FAILED - Expected InvalidInputException was not thrown.");
            fail("Expected InvalidInputException was not thrown.");
        } catch (InvalidInputException ex) {
            System.out.println("Status: PASSED - Caught expected exception.");
            System.out.println("Exception message: " + ex.getMessage());
            assertEquals("Item already exists: Amul, Milk", ex.getMessage());
        }
    }


    @Test
    void testAddInventoryForUnknownItem() {
        Exception exception = assertThrows(ItemNotFoundException.class, () -> {
            service.addInventory("Unknown", "Milk", 10);
        });
        System.out.println("Status: PASSED - testAddInventoryForUnknownItem threw expected exception.");
        System.out.println("Exception message: " + exception.getMessage());
        assertEquals("Item not found: Unknown, Milk", exception.getMessage());
    }

    @Test
    void testAddInventoryWithZeroQuantity() {
        Exception exception = assertThrows(InvalidInputException.class, () -> {
            service.addInventory("Amul", "Milk", 0);
        });
        System.out.println("Status: PASSED - testAddInventoryWithZeroQuantity threw expected exception.");
        System.out.println("Exception message: " + exception.getMessage());
        assertEquals("Quantity must be positive.", exception.getMessage());
    }

    @Test
    void testAddInventoryWithNegativeQuantity() {
        Exception exception = assertThrows(InvalidInputException.class, () -> {
            service.addInventory("Amul", "Milk", -5);
        });
        System.out.println("Status: PASSED - testAddInventoryWithNegativeQuantity threw expected exception.");
        System.out.println("Exception message: " + exception.getMessage());
        assertEquals("Quantity must be positive.", exception.getMessage());
    }


    @Test
    void testAddItemWithInvalidInput() {
        Exception exception1 = assertThrows(InvalidInputException.class, () -> {
            service.addItem("", "Milk", 100);
        });
        System.out.println("Status: PASSED | Message: Empty brand throws exception - " + exception1.getMessage());
        assertEquals("Brand cannot be null or empty.", exception1.getMessage());

        Exception exception2 = assertThrows(InvalidInputException.class, () -> {
            service.addItem("Amul", "", 100);
        });
        System.out.println("Status: PASSED | Message: Empty category throws exception - " + exception2.getMessage());
        assertEquals("Category cannot be null or empty.", exception2.getMessage());

        Exception exception3 = assertThrows(InvalidInputException.class, () -> {
            service.addItem("Amul", "Milk", 0);
        });
        System.out.println("Status: PASSED | Message: Zero price throws exception - " + exception3.getMessage());
        assertEquals("Price must be greater than zero.", exception3.getMessage());
    }

    @Test
    void testSearchByBrand() {
        List<Item> items = service.searchItems(Map.of("brand", List.of("Nestle")), null, "price", true);
        System.out.println("Status: PASSED | Message: Search by Brand 'Nestle' found " + items.size() + " items.");

        System.out.println("Items:");
        for (Item item : items) {
            System.out.printf("  Brand: %s | Category: %s | Price: %d | Quantity: %d%n",
                    item.getBrand(), item.getCategory(), item.getPrice(), item.getQuantity());
        }

        assertEquals(2, items.size());
        assertTrue(items.stream().allMatch(i -> i.getBrand().equalsIgnoreCase("Nestle")));
    }


    @Test
    void testSearchByCategory() {
        List<Item> items = service.searchItems(Map.of("category", List.of("Milk")), null, "price", true);
        System.out.println("Status: PASSED | Message: Search by Category 'Milk' found " + items.size() + " items.");
        printItems(items);
        assertEquals(2, items.size());
        assertTrue(items.stream().allMatch(i -> i.getCategory().equalsIgnoreCase("Milk")));
    }

    @Test
    void testSearchByMultipleBrands() {
        List<Item> items = service.searchItems(Map.of("brand", List.of("Amul", "Nestle")), null, "price", true);
        System.out.println("Status: PASSED | Message: Search by Brands 'Amul, Nestle' found " + items.size() + " items.");
        printItems(items);
        assertEquals(4, items.size());
    }

    @Test
    void testSearchByMultipleCategories() {
        List<Item> items = service.searchItems(Map.of("category", List.of("Milk", "Curd")), null, "price", true);
        System.out.println("Status: PASSED | Message: Search by Categories 'Milk, Curd' found " + items.size() + " items.");
        printItems(items);
        assertEquals(4, items.size());
    }

    @Test
    void testSearchByPriceRange() {
        List<Item> items = service.searchItems(Map.of(), new int[]{50, 100}, "price", true);
        System.out.println("Status: PASSED | Message: Search by Price Range 50 to 100 found " + items.size() + " items.");
        printItems(items);
        assertFalse(items.isEmpty());
        assertTrue(items.stream().allMatch(i -> i.getPrice() >= 50 && i.getPrice() <= 100));
    }

    @Test
    void testSearchWithInvalidPriceRange() {
        Exception ex = assertThrows(InvalidInputException.class, () -> {
            service.searchItems(Map.of(), new int[]{100, 50}, "price", true);
        });
        System.out.println("Status: PASSED | Message: Invalid price range exception caught - " + ex.getMessage());
    }

    @Test
    void testSearchMultipleFilters() {
        List<Item> items = service.searchItems(
                Map.of("brand", List.of("Amul"), "category", List.of("Curd")),
                new int[]{40, 60},
                "price",
                true);
        System.out.println("Status: PASSED | Message: Search with multiple filters returned " + items.size() + " item(s).");
        printItems(items);
        assertEquals(1, items.size());
        Item item = items.get(0);
        assertEquals("Amul", item.getBrand());
        assertEquals("Curd", item.getCategory());
        assertTrue(item.getPrice() >= 40 && item.getPrice() <= 60);
    }

    // Utility method to print item details neatly
    private void printItems(List<Item> items) {
        System.out.println("Items:");
        for (Item item : items) {
            System.out.printf("  Brand: %s | Category: %s | Price: %d | Quantity: %d%n",
                    item.getBrand(), item.getCategory(), item.getPrice(), item.getQuantity());
        }
    }


// --- Sorting Tests ---

    @Test
    void testSearchSortByPriceAsc() {
        List<Item> items = service.searchItems(Map.of("category", List.of("Milk")), null, "price", true);
        System.out.println("Status: PASSED | Message: Sorted by price ascending, first price = " + items.get(0).getPrice() + ", second price = " + items.get(1).getPrice());
        printItems(items);
        assertEquals(2, items.size());
        assertTrue(items.get(0).getPrice() <= items.get(1).getPrice());
    }

    @Test
    void testSearchSortByPriceDesc() {
        List<Item> items = service.searchItems(Map.of("category", List.of("Milk")), null, "price", false);
        System.out.println("Status: PASSED | Message: Sorted by price descending, first price = " + items.get(0).getPrice() + ", second price = " + items.get(1).getPrice());
        printItems(items);
        assertEquals(2, items.size());
        assertTrue(items.get(0).getPrice() >= items.get(1).getPrice());
    }

    @Test
    void testSearchSortByQuantityAsc() {
        List<Item> items = service.searchItems(Map.of("category", List.of("Milk")), null, "itemqty", true);
        System.out.println("Status: PASSED | Message: Sorted by quantity ascending, first qty = " + items.get(0).getQuantity() + ", second qty = " + items.get(1).getQuantity());
        printItems(items);
        assertEquals(2, items.size());
        assertTrue(items.get(0).getQuantity() <= items.get(1).getQuantity());
    }

    @Test
    void testSearchSortByQuantityDesc() {
        List<Item> items = service.searchItems(Map.of("category", List.of("Milk")), null, "itemqty", false);
        System.out.println("Status: PASSED | Message: Sorted by quantity descending, first qty = " + items.get(0).getQuantity() + ", second qty = " + items.get(1).getQuantity());
        printItems(items);
        assertEquals(2, items.size());
        assertTrue(items.get(0).getQuantity() >= items.get(1).getQuantity());
    }

    @Test
    void testSearchSortDefaultCriteria() {
        List<Item> items = service.searchItems(Map.of("category", List.of("Milk")), null, "", true);
        System.out.println("Status: PASSED | Message: Default sort (price asc) applied, first price = " + items.get(0).getPrice() + ", second price = " + items.get(1).getPrice());
        printItems(items);
        assertEquals(2, items.size());
        assertTrue(items.get(0).getPrice() <= items.get(1).getPrice());
    }

    @Test
    void testAddInventoryMultipleTimes() {
        service.addInventory("Amul", "Milk", 5);
        service.addInventory("Amul", "Milk", 10);
        List<Item> items = service.searchItems(Map.of("brand", List.of("Amul")), null, "price", true);
        System.out.println("Status: PASSED | Message: After multiple inventory additions, quantity for Amul Milk is " + items.get(1).getQuantity());
        printItems(items);
        assertEquals(35, items.get(1).getQuantity()); // 20 (initial) + 5 + 10
    }

    @Test
    void testSearchInvalidOrderByDefaultsToPrice() {
        List<Item> items = service.searchItems(Map.of("category", List.of("Milk")), null, "unknown", true);
        System.out.println("Status: PASSED | Message: Search with invalid orderBy defaults to price, items found: " + items.size());
        printItems(items);
        assertEquals(2, items.size(), "Expected 2 items for category Milk");
        assertTrue(items.get(0).getPrice() <= items.get(1).getPrice());
    }

    @Test
    void testSearchWithNullFiltersReturnsAll() {
        List<Item> items = service.searchItems(null, null, "price", true);
        System.out.println("Status: PASSED | Message: Search with null filters returned " + items.size() + " items.");
        printItems(items);
        assertFalse(items.isEmpty());
    }
}
