package com.example.foodapp.chefFoodPanel;

public class FoodDetails {

    public String Dishes,Quantity,Price,Description,ImageURL,RandomUID,ChefId;

    public FoodDetails() {
        this.Dishes = "";
        this.ImageURL = "";
        this.Price = "0";
        this.Quantity = "0";
        this.Description = "";
        this.RandomUID = "";
        this.ChefId = "";
    }

    public FoodDetails(String dishes, String quantity, String price, String description, String imageURL, String randomUID, String chefid) {
        Dishes = dishes;
        Quantity = quantity;
        Price = price;
        Description = description;
        ImageURL = imageURL;
        RandomUID = randomUID;
        ChefId = chefid;
    }

}
