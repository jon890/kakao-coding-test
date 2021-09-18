package com.bifos.kakao.network;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class HttpClient {

    private final Gson gson;

    public HttpClient(Gson gson) {
        this.gson = gson;
    }

    public String send(String urlString, Map<String, String> headers, Map<String, Object> body) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept-Charset", "UTF-8");
            for (Map.Entry<String, String> headerEntry : headers.entrySet()) {
                connection.setRequestProperty(headerEntry.getKey(), headerEntry.getValue());
            }
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);

            connection.setDoOutput(true); // POST 요청시 데이터 넘겨줄때 사용해야함
            DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
            String bodyJson = gson.toJson(body);
            outputStream.writeBytes(bodyJson);
            outputStream.flush();
            outputStream.close();

            int responseCode = connection.getResponseCode();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder stringBuffer = new StringBuilder();
            String inputLine;

            while ((inputLine = bufferedReader.readLine()) != null) {
                stringBuffer.append(inputLine);
            }
            bufferedReader.close();
            String response = stringBuffer.toString();

            return response;
        } catch (MalformedURLException e) {
            // URL 형식이 잘못
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            // Connection Open Error
            e.printStackTrace();
            return null;
        }
    }

    public <T> void parseJson(String jsonString, Class<T> clazz) {
        gson.fromJson(jsonString, clazz);
    }
}
