package com.example.a11699.graduatemanager.askHttpDao;

public interface zhoujiDao {
    public void subbmitzhou(String time,String title,String body);//发表周记
    public void chartList(String page,Boolean a,int aa);//查看losing周记
    public void chayidu(String page,Boolean a);//查看已经读的
    public void quanbuzhouji();//全部周记
    public void acinformation(String idd);//周记详情
}
