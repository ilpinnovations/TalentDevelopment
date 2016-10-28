package com.tcs.maverick.talentdevelopment.activities;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.tcs.maverick.talentdevelopment.R;
import com.tcs.maverick.talentdevelopment.utilities.ConnectionDetector;
import com.tcs.maverick.talentdevelopment.utilities.GenerateGCMKey;
import com.tcs.maverick.talentdevelopment.utilities.RegisterUserAsync;
import com.tcs.maverick.talentdevelopment.utilities.SaveImageAsync;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by abhi on 3/1/2016.
 */

public class LoginActivity extends AppCompatActivity {
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@tcs.com", Pattern.CASE_INSENSITIVE);
    private static final int SELECT_PICTURE = 0;
    private EditText empId;
    private EditText empEmail;
    private CircleImageView userImage;
    private Button submitButton;
    private View view1, view2;
    private TextView messageText;
    private CoordinatorLayout coordinatorLayout;
    private Uri uri;
    private String fname = "";
    private TextView chooseImageMessageText;
    private RadioButton radioButton1, radioButton2;
    private String type;

    public static boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getSharedPreferences("Talent Development", MODE_PRIVATE);
        String employeeId = sharedPreferences.getString("emp_id", "");

        if (employeeId != "") {
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }

        setContentView(R.layout.login_activity);
        getSupportActionBar().setTitle("Register");

