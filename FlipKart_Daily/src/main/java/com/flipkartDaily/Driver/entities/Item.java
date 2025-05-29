package com.flipkartDaily.Driver.entities;

import java.util.Objects;

public class Item {
    private final String brand;
    public final String category;
    private final int price;
    private int quantity;

    public Item(String brand, String category, int price) {
        this.brand = brand;
        this.category = category;
        this.price = price;
        this.quantity = 0;
    }

    public void addQuantity(int qty) {
        if (qty < 0)
            throw new IllegalArgumentException("Quantity cannot be negative");
        this.quantity += qty;
    }

    public String getBrand() {
        return brand;
    }

    public String getCategory() {
        return category;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Item))
            return false;
        Item item = (Item) o;
        return brand.equalsIgnoreCase(item.brand) && category.equalsIgnoreCase(item.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(brand.toLowerCase(), category.toLowerCase());
    }

    @Override
    public String toString() {
        return brand + ", " + category + ", " + quantity;
    }
}

