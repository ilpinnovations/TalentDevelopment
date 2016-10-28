package com.tcs.maverick.talentdevelopment.utilities;

/**
 * Created by abhi on 3/13/2016.
 */

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by abhi on 3/9/2016.
 */

public class SubmitFeedbackAsync extends AsyncTask<String, Void, String> {
    private OnService onService;
    private Context mContext;

    public SubmitFeedbackAsync(Context mContext, OnService onService) {
        this.mContext = mContext;
        this.onService = onService;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... arg0) {
        String param1 = arg0[0];
        String param2 = arg0[1];
        String param3 = arg0[2];
        String param4 = arg0[3];

        String param5 = arg0[4];
        String param6 = arg0[5];
        String param7 = arg0[6];
        String param8 = arg0[7];
        String param9 = arg0[8];
        String param10 = arg0[9];
        String param11 = arg0[10];
        String param12 = arg0[11];
        String param13 = arg0[12];
        String param14 = arg0[13];
        String param15 = arg0[14];
        String param16 = arg0[15];

        String param17 = arg0[16];
        String param18 = arg0[17];
        String param19 = arg0[18];
        String param20 = arg0[19];
        String param21 = arg0[20];
        String param22 = arg0[21];
        String param23 = arg0[22];
        String param24 = arg0[23];
        String param25 = arg0[24];
        String param26 = arg0[25];
        String param27 = arg0[26];
        String param28 = arg0[27];
        String param29 = arg0[28];
        String param30 = arg0[29];
        String param31 = arg0[30];


        try {
            String urlString = AppConstants.URL + "feedback1.php";
            URL url = new URL(urlString);

            Map<String, Object> params = new LinkedHashMap<>();

            params.put("sessionId", param1);
            params.put("employeeId", param2);
            params.put("date", param3);
            params.put("registrationId", param4);

            params.put("f2_rat1", param5);
            params.put("f2_rat2", param6);
            params.put("f2_rat3", param7);
            params.put("f2_rat4", param8);
            params.put("f2_rat5", param9);
            params.put("f2_rat6", param10);
            params.put("f2_rat7", param11);
            params.put("f2_rat8", param12);
            params.put("f2_rat9", param13);
            params.put("f2_rat10", param14);

            params.put("com1_f2", param15);
            params.put("com2_f2", param16);

            params.put("f1_rat1", param17);
            params.put("f1_rat2", param18);
            params.put("f1_rat3", param19);
            params.put("f1_rat4", param20);
            params.put("f1_rat5", param21);
            params.put("f1_rat6", param22);
            params.put("f1_rat7", param23);
            params.put("f1_rat8", param24);
            params.put("f1_rat9", param25);
            params.put("f1_rat10", param26);

            params.put("com1_f1", param27);
            params.put("com2_f1", param28);
            params.put("com3_f1", param29);
            params.put("com4_f1", param30);
            params.put("com5_f1", param31);

            Log.d("Hitting the server", "" + url);

            StringBuilder postData = new StringBuilder();

            for (Map.Entry<String, Object> param : params.entrySet()) {
                if (postData.length() != 0) postData.append('&');
                postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                //Log.d("Post data", String.valueOf(postData));
            }

            String urlParameters = postData.toString();
            URLConnection conn = url.openConnection();

            conn.setDoOutput(true);

            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());

            writer.write(urlParameters);
            writer.flush();

            BufferedReader reader = new BufferedReader
                    (new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;
            // Read Server Response
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                break;
            }
            return sb.toString();
        } catch (Exception e) {
            return new String(e.getMessage() + "Exception: null");
        }
    }

    @Override
    protected void onPostExecute(String result) {
        onService.onService(result);
    }

    public interface OnService {
        void onService(String string);
    }

}
