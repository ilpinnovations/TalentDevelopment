package com.tcs.maverick.talentdevelopment.activities;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tcs.maverick.talentdevelopment.R;
import com.tcs.maverick.talentdevelopment.utilities.SubmitFeedbackAsync;

/**
 * Created by abhi on 3/12/2016.
 */
public class FeedbackActivity2 extends AppCompatActivity {

    //form 2 widgets
    private RatingBar ratingBar1;
    private RatingBar ratingBar2;
    private RatingBar ratingBar3;
    private RatingBar ratingBar4;
    private RatingBar ratingBar5;
    private RatingBar ratingBar6;
    private RatingBar ratingBar7;
    private RatingBar ratingBar8;
    private RatingBar ratingBar9;
    private RatingBar ratingBar10;

    private EditText editText1;
    private EditText editText2;

    private Button mSubmit;
    private CoordinatorLayout coordinatorLayout;

    private View view1, view2;
    private TextView messageText;
    // form 2 values
    private float f2_rat1;
    private float f2_rat2;
    private float f2_rat3;
    private float f2_rat4;
    private float f2_rat5;
    private float f2_rat6;
    private float f2_rat7;
    private float f2_rat8;
    private float f2_rat9;
    private float f2_rat10;

    private String f2_com1;
    private String f2_com2;


    //form 1 from bundle
    private String f1_rat1;
    private String f1_rat2;
    private String f1_rat3;
    private String f1_rat4;
    private String f1_rat5;
    private String f1_rat6;
    private String f1_rat7;
    private String f1_rat8;
    private String f1_rat9;
    private String f1_rat10;

    private String f1_com1;
    private String f1_com2;
    private String f1_com3;
    private String f1_com4;
    private String f1_com5;

