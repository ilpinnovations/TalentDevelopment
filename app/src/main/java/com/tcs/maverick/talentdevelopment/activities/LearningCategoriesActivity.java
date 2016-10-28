package com.tcs.maverick.talentdevelopment.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tcs.maverick.talentdevelopment.R;
import com.tcs.maverick.talentdevelopment.beans.LearningCategoriesBean;

import java.util.ArrayList;

/**
 * Created by abhi on 3/25/2016.
 */

public class LearningCategoriesActivity extends AppCompatActivity {

    private ArrayList<LearningCategoriesBean> data = new ArrayList<>();
    RecyclerView recyclerView, recyclerView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_layout);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Learning Categories");
        setupMenuItems();

        recyclerView = (RecyclerView) findViewById(R.id.list);
        recyclerView1 = (RecyclerView) findViewById(R.id.list1);

        CustomAdapter customAdapter = new CustomAdapter(data);


        recyclerView1.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);

        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);


        float density = getResources().getDisplayMetrics().density;
        float dpWidth = outMetrics.widthPixels / density;
        int columns = Math.round(dpWidth / 150);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(LearningCategoriesActivity.this, columns);
        recyclerView1.setLayoutManager(gridLayoutManager);
        recyclerView1.setAdapter(customAdapter);

    }

    private void setupMenuItems() {
        LearningCategoriesBean learningCategoriesBean = new LearningCategoriesBean(1, "WEB X");
        LearningCategoriesBean learningCategoriesBean1 = new LearningCategoriesBean(2, "Workshops");
        LearningCategoriesBean learningCategoriesBean2 = new LearningCategoriesBean(3, "TCS ION");
        LearningCategoriesBean learningCategoriesBean3 = new LearningCategoriesBean(4, "Certifications");
        LearningCategoriesBean learningCategoriesBean4 = new LearningCategoriesBean(5, "On Demand Sessions");

        data.add(learningCategoriesBean);
        data.add(learningCategoriesBean1);
        data.add(learningCategoriesBean2);
        data.add(learningCategoriesBean3);
        data.add(learningCategoriesBean4);

    }


    private class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
        ArrayList<LearningCategoriesBean> data;

        public CustomAdapter(ArrayList<LearningCategoriesBean> data) {
            this.data = data;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View v;
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.menu_item1, viewGroup, false);
            ViewHolder viewHolder = new ViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, final int i) {
            viewHolder.title.setText(data.get(i).getLearningCategory());
            setupImages(viewHolder, data.get(i).getLearningCategory());

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    manageItemClick(data.get(i).getLearningCategoryId());
                }
            });
        }

        private void manageItemClick(int i) {
            switch (i) {
                case 1:
                    Intent i1 = new Intent(LearningCategoriesActivity.this, DisplayCoursesActivity.class);
                    i1.putExtra("type", "webex");
                    startActivity(i1);
                    break;
                case 2:
                    Intent i2 = new Intent(LearningCategoriesActivity.this, DisplayCoursesActivity.class);
                    i2.putExtra("type", "workshops");
                    startActivity(i2);
                    break;
                case 3:
                    Intent i3 = new Intent(LearningCategoriesActivity.this, DisplayCoursesActivity.class);
                    i3.putExtra("type", "tcsion");
                    startActivity(i3);
                    break;
                case 4:
                    Intent i4 = new Intent(LearningCategoriesActivity.this, DisplayCoursesActivity.class);
                    i4.putExtra("type", "certifications");
                    startActivity(i4);
                    break;

                case 5:
                    Intent i5 = new Intent(LearningCategoriesActivity.this, DisplayCoursesActivity.class);
                    i5.putExtra("type", "ondemandsessions");
                    startActivity(i5);
                    break;
            }
        }

        private void setupImages(ViewHolder viewHolder, String item) {
            switch (item) {
                case "WEB X":
                    viewHolder.itemView.setBackgroundColor(getResources().getColor(R.color.color1));
                    viewHolder.imageview.setImageResource(R.drawable.webx_icon);
                    break;
                case "Workshops":
                    viewHolder.itemView.setBackgroundColor(getResources().getColor(R.color.color2));
                    viewHolder.imageview.setImageResource(R.drawable.workshop_icon);
                    break;

                case "TCS ION":
                    viewHolder.itemView.setBackgroundColor(getResources().getColor(R.color.color3));
                    viewHolder.imageview.setImageResource(R.drawable.ion_icon);
                    break;

                case "On Demand Sessions":
                    viewHolder.itemView.setBackgroundColor(getResources().getColor(R.color.color4));
                    viewHolder.imageview.setImageResource(R.drawable.on_demand_sessions_icon);
                    break;

                case "Certifications":
                    viewHolder.itemView.setBackgroundColor(getResources().getColor(R.color.color5));
                    viewHolder.imageview.setImageResource(R.drawable.certifications_icon);
                    break;


            }
        }


        @Override
        public int getItemCount() {
            return data.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public TextView title;
            public ImageView imageview;

            public ViewHolder(View itemView) {
                super(itemView);
                title = (TextView) itemView.findViewById(R.id.menuTitle);
                imageview = (ImageView) itemView.findViewById(R.id.menuItemImage);
            }

        }
    }
}
