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
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.tcs.maverick.talentdevelopment.R;
import com.tcs.maverick.talentdevelopment.beans.RegisteredCoursesBean;
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
public class RegisteredCoursesActivity extends AppCompatActivity {

    private View view1;
    private View view2;
    private RecyclerView recyclerView;
    private TextView messageText, errorText;
    private ArrayList<RegisteredCoursesBean> data = new ArrayList<>(), searchData = new ArrayList<>();
    private String employeeId;
    private CoordinatorLayout coordinatorLayout;
    private Menu menu;
    private CustomAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_layout);

        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Registered Courses");

        view1 = findViewById(R.id.view1);
        view2 = findViewById(R.id.view2);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        customAdapter = new CustomAdapter(searchData);
        recyclerView = (RecyclerView) findViewById(R.id.list);
        messageText = (TextView) findViewById(R.id.messageText);
        errorText = (TextView) findViewById(R.id.errorText);
        recyclerView.setLayoutManager(new LinearLayoutManager(RegisteredCoursesActivity.this));
        recyclerView.addItemDecoration(new DividerItemDecorator(RegisteredCoursesActivity.this, LinearLayoutManager.VERTICAL));
        getData();
    }

    private void getData() {
        SharedPreferences sharedPreferences = getSharedPreferences("Talent Development", MODE_PRIVATE);
        employeeId = sharedPreferences.getString("emp_id", "");

        String url = AppConstants.URL + "get_registered_sessions.php?emp_id=" + employeeId;
        Log.d("Url", url);
        HttpManager httpManager = new HttpManager(RegisteredCoursesActivity.this, new HttpManager.ServiceResponse() {
            @Override
            public void onServiceResponse(String serviceResponse) {
                view1.setVisibility(View.GONE);
                if (serviceResponse != null && HttpManager.getStatusCode() == 200) {
                    Log.d("Registered courses", serviceResponse);
                    data = new ArrayList<>();
                    searchData = new ArrayList<>();
                    try {
                        JSONObject jsonObj = new JSONObject(serviceResponse);
                        JSONArray jsonArray = jsonObj.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                            String courseTitle = jsonObject.getString("session_name");
                            String strDate = jsonObject.getString("start_date");
                            String enDate = jsonObject.getString("end_date");

                            String startTime = jsonObject.getString("start_time");
                            String endTime = jsonObject.getString("end_time");

                            String isAttendance = jsonObject.getString("is_attended");
                            String isFeedback = jsonObject.getString("is_feedback");
                            String isConfirmed = jsonObject.getString("is_confirmed");

                            String regId = jsonObject.getString("registeration_id");
                            String status = jsonObject.getString("status");
                            String numOfdays = jsonObject.getString("noofdays");
                            String isMultiple = Integer.parseInt(numOfdays) > 1 ? "True" : "False";

                            Date date = new SimpleDateFormat("yyyy-mm-dd").parse(strDate);
                            String startDate = new SimpleDateFormat("dd/mm/yyyy").format(date);

                            Date date1 = new SimpleDateFormat("yyyy-mm-dd").parse(enDate);
                            String endDate = new SimpleDateFormat("dd/mm/yyyy").format(date1);
                            String time = startTime + " - " + endTime;

                            String courseId = jsonObject.getString("session_id");


                            RegisteredCoursesBean registeredCoursesBean = new RegisteredCoursesBean(courseId, courseTitle, startDate, endDate, time, isFeedback, isAttendance, isMultiple, status, regId);
                            data.add(registeredCoursesBean);
                            searchData.add(registeredCoursesBean);
                        }
                        if (data.size() > 0) {
                            customAdapter = new CustomAdapter(searchData);
                            recyclerView.setVisibility(View.VISIBLE);
                            recyclerView.setAdapter(customAdapter);
                        } else {
                            recyclerView.setVisibility(View.GONE);
                            view2.setVisibility(View.VISIBLE);
                            errorText.setText("No registered courses found.");
                        }
                    } catch (JSONException e) {
                        view2.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                        errorText.setText("No registered courses found.");
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
        view2.setVisibility(View.GONE);
        messageText.setText("Loading courses...");
    }

    private class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
        ArrayList<RegisteredCoursesBean> data;

        public CustomAdapter(ArrayList<RegisteredCoursesBean> data) {
            this.data = data;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View v;
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.registered_courses_row, viewGroup, false);
            ViewHolder viewHolder = new ViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, final int i) {
            String status = "";
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            final String currentDate = df.format(c.getTime());

            viewHolder.courseTitle.setText(data.get(i).getCourseTitle());
            viewHolder.startDate.setText("Start date : " + data.get(i).getStartDate());
            viewHolder.endDate.setText("End date : " + data.get(i).getEndDate());
            viewHolder.time.setText("Time : " + data.get(i).getTime());

            if (data.get(i).getIsAttend().equals("0")) {
                status = "Registered";
                viewHolder.messageText.setText("Tap to mark attendance.");
            } else if (data.get(i).getIsAttend().equals("1") && data.get(i).getIsFeedback().equals("0")) {
                status = "Attended";
                viewHolder.messageText.setText("Tap to give feedback.");
            } else if (data.get(i).getIsAttend().equals("1") && data.get(i).getIsFeedback().equals("1")) {
                status = "Feedback submitted";
                viewHolder.messageText.setVisibility(View.GONE);
            }

            viewHolder.status.setText("Status : " + status);


            final String finalStatus1 = status;

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (data.get(i).getIsMultiple().equalsIgnoreCase("True")) {
                        Intent intent = new Intent(RegisteredCoursesActivity.this, MultipleDayRegisteredCoursesActivity.class);
                        intent.putExtra("session_id", data.get(i).getRegisteredCourseId());
                        intent.putExtra("registration_id", data.get(i).getRegId());
                        startActivity(intent);
                    } else {
                        if (data.get(i).getIsAttend().equals("0")) {
                            if (data.get(i).getStartDate().compareTo(currentDate) == 0) {
                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(RegisteredCoursesActivity.this);
                                alertDialog.setTitle("Mark Attendance...");
                                alertDialog.setMessage("Are you sure you want to mark attendance for this session?");
                                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        String url = AppConstants.URL + "give_attendence.php?emp_id=" + employeeId + "&session_id=" + data.get(i).getRegisteredCourseId() + "&date=" + data.get(i).getStartDate() + "&registration_id=" + data.get(i).getRegId();
                                        HttpManager httpManager = new HttpManager(RegisteredCoursesActivity.this, new HttpManager.ServiceResponse() {
                                            @Override
                                            public void onServiceResponse(String serviceResponse) {
                                                if (serviceResponse != null && HttpManager.getStatusCode() == 200) {
                                                   // Log.d("Attendance ", serviceResponse);
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
                                Toast.makeText(RegisteredCoursesActivity.this, "Cannot mark attendance for this course today.", Toast.LENGTH_LONG).show();
                                return;
                            }
                        } else if (finalStatus1.equalsIgnoreCase("Attended")) {
                            Log.d("My Tag", "Feedback form");
                            Intent intent = new Intent(RegisteredCoursesActivity.this, FeedbackActivity1.class);
                            intent.putExtra("session_id", data.get(i).getRegisteredCourseId());
                            intent.putExtra("employee_id", employeeId);
                            intent.putExtra("date", data.get(i).getEndDate());
                            intent.putExtra("registration_id", data.get(i).getRegId());
                            startActivity(intent);
                        }
                    }
                }
            });


        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public TextView courseTitle, startDate, endDate, time, status, messageText;


            public ViewHolder(View itemView) {
                super(itemView);
                courseTitle = (TextView) itemView.findViewById(R.id.sessionTitle);
                startDate = (TextView) itemView.findViewById(R.id.startDate);
                endDate = (TextView) itemView.findViewById(R.id.endDate);
                time = (TextView) itemView.findViewById(R.id.time);
                status = (TextView) itemView.findViewById(R.id.status);
                messageText = (TextView) itemView.findViewById(R.id.messageText);

            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.courses, menu);
        this.menu = menu;
        MenuItem myActionMenuItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) myActionMenuItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!searchView.isIconified()) {
                    searchView.setIconified(true);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                searchData.clear();
                for (int i = 0; i < data.size(); i++) {
                    if (data.get(i).getCourseTitle().toLowerCase().contains(s.toLowerCase())) {
                        searchData.add(data.get(i));
                    }
                }
                customAdapter.notifyDataSetChanged();
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            getData();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
