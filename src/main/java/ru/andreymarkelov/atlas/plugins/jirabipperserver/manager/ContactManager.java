package ru.andreymarkelov.atlas.plugins.jirabipperserver.manager;

import java.util.List;

import com.atlassian.activeobjects.tx.Transactional;
import ru.andreymarkelov.atlas.plugins.jirabipperserver.data.Contact;

@Transactional
public interface ContactManager {
    List<Contact> getAllContacts();
    Contact update(Contact contact);
    Contact save(Contact contact);
    Contact getById(Integer id);
    void deleteById(Integer id);

    String getPhoneByKey(String key);
    String getPhoneByName(String name);
    void storePhone(String key, String name, String phone);
}
