package ru.andreymarkelov.atlas.plugins.jirabipperserver.rest;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "accountSettingsResp")
@XmlAccessorType(XmlAccessType.FIELD)
public class AccountSettingsRespModel {
    @XmlElement(name = "sender")
    private String sender;

    protected AccountSettingsRespModel() {
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}
