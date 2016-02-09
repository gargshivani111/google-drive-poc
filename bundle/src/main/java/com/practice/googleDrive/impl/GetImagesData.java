package com.practice.googleDrive.impl;



import org.apache.sling.commons.json.JSONArray;
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
        JSONObject jsonObject;//=new JSONObject();
         accessToken=getAccessToken(accessToken);
        String url = "https://content.googleapis.com/drive/v2/files/0B2NqFQc4CTl6Tk1PMTlaaEZFYkk/children?key=AIzaSyA7CRgJ64rm5_o7u5WIYeMzimXyaFatwUQ";
        String str = getHttpResponse(url, accessToken);
        System.out.print(str);
        jsonObject = new JSONObject(str);

        JSONArray jsonArray = new JSONArray();
        jsonArray = (JSONArray)jsonObject.get("items");

            try {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject item = jsonArray.getJSONObject(i);
                    String selfLink = item.getString("childLink");
                    String val = getHttpResponse(selfLink, accessToken);
                    jsonArray.getJSONObject(i).put("self", new JSONObject(val));
                }
            } catch (Exception e)
            {
              System.out.print("exception");
            }

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

           private String getAccessToken(String accessTokenStr) throws JSONException{
                JSONObject obj = new JSONObject(accessTokenStr);
               return obj.get("access_token").toString() ;

           }

                    }



