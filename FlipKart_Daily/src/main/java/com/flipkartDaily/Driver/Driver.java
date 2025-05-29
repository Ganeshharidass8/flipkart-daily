package com.flipkartDaily.Driver;



import com.flipkartDaily.Driver.entities.Item;
import com.flipkartDaily.Driver.service.InventoryService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.*;

@SpringBootApplication
public class Driver {

	private static InventoryService service = new InventoryService();

	public static void main(String[] args) {
		SpringApplication.run(Driver.class, args);

		addItemsAndInventory();

		System.out.println("--- Initial Inventory ---");
		service.printInventory();

		System.out.println("\n--- Filters & Searches ---");
		executeSearches();
	}

	private static void addItemsAndInventory() {
		// Add items
		service.addItem("Amul", "Milk", 100);
		service.addItem("Amul", "Curd", 50);
		service.addItem("Nestle", "Milk", 60);
		service.addItem("Nestle", "Curd", 90);
		service.addItem("Britannia", "Bread", 40);
		service.addItem("Britannia", "Cake", 120);
		service.addItem("DairyPure", "Milk", 80);
		service.addItem("DairyPure", "Butter", 150);
		service.addItem("Britannia", "Butter", 140);
		service.addItem("Amul", "Butter", 130);

		// Add inventory counts
		service.addInventory("Amul", "Milk", 10);
		service.addInventory("Nestle", "Milk", 5);
		service.addInventory("Nestle", "Curd", 101);
		service.addInventory("Amul", "Milk", 10);
		service.addInventory("Amul", "Curd", 5);
		service.addInventory("Britannia", "Bread", 15);
		service.addInventory("Britannia", "Cake", 7);
		service.addInventory("DairyPure", "Milk", 20);
		service.addInventory("DairyPure", "Butter", 8);
		service.addInventory("Britannia", "Butter", 10);
		service.addInventory("Amul", "Butter", 12);
	}

	private static void executeSearches() {
		// Search 1: Brand = Nestle, sort by Price ascending
		searchAndPrint(
				Map.of("brand", List.of("Nestle")),
				null,
				"price",
				true,
				"Search by Brand = Nestle sorted by Price Asc"
		);

		// Search 2: Category = Curd, sort by Price ascending
		searchAndPrint(
				Map.of("category", List.of("Curd")),
				null,
				"price",
				true,
				"Search by Category = Curd sorted by Price Asc"
		);

		// Search 3: Price range 50 to 100, sort by Price ascending
		searchAndPrint(
				Collections.emptyMap(),
				new int[]{50, 100},
				"price",
				true,
				"Search by Price Range 50 to 100 sorted by Price Asc"
		);

		// Search 4: Brand = Amul, Category = Curd, Price 40-60, sort by Price ascending
		searchAndPrint(
				Map.of("brand", List.of("Amul"), "category", List.of("Curd")),
				new int[]{40, 60},
				"price",
				true,
				"Search by Brand=Amul, Category=Curd, Price 40-60 sorted by Price Asc"
		);

		// Search 5: Brands = Amul, Britannia; Categories = Butter, Bread; sort by Quantity descending
		searchAndPrint(
				Map.of("brand", List.of("Amul", "Britannia"), "category", List.of("Butter", "Bread")),
				null,
				"itemqty",
				false,
				"Search by Brands=Amul,Britannia and Categories=Butter,Bread sorted by Quantity Desc"
		);

		// Search 6: Category = Butter, Price range 100-150, sort by Price descending
		searchAndPrint(
				Map.of("category", List.of("Butter")),
				new int[]{100, 150},
				"price",
				false,
				"Search by Category=Butter, Price 100-150 sorted by Price Desc"
		);

		// Search 7: Brand = DairyPure, sort by Quantity ascending
		searchAndPrint(
				Map.of("brand", List.of("DairyPure")),
				null,
				"itemqty",
				true,
				"Search by Brand=DairyPure sorted by Quantity Asc"
		);
	}

	private static void searchAndPrint(
			Map<String, List<String>> filters,
			int[] priceRange,
			String orderBy,
			boolean ascending,
			String description
	) {
		System.out.println("\n" + description);
		List<Item> results = service.searchItems(filters, priceRange, orderBy, ascending);
		if (results.isEmpty()) {
			System.out.println("No items found.");
			return;
		}
		for (Item item : results) {
			System.out.printf("Brand: %s | Category: %s | Price: %d | Quantity: %d%n",
					item.getBrand(), item.getCategory(), item.getPrice(), item.getQuantity());
		}
	}

}

