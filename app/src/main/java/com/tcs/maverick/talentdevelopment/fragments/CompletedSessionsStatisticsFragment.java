package com.tcs.maverick.talentdevelopment.fragments;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tcs.maverick.talentdevelopment.R;
import com.tcs.maverick.talentdevelopment.activities.CompletedCoursesDetailsActivity;
import com.tcs.maverick.talentdevelopment.utilities.AppConstants;
import com.tcs.maverick.talentdevelopment.utilities.HttpManager;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by abhi on 3/28/2016.
 */
public class CompletedSessionsStatisticsFragment extends Fragment {
    private Menu menu;
    private String courseId;
    int[] feedbackValues = new int[4];
    public static final String TYPE = "type";
    private static int[] COLORS = new int[]{Color.GREEN, Color.BLUE, Color.MAGENTA, Color.CYAN};
    private static String[] labels = new String[]{"One star", "Two stars", "Three stars", "Four stars"};
    private CategorySeries mSeries = new CategorySeries("");
    private DefaultRenderer mRenderer = new DefaultRenderer();
    private GraphicalView mChartView;
    private String registrations, attendees, rating1, rating2, rating3, rating4;
    private LinearLayout layout;
    private View view1;
    private TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.stastics_layout_activity, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        courseId = CompletedCoursesDetailsActivity.getCourseId();
        layout = (LinearLayout) view.findViewById(R.id.chart);
        view1 = view.findViewById(R.id.view2);
        textView = (TextView) view.findViewById(R.id.messageText);

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

                        //Log.d("My Tag", String.valueOf(numFeedbacks));
                        if (numFeedbacks > 0) {
                            float val1per = (val1 / numFeedbacks) * 100f;
                            //Log.d("Val1", String.valueOf(val1per));
                            float val2per = (val2 / numFeedbacks) * 100f;
                            //Log.d("Val2", String.valueOf(val2per));
                            float val3per = (val3 / numFeedbacks) * 100f;
                            //Log.d("Val3", String.valueOf(val3per));
                            float val4per = (val4 / numFeedbacks) * 100f;
                            //Log.d("Val4", String.valueOf(val4per));

                            feedbackValues[0] = (int) val1per;
                            feedbackValues[1] = (int) val2per;
                            feedbackValues[2] = (int) val3per;
                            feedbackValues[3] = (int) val4per;

                            view1.setVisibility(View.GONE);
                            layout.setVisibility(View.VISIBLE);

                            mRenderer.setChartTitleTextSize(20);
                            mRenderer.setLabelsTextSize(20);
                            mRenderer.setLegendTextSize(20);
                            mRenderer.setMargins(new int[]{40, 40, 40, 40});
                            mRenderer.setZoomButtonsVisible(true);
                            mRenderer.setStartAngle(90);

                            if (mChartView == null) {
                                mChartView = ChartFactory.getPieChartView(getActivity(), mSeries, mRenderer);
                                mRenderer.setClickEnabled(true);
                                mRenderer.setSelectableBuffer(10);
                                layout.addView(mChartView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                            } else {
                                mChartView.repaint();
                            }

                            fillPieChart();

                        } else {
                            view1.setVisibility(View.VISIBLE);
                            layout.setVisibility(View.GONE);
                            textView.setText("No feedback submitted...");
                        }


                    } catch (JSONException e) {
                        view1.setVisibility(View.VISIBLE);
                        layout.setVisibility(View.GONE);
                        textView.setText("No internet connection, please try again later...");
                    }

                } else {
                    view1.setVisibility(View.VISIBLE);
                    layout.setVisibility(View.GONE);
                    textView.setText("No internet connection, please try again later...");
                }
            }
        });
        httpManager.execute(url);
        view1.setVisibility(View.VISIBLE);
        layout.setVisibility(View.GONE);
        textView.setText("Loading statistics...");
    }


    public void fillPieChart() {
        mSeries.clear();
        for (int i = 0; i < feedbackValues.length; i++) {
            mSeries.add(labels[i], feedbackValues[i]);
            SimpleSeriesRenderer renderer = new SimpleSeriesRenderer();
            renderer.setColor(COLORS[(mSeries.getItemCount() - 1) % COLORS.length]);
            mRenderer.addSeriesRenderer(renderer);
            if (mChartView != null)
                mChartView.repaint();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
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
