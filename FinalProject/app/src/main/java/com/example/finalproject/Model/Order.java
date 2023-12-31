package com.example.finalproject.Model;

public class Order {

    private String ProductId;
    private String ProductName;
    private String Quantity;
    private String Price;
    private String Discount;


    public Order() {
    }

    public Order(String productID, String productName, String quantity, String price, String discount) {
        ProductId = productID;
        ProductName = productName;
        Quantity = quantity;
        Price = price;
        Discount = discount;
    }

    public String getProductID() {
        return ProductId;
    }

    public void setProductID(String productID) {
        ProductId = productID;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }
}
