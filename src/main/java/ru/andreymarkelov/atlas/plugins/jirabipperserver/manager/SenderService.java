package ru.andreymarkelov.atlas.plugins.jirabipperserver.manager;

public interface SenderService {
    String generateApiKey(String accountId, String password) throws Exception;
}
