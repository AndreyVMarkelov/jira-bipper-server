package ru.andreymarkelov.atlas.plugins.jirabipperserver.rest;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "phonenumber")
@XmlAccessorType(XmlAccessType.FIELD)
public class PhoneNumberModel {
    @XmlElement(name = "key")
    private String key;

    @XmlElement(name = "name")
    private String name;

    @XmlElement(name = "phone", nillable = true)
    private String phone;

    protected PhoneNumberModel() {
    }

    public PhoneNumberModel(String key, String name, String phone) {
        this.key = key;
        this.name = name;
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "PhoneNumberModel{" +
                "key='" + key + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
