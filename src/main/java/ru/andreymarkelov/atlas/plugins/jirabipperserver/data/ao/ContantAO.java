package ru.andreymarkelov.atlas.plugins.jirabipperserver.data.ao;

import net.java.ao.Entity;
import net.java.ao.Preload;
import net.java.ao.schema.Indexed;

@Preload
public interface ContantAO extends Entity {
    @Indexed
    Long getName();
    void setName(String name);

    @Indexed
    Long getEmail();
    void setEmail(String email);

    String getPhone();
    void setPhone(String phone);
}
