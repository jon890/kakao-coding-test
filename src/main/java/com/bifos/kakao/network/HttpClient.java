package com.bifos.kakao.network;

import com.bifos.kakao.network.model.BaseResponse;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class HttpClient {

    public enum RequestMethod {
        POST("POST"),
        GET("GET"),
        PUT("PUT")
        ;

        String value;

        RequestMethod(String value) {
            this.value = value;
        }
    }

    public HttpClient() {
    }

    public BaseResponse<String, Exception> request(RequestMethod method,
                                                   String urlString,
                                                   Map<String, String> headers,
                                                   Map<String, Object> body){
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod(method.value);
//            connection.setRequestProperty("Content-Type", "application/json");
//            connection.setRequestProperty("Accept-Charset", "UTF-8");

            for (Map.Entry<String, String> headerEntry : headers.entrySet()) {
                connection.setRequestProperty(headerEntry.getKey(), headerEntry.getValue());
            }

            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);

            if (method == RequestMethod.POST) {
                connection.setDoOutput(true);
                DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
                String bodyJson = gson.toJson(body);
                outputStream.writeBytes(bodyJson);
                outputStream.flush();
                outputStream.close();
            }

            int responseCode = connection.getResponseCode();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder stringBuffer = new StringBuilder();
            String inputLine;

            while ((inputLine = bufferedReader.readLine()) != null) {
                stringBuffer.append(inputLine);
            }
            bufferedReader.close();
            String data = stringBuffer.toString();

            return new BaseResponse<>(responseCode, data);
        } catch (IOException e) {
            // URL 형식이 잘못
            // Connection Open Error
            // 할 수 있는게 없음
            e.printStackTrace();
            return null;
        }
    }
}
