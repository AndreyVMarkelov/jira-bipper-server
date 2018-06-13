package ru.andreymarkelov.atlas.plugins.jirabipperserver.data.ao;

import net.java.ao.Entity;
import net.java.ao.Preload;

@Preload
public interface EventSettingsAO extends Entity {
    int getEventType();
    void setEventType(int eventType);
}