        userImage = (CircleImageView) findViewById(R.id.userImage);
        empId = (EditText) findViewById(R.id.employeeId);
        empEmail = (EditText) findViewById(R.id.employeeEmail);
        submitButton = (Button) findViewById(R.id.submitButton);
        view1 = findViewById(R.id.view1);
        view2 = findViewById(R.id.view2);
        messageText = (TextView) findViewById(R.id.messageText);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        chooseImageMessageText = (TextView) findViewById(R.id.chooseImageText);
        radioButton1 = (RadioButton) findViewById(R.id.trainee);
        radioButton2 = (RadioButton) findViewById(R.id.trainer);

        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickPhoto(v);
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validData()) {
                    if (new ConnectionDetector(LoginActivity.this).isConnectingToInternet()) {
                        performRegistration();
                    } else {
                        displaySnackbar();
                    }

                }
            }
        });

    }

    private void displaySnackbar() {
        final Snackbar snackbar = Snackbar.make(coordinatorLayout, "No internet connectivity,please try again later.", Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("Retry", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (new ConnectionDetector(LoginActivity.this).isConnectingToInternet()) {
                    performRegistration();
                    snackbar.dismiss();
                } else {
                    displaySnackbar();
                }
            }
        });
        snackbar.show();
    }


    private void performRegistration() {
        if (radioButton1.isChecked()) {
            type = "Trainee";
        } else {
            type = "Trainer";
        }

        final String employeeId = empId.getText().toString().trim();
        final String employeeEmail = empEmail.getText().toString().trim();

        final GenerateGCMKey generateGCMKey = new GenerateGCMKey(LoginActivity.this, new GenerateGCMKey.ServiceResponse() {

            @Override
            public void onServiceResponse(final String serviceResponse) {
                if (serviceResponse != null) {
                    final String gcmKey = serviceResponse;

                    //Log.d("GCM Key", gcmKey);
                    RegisterUserAsync registerUserAsync = new RegisterUserAsync(LoginActivity.this, new RegisterUserAsync.OnService() {
                        @Override
                        public void onService(String string) {
                            //Log.d("Registration Response", "Hello " + string);
                            //Log.d("Is null", String.valueOf(string.isEmpty()));
                            if (string != null) {
                                try {
                                    JSONObject jsonObject = new JSONObject(string);
                                    JSONObject jsonObject1 = jsonObject.getJSONObject("data1");

                                    String success = jsonObject1.getString("success");
                                    if (success.equals("0")) {
                                        view2.setVisibility(View.GONE);
                                        view1.setVisibility(View.VISIBLE);

                                        Snackbar snackbar = Snackbar.make(coordinatorLayout, "Registration unsuccessful, unauthorized user.", Snackbar.LENGTH_LONG);
                                        snackbar.show();
                                        return;
                                    } else if (success.equals("1")) {

                                        JSONObject jsonObject2 = jsonObject.getJSONObject("data");
                                        String employeeId = jsonObject2.getString("emp_id");
                                        String employeeName = jsonObject2.getString("emp_name");
                                        String role = jsonObject2.getString("role_intcs");
                                        String emailId = jsonObject2.getString("emp_email");

                                        SharedPreferences sharedPreferences = getSharedPreferences("Talent Development", MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();

                                        editor.putString("emp_id", employeeId);
                                        //Log.d("Employee id", employeeId);
                                        editor.putString("emp_email", employeeEmail);
                                        //Log.d("Employee email", employeeEmail);
                                        editor.putString("emp_image", fname);
                                        editor.putString("emp_name", employeeName);
                                        //Log.d("Employee name", employeeName);
                                        editor.putString("emp_role", role);
                                        //Log.d("Employee role", role);
                                        editor.putString("type", type);
                                        editor.commit();

                                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                        startActivity(intent);
                                        finish();


                                    } else {
                                        view2.setVisibility(View.GONE);
                                        view1.setVisibility(View.VISIBLE);
                                        final Snackbar snackbar = Snackbar.make(coordinatorLayout, "No internet connectivity,please try again later.", Snackbar.LENGTH_INDEFINITE);
                                        Toast.makeText(getApplicationContext(), "No internet connection, please try again later", Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    view1.setVisibility(View.VISIBLE);
                                    view2.setVisibility(View.GONE);
                                    final Snackbar snackbar = Snackbar.make(coordinatorLayout, "No internet connectivity,please try again later.", Snackbar.LENGTH_INDEFINITE);
                                    Toast.makeText(getApplicationContext(), "No internet connection, please try again later", Toast.LENGTH_LONG).show();

                                }
                            } else {
                                //Log.d("My Tag", "Inside else");
                                view1.setVisibility(View.VISIBLE);
                                view2.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(), "No internet connection, please try again later", Toast.LENGTH_LONG).show();
                            }


                        }
                    });
                    registerUserAsync.execute(employeeId, employeeEmail, gcmKey, type);
                    messageText.setText("Performing registration...");
                } else {
                    //Log.d("My tag", "Null response");
                    view1.setVisibility(View.VISIBLE);
                    view2.setVisibility(View.GONE);
                    final Snackbar snackbar = Snackbar.make(coordinatorLayout, "No internet connectivity,please try again later.", Snackbar.LENGTH_INDEFINITE);
                    Toast.makeText(getApplicationContext(), "No internet connection, please try again later", Toast.LENGTH_LONG).show();

                }
            }
        });
        generateGCMKey.execute();
        view1.setVisibility(View.GONE);
        view2.setVisibility(View.VISIBLE);
        messageText.setText("Generating unique id...");

    }

    private boolean validData() {
        String employeeId = empId.getText().toString().trim();
        String employeeEmail = empEmail.getText().toString().trim();

        if (employeeId.length() == 0) {
            empId.setError("Please enter a valid employee id.");
            return false;
        } else if (!(employeeEmail.length() > 0 && validate(employeeEmail))) {
            empEmail.setError("Please enter a valid email address.");
            return false;
        }

        return true;
    }


    public void pickPhoto(View view) {
        //TODO: launch the photo picker
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,
                "Select Picture"), SELECT_PICTURE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            uri = data.getData();
            //userImage.setImageURI(uri);

            final Uri imageUri = data.getData();
            final InputStream imageStream;
            try {

                imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                userImage.setImageBitmap(selectedImage);
                chooseImageMessageText.setText("Change image...");
                SaveImageAsync saveImageAsync = new SaveImageAsync(LoginActivity.this, new SaveImageAsync.ServiceResponse() {
                    @Override
                    public void onServiceResponse(String serviceResponse) {
                        fname = saveToInternalStorage(selectedImage);
                    }
                });
                saveImageAsync.execute(selectedImage);


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }


        }
    }

    private String saveToInternalStorage(Bitmap bitmapImage) {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
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
