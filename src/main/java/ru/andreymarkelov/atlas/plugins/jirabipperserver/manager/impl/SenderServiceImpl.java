package ru.andreymarkelov.atlas.plugins.jirabipperserver.manager.impl;

import ru.andreymarkelov.atlas.plugins.jirabipperserver.manager.SenderService;

public class SenderServiceImpl implements SenderService {
    @Override
    public String generateApiKey(String accountId, String password) throws Exception {
        //throw new RuntimeException("wedwe");
        return "sample";
    }
}
