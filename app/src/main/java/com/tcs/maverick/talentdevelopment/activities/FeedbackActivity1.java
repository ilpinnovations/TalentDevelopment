package com.tcs.maverick.talentdevelopment.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRatingBar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.tcs.maverick.talentdevelopment.R;

/**
 * Created by abhi on 3/12/2016.
 */
public class FeedbackActivity1 extends AppCompatActivity {

    private AppCompatRatingBar ratingBar1, ratingBar2, ratingBar3, ratingBar4, ratingBar5, ratingBar6, ratingBar7, ratingBar8, ratingBar9, ratingBar10;
    private EditText editText1, editText2, editText3, editText4, editText5;
    private Button nextButton;
    private CoordinatorLayout coordinatorLayout;

    private float f1_rat1;
    private float f1_rat2;
    private float f1_rat3;
    private float f1_rat4;
    private float f1_rat5;
    private float f1_rat6;
    private float f1_rat7;
    private float f1_rat8;
    private float f1_rat9;
    private float f1_rat10;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback_activity1);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Feedback");

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            sessionId = bundle.getString("session_id");
            employeeId = bundle.getString("employee_id");
            date = bundle.getString("date");
            registrationId = bundle.getString("registration_id");
        }
        setupForm();

        nextButton.setOnClickListener(new SubmitFeedback());

    }

    private void setupForm() {

        ratingBar1 = (AppCompatRatingBar) findViewById(R.id.f1_rat1);
        ratingBar2 = (AppCompatRatingBar) findViewById(R.id.f1_rat2);
        ratingBar3 = (AppCompatRatingBar) findViewById(R.id.f1_rat3);
        ratingBar4 = (AppCompatRatingBar) findViewById(R.id.f1_rat4);
        ratingBar5 = (AppCompatRatingBar) findViewById(R.id.f1_rat5);
        ratingBar6 = (AppCompatRatingBar) findViewById(R.id.f1_rat6);
        ratingBar7 = (AppCompatRatingBar) findViewById(R.id.f1_rat7);
        ratingBar8 = (AppCompatRatingBar) findViewById(R.id.f1_rat8);
        ratingBar9 = (AppCompatRatingBar) findViewById(R.id.f1_rat9);
        ratingBar10 = (AppCompatRatingBar) findViewById(R.id.f1_rat10);

        editText1 = (EditText) findViewById(R.id.f1_com1);
        editText2 = (EditText) findViewById(R.id.f1_com2);
        editText3 = (EditText) findViewById(R.id.f1_com3);
        editText4 = (EditText) findViewById(R.id.f1_com4);
        editText5 = (EditText) findViewById(R.id.f1_com5);

        nextButton = (Button) findViewById(R.id.nextButton);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
    }

    private class SubmitFeedback implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            f1_rat1 = ratingBar1.getRating();
            f1_rat2 = ratingBar2.getRating();
            f1_rat3 = ratingBar3.getRating();
            f1_rat4 = ratingBar4.getRating();
            f1_rat5 = ratingBar5.getRating();
            f1_rat6 = ratingBar6.getRating();
            f1_rat7 = ratingBar7.getRating();
            f1_rat8 = ratingBar8.getRating();
            f1_rat9 = ratingBar9.getRating();
            f1_rat10 = ratingBar10.getRating();

            f1_com1 = editText1.getText().toString();
            f1_com2 = editText2.getText().toString();
            f1_com3 = editText3.getText().toString();
            f1_com4 = editText4.getText().toString();
            f1_com5 = editText5.getText().toString();

            if (validateData()) {
                Intent intent = new Intent(FeedbackActivity1.this, FeedbackActivity2.class);
                intent.putExtra("f1_rat1", f1_rat1 + "");
                intent.putExtra("f1_rat2", f1_rat2 + "");
                intent.putExtra("f1_rat3", f1_rat3 + "");
                intent.putExtra("f1_rat4", f1_rat4 + "");
                intent.putExtra("f1_rat5", f1_rat5 + "");

                intent.putExtra("f1_rat6", f1_rat6 + "");
                intent.putExtra("f1_rat7", f1_rat7 + "");
                intent.putExtra("f1_rat8", f1_rat8 + "");
                intent.putExtra("f1_rat9", f1_rat9 + "");
                intent.putExtra("f1_rat10", f1_rat10 + "");

                intent.putExtra("f1_com1", f1_com1 + "");
                intent.putExtra("f1_com2", f1_com2 + "");
                intent.putExtra("f1_com3", f1_com3 + "");
                intent.putExtra("f1_com4", f1_com4 + "");
                intent.putExtra("f1_com5", f1_com5 + "");

                intent.putExtra("session_id", sessionId);
                intent.putExtra("employee_id", employeeId);
                intent.putExtra("date", date);
                intent.putExtra("registration_id", registrationId);

                startActivity(intent);
                finish();

            } else {
                Snackbar snackbar = Snackbar.make(coordinatorLayout, "Ratings for all fields are compulsory.", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        }

        private boolean validateData() {
            if (f1_rat1 == 0 || f1_rat2 == 0 || f1_rat3 == 0 || f1_rat4 == 0 || f1_rat5 == 0 || f1_rat6 == 0 || f1_rat7 == 0 || f1_rat8 == 0 || f1_rat9 == 0 || f1_rat10 == 0) {
                return false;
            }
            return true;
        }
    }
}
