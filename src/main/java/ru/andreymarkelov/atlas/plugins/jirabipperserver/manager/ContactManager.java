package ru.andreymarkelov.atlas.plugins.jirabipperserver.manager;

import com.atlassian.activeobjects.tx.Transactional;

@Transactional
public interface ContactManager {
    String getPhoneByKey(String key);
    String getPhoneByName(String name);
    void storePhone(String key, String name, String phone);
}
