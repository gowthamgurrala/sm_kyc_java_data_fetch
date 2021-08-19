package com.itr_downloads.lambdaApi;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.*;

import javax.net.ssl.*;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

public class saveEntity implements RequestHandler<SQSEvent, Void> {
    Gson gson = new GsonBuilder().serializeSpecialFloatingPointValues().enableComplexMapKeySerialization()
            .serializeNulls().setVersion(1.0).create();

    public static Object getDetails(Map<String, Object> jsonData) throws IOException {
        Gson gson = new Gson();
        String json = gson.toJson(jsonData);

        OkHttpClient client = getUnsafeOkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, json);
        Request request = new Request.Builder()
                .url("https://eportal.incometax.gov.in/iec/servicesapi/saveEntity")
                .method("POST", body)
                .addHeader("Connection", "keep-alive")
                .addHeader("sec-ch-ua", "\" Not;A Brand\";v=\"99\", \"Google Chrome\";v=\"91\", \"Chromium\";v=\"91\"")
                .addHeader("Accept", "application/json, text/plain, */*")
                .addHeader("sec-ch-ua-mobile", "?0")
                .addHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.114 Safari/537.36")
                .addHeader("Content-Type", "application/json")
                .addHeader("Origin", "https://eportal.incometax.gov.in")
                .addHeader("Sec-Fetch-Site", "same-origin")
                .addHeader("Sec-Fetch-Mode", "cors")
                .addHeader("Sec-Fetch-Dest", "empty")
                .addHeader("Referer", "https://eportal.incometax.gov.in/iec/foservices/")
                .addHeader("Accept-Language", "en-US,en;q=0.9,lb;q=0.8")
                .build();
        Response response = client.newCall(request).execute();

        if (response != null) {
            OkHttpClient client1 = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType1 = MediaType.parse("application/json");
            RequestBody body1 = RequestBody.create(mediaType1, "{\"referenceId\":\"1683f278-8502-401a-bc1e-d20157881afe\",\n\"productType\":\"PAN\",\n\"responseCode\":\"2000\"}");
            Request request1 = new Request.Builder()
                    .url("https://sm-kyc-quality.scoreme.in/kyc/save/smkycvendorrequestdetails")
                    .method("POST", body1)
                    .addHeader("ClientId", "85a55d204c3fc467764f1985205aaa67")
                    .addHeader("ClientSecret", "4ac8ec0816af87f3fe3ea6c985236f5b6ce8a90bb2ef3167151cf0e761142569")
                    .addHeader("Content-Type", "application/json")
                    .build();
            Response response1 = client1.newCall(request1).execute();

            System.out.println(response1.body().string());
            System.out.println(response.body().string());
        }
        return null;
    }

    public static void main(String[] args) throws IOException {
        Map<String, Object> map = new HashMap<>();
        map.put("serviceName", "itrStatusService");
        map.put("ackNum", "121212301293819");
        map.put("mobNum", "9643101022");
        getDetails(map);
    }

    private static OkHttpClient getUnsafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(X509Certificate[] chain,
                                                       String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] chain,
                                                       String authType) throws CertificateException {
                        }

                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[0];
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            return new OkHttpClient.Builder()
                    .sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0])
                    .hostnameVerifier(new HostnameVerifier() {
                        @Override
                        public boolean verify(String hostname, SSLSession session) {
                            return true;
                        }
                    }).build();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Void handleRequest(SQSEvent sqsEvent, Context context) {
        return null;
    }

}

