package ru.andreymarkelov.atlas.plugins.jirabipperserver.manager;

public interface ContactManager {
    String getPhoneByName(String name);
    String getPhoneByEmail(String name);
}
