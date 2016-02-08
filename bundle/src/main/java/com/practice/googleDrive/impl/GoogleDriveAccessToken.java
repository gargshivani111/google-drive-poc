package com.practice.googleDrive.impl;



import org.apache.commons.codec.binary.Base64;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;

@Component(immediate = true, metatype = false, label = "Get Access Token")
@Service
@Properties(value = {
        @Property(name = "sling.servlet.methods", value = "POST"),
        @Property(name = "sling.servlet.paths", value = "/bin/googleDriveToken")
})
public class GoogleDriveAccessToken extends SlingAllMethodsServlet {
    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        String url = "https://www.googleapis.com/oauth2/v4/token";
        String method = "POST";
        String grant_type = "authorization_code";
        String code = request.getParameter("code");
        String redirect_uri = "http://localhost:7502/etc/cloudservices/googledriveconnect/GoogleDriveDemo.html";
        HashMap <String ,String> map = new HashMap<String, String>();
        map.put("grant_type", grant_type);
        map.put("code", code);
        map.put("redirect_uri", redirect_uri);
        map.put("state","https://www.googleapis.com/auth/drive");


        String str = getHttpResponse(url, method,  map );



        try {
             JSONObject jsonObject=new JSONObject();
            GetImagesData getImagesData=new GetImagesData();
            jsonObject=getImagesData.ImagesJson(str);
            response.getWriter().write(jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public static String getHttpResponse(String url, String method, HashMap params) throws IOException {
        HttpURLConnection urlConnection=null;
        InputStream is =null;
        if(url != null && url.trim().length()>0){

            try{
                String name = "260020511067-04b4a7gqd3brujo5slvss6tfpam57oac.apps.googleusercontent.com";
                String password = "wivlMVO6vzv11GKqvgp6am3a";
                String authString = name + ":" + password;

                byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
                String s = new String(authEncBytes);
                URL _url = new URL(url);
                urlConnection = (HttpURLConnection)_url.openConnection();
                urlConnection.setRequestProperty("Authorization", "basic " + s);
                urlConnection.setDoInput(true);

                StringBuffer requestParams = new StringBuffer();

                if (params != null && params.size() > 0) {

                    urlConnection.setDoOutput(true); // true indicates POST request

                    // creates the params string, encode them using URLEncoder
                    Iterator<String> paramIterator = params.keySet().iterator();
                    while (paramIterator.hasNext()) {
                        String key = paramIterator.next();
                        String value = params.get(key).toString();
                        requestParams.append(URLEncoder.encode(key, "UTF-8"));
                        requestParams.append("=").append(
                                URLEncoder.encode(value, "UTF-8"));
                        requestParams.append("&");
                    }

                    // sends POST data
                    OutputStreamWriter writer = new OutputStreamWriter(
                            urlConnection.getOutputStream());
                    writer.write(requestParams.toString());
                    writer.flush();
                    is = urlConnection.getInputStream();
                    BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                    String line;
                    StringBuffer response = new StringBuffer();
                    while((line = rd.readLine()) != null) {
                        response.append(line);
                        response.append('\r');
                    }
                    rd.close();
                    return response.toString();
                }



            }catch(MalformedURLException e){
                e.printStackTrace();
            }catch(IOException e){
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