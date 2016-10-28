package com.tcs.maverick.talentdevelopment.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
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

import com.tcs.maverick.talentdevelopment.R;
import com.tcs.maverick.talentdevelopment.beans.FaqsBean;
import com.tcs.maverick.talentdevelopment.utilities.AppConstants;
import com.tcs.maverick.talentdevelopment.utilities.DividerItemDecorator;
import com.tcs.maverick.talentdevelopment.utilities.HttpManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by abhi on 3/25/2016.
 */


public class FaqsActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private ArrayList<FaqsBean> data = new ArrayList<>();
    private ArrayList<FaqsBean> searchData = new ArrayList<>();
    private RecyclerView recyclerView;
    private CustomAdapter customAdapter;
    private Menu menu;
    private TextView messageText;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_layout1);
        getSupportActionBar().setTitle("FAQs");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.list);
        customAdapter = new CustomAdapter(searchData);
        messageText = (TextView) findViewById(R.id.messageText);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeLayout);
        recyclerView.setLayoutManager(new LinearLayoutManager(FaqsActivity.this));
        recyclerView.addItemDecoration(new DividerItemDecorator(FaqsActivity.this, LinearLayoutManager.VERTICAL));
        onRefresh();

        swipeRefreshLayout.setOnRefreshListener(this);

    }

    public void getData() {
        SharedPreferences sharedPreferences = getSharedPreferences("Talent Development", MODE_PRIVATE);
        String employeeId = sharedPreferences.getString("emp_id", "");
        String url = AppConstants.URL + "faq.php";

        HttpManager httpManager = new HttpManager(FaqsActivity.this, new HttpManager.ServiceResponse() {

            @Override
            public void onServiceResponse(String serviceResponse) {
                if (serviceResponse != null && HttpManager.getStatusCode() == 200) {
                    //Log.d("My Tag", serviceResponse);
                    try {
                        data = new ArrayList<>();
                        searchData = new ArrayList<>();
                        JSONObject jsonObject = new JSONObject(serviceResponse);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);
                            String question = jsonObject1.getString("question");
                            String answer = jsonObject1.getString("answer");
                            FaqsBean faqsBean = new FaqsBean(question, answer);
                            data.add(faqsBean);
                            searchData.add(faqsBean);
                        }

                        if (data.size() > 0) {
                            customAdapter = new CustomAdapter(searchData);
                            recyclerView.setVisibility(View.VISIBLE);
                            recyclerView.setAdapter(customAdapter);
                            swipeRefreshLayout.setRefreshing(false);
                        } else {
                            recyclerView.setVisibility(View.GONE);
                            messageText.setVisibility(View.VISIBLE);
                            messageText.setText("No Faqs are available now.");
                        }


                    } catch (JSONException e) {
                        recyclerView.setVisibility(View.GONE);
                        messageText.setVisibility(View.VISIBLE);
                        messageText.setText("No internet connect, please connect to internet and try again.");
                    }

                } else {
                    recyclerView.setVisibility(View.GONE);
                    messageText.setVisibility(View.VISIBLE);
                    messageText.setText("No internet connect, please connect to internet and try again.");
                }
            }
        });
        httpManager.execute(url);
    }


    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        getData();
    }

    private class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
        ArrayList<FaqsBean> data;
        Context context;
        private ArrayList<ViewHolder> viewH = new ArrayList<>();

        public CustomAdapter(ArrayList<FaqsBean> data) {
            this.data = data;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View v;
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.faqs_element, viewGroup, false);
            ViewHolder viewHolder = new ViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(CustomAdapter.ViewHolder viewHolder, final int i) {
            viewH.add(viewHolder);
            viewHolder.question.setText(data.get(i).getQuestion());
            viewHolder.answer.setText(data.get(i).getAnswer());
        }


        @Override
        public int getItemCount() {
            return data.size();
        }


        public class ViewHolder extends RecyclerView.ViewHolder {

            public TextView question, answer;

            public ViewHolder(View itemView) {
                super(itemView);
                question = (TextView) itemView.findViewById(R.id.question);
                answer = (TextView) itemView.findViewById(R.id.answer);

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
                    if (data.get(i).getQuestion().toLowerCase().contains(s.toLowerCase())) {
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
            onRefresh();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
