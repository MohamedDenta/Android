package com.denta.etbar3;

import java.io.Serializable;

public class User implements Serializable {
    String name , phone  , bloodtype , dntDate;
    public User() {
    }
    public User(String name, String phone, String bloodtype, String dntDate) {
        this.name = name;
        this.phone = phone;
        this.bloodtype = bloodtype;
        this.dntDate = dntDate;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getBloodtype() {
        return bloodtype;
    }

    public String getDntDate() {
        return dntDate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setBloodtype(String bloodtype) {
        this.bloodtype = bloodtype;
    }

    public void setDntDate(String dntDate) {
        this.dntDate = dntDate;
    }
}
