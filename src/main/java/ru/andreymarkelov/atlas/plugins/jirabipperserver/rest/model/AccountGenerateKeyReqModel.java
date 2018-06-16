package ru.andreymarkelov.atlas.plugins.jirabipperserver.rest.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "accountGenerateReq")
@XmlAccessorType(XmlAccessType.FIELD)
public class AccountGenerateKeyReqModel {
    @XmlElement(name = "sender")
    private String sender;

    @XmlElement(name = "accountId")
    private String accountId;

    @XmlElement(name = "password")
    private String password;

    protected AccountGenerateKeyReqModel() {
    }

    public AccountGenerateKeyReqModel(String sender, String accountId, String password) {
        this.sender = sender;
        this.accountId = accountId;
        this.password = password;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "AccountGenerateKeyReqModel{" +
                "sender='" + sender + '\'' +
                ", accountId='" + accountId + '\'' +
                '}';
    }
}
