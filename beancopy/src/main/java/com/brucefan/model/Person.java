package com.brucefan.model;

import java.util.Date;
import java.util.List;

/**
 * Created by bruce01.fan on 2015/11/3.
 */
public class Person {

    private String name;

    private int age;

    private Date brithday;

    private List<Address> address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Date getBrithday() {
        return brithday;
    }

    public void setBrithday(Date brithday) {
        this.brithday = brithday;
    }

    public List<Address> getAddress() {
        return address;
    }

    public void setAddress(List<Address> address) {
        this.address = address;
    }
}
