package com.tcs.maverick.talentdevelopment.utilities;

/**
 * Created by maverick on 12/26/2015.
 */

import android.content.Context;
import android.os.AsyncTask;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;


public class GenerateGCMKey extends AsyncTask<String, Void, String> {
    private ServiceResponse mServiceResponse;
    private Context mContext;
    private static int statusCode = 0;
    private InstanceID instanceID;
    private String regId;

    public GenerateGCMKey(Context mContext, ServiceResponse serviceResponse) {
        mServiceResponse = serviceResponse;
        this.mContext = mContext;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected String doInBackground(String... params) {

        String msg = "";

        try {
            instanceID = InstanceID.getInstance(mContext);
            regId = instanceID.getToken(String.valueOf(AppConstants.GOOGLE_PROJ_ID), GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            msg = "Registration ID :" + regId;
        } catch (IOException ex) {
            msg = "Error :" + ex.getMessage();
        }
        return regId;

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
