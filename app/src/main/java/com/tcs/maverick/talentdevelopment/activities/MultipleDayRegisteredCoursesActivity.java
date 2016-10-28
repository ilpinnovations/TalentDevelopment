package com.tcs.maverick.talentdevelopment.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.tcs.maverick.talentdevelopment.R;
import com.tcs.maverick.talentdevelopment.beans.MultipleDayRegisteredCoursesBean;
import com.tcs.maverick.talentdevelopment.utilities.AppConstants;
import com.tcs.maverick.talentdevelopment.utilities.DividerItemDecorator;
import com.tcs.maverick.talentdevelopment.utilities.HttpManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by abhi on 3/11/2016.
 */
public class MultipleDayRegisteredCoursesActivity extends AppCompatActivity {

    private View view1;
    private View view2;
    private RecyclerView recyclerView;
    private TextView messageText, errorText;
    private ArrayList<MultipleDayRegisteredCoursesBean> data;
    private String sessionId;
    private String registrationId;
    private String employeeId;
    private CoordinatorLayout coordinatorLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_layout);

        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Registered Courses");

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            sessionId = bundle.getString("session_id");
            registrationId = bundle.getString("registration_id");
        }

        view1 = findViewById(R.id.view1);
        view2 = findViewById(R.id.view2);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

        recyclerView = (RecyclerView) findViewById(R.id.list);
        messageText = (TextView) findViewById(R.id.messageText);
        errorText = (TextView) findViewById(R.id.errorText);
        recyclerView.setLayoutManager(new LinearLayoutManager(MultipleDayRegisteredCoursesActivity.this));
        recyclerView.addItemDecoration(new DividerItemDecorator(MultipleDayRegisteredCoursesActivity.this, LinearLayoutManager.VERTICAL));
        getData();
    }

    private void getData() {
        SharedPreferences sharedPreferences = getSharedPreferences("Talent Development", MODE_PRIVATE);
        employeeId = sharedPreferences.getString("emp_id", "");
        String url = AppConstants.URL + "registered_sessions.php?emp_id=" + employeeId + "&session_id=" + sessionId + "&registration_id=" + registrationId;
        //Log.d("Url", url);
        HttpManager httpManager = new HttpManager(MultipleDayRegisteredCoursesActivity.this, new HttpManager.ServiceResponse() {
            @Override
            public void onServiceResponse(String serviceResponse) {
                if (serviceResponse != null && HttpManager.getStatusCode() == 200) {
                    //Log.d("Multiple day courses", serviceResponse);
                    try {
                        data = new ArrayList<>();
                        JSONObject jsonObject = new JSONObject(serviceResponse);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);
                            JSONObject data1 = jsonObject1.getJSONObject("data1");
                            JSONObject data2 = jsonObject1.getJSONObject("data2");
                            JSONObject data3 = jsonObject1.getJSONObject("data3");

                            String sessionId = data1.getString("session_id");
                            String sessionName = data1.getString("session_name");
                            String time = data1.getString("start_time") + " - " + data1.getString("end_time");
                            String strDate = data2.getString("start_date");

                            Date date = new SimpleDateFormat("yyyy-mm-dd").parse(strDate);
                            String startDate = new SimpleDateFormat("dd/mm/yyyy").format(date);

                            String isAttend = data3.getString("is_attend");
                            String isFeedback = "0";

                            if (isAttend.equals("0")) {
                                isFeedback = "0";
                            } else if (isAttend.equals("1")) {
                                isFeedback = data3.getString("is_feedback");
                            }

                            MultipleDayRegisteredCoursesBean multipleDayRegisteredCoursesBean = new MultipleDayRegisteredCoursesBean(sessionId, sessionName, time, startDate, isAttend, isFeedback);
                            data.add(multipleDayRegisteredCoursesBean);
                        }
                        if (data.size() > 0) {
                            view1.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            recyclerView.setAdapter(new CustomAdapter(data));
                        } else {
                            view2.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                            errorText.setText("No registered courses found.");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        view2.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                        errorText.setText("No internet connection, please try again later.");
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    view2.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    errorText.setText("No internet connection, please try again later.");
                }
            }
        });
        httpManager.execute(url);
        recyclerView.setVisibility(View.GONE);
        view1.setVisibility(View.VISIBLE);
        messageText.setText("Loading courses...");
    }


    private class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
        ArrayList<MultipleDayRegisteredCoursesBean> data;

        public CustomAdapter(ArrayList<MultipleDayRegisteredCoursesBean> data) {
            this.data = data;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View v;
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.multiple_day_registered_courses_row, viewGroup, false);
            ViewHolder viewHolder = new ViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, final int i) {
            String status = "";
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            final String currentDate = df.format(c.getTime());

            viewHolder.courseTitle.setText(data.get(i).getSessionName());
            viewHolder.date.setText("Date : " + data.get(i).getDate());
            viewHolder.time.setText("Time : " + data.get(i).getTime());

            if (data.get(i).getIsAttend().equals("0")) {
                status = "Registered";
                viewHolder.messageTextView.setText("Tap to mark attendance.");
            } else if (data.get(i).getIsAttend().equals("1") && data.get(i).getIsFeedback().equals("0")) {
                status = "Attended";
                viewHolder.messageTextView.setText("Tap to give feedback.");
            } else if (data.get(i).getIsAttend().equals("1") && data.get(i).getIsFeedback().equals("1")) {
                status = "Feedback submitted";
                viewHolder.messageTextView.setVisibility(View.GONE);
            }

            viewHolder.status.setText("Status : " + status);

            final String finalStatus = status;
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                                                       @Override
                                                       public void onClick(View v) {

                                                           if (data.get(i).getIsAttend().equals("0")) {
                                                               if (data.get(i).getDate().compareTo(currentDate) == 0) {
                                                                   AlertDialog.Builder alertDialog = new AlertDialog.Builder(MultipleDayRegisteredCoursesActivity.this);
                                                                   alertDialog.setTitle("Mark Attendance...");
                                                                   alertDialog.setMessage("Are you sure you want to mark attendance for this session?");
                                                                   alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                                                       public void onClick(DialogInterface dialog, int which) {
                                                                           String url = AppConstants.URL + "mark_attendence.php?emp_id=" + employeeId + "&session_id=" + sessionId + "&date=" + data.get(i).getDate() + "&registration_id=" + registrationId;
                                                                           //Log.d("Url mark attendance", url);
                                                                           HttpManager httpManager = new HttpManager(MultipleDayRegisteredCoursesActivity.this, new HttpManager.ServiceResponse() {
                                                                               @Override
                                                                               public void onServiceResponse(String serviceResponse) {
                                                                                   if (serviceResponse != null && HttpManager.getStatusCode() == 200) {
                                                                                       //Log.d("Attendance ", serviceResponse);
                                                                                       if (serviceResponse.equalsIgnoreCase("true")) {
                                                                                           Snackbar snackbar = Snackbar.make(coordinatorLayout, "Attendance marked successfully.", Snackbar.LENGTH_LONG);
                                                                                           snackbar.show();
                                                                                           getData();
                                                                                       } else if (serviceResponse.equalsIgnoreCase("false")) {
                                                                                           recyclerView.setVisibility(View.VISIBLE);
                                                                                           view1.setVisibility(View.GONE);
                                                                                           Snackbar snackbar = Snackbar.make(coordinatorLayout, "Attendance cannot be marked, please contact admin.", Snackbar.LENGTH_LONG);
                                                                                           snackbar.show();
                                                                                       } else {
                                                                                           recyclerView.setVisibility(View.VISIBLE);
                                                                                           view1.setVisibility(View.GONE);
                                                                                           Snackbar snackbar = Snackbar.make(coordinatorLayout, "No internet connectivity,please check your internet", Snackbar.LENGTH_LONG);
                                                                                           snackbar.show();
                                                                                       }
                                                                                   } else {
                                                                                       recyclerView.setVisibility(View.VISIBLE);
                                                                                       view1.setVisibility(View.GONE);
                                                                                       Snackbar snackbar = Snackbar.make(coordinatorLayout, "No internet connectivity,please check your internet", Snackbar.LENGTH_LONG);
                                                                                       snackbar.show();
                                                                                   }
                                                                               }
                                                                           });
                                                                           httpManager.execute(url);
                                                                           recyclerView.setVisibility(View.GONE);
                                                                           view1.setVisibility(View.VISIBLE);
                                                                           messageText.setText("Marking attendance...");
                                                                       }
                                                                   });

                                                                   alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                                                       public void onClick(DialogInterface dialog, int which) {
                                                                           dialog.cancel();
                                                                           return;
                                                                       }
                                                                   });

                                                                   alertDialog.show();
                                                               } else {
                                                                   Toast.makeText(MultipleDayRegisteredCoursesActivity.this, "Cannot mark attendance for this course today.", Toast.LENGTH_LONG).show();
                                                                   return;
                                                               }
                                                           } else if (finalStatus.equalsIgnoreCase("Attended")) {
                                                               Log.d("My Tag", "Feedback form");
                                                               Intent intent = new Intent(MultipleDayRegisteredCoursesActivity.this, FeedbackActivity1.class);
                                                               intent.putExtra("session_id", sessionId);
                                                               intent.putExtra("employee_id", employeeId);
                                                               intent.putExtra("date", data.get(i).getDate());
                                                               intent.putExtra("registration_id", registrationId);
                                                               startActivity(intent);
                                                           } else if (finalStatus.equalsIgnoreCase("Feedback submitted")) {
                                                               Snackbar snackbar = Snackbar.make(coordinatorLayout, "Feedback already submitted for this session", Snackbar.LENGTH_LONG);
                                                               snackbar.show();

                                                           }
                                                       }

                                                   }

            );


        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public TextView courseTitle, date, time, status, messageTextView;

            public ViewHolder(View itemView) {
                super(itemView);
                courseTitle = (TextView) itemView.findViewById(R.id.sessionTitle);
                date = (TextView) itemView.findViewById(R.id.date);
                time = (TextView) itemView.findViewById(R.id.time);
                status = (TextView) itemView.findViewById(R.id.status);
                messageTextView = (TextView) itemView.findViewById(R.id.messageText);
            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getData();
    }
}
