package ru.andreymarkelov.atlas.plugins.jirabipperserver.manager;

public interface SenderService {
    String generateApiKey(String accountKey, String accountId, String password) throws Exception;
}
