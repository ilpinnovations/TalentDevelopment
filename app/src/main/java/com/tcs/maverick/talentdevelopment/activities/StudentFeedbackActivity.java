package com.tcs.maverick.talentdevelopment.activities;

import android.content.SharedPreferences;
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
import com.tcs.maverick.talentdevelopment.utilities.SubmitStudentFeedbackAsync;

/**
 * Created by abhi on 3/27/2016.
 */
public class StudentFeedbackActivity extends AppCompatActivity {

    private RatingBar ratingBar1;
    private EditText editText1;
    private String employeeId;
    private String sessionId;
    private Button mSubmit;
    private CoordinatorLayout coordinatorLayout;
    private View view1, view2;
    private TextView messageText;
    private float f2_rat1;
    private String comments, trainerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_feedback);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Associate Feedback");

        SharedPreferences sharedPreferences = getSharedPreferences("Talent Development", MODE_PRIVATE);
        trainerId = sharedPreferences.getString("emp_id", "");

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            employeeId = bundle.getString("employeeId");
            sessionId = bundle.getString("sessionId");
        }
        setupForm();
        mSubmit.setOnClickListener(new SubmitFeedback());
    }

    private void setupForm() {

        ratingBar1 = (RatingBar) findViewById(R.id.f1_rat1);
        editText1 = (EditText) findViewById(R.id.f1_com);
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
            comments = editText1.getText().toString();
            //Log.d("f2_rat1", f2_rat1 + "");
            if (validate()) {
                SubmitStudentFeedbackAsync submitFeedbackAsync = new SubmitStudentFeedbackAsync(StudentFeedbackActivity.this, new SubmitStudentFeedbackAsync.OnService() {
                    @Override
                    public void onService(String string) {
                        //Log.d("Feedback_response", "Hello" + string);
                        if (string != null) {
                            //Log.d("Feedback_response", string);
                            if (string.equalsIgnoreCase("Feedback submitted successfully")) {
                                finish();
                                Toast.makeText(StudentFeedbackActivity.this, "Feedback submitted successfully", Toast.LENGTH_LONG).show();
                            } else if (string.equalsIgnoreCase("false")) {
                                view1.setVisibility(View.VISIBLE);
                                view2.setVisibility(View.GONE);
                                Snackbar snackbar = Snackbar.make(coordinatorLayout, "Feedback could not be submitted.", Snackbar.LENGTH_LONG);
                                snackbar.show();
                            } else if (string.equalsIgnoreCase("Feedback already submitted")) {
                                view1.setVisibility(View.VISIBLE);
                                view2.setVisibility(View.GONE);
                                Snackbar snackbar = Snackbar.make(coordinatorLayout, "Feedback already submitted for the associate.", Snackbar.LENGTH_LONG);
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

                submitFeedbackAsync.execute(trainerId, employeeId, f2_rat1 + "", comments, sessionId);
                view1.setVisibility(View.GONE);
                view2.setVisibility(View.VISIBLE);
                messageText.setText("Submitting feedback...");

            } else {
                Snackbar snackbar = Snackbar.make(coordinatorLayout, "Ratings is compulsory for submitting feedback.", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        }

        private boolean validate() {
            if (f2_rat1 == 0) {
                return false;
            }
            return true;
        }
    }
}
