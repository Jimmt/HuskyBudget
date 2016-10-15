package com.jimmt.huskybudget;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;

public class GetTermDataTask extends AsyncTask<String, String, JSONObject> {

    @Override
    protected JSONObject doInBackground(String... params) {
        DefaultHttpClient httpClient = new DefaultHttpClient(new BasicHttpParams());
        HttpGet httpGet = new HttpGet(
        "https://ws.admin.washington.edu/student/v5/term/current.json");
        httpGet.setHeader("Authorization", "Bearer 74064A37-E71B-41E4-BE38-9D4CA35E6356");
        httpGet.setHeader("Accept", "application/json");

        InputStream inputStream = null;
        String result = null;
        try {
            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            inputStream = entity.getContent();

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            StringBuilder sb = new StringBuilder();

            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            result = sb.toString();
            JSONObject object = new JSONObject(result);
            
            return object;
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null)
                    inputStream.close();
            } catch (Exception squish) {
            }
        }
        return null;
    }
    
    @Override
    protected void onPostExecute(JSONObject result) {
        //        try {
        //            System.out.println(result.get("LastDayOfClasses"));
        //        } catch (JSONException e) {
        //            e.printStackTrace();
        //        }
    }

}
