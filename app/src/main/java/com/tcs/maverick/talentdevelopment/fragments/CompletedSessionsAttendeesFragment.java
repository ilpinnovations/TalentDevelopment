package com.tcs.maverick.talentdevelopment.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tcs.maverick.talentdevelopment.R;
import com.tcs.maverick.talentdevelopment.activities.CompletedCoursesDetailsActivity;
import com.tcs.maverick.talentdevelopment.activities.StudentFeedbackActivity;
import com.tcs.maverick.talentdevelopment.beans.UsersBean;
import com.tcs.maverick.talentdevelopment.utilities.AppConstants;
import com.tcs.maverick.talentdevelopment.utilities.DividerItemDecorator;
import com.tcs.maverick.talentdevelopment.utilities.HttpManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by abhi on 3/28/2016.
 */
public class CompletedSessionsAttendeesFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private ArrayList<UsersBean> data = new ArrayList<>();
    private ArrayList<UsersBean> searchData = new ArrayList<>();
    private RecyclerView recyclerView;
    private CustomAdapter customAdapter;
    private Menu menu;
    private TextView messageText;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String courseId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list_layout1, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        courseId = CompletedCoursesDetailsActivity.getCourseId();
        recyclerView = (RecyclerView) view.findViewById(R.id.list);
        customAdapter = new CustomAdapter(searchData);
        messageText = (TextView) view.findViewById(R.id.messageText);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeLayout);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecorator(getActivity(), LinearLayoutManager.VERTICAL));
        swipeRefreshLayout.setOnRefreshListener(this);
        onRefresh();

    }

    public void getData() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Talent Development", getActivity().MODE_PRIVATE);
        String employeeId = sharedPreferences.getString("emp_id", "");
        String url = AppConstants.URL + "trainer_attended_members.php?emp_id=" + employeeId + "&session_id=" + courseId;
        //Log.d("Url", url);
        HttpManager httpManager = new HttpManager(getActivity(), new HttpManager.ServiceResponse() {
            @Override
            public void onServiceResponse(String serviceResponse) {
                if (serviceResponse != null && HttpManager.getStatusCode() == 200) {
                    //Log.d("My Tag", serviceResponse);
                    try {
                        data = new ArrayList<>();
                        searchData = new ArrayList<>();
                        JSONArray jsonArray = new JSONArray(serviceResponse);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);
                            String empName = jsonObject1.getString("emp_name");
                            String empId = jsonObject1.getString("emp_id");
                            String email = jsonObject1.getString("emp_email");
                            UsersBean usersBean = new UsersBean(empId, empName, email);
                            data.add(usersBean);
                            searchData.add(usersBean);
                        }

                        if (data.size() > 0) {
                            messageText.setVisibility(View.GONE);
                            customAdapter = new CustomAdapter(searchData);
                            recyclerView.setVisibility(View.VISIBLE);
                            recyclerView.setAdapter(customAdapter);
                            swipeRefreshLayout.setRefreshing(false);
                        } else {
                            recyclerView.setVisibility(View.GONE);
                            messageText.setVisibility(View.VISIBLE);
                            messageText.setText("No attendees for this session.");
                        }


                    } catch (JSONException e) {
                        recyclerView.setVisibility(View.GONE);
                        messageText.setVisibility(View.VISIBLE);
                        messageText.setText("No attendees for this session.");
                    }

                } else {
                    recyclerView.setVisibility(View.GONE);
                    messageText.setVisibility(View.VISIBLE);
                    messageText.setText("No internet connect, please connect to internet and try again.");
                }
            }
        });
        httpManager.execute(url);
        recyclerView.setVisibility(View.GONE);
        messageText.setVisibility(View.VISIBLE);
        messageText.setText("Loading list of attendees");
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        getData();
    }

    private class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
        ArrayList<UsersBean> data;

        private ArrayList<ViewHolder> viewH = new ArrayList<>();

        public CustomAdapter(ArrayList<UsersBean> data) {
            this.data = data;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View v;
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.la_layout, viewGroup, false);
            ViewHolder viewHolder = new ViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(CustomAdapter.ViewHolder viewHolder, final int i) {
            viewH.add(viewHolder);
            viewHolder.laName.setText(data.get(i).getEmpName());
            viewHolder.laEmail.setText(data.get(i).getEmpEmail());
            viewHolder.laEmpId.setText("Employee Id : " + data.get(i).getEmpId());
            viewHolder.messageText.setVisibility(View.VISIBLE);
            setupImages(viewHolder, data.get(i).getEmpName().charAt(0));

            viewHolder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), StudentFeedbackActivity.class);
                    intent.putExtra("employeeId", data.get(i).getEmpId());
                    intent.putExtra("sessionId", courseId);
                    startActivity(intent);
                }
            });
        }

        private void setupImages(ViewHolder viewHolder, char startingChar) {
            switch (startingChar) {
                case 'A':
                    viewHolder.imageview.setImageResource(R.drawable.a_icon);
                    break;
                case 'B':
                    viewHolder.imageview.setImageResource(R.drawable.b_icon);
                    break;
                case 'C':
                    viewHolder.imageview.setImageResource(R.drawable.c_icon);
                    break;
                case 'D':
                    viewHolder.imageview.setImageResource(R.drawable.d_icon);
                    break;
                case 'E':
                    viewHolder.imageview.setImageResource(R.drawable.e_icon);
                    break;
                case 'F':
                    viewHolder.imageview.setImageResource(R.drawable.f_icon);
                    break;
                case 'G':
                    viewHolder.imageview.setImageResource(R.drawable.g_icon);
                    break;
                case 'H':
                    viewHolder.imageview.setImageResource(R.drawable.h_icon);
                    break;
                case 'I':
                    viewHolder.imageview.setImageResource(R.drawable.i_icon);
                    break;
                case 'J':
                    viewHolder.imageview.setImageResource(R.drawable.j_icon);
                    break;
                case 'K':
                    viewHolder.imageview.setImageResource(R.drawable.k_icon);
                    break;
                case 'L':
                    viewHolder.imageview.setImageResource(R.drawable.l_icon);
                    break;
                case 'M':
                    viewHolder.imageview.setImageResource(R.drawable.m_icon);
                    break;
                case 'N':
                    viewHolder.imageview.setImageResource(R.drawable.n_icon);
                    break;
                case 'O':
                    viewHolder.imageview.setImageResource(R.drawable.o_icon);
                    break;
                case 'P':
                    viewHolder.imageview.setImageResource(R.drawable.p_icon);
                    break;
                case 'Q':
                    viewHolder.imageview.setImageResource(R.drawable.q_icon);
                    break;
                case 'R':
                    viewHolder.imageview.setImageResource(R.drawable.r_icon);
                    break;
                case 'S':
                    viewHolder.imageview.setImageResource(R.drawable.s_icon);
                    break;
                case 'T':
                    viewHolder.imageview.setImageResource(R.drawable.t_icon);
                    break;
                case 'U':
                    viewHolder.imageview.setImageResource(R.drawable.u_icon);
                    break;
                case 'V':
                    viewHolder.imageview.setImageResource(R.drawable.v_icon);
                    break;
                case 'W':
                    viewHolder.imageview.setImageResource(R.drawable.w_icon);
                    break;
                case 'X':
                    viewHolder.imageview.setImageResource(R.drawable.x_icon);
                    break;
                case 'Y':
                    viewHolder.imageview.setImageResource(R.drawable.y_icon);
                    break;
                case 'Z':
                    viewHolder.imageview.setImageResource(R.drawable.z_icon);
                    break;

            }

        }

        @Override
        public int getItemCount() {
            return data.size();
        }


        public class ViewHolder extends RecyclerView.ViewHolder {

            public TextView laName, laEmpId, laEmail, messageText;
            public ImageView imageview;
            private View view;


            public ViewHolder(View itemView) {
                super(itemView);
                laName = (TextView) itemView.findViewById(R.id.laName);
                laEmail = (TextView) itemView.findViewById(R.id.laEmail);
                laEmpId = (TextView) itemView.findViewById(R.id.laEmpId);
                imageview = (ImageView) itemView.findViewById(R.id.userImage);
                view = itemView.findViewById(R.id.view);
                messageText = (TextView) itemView.findViewById(R.id.messageText);

            }

        }


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.registeredcourses, menu);
        super.onCreateOptionsMenu(menu, inflater);
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
                    if (data.get(i).getEmpName().toLowerCase().contains(s.toLowerCase())) {
                        searchData.add(data.get(i));
                    }
                }
                customAdapter.notifyDataSetChanged();
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                onRefresh();
                return true;

        }

        return false;
    }


}
