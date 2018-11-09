package com.example.a11699.graduatemanager.lei;

import java.util.List;

public class telPeople {
    public List<String> telList;
    private String name;
    private String id;

    public telPeople(String name, String id) {
        this.name = name;
        this.id = id;
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
}
