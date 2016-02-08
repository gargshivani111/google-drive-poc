package com.practice.googleDrive.impl;



import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


import java.io.IOException;

public class GetImagesData {
    public JSONObject ImagesJson(String accessToken) throws IOException,JSONException {

        System.out.print("i m inside imageJson");
        String url = "https://content.googleapis.com/drive/v2/files/0B2NqFQc4CTl6Tk1PMTlaaEZFYkk/children?key=AIzaSyC_WHu3zJbeY0iyHQJj-JhMmCJ8mmRycy4";
        String str = getHttpResponse(url, accessToken);
        JSONObject jsonObject = new JSONObject(str);
        return jsonObject;
    }

    public static String getHttpResponse(String url, String accessToken) throws IOException {
        HttpsURLConnection urlConnection = null;
        InputStream is=null;
        if (url != null && url.trim().length() > 0) {

            try {
                URL _url = new URL(url);
                urlConnection = (HttpsURLConnection) _url.openConnection();
                urlConnection.setRequestMethod("GET");

                urlConnection.setRequestProperty("Authorization", "Bearer " + accessToken);
                int responseCode = urlConnection.getResponseCode();
                System.out.println("Response Code  -- -- -- -- -- --  " + responseCode);
                InputStream inputStream = urlConnection.getInputStream();
                BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                return response.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                if (urlConnection instanceof HttpURLConnection) {
                    HttpURLConnection httpConn = urlConnection;
                    int statusCode = httpConn.getResponseCode();
                    if (statusCode != 200) {
                        is = httpConn.getErrorStream();
                    }
                }
                return e.getMessage();
            }
        }
        return null;
    }
                    }



