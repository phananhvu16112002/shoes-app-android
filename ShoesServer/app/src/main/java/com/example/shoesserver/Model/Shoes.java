package com.example.shoesserver.Model;

public class Shoes {

    private String Name;
    private String Image;
    private String Description;
    private String Price;
    private String Discount;
    private String ListId;

    public Shoes(String name, String image, String description, String price, String discount, String listId) {
        Name = name;
        Image = image;
        Description = description;
        Price = price;
        Discount = discount;
        ListId = listId;
    }

    public Shoes() {
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
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

    public String getListId() {
        return ListId;
    }

    public void setListId(String listId) {
        ListId = listId;
    }
}
