package com.tcs.maverick.talentdevelopment.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tcs.maverick.talentdevelopment.R;
import com.tcs.maverick.talentdevelopment.beans.CoursesBean;
import com.tcs.maverick.talentdevelopment.utilities.AppConstants;
import com.tcs.maverick.talentdevelopment.utilities.HttpManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by abhi on 3/11/2016.
 */

public class DisplayCoursesActivity extends AppCompatActivity {
    private View view1;
    private View view2;
    private RecyclerView recyclerView;
    private TextView messageText, errorText;
    private ArrayList<CoursesBean> data = new ArrayList<>(), searchData = new ArrayList<>();
    private Menu menu;
    private CustomAdapter customAdapter;
    private String url;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_layout);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            type = bundle.getString("type");
        }

        url = getUrl();
        getSupportActionBar().setTitle(type);

        view1 = findViewById(R.id.view1);
        view2 = findViewById(R.id.view2);

        recyclerView = (RecyclerView) findViewById(R.id.list);
        messageText = (TextView) findViewById(R.id.messageText);
        errorText = (TextView) findViewById(R.id.errorText);
        customAdapter = new CustomAdapter(searchData);

        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float density = getResources().getDisplayMetrics().density;
        float dpWidth = outMetrics.widthPixels / density;
        int columns = Math.round(dpWidth / 150);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(DisplayCoursesActivity.this, columns);
        recyclerView.setLayoutManager(gridLayoutManager);
        getData();


    }

    private void getData() {
        HttpManager httpManager = new HttpManager(DisplayCoursesActivity.this, new HttpManager.ServiceResponse() {
            @Override
            public void onServiceResponse(String serviceResponse) {
                view1.setVisibility(View.GONE);
                if (serviceResponse != null && HttpManager.getStatusCode() == 200) {
                    //Log.d("My Tag:", serviceResponse);

                    data = new ArrayList<>();
                    searchData = new ArrayList<>();
                    try {
                        String status = "";
                        JSONObject jsonObj = new JSONObject(serviceResponse);
                        JSONArray jsonArray = jsonObj.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                            JSONObject jsonObject1 = jsonObject.getJSONObject("data1");
                            JSONObject jsonObject2 = jsonObject.getJSONObject("data2");

                            String sessionTitle = jsonObject1.getString("session_name");
                            //Log.d("My Tag:", sessionTitle);
                            String sessionId = jsonObject1.getString("session_id");
                            //Log.d("Session id", sessionId);
                            String sessionDescription = jsonObject1.getString("session_description");
                            //Log.d("My Tag:", sessionDescription);
                            String strDate = jsonObject1.getString("start_date");
                            String enDate = jsonObject1.getString("end_date");
                            String time = jsonObject1.getString("start_time") + " - " + jsonObject1.getString("end_time");
                            //Log.d("My Tag:", time);
                            String isRegistered = jsonObject2.getString("is_registered");
                            //Log.d("My Tag:", isRegistered);
                            String expectedOutcome = jsonObject1.getString("expected_outcome");
                            //Log.d("My Tag:", expectedOutcome);
                            String modeOfDelivery = jsonObject1.getString("mode_of_delivery");
                            //Log.d("My Tag:", modeOfDelivery);

                            if (isRegistered.equalsIgnoreCase("False")) {
                                status = "Not registered";
                            } else if (isRegistered.equalsIgnoreCase("True")) {
                                int st = Integer.parseInt(jsonObject2.getString("is_confirmed"));
                                switch (st) {
                                    case 0:
                                        status = "Request Pending";
                                        break;
                                    case 1:
                                        status = "Request Approved";
                                        break;
                                    case 2:
                                        status = "Request Rejected";
                                        break;
                                }
                            }


                            Date date = new SimpleDateFormat("yyyy-mm-dd").parse(strDate);
                            String startDate = new SimpleDateFormat("dd/mm/yyyy").format(date);
                            //Log.d("My Tag:", startDate);
                            Date date1 = new SimpleDateFormat("yyyy-mm-dd").parse(enDate);
                            String endDate = new SimpleDateFormat("dd/mm/yyyy").format(date1);
                            //Log.d("My Tag:", endDate);
                            CoursesBean coursesBean = new CoursesBean(sessionId, sessionTitle, sessionDescription, startDate, endDate, time, status, expectedOutcome, modeOfDelivery);
                            data.add(coursesBean);
                            searchData.add(coursesBean);

                        }
                        if (searchData.size() > 0) {
                            recyclerView.setVisibility(View.VISIBLE);
                            customAdapter = new CustomAdapter(searchData);
                            recyclerView.setAdapter(customAdapter);
                        } else {
                            recyclerView.setVisibility(View.GONE);
                            view2.setVisibility(View.VISIBLE);
                            errorText.setText("No courses found.");
                        }
                    } catch (JSONException e) {
                        view2.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                        errorText.setText("No courses found.");
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    view2.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    errorText.setText("No internet connection, please try again later.");
                }
                customAdapter.notifyDataSetChanged();
            }
        });

        httpManager.execute(url);
        recyclerView.setVisibility(View.GONE);
        view1.setVisibility(View.VISIBLE);
        view2.setVisibility(View.GONE);
        messageText.setText("Loading courses...");

    }

    private String getUrl() {
        SharedPreferences sharedPreferences = getSharedPreferences("Talent Development", MODE_PRIVATE);
        String employeeId = sharedPreferences.getString("emp_id", "");


        switch (type) {
            case "learningCalendar":
                type = "Learning Calendar";
                return AppConstants.URL + "session_details.php?emp_id=" + employeeId;

            case "webex":
                type = "Web X";
                return AppConstants.URL + "session_categories.php?emp_id=" + employeeId + "&category=webx";

            case "workshops":
                type = "Workshops";
                return AppConstants.URL + "session_categories.php?emp_id=" + employeeId + "&category=workshop";

            case "tcsion":
                type = "TCS ION";
                return AppConstants.URL + "session_categories.php?emp_id=" + employeeId + "&category=tcsion";

            case "certifications":
                type = "Certifications";
                return AppConstants.URL + "session_categories.php?emp_id=" + employeeId + "&category=certifications";

            case "ondemandsessions":
                type = "On Demand Sessions";
                return AppConstants.URL + "session_categories.php?emp_id=" + employeeId + "&category=ondemand";


        }
        return null;
    }

    private class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
        ArrayList<CoursesBean> data;
        int i = 0;

        public CustomAdapter(ArrayList<CoursesBean> data) {
            this.data = data;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View v;
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.session_element, viewGroup, false);
            ViewHolder viewHolder = new ViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, final int i) {
            viewHolder.courseTitle.setText(data.get(i).getCourseName());
            viewHolder.startDate.setText("Start date : " + data.get(i).getStartDate());
            viewHolder.endDate.setText("End date : " + data.get(i).getEndDate());
            viewHolder.time.setText("Time : " + data.get(i).getTime());
            viewHolder.registrationStatus.setText(data.get(i).getIsRegistered());
            setupColor(viewHolder);

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(DisplayCoursesActivity.this, CourseDetailsActivity.class);
                    intent.putExtra("CoursesBean", data.get(i));
                    startActivity(intent);
                }
            });


        }

        private void setupColor(ViewHolder viewHolder) {
            switch (i) {
                case 0:
                    viewHolder.itemView.setBackgroundColor(getResources().getColor(R.color.color1));
                    modulateI();
                    break;
                case 1:
                    viewHolder.itemView.setBackgroundColor(getResources().getColor(R.color.color2));
                    modulateI();
                    break;

                case 2:
                    viewHolder.itemView.setBackgroundColor(getResources().getColor(R.color.color3));
                    modulateI();
                    break;

                case 3:
                    viewHolder.itemView.setBackgroundColor(getResources().getColor(R.color.color4));
                    modulateI();
                    break;
                case 4:
                    viewHolder.itemView.setBackgroundColor(getResources().getColor(R.color.color5));
                    modulateI();
                    break;


            }
        }

        private void modulateI() {
            if (i < 4) {
                i = i + 1;

            } else if (i == 4) {
                i = 0;

            }

        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public TextView courseTitle, startDate, endDate, time, registrationStatus;


            public ViewHolder(View itemView) {
                super(itemView);
                courseTitle = (TextView) itemView.findViewById(R.id.sessionTitle);
                startDate = (TextView) itemView.findViewById(R.id.startDate);
                endDate = (TextView) itemView.findViewById(R.id.endDate);
                time = (TextView) itemView.findViewById(R.id.time);
                registrationStatus = (TextView) itemView.findViewById(R.id.status);
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
                    if (data.get(i).getCourseName().toLowerCase().contains(s.toLowerCase())) {
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
