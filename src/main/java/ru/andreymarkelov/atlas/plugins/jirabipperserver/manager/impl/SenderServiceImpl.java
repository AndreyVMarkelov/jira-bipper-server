package ru.andreymarkelov.atlas.plugins.jirabipperserver.manager.impl;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;

import com.google.gson.Gson;
import com.nebhale.jsonpath.JsonPath;
import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.andreymarkelov.atlas.plugins.jirabipperserver.manager.SenderService;

import static java.util.Base64.getEncoder;
import static java.util.Collections.singletonMap;

public class SenderServiceImpl implements SenderService {
    private static final Logger log = LoggerFactory.getLogger(SenderServiceImpl.class);

    private static final String INFOBIP_AUTH_URL = "https://api.infobip.com/settings/1/accounts/ACCOUNT_KEY/api-keys";

    private static SSLContext createSslContext() {
        try {
            return new SSLContextBuilder().loadTrustMaterial(null, (x509Certificates, authType) -> true).build();
        } catch (NoSuchAlgorithmException e) {
            log.error("Error creating SSL context", e);
            throw new RuntimeException("Error creating SSL context. No such algorithm.");
        } catch (KeyManagementException e) {
            log.error("Error creating SSL context", e);
            throw new RuntimeException("Error creating SSL context. Key manager exception.");
        } catch (KeyStoreException e) {
            log.error("Error creating SSL context", e);
            throw new RuntimeException("Error creating SSL context. Keystore exception.");
        }
    }

    @Override
    public String generateApiKey(String accountKey, String accountId, String password) throws Exception {
        CloseableHttpClient httpClient = createClient();
        HttpPost request = new HttpPost(INFOBIP_AUTH_URL.replace("ACCOUNT_KEY", accountKey));
        request.setHeader(HttpHeaders.AUTHORIZATION, "Basic " + new String(getEncoder().encode((accountId + ":" + password).getBytes())));
        request.setEntity(new StringEntity(new Gson().toJson(singletonMap("name", "JIRA API KEY"))));

        CloseableHttpResponse httpResponse = httpClient.execute(request);
        if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_CREATED) {
            String message = JsonPath.compile("$.requestError.serviceException.text").read(EntityUtils.toString(httpResponse.getEntity()), String.class);
            throw new RuntimeException(Objects.toString(message, "Unhandled exception"));
        }
        return JsonPath.compile("$.publicApiKey").read(EntityUtils.toString(httpResponse.getEntity()), String.class);
    }

    private CloseableHttpClient createClient() {
        SSLContext sslContext = createSslContext();
        HostnameVerifier hostnameVerifier = NoopHostnameVerifier.INSTANCE;
        SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContext, hostnameVerifier);

        List<Header> headers = new ArrayList<>();
        headers.add(new BasicHeader(HttpHeaders.CONTENT_TYPE, "application/json"));
        headers.add(new BasicHeader(HttpHeaders.ACCEPT, "application/json"));
        HttpClientBuilder httpClientBuilder = HttpClients.custom()
                .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                .setSSLSocketFactory(sslSocketFactory)
                .setDefaultHeaders(headers);
        return httpClientBuilder.build();
    }
}
