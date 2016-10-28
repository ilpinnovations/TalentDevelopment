package com.tcs.maverick.talentdevelopment.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tcs.maverick.talentdevelopment.R;
import com.tcs.maverick.talentdevelopment.beans.LeadersBean;
import com.tcs.maverick.talentdevelopment.utilities.AppConstants;
import com.tcs.maverick.talentdevelopment.utilities.DividerItemDecorator;
import com.tcs.maverick.talentdevelopment.utilities.HttpManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by abhi on 3/17/2016.
 */
public class LeaderboardActivity extends AppCompatActivity {
    private ArrayList<LeadersBean> data = new ArrayList<>();
    private RecyclerView recyclerView;
    private CustomAdapter customAdapter;
    private Menu menu;
    private View view1;
    private View view2;
    private TextView messageText, errorText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_layout);

        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Leaderboard");
        view1 = findViewById(R.id.view1);
        view2 = findViewById(R.id.view2);
        customAdapter = new CustomAdapter(data);

        recyclerView = (RecyclerView) findViewById(R.id.list);
        messageText = (TextView) findViewById(R.id.messageText);
        errorText = (TextView) findViewById(R.id.errorText);
        customAdapter = new CustomAdapter(data);
        recyclerView.setLayoutManager(new LinearLayoutManager(LeaderboardActivity.this));
        recyclerView.addItemDecoration(new DividerItemDecorator(LeaderboardActivity.this, LinearLayoutManager.VERTICAL));

        getData();


    }

    private void getData() {
        String url = AppConstants.URL + "leader_board.php";
        HttpManager httpManager = new HttpManager(LeaderboardActivity.this, new HttpManager.ServiceResponse() {
            @Override
            public void onServiceResponse(String serviceResponse) {
                view1.setVisibility(View.GONE);
                if (serviceResponse != null && HttpManager.getStatusCode() == 200) {
                    //Log.d("My Tag:", serviceResponse);

                    data = new ArrayList<>();
                    try {
                        JSONArray jsonArray = new JSONArray(serviceResponse);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                            String leaderName = jsonObject.getString("emp_name");
                            String leaderScore = jsonObject.getString("score");

                            LeadersBean leadersBean = new LeadersBean(leaderName, leaderScore);
                            data.add(leadersBean);
                        }
                        if (data.size() > 0) {
                            recyclerView.setVisibility(View.VISIBLE);
                            customAdapter = new CustomAdapter(data);
                            recyclerView.setAdapter(customAdapter);
                        } else {
                            recyclerView.setVisibility(View.GONE);
                            view2.setVisibility(View.VISIBLE);
                            errorText.setText("Leaderboard not found.");
                        }

                    } catch (JSONException e) {
                        view2.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                        errorText.setText("Leaderboard not found.");
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
        messageText.setText("Loading leaderboard...");

    }

    private class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
        ArrayList<LeadersBean> data;


        public CustomAdapter(ArrayList<LeadersBean> data) {
            this.data = data;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View v;
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.leaderboard_row, viewGroup, false);
            ViewHolder viewHolder = new ViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final CustomAdapter.ViewHolder viewHolder, final int i) {
            viewHolder.leaderName.setText(data.get(i).getLeaderName());
            viewHolder.leaderScore.setText("Score : " + data.get(i).getLeaderScore());
            setupImages(viewHolder, data.get(i).getLeaderName().toUpperCase().charAt(0));
        }

        private void setupImageDialog(ImageView viewHolder, char startingChar) {
            switch (startingChar) {
                case 'A':
                    viewHolder.setImageResource(R.drawable.a_icon);
                    break;
                case 'B':
                    viewHolder.setImageResource(R.drawable.b_icon);
                    break;
                case 'C':
                    viewHolder.setImageResource(R.drawable.c_icon);
                    break;
                case 'D':
                    viewHolder.setImageResource(R.drawable.d_icon);
                    break;
                case 'E':
                    viewHolder.setImageResource(R.drawable.e_icon);
                    break;
                case 'F':
                    viewHolder.setImageResource(R.drawable.f_icon);
                    break;
                case 'G':
                    viewHolder.setImageResource(R.drawable.g_icon);
                    break;
                case 'H':
                    viewHolder.setImageResource(R.drawable.h_icon);
                    break;
                case 'I':
                    viewHolder.setImageResource(R.drawable.i_icon);
                    break;
                case 'J':
                    viewHolder.setImageResource(R.drawable.j_icon);
                    break;
                case 'K':
                    viewHolder.setImageResource(R.drawable.k_icon);
                    break;
                case 'L':
                    viewHolder.setImageResource(R.drawable.l_icon);
                    break;
                case 'M':
                    viewHolder.setImageResource(R.drawable.m_icon);
                    break;
                case 'N':
                    viewHolder.setImageResource(R.drawable.n_icon);
                    break;
                case 'O':
                    viewHolder.setImageResource(R.drawable.o_icon);
                    break;
                case 'P':
                    viewHolder.setImageResource(R.drawable.p_icon);
                    break;
                case 'Q':
                    viewHolder.setImageResource(R.drawable.q_icon);
                    break;
                case 'R':
                    viewHolder.setImageResource(R.drawable.r_icon);
                    break;
                case 'S':
                    viewHolder.setImageResource(R.drawable.s_icon);
                    break;
                case 'T':
                    viewHolder.setImageResource(R.drawable.t_icon);
                    break;
                case 'U':
                    viewHolder.setImageResource(R.drawable.u_icon);
                    break;
                case 'V':
                    viewHolder.setImageResource(R.drawable.v_icon);
                    break;
                case 'W':
                    viewHolder.setImageResource(R.drawable.w_icon);
                    break;
                case 'X':
                    viewHolder.setImageResource(R.drawable.x_icon);
                    break;
                case 'Y':
                    viewHolder.setImageResource(R.drawable.y_icon);
                    break;
                case 'Z':
                    viewHolder.setImageResource(R.drawable.z_icon);
                    break;

            }

        }

        @Override
        public int getItemCount() {
            return data.size();
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

        public class ViewHolder extends RecyclerView.ViewHolder {

            public TextView leaderName, leaderScore;
            public ImageView imageview;

            public ViewHolder(View itemView) {
                super(itemView);
                leaderName = (TextView) itemView.findViewById(R.id.leaderName);
                leaderScore = (TextView) itemView.findViewById(R.id.score);
                imageview = (ImageView) itemView.findViewById(R.id.userImage);
            }

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.leaders, menu);
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
