package com.example.a11699.graduatemanager.lei;

import java.util.ArrayList;
import java.util.List;

public class tuisongInformation {
    public static List<String> list=new ArrayList<>();
    private String detail;
    private String type;
    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public static List<String> getList() {
        return list;
    }

    public static void setList(List<String> list) {
        tuisongInformation.list = list;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public tuisongInformation(String detail, String type) {
        this.detail = detail;
        this.type=type;
    }
}
