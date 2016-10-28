package com.tcs.maverick.talentdevelopment.utilities;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by abhi on 3/29/2016.
 */
public class SaveImageAsync extends AsyncTask<Bitmap, Void, String> {
    private Context context;
    private ServiceResponse mServiceResponse;

    public SaveImageAsync(Context context, ServiceResponse mServiceResponse) {
        this.mServiceResponse = mServiceResponse;
        this.context = context;
    }

    @Override
    protected String doInBackground(Bitmap... params) {
        String path = saveToInternalStorage(params[0]);
        return path;
    }

    @Override
    protected void onPostExecute(String string) {
        super.onPostExecute(string);
        mServiceResponse.onServiceResponse(string);
    }

    public interface ServiceResponse {
        void onServiceResponse(String serviceResponse);
    }


    private String saveToInternalStorage(Bitmap bitmapImage) {
        ContextWrapper cw = new ContextWrapper(context);
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        File mypath = new File(directory, "profile.jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return directory.getAbsolutePath();
    }
}
