package com.sehalsein.alzarcapartment.Model;

/**
 * Created by sehalsein on 21/11/17.
 */

public class SecurityDetail {

    private String id;
    private String name;
    private String timing;
    private String address;
    private String phoneNo;


    public SecurityDetail() {
    }

    public SecurityDetail(String id, String name, String timing) {
        this.id = id;
        this.name = name;
        this.timing = timing;
    }


    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTiming() {
        return timing;
    }

    public void setTiming(String timing) {
        this.timing = timing;
    }
}
