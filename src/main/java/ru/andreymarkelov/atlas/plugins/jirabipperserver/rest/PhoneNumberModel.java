package ru.andreymarkelov.atlas.plugins.jirabipperserver.rest;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "phonenumber")
@XmlAccessorType(XmlAccessType.FIELD)
public class PhoneNumberModel {
    @XmlElement(name = "phone")
    private String phone;

    protected PhoneNumberModel() {
    }

    public PhoneNumberModel(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "PhoneNumberModel{" +
                "phone='" + phone + '\'' +
                '}';
    }
}
