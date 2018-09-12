package com.btl.hcj.myapplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MyUtils {

    private static HttpsURLConnection mHttpsURLConnection;
    private static InputStream mInputStream;
    public static final String apiKey = "AIzaSyAU8W1FY9oL7AVgl467lvvTNHCh1E79PVI";

    public static String getJson(URL url) {
        String json = null;
        try {
            mHttpsURLConnection = (HttpsURLConnection) url.openConnection();
            mHttpsURLConnection.connect();
            mInputStream = mHttpsURLConnection.getInputStream();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(mInputStream, "UTF-8"));
            StringBuilder sb = new StringBuilder();
            String line = "";

            while ((line = bufferedReader.readLine()) != null)
                sb.append(line);

            json = sb.toString();

            bufferedReader.close();
            mInputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return json;
    }

    public static String getJson(InputStream is) {
        String json = null;
        try {
            mInputStream = is;

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(mInputStream, "UTF-8"));
            StringBuilder sb = new StringBuilder();
            String line = "";

            while ((line = bufferedReader.readLine()) != null)
                sb.append(line);

            json = sb.toString();

            bufferedReader.close();
            mInputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return json;
    }
}
