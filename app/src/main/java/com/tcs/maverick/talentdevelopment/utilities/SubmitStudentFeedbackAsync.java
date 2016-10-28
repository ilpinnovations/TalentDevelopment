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

public class SubmitStudentFeedbackAsync extends AsyncTask<String, Void, String> {
    private OnService onService;
    private Context mContext;

    public SubmitStudentFeedbackAsync(Context mContext, OnService onService) {
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


        try {
            String urlString = AppConstants.URL + "trainer_feedback_insert.php";
            URL url = new URL(urlString);

            Map<String, Object> params = new LinkedHashMap<>();

            params.put("trainer_emp_id", param1);
            params.put("trainee_emp_id", param2);
            params.put("rating", param3);
            params.put("comment", param4);
            params.put("session_id", param5);


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
