package com.shop.entity;

public class Address {


    private String city; //서울시
    private String street; //~구 ~동
    private String zipcode; //상세주소

    protected Address() {
    }
    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
