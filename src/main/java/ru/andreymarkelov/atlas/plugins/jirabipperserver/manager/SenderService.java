package ru.andreymarkelov.atlas.plugins.jirabipperserver.manager;

import java.util.List;

public interface SenderService {
    String generateApiKey(String accountKey, String accountId, String password) throws Exception;
    boolean sendMessage(String text, List<String> phones) throws Exception;
}
