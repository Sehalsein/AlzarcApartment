package com.pavigeeth.alzarcapartment.Model;

/**
 * Created by sehalsein on 21/12/17.
 */

public class BasicUserModal {

    private String id;
    private String name;

    public BasicUserModal() {
    }

    public BasicUserModal(String id, String name) {
        this.id = id;
        this.name = name;
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
}
