package ru.andreymarkelov.atlas.plugins.jirabipperserver.manager.impl;

import java.util.stream.Stream;

import com.atlassian.activeobjects.external.ActiveObjects;
import net.java.ao.Query;
import ru.andreymarkelov.atlas.plugins.jirabipperserver.data.ao.ContantAO;
import ru.andreymarkelov.atlas.plugins.jirabipperserver.manager.ContactManager;

public class ContactManagerImpl implements ContactManager {
    private final ActiveObjects ao;

    public ContactManagerImpl(ActiveObjects ao) {
        this.ao = ao;
    }

    @Override
    public String getPhoneByKey(String key) {
        ContantAO[] contantAOs = ao.find(ContantAO.class, Query.select().where("\"KEY\" = ?", key));
        if (contantAOs.length > 0) {
            return contantAOs[0].getPhone();
        }
        return null;
    }

    @Override
    public String getPhoneByName(String name) {
        ContantAO[] contantAOs = ao.find(ContantAO.class, Query.select().where("\"NAME\" = ?", name));
        if (contantAOs.length > 0) {
            return contantAOs[0].getPhone();
        }
        return null;
    }

    @Override
    public void storePhone(String key, String name, String phone) {
        ContantAO[] contantAOs = ao.find(ContantAO.class, Query.select().where("\"KEY\" = ?", key));
        if (contantAOs.length > 0) {
            Stream.of(contantAOs).forEach(contantAO -> {
                contantAO.setKey(key);
                contantAO.setName(name);
                contantAO.setPhone(phone);
                contantAO.save();
            });
        } else {
            ContantAO contantAO = ao.create(ContantAO.class);
            contantAO.setKey(key);
            contantAO.setName(name);
            contantAO.setPhone(phone);
            contantAO.save();
        }
    }
}