    private String sessionId;
    private String employeeId;
    private String date;
    private String registrationId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback_activity2);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Feedback");

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            f1_rat1 = bundle.getString("f1_rat1");
            //Log.d("f1_rat1", f1_rat1);
            f1_rat2 = bundle.getString("f1_rat2");
            //Log.d("f1_rat2", f1_rat2);
            f1_rat3 = bundle.getString("f1_rat3");
            //Log.d("f1_rat3", f1_rat3);
            f1_rat4 = bundle.getString("f1_rat4");
            //Log.d("f1_rat4", f1_rat4);
            f1_rat5 = bundle.getString("f1_rat5");
            //Log.d("f1_rat5", f1_rat5);
            f1_rat6 = bundle.getString("f1_rat6");
            //Log.d("f1_rat6", f1_rat6);
            f1_rat7 = bundle.getString("f1_rat7");
            //Log.d("f1_rat7", f1_rat7);
            f1_rat8 = bundle.getString("f1_rat8");
            //Log.d("f1_rat8", f1_rat8);
            f1_rat9 = bundle.getString("f1_rat9");
            //Log.d("f1_rat9", f1_rat9);
            f1_rat10 = bundle.getString("f1_rat10");
            //Log.d("f1_rat10", f1_rat10);

            f1_com1 = bundle.getString("f1_com1");
            //Log.d("f1_com1", f1_com1);
            f1_com2 = bundle.getString("f1_com2");
            //Log.d("f1_com2", f1_com2);
            f1_com3 = bundle.getString("f1_com3");
            //Log.d("f1_com3", f1_com3);
            f1_com4 = bundle.getString("f1_com4");
            //Log.d("f1_com4", f1_com4);
            f1_com5 = bundle.getString("f1_com5");
            //Log.d("f1_com5", f1_com5);
            sessionId = bundle.getString("session_id");
            //Log.d("Session id", sessionId);
            employeeId = bundle.getString("employee_id");
            //Log.d("Employee id", employeeId);
            date = bundle.getString("date");
            //Log.d("Date", date);
            registrationId = bundle.getString("registration_id");
            //Log.d("Registration id", registrationId);
        }


        setupForm();
        mSubmit.setOnClickListener(new SubmitFeedback());
    }

    private void setupForm() {

        ratingBar1 = (RatingBar) findViewById(R.id.f2_rat1);
        ratingBar2 = (RatingBar) findViewById(R.id.f2_rat2);
        ratingBar3 = (RatingBar) findViewById(R.id.f2_rat3);
        ratingBar4 = (RatingBar) findViewById(R.id.f2_rat4);
        ratingBar5 = (RatingBar) findViewById(R.id.f2_rat5);

        ratingBar6 = (RatingBar) findViewById(R.id.f2_rat6);
        ratingBar7 = (RatingBar) findViewById(R.id.f2_rat7);
        ratingBar8 = (RatingBar) findViewById(R.id.f2_rat8);
        ratingBar9 = (RatingBar) findViewById(R.id.f2_rat9);
        ratingBar10 = (RatingBar) findViewById(R.id.f2_rat10);

        editText1 = (EditText) findViewById(R.id.f2_com1);
        editText2 = (EditText) findViewById(R.id.f2_com2);

        mSubmit = (Button) findViewById(R.id.submitButton);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

        view1 = findViewById(R.id.view1);
        view2 = findViewById(R.id.view2);
        messageText = (TextView) findViewById(R.id.messageText);

    }

    private class SubmitFeedback implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            f2_rat1 = ratingBar1.getRating();
            //Log.d("f2_rat1", f2_rat1 + "");
            f2_rat2 = ratingBar2.getRating();
            //Log.d("f2_rat2", f2_rat2 + "");
            f2_rat3 = ratingBar3.getRating();
            //Log.d("f2_rat3", f2_rat3 + "");
            f2_rat4 = ratingBar4.getRating();
            //Log.d("f2_rat4", f2_rat4 + "");
            f2_rat5 = ratingBar5.getRating();
            //Log.d("f2_rat5", f2_rat5 + "");
            f2_rat6 = ratingBar6.getRating();
            //Log.d("f2_rat6", f2_rat6 + "");
            f2_rat7 = ratingBar7.getRating();
            //Log.d("f2_rat7", f2_rat7 + "");
            f2_rat8 = ratingBar8.getRating();
            //Log.d("f2_rat8", f2_rat8 + "");
            f2_rat9 = ratingBar9.getRating();
            //Log.d("f2_rat9", f2_rat9 + "");
            f2_rat10 = ratingBar10.getRating();
            //Log.d("f2_rat10", f2_rat10 + "");

            f2_com1 = editText1.getText().toString();
            //Log.d("f2_com1", f2_com1 + "");
            f2_com2 = editText2.getText().toString();
            //Log.d("f2_com2", f2_com2 + "");

            if (validate()) {
                SubmitFeedbackAsync submitFeedbackAsync = new SubmitFeedbackAsync(FeedbackActivity2.this, new SubmitFeedbackAsync.OnService() {
                    @Override
                    public void onService(String string) {
                        if (string != null) {
                            //Log.d("Feedback_response", string);
                            if (string.equalsIgnoreCase("true")) {
                                finish();
                                Toast.makeText(FeedbackActivity2.this, "Feedback submitted successfully", Toast.LENGTH_LONG).show();
                            } else if (string.equalsIgnoreCase("false")) {
                                view1.setVisibility(View.VISIBLE);
                                view2.setVisibility(View.GONE);
                                Snackbar snackbar = Snackbar.make(coordinatorLayout, "Feedback could not be submitted.", Snackbar.LENGTH_LONG);
                                snackbar.show();
                            } else {
                                view1.setVisibility(View.VISIBLE);
                                view2.setVisibility(View.GONE);
                                Snackbar snackbar = Snackbar.make(coordinatorLayout, "No internet connectivity,please check your internet.", Snackbar.LENGTH_LONG);
                                snackbar.show();
                            }
                        } else {
                            view1.setVisibility(View.VISIBLE);
                            view2.setVisibility(View.GONE);
                            Snackbar snackbar = Snackbar.make(coordinatorLayout, "No internet connectivity,please check your internet.", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
                    }
                });
                submitFeedbackAsync.execute(sessionId, employeeId, date, registrationId,
                        f2_rat1 + "", f2_rat2 + "", f2_rat3 + "", f2_rat4 + "", f2_rat5 + "",
                        f2_rat6 + "", f2_rat7 + "", f2_rat8 + "", f2_rat9 + "", f2_rat10 + "",
                        f2_com1, f2_com2,
                        f1_rat1, f1_rat2, f1_rat3, f1_rat4, f1_rat5,
                        f1_rat6, f1_rat7, f1_rat8, f1_rat9, f1_rat10,
                        f1_com1, f1_com2, f1_com3, f1_com4, f1_com5);
                view1.setVisibility(View.GONE);
                view2.setVisibility(View.VISIBLE);
                messageText.setText("Submitting feedback...");

            } else {
                Snackbar snackbar = Snackbar.make(coordinatorLayout, "Ratings for all fields are compulsory.", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        }

        private boolean validate() {
            if (f2_rat1 == 0 || f2_rat2 == 0 || f2_rat3 == 0 || f2_rat4 == 0 || f2_rat5 == 0 || f2_rat6 == 0 || f2_rat7 == 0 || f2_rat8 == 0 || f2_rat9 == 0 || f2_rat10 == 0) {
                return false;
            }
            return true;
        }
    }
}
