package ru.andreymarkelov.atlas.plugins.jirabipperserver.manager.impl;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.atlassian.activeobjects.external.ActiveObjects;
import net.java.ao.Query;
import ru.andreymarkelov.atlas.plugins.jirabipperserver.data.Contact;
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
    public List<Contact> getAllContacts() {
        ContantAO[] contantAOs = ao.find(ContantAO.class, Query.select());
        return Stream.of(contantAOs)
                .map(contantAO -> new Contact(contantAO.getID(), contantAO.getKey(), contantAO.getName(), contantAO.getPhone()))
                .collect(Collectors.toList());
    }

    @Override
    public Contact update(Contact contact) {
        ContantAO contantAO = ao.get(ContantAO.class, contact.getId());
        if (contantAO != null) {
            if (contact.getKey() != null) {
                contantAO.setKey(contact.getKey());
            }
            if (contact.getName() != null) {
                contantAO.setName(contact.getName());
            }
            if (contact.getPhone() != null) {
                contantAO.setPhone(contact.getPhone());
            }
            contantAO.save();
            contact.setId(contantAO.getID());
            contact.setKey(contantAO.getKey());
            contact.setName(contantAO.getName());
            contact.setPhone(contantAO.getPhone());
        }
        return contact;
    }

    @Override
    public Contact save(Contact contact) {
        ContantAO[] contantAOs = ao.find(ContantAO.class, Query.select().where("\"KEY\" = ?", contact.getKey()));
        if (contantAOs.length > 0) {
            Stream.of(contantAOs).forEach(contantAO -> {
                contantAO.setKey(contact.getKey());
                contantAO.setName(contact.getName());
                contantAO.setPhone(contact.getPhone());
                contantAO.save();
                contact.setId(contantAO.getID());
            });
        } else {
            ContantAO contantAO = ao.create(ContantAO.class);
            contantAO.setKey(contact.getKey());
            contantAO.setName(contact.getName());
            contantAO.setPhone(contact.getPhone());
            contantAO.save();
            contact.setId(contantAO.getID());
        }
        return contact;
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

    @Override
    public void deleteById(Integer id) {
        ContantAO contantAO = ao.get(ContantAO.class, id);
        if (contantAO != null) {
            ao.delete(contantAO);
        }
    }

    @Override
    public Contact getById(Integer id) {
        ContantAO contantAO = ao.get(ContantAO.class, id);
        if (contantAO != null) {
            Contact contact = new Contact();
            contact.setId(contantAO.getID());
            contact.setKey(contantAO.getKey());
            contact.setName(contantAO.getName());
            contact.setPhone(contantAO.getPhone());
        }
        return null;
    }
}
