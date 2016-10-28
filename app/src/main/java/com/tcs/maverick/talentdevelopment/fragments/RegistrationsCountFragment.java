package com.tcs.maverick.talentdevelopment.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tcs.maverick.talentdevelopment.R;
import com.tcs.maverick.talentdevelopment.activities.CompletedCoursesDetailsActivity;
import com.tcs.maverick.talentdevelopment.utilities.AppConstants;
import com.tcs.maverick.talentdevelopment.utilities.HttpManager;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by abhi on 3/29/2016.
 */
public class RegistrationsCountFragment extends Fragment {
    private Menu menu;
    private String courseId;
    private String registrations, attendees, rating1, rating2, rating3, rating4;
    private TextView textView1, textView2, textView3, messageText;
    private View view1, view2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.registration_count_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        courseId = CompletedCoursesDetailsActivity.getCourseId();

        textView1 = (TextView) view.findViewById(R.id.registrations);
        textView2 = (TextView) view.findViewById(R.id.attendees);
        textView3 = (TextView) view.findViewById(R.id.feedbacks);

        view1 = view.findViewById(R.id.view1);
        view2 = view.findViewById(R.id.view2);
        messageText = (TextView) view.findViewById(R.id.messageText);
        getData();


    }


    public void getData() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Talent Development", getActivity().MODE_PRIVATE);
        String employeeId = sharedPreferences.getString("emp_id", "");
        String url = AppConstants.URL + "trainer_completed_session_statstics.php?emp_id=" + employeeId + "&session_id=" + courseId;
        //Log.d("Url", url);
        HttpManager httpManager = new HttpManager(getActivity(), new HttpManager.ServiceResponse() {
            @Override
            public void onServiceResponse(String serviceResponse) {
                if (serviceResponse != null && HttpManager.getStatusCode() == 200) {
                    //Log.d("My Tag: Statistics", serviceResponse);
                    try {
                        view1.setVisibility(View.VISIBLE);
                        view2.setVisibility(View.GONE);


                        JSONObject jsonObject1 = new JSONObject(serviceResponse);
                        JSONObject jsonObject2 = jsonObject1.getJSONObject("data1");
                        JSONObject jsonObject3 = jsonObject1.getJSONObject("data2");
                        JSONObject jsonObject4 = jsonObject1.getJSONObject("data3");

                        registrations = jsonObject2.getString("registrations");
                        //Log.d("Registrations ", registrations);
                        attendees = jsonObject3.getString("attendies");
                        //Log.d("Attendees", attendees);
                        rating1 = jsonObject4.getString("rating1");
                        //Log.d("Rating1", rating1);
                        rating2 = jsonObject4.getString("rating2");
                        //Log.d("Rating2", rating2);
                        rating3 = jsonObject4.getString("rating3");
                        //Log.d("Rating3", rating3);
                        rating4 = jsonObject4.getString("rating4");
                        //Log.d("Rating4", rating4);


                        int val1 = Integer.parseInt(rating1);
                        //Log.d("Val1", String.valueOf(val1));
                        int val2 = Integer.parseInt(rating2);
                        //Log.d("Val1", String.valueOf(val2));
                        int val3 = Integer.parseInt(rating3);
                        //Log.d("Val1", String.valueOf(val3));
                        int val4 = Integer.parseInt(rating4);
                        //Log.d("Val1", String.valueOf(val4));

                        float numFeedbacks = val1 + val2 + val3 + val4;
                        textView1.setText(registrations);
                        textView2.setText(attendees);
                        textView3.setText("" + (int) numFeedbacks);


                    } catch (JSONException e) {
                        view1.setVisibility(View.GONE);
                        view2.setVisibility(View.VISIBLE);
                        messageText.setText("No internet connection...");
                    }

                } else {
                    view1.setVisibility(View.GONE);
                    view2.setVisibility(View.VISIBLE);
                    messageText.setText("No internet connection...");
                }
            }
        });
        httpManager.execute(url);
        view1.setVisibility(View.GONE);
        view2.setVisibility(View.VISIBLE);
        messageText.setText("Loading number of registrations...");
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.refresh_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                getData();
                return true;

        }

        return false;
    }


}
