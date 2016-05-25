package com.nibodha.agora.services.objectmapper;

public class Customer {

    private String date;
    private Float price;
    private Integer quantity;
    private Float total;
    private String name;
    private String id;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Float getTotal() {
        return total;
    }

    public void setTotal(Float total) {
        this.total = total;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "date='" + date + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", total=" + total +
                ", name='" + name + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}