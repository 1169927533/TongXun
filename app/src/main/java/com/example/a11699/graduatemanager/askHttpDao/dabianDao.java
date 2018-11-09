package com.example.a11699.graduatemanager.askHttpDao;

public interface dabianDao {
    void getDabainInfor(String s_sid);//获取答辩列表
    void putDabain(String s_sid,String neirong,String r_id);//学生进行答辩
}
