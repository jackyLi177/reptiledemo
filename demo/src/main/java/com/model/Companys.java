package com.model;

import lombok.Data;

@Data
public class Companys {
    private Integer id;

    private String name;

    private String tel;

    private String orgcode;

    private String code;

    private String address;

    private String business;

    private String owner;

    public Companys(String name, String tel, String orgcode, String code, String address, String business, String owner) {
        this.name = name;
        this.tel = tel;
        this.orgcode = orgcode;
        this.code = code;
        this.address = address;
        this.business = business;
        this.owner = owner;
    }
}