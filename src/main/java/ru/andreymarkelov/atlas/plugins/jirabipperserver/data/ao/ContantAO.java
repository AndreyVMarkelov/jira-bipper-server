package ru.andreymarkelov.atlas.plugins.jirabipperserver.data.ao;

import net.java.ao.Entity;
import net.java.ao.Preload;
import net.java.ao.schema.Indexed;

@Preload
public interface ContantAO extends Entity {
    @Indexed
    String getKey();
    void setKey(String key);

    @Indexed
    String getName();
    void setName(String name);

    String getPhone();
    void setPhone(String phone);
}
