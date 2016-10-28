package com.tcs.maverick.talentdevelopment.utilities;

/**
 * Created by maverick on 12/26/2015.
 */

import android.content.Context;
import android.os.AsyncTask;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class HttpManager extends AsyncTask<String, Void, String> {
    private ServiceResponse mServiceResponse;
    private Context mContext;
    private static int statusCode=0;

    public HttpManager(Context mContext, ServiceResponse serviceResponse) {
        mServiceResponse = serviceResponse;
        this.mContext = mContext;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            String jsonString = null;
            URL urlConnection = new URL(params[0]);
            HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection.openConnection();
            httpURLConnection.connect();
            statusCode=httpURLConnection.getResponseCode();
            InputStream inputStream = httpURLConnection.getInputStream();
            jsonString = convertInputStreamToString(inputStream);
            return jsonString;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String convertInputStreamToString(InputStream inputStream) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String resultString = "";
        String line = "";
        try {
            while ((line = bufferedReader.readLine()) != null) {
                resultString += line;
            }
            return resultString;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    @Override
    protected void onPostExecute(String s) {
        mServiceResponse.onServiceResponse(s);
    }


    public interface ServiceResponse {
        void onServiceResponse(String serviceResponse);
    }

    public static int getStatusCode() {
        return statusCode;
    }
}
