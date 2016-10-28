package com.tcs.maverick.talentdevelopment.activities;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.tcs.maverick.talentdevelopment.R;
import com.tcs.maverick.talentdevelopment.beans.CoursesBean;
import com.tcs.maverick.talentdevelopment.utilities.AppConstants;
import com.tcs.maverick.talentdevelopment.utilities.HttpManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


/**
 * Created by abhi on 3/13/2016.
 */
public class CourseDetailsActivity extends AppCompatActivity {

    //Widgets declarations
    private TextView courseTitle;
    private TextView courseDetails;
    private TextView expectedOutcome;
    private TextView modeOfDelivery;
    private Button registerButton, shareButton;
    private CoordinatorLayout coordinatorLayout;
    private View view1, view2;
    private TextView messageText;

    //Data
    private CoursesBean coursesBean;
    private long SPLASH_TIME_OUT = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_detail_activity);

        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Course Details");

        Intent intent = getIntent();
        if (intent != null) {
            coursesBean = (CoursesBean) intent.getSerializableExtra("CoursesBean");
        }

        setupLayout();

        courseTitle.setText(coursesBean.getCourseName());
        courseDetails.setText(coursesBean.getCourseDescription());
        expectedOutcome.setText(coursesBean.getExpectedOutcome());
        modeOfDelivery.setText(coursesBean.getModeOfDelivery());

        registerButton.setOnClickListener(new RegisterForCourse());

    }

    private void setupLayout() {
        courseTitle = (TextView) findViewById(R.id.courseTitle);
        courseDetails = (TextView) findViewById(R.id.courseDetails);
        expectedOutcome = (TextView) findViewById(R.id.expectedOutcome);
        modeOfDelivery = (TextView) findViewById(R.id.modeOfDelivery);
        registerButton = (Button) findViewById(R.id.registerButton);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        shareButton = (Button) findViewById(R.id.shareButton);
        shareButton.setOnClickListener(new ShareCourse());
        view1 = findViewById(R.id.view1);
        view2 = findViewById(R.id.view2);
        messageText = (TextView) findViewById(R.id.messageText);
    }

    private class RegisterForCourse implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (coursesBean.getIsRegistered().equalsIgnoreCase("Not registered")) {
                SharedPreferences sharedPreferences = getSharedPreferences("Talent Development", MODE_PRIVATE);
                String employeeId = sharedPreferences.getString("emp_id", "");
                String url = AppConstants.URL + "session_register.php?emp_id=" + employeeId + "&session_id=" + coursesBean.getSessionId();
                //Log.d("URL", url);
                HttpManager httpManager = new HttpManager(CourseDetailsActivity.this, new HttpManager.ServiceResponse() {
                    @Override
                    public void onServiceResponse(String serviceResponse) {
                        if (serviceResponse != null && HttpManager.getStatusCode() == 200) {
                            //Log.d("Registration:", serviceResponse);
                            if (serviceResponse.equalsIgnoreCase("success")) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(CourseDetailsActivity.this);
                                builder.setTitle("Register");
                                builder.setMessage("Your request for registration is successfully submitted, would you like to create reminder for this course");
                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        try {
                                            ContentResolver cr = getContentResolver();
                                            ContentValues values = new ContentValues();

                                            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                                            SimpleDateFormat df1 = new SimpleDateFormat("dd/MM/yyyy");
                                            Date date = null;
                                            Date date1 = null;

                                            date = df.parse(coursesBean.getStartDate() + " " + coursesBean.getTime().split("-")[0]);
                                            //Log.d("Date ", date + "");
                                            date1 = df.parse(coursesBean.getEndDate() + " " + coursesBean.getTime().split("-")[1]);

                                            Calendar cal = Calendar.getInstance();
                                            cal.setTime(date);
                                            Calendar cal1 = Calendar.getInstance();
                                            cal1.setTime(date1);
                                            long start = cal.getTimeInMillis();
                                            long end = cal1.getTimeInMillis();

                                            values.put(CalendarContract.Events.CALENDAR_ID, Long.toString(getCalenderId()));
                                            values.put(CalendarContract.Events.DTSTART, start);
                                            values.put(CalendarContract.Events.DTEND, end);
                                            values.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());
                                            values.put(CalendarContract.Events.TITLE, "Talent Development");
                                            values.put(CalendarContract.Events.DESCRIPTION, coursesBean.getCourseName());
                                            Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
                                            long eventId = Long.parseLong(uri.getLastPathSegment());

                                            setReminder(cr, eventId, 5);
                                            setReminder(cr, eventId, 60);

                                            finish();
                                            Toast.makeText(CourseDetailsActivity.this, "Course successfully added to your calendar", Toast.LENGTH_SHORT).show();
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        } catch (Exception ex) {
                                            Toast.makeText(CourseDetailsActivity.this, "Permission denied, please enable Calendar Permission for Talent Development", Toast.LENGTH_LONG).show();
                                           /* Intent intent = new Intent();
                                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                            Uri uri = Uri.fromParts("package", getPackageName(), null);
                                            intent.setData(uri);
                                            startActivity(intent);*/
                                            finish();
                                        }
                                    }
                                });
                                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                                });
                                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialog) {
                                        finish();
                                    }
                                });

                                builder.show();


                            } else if (serviceResponse.equalsIgnoreCase("fail")) {
                                view2.setVisibility(View.GONE);
                                view1.setVisibility(View.VISIBLE);
                                Snackbar snackbar = Snackbar.make(coordinatorLayout, "Operation failed, please contact admin.", Snackbar.LENGTH_LONG);
                                snackbar.show();

                            } else {
                                view1.setVisibility(View.VISIBLE);
                                view2.setVisibility(View.GONE);
                                Snackbar snackbar = Snackbar.make(coordinatorLayout, "No internet connectivity, please check your internet.", Snackbar.LENGTH_LONG);
                                snackbar.show();
                            }


                        } else {
                            view2.setVisibility(View.GONE);
                            view1.setVisibility(View.VISIBLE);
                            Snackbar snackbar = Snackbar.make(coordinatorLayout, "No internet connectivity, please check your internet.", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }

                    }
                });

                httpManager.execute(url);
                view1.setVisibility(View.GONE);
                view2.setVisibility(View.VISIBLE);
                messageText.setText("Requesting for registration");

            } else {
                Snackbar snackbar = Snackbar.make(coordinatorLayout, "You have already requested for registration.", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        }


        private long getCalenderId() {
            long id = 0;
            String[] projection = new String[]{
                    CalendarContract.Calendars._ID,                    // 0
                    CalendarContract.Calendars.NAME,                   // 1
                    CalendarContract.Calendars.ACCOUNT_NAME,           // 2
                    CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,  // 3
                    CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL,  // 4
            };

            Cursor calCursor = getContentResolver().query(
                    CalendarContract.Calendars.CONTENT_URI,
                    projection,
                    (CalendarContract.Calendars.VISIBLE + " = 1 and " +
                            CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL + " >= " + CalendarContract.Calendars.CAL_ACCESS_CONTRIBUTOR),
                    null,
                    CalendarContract.Calendars._ID + " ASC");

            while (calCursor.moveToNext()) {
                id = calCursor.getLong(0);
                String name = calCursor.getString(3); // display name
                if (name == null)
                    name = calCursor.getString(2); // account name
                if (name == null)
                    name = "unknown";


            }
            return id;
        }
    }

    private class ShareCourse implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            final String text = "Have a look on this course of Talent Development Program, Course name :" + coursesBean.getCourseName() + ", Date :" + coursesBean.getStartDate();
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, text);
            startActivity(Intent.createChooser(intent, "Choose an app to share Talent Development course. "));
        }
    }

    public void setReminder(ContentResolver cr, long eventID, int timeBefore) {
        try {
            ContentValues values = new ContentValues();
            values.put(CalendarContract.Reminders.MINUTES, timeBefore);
            values.put(CalendarContract.Reminders.EVENT_ID, eventID);
            values.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
            Uri uri = cr.insert(CalendarContract.Reminders.CONTENT_URI, values);
            Cursor c = CalendarContract.Reminders.query(cr, eventID,
                    new String[]{CalendarContract.Reminders.MINUTES});
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
