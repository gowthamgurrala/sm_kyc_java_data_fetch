package com.itr_downloads.lambdaApi;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import okhttp3.*;

import javax.net.ssl.*;
import java.io.IOException;
import java.lang.reflect.Type;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

public class Login implements RequestHandler<SQSEvent, Void> {
    Gson gson = new GsonBuilder().serializeSpecialFloatingPointValues().enableComplexMapKeySerialization()
            .serializeNulls().setVersion(1.0).create();

    public static Object getDetails(Map<String, Object> jsonData) throws IOException {
        Gson gson = new Gson();
        String json = gson.toJson(jsonData);

        OkHttpClient client = getUnsafeOkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\r\n    \"errors\": [],\r\n    \"reqId\": \"FOS000062556903\",\r\n    \"entity\": \"AAACB6668A\",\r\n    \"entityType\": \"PAN\",\r\n    \"role\": \"CO\",\r\n    \"uidValdtnFlg\": \"true\",\r\n    \"aadhaarMobileValidated\": \"false\",\r\n    \"secAccssMsg\": \"\",\r\n    \"secLoginOptions\": \"\",\r\n    \"aadhaarLinkedWithUserId\": \"Y\",\r\n    \"exemptedPan\": \"false\",\r\n    \"userConsent\": \"\",\r\n    \"imgByte\": null,\r\n    \"pass\": \"amFnaXdhbGFAMTIzNDU=\",\r\n    \"passValdtnFlg\": null,\r\n    \"otpGenerationFlag\": null,\r\n    \"otp\": null,\r\n    \"otpValdtnFlg\": null,\r\n    \"otpSourceFlag\": null,\r\n    \"contactPan\": null,\r\n    \"contactMobile\": null,\r\n    \"contactEmail\": null,\r\n    \"email\": null,\r\n    \"mobileNo\": null,\r\n    \"forgnDirEmailId\": null,\r\n    \"imagePath\": null,\r\n    \"serviceName\": \"loginService\"\r\n}");
        Request request = new Request.Builder()
                .url("http://eportal.incometax.gov.in/iec/loginapi/login")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
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
        map.put("errors", "");
        map.put("name", "Neeraj Gaur");
        map.put("date_of_birth", "1989-12-25");
        map.put("consent", "Y");
        getDetails(map);
    }

    private static OkHttpClient getUnsafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain,
                                                       String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain,
                                                       String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
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
    public Void handleRequest(SQSEvent event, Context context) {
        for (SQSEvent.SQSMessage msg : event.getRecords()) {
            Type type = new TypeToken<Map<String, Object>>() {
            }.getType();
            System.out.println(msg.getBody());
            Map<String, Object> myMap = gson.fromJson(msg.getBody(), type);
            try {
                getDetails(myMap);
            } catch (IOException e) {

                e.printStackTrace();
            }
        }
        return null;
    }


}

