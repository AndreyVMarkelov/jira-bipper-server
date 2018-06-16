package ru.andreymarkelov.atlas.plugins.jirabipperserver.manager;

public interface AuthManager {
    String getSenderName();
    void setSenderName(String senderName);

    String getApiKey();
    void setApiKey(String apiKey);

    Long getGenerationTime();
    void setGenerationTime(Long generationTime);
}
