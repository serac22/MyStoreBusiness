package com.example.mystorebusiness.account.ui.stocks;


import androidx.annotation.NonNull;

public class StockItem {

    private int id,id_user;
    private  String productName,price,quantity,description,image,cod,addition,final_price,data_add,data_expiration;

    public StockItem(int id_user,String cod,String productName, String price,String addition,String final_price, String quantity,String data_add,String data_expiration, String description, String image) {
        this.id_user=id_user;
        this.cod=cod;
        this.addition=addition;
        this.final_price=final_price;
        this.data_add=data_add;
        this.data_expiration=data_expiration;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.description=description;
        this.image = image;
    }

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public String getAddition() {
        return addition;
    }

    public void setAddition(String addition) {
        this.addition = addition;
    }

    public String getFinal_price() {
        return final_price;
    }

    public void setFinal_price(String final_price) {
        this.final_price = final_price;
    }

    public String getData_add() {
        return data_add;
    }

    public void setData_add(String data_add) {
        this.data_add = data_add;
    }

    public String getData_expiration() {
        return data_expiration;
    }

    public void setData_expiration(String data_expiration) {
        this.data_expiration = data_expiration;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductName() {
        return productName;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPrice() {
        return price;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getQuantity() {

        return quantity;
    }

    public void setImage(String image) {
        this.image = image;
    }


    public String getImage() {
        return image;
    }

    @NonNull
    @Override
    public String toString() {
        return "StockItem{" +
                "productName='" + productName + '\'' +
                ", price='" + price + '\'' +
                ", quantity=" + quantity +
                '}';
    }

}
