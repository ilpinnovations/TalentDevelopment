package com.tcs.maverick.talentdevelopment.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tcs.maverick.talentdevelopment.R;
import com.tcs.maverick.talentdevelopment.beans.CompletedCoursesBean;
import com.tcs.maverick.talentdevelopment.utilities.AppConstants;
import com.tcs.maverick.talentdevelopment.utilities.DividerItemDecorator;
import com.tcs.maverick.talentdevelopment.utilities.HttpManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * Created by abhi on 3/13/2016.
 */

public class CompletedCoursesActivity extends AppCompatActivity {

    private View view1;
    private View view2;
    private RecyclerView recyclerView;
    private TextView messageText, errorText;
    private ArrayList<CompletedCoursesBean> data = new ArrayList<>(), searchData = new ArrayList<>();
    private Menu menu;
    private CustomAdapter customAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_layout);

        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Completed courses");
        view1 = findViewById(R.id.view1);
        view2 = findViewById(R.id.view2);
        customAdapter = new CustomAdapter(searchData);

        recyclerView = (RecyclerView) findViewById(R.id.list);
        messageText = (TextView) findViewById(R.id.messageText);
        errorText = (TextView) findViewById(R.id.errorText);
        customAdapter = new CustomAdapter(searchData);
        recyclerView.setLayoutManager(new LinearLayoutManager(CompletedCoursesActivity.this));
        recyclerView.addItemDecoration(new DividerItemDecorator(CompletedCoursesActivity.this, LinearLayoutManager.VERTICAL));

        getData();

    }

    private void getData() {
        SharedPreferences sharedPreferences = getSharedPreferences("Talent Development", MODE_PRIVATE);
        String employeeId = sharedPreferences.getString("emp_id", "");

        String url = AppConstants.URL + "completed_sessions.php?emp_id=" + employeeId;
        HttpManager httpManager = new HttpManager(CompletedCoursesActivity.this, new HttpManager.ServiceResponse() {
            @Override
            public void onServiceResponse(String serviceResponse) {
                view1.setVisibility(View.GONE);
                if (serviceResponse != null && HttpManager.getStatusCode() == 200) {
                    //Log.d("My Tag:", serviceResponse);

                    data = new ArrayList<>();
                    searchData = new ArrayList<>();
                    try {
                        JSONObject jsonObj = new JSONObject(serviceResponse);
                        JSONArray jsonArray = jsonObj.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                            String sessionName = jsonObject.getString("session_name");
                            String strDate = jsonObject.getString("end_date");
                            Date date = new SimpleDateFormat("yyyy-mm-dd").parse(strDate);
                            String endDate = new SimpleDateFormat("dd/mm/yyyy").format(date);
                            CompletedCoursesBean completedCoursesBean = new CompletedCoursesBean(sessionName, endDate);
                            data.add(completedCoursesBean);
                            searchData.add(completedCoursesBean);
                        }
                        if (data.size() > 0) {
                            recyclerView.setVisibility(View.VISIBLE);
                            customAdapter = new CustomAdapter(searchData);
                            recyclerView.setAdapter(customAdapter);
                        } else {
                            recyclerView.setVisibility(View.GONE);
                            view2.setVisibility(View.VISIBLE);
                            errorText.setText("No completed courses found.");
                        }

                    } catch (JSONException e) {
                        view2.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                        errorText.setText("No completed courses found.");
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
        ArrayList<CompletedCoursesBean> data;

        public CustomAdapter(ArrayList<CompletedCoursesBean> data) {
            this.data = data;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View v;
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.completed_courses_row, viewGroup, false);
            ViewHolder viewHolder = new ViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, final int i) {
            viewHolder.courseTitle.setText(data.get(i).getCourseTitle());
            viewHolder.completionDate.setText("Completed on : " + data.get(i).getCompletedOn());
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public TextView courseTitle, completionDate;

            public ViewHolder(View itemView) {
                super(itemView);
                courseTitle = (TextView) itemView.findViewById(R.id.courseTitle);
                completionDate = (TextView) itemView.findViewById(R.id.completedOn);

            }
        }
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
