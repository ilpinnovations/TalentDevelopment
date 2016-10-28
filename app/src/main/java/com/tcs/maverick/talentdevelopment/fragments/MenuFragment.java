package com.tcs.maverick.talentdevelopment.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tcs.maverick.talentdevelopment.R;
import com.tcs.maverick.talentdevelopment.activities.CompletedCoursesActivity;
import com.tcs.maverick.talentdevelopment.activities.ContactsActivity;
import com.tcs.maverick.talentdevelopment.activities.DisplayCoursesActivity;
import com.tcs.maverick.talentdevelopment.activities.FaqsActivity;
import com.tcs.maverick.talentdevelopment.activities.LeaderboardActivity;
import com.tcs.maverick.talentdevelopment.activities.LearningAmbassadors;
import com.tcs.maverick.talentdevelopment.activities.LearningCategoriesActivity;
import com.tcs.maverick.talentdevelopment.activities.LoadCompletedSessions;
import com.tcs.maverick.talentdevelopment.activities.LoadUpcomingSessions;
import com.tcs.maverick.talentdevelopment.activities.RegisteredCoursesActivity;
import com.tcs.maverick.talentdevelopment.beans.MenuBean;
import com.tcs.maverick.talentdevelopment.utilities.DividerItemDecorator;

import java.util.ArrayList;

/**
 * Created by abhi on 3/1/2016.
 */

public class MenuFragment extends Fragment {
    private ArrayList<MenuBean> data = new ArrayList<>();
    private ArrayList<MenuBean> searchData = new ArrayList<>();
    private Menu menu;
    private boolean flag = false;
    RecyclerView recyclerView, recyclerView1;
    CustomAdapter customAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list_layout, container, false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        setupMenuItems();
        recyclerView = (RecyclerView) view.findViewById(R.id.list);
        recyclerView1 = (RecyclerView) view.findViewById(R.id.list1);
        recyclerView.addItemDecoration(new DividerItemDecorator(getActivity(), LinearLayout.VERTICAL));
        customAdapter = new CustomAdapter(searchData);
        setupRecyclerView();

    }

    private void setupMenuItems() {

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Talent Development", getActivity().MODE_PRIVATE);
        String type = sharedPreferences.getString("type", "");

        MenuBean menuBean = new MenuBean(1, "Learning Calendar");
        MenuBean menuBean1 = new MenuBean(2, "Learning Categories");
        MenuBean menuBean4 = new MenuBean(3, "Learning Ambassadors");
        MenuBean menuBean5 = new MenuBean(4, "Registered Courses");
        MenuBean menuBean6 = new MenuBean(5, "Completed Courses");
        MenuBean menuBean2 = new MenuBean(6, "FAQs");
        MenuBean menuBean3 = new MenuBean(7, "Contacts");

        MenuBean menuBean7 = new MenuBean(8, "Leaderboard");

        MenuBean menuBean8 = new MenuBean(9, "Upcoming Sessions");
        MenuBean menuBean9 = new MenuBean(10, "Completed Sessions");


        if (type.equalsIgnoreCase("Trainee")) {
            data.add(menuBean);
            data.add(menuBean1);
            data.add(menuBean4);
            data.add(menuBean5);
            data.add(menuBean6);
            data.add(menuBean2);
            data.add(menuBean3);
            data.add(menuBean7);

            searchData.add(menuBean);
            searchData.add(menuBean1);
            searchData.add(menuBean4);
            searchData.add(menuBean5);
            searchData.add(menuBean6);
            searchData.add(menuBean2);
            searchData.add(menuBean3);
            searchData.add(menuBean7);
        } else {
            data.add(menuBean8);
            data.add(menuBean9);
            data.add(menuBean2);
            data.add(menuBean3);

            searchData.add(menuBean8);
            searchData.add(menuBean9);
            searchData.add(menuBean2);
            searchData.add(menuBean3);
        }


    }

    private void setupRecyclerView() {
        if (flag) {
            recyclerView1.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(customAdapter);
        } else {
            recyclerView1.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);

            Display display = getActivity().getWindowManager().getDefaultDisplay();
            DisplayMetrics outMetrics = new DisplayMetrics();
            display.getMetrics(outMetrics);

            float density = getResources().getDisplayMetrics().density;
            float dpWidth = outMetrics.widthPixels / density;
            int columns = Math.round(dpWidth / 150);

            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), columns);
            recyclerView1.setLayoutManager(gridLayoutManager);
            recyclerView1.setAdapter(customAdapter);
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.

        inflater.inflate(R.menu.home, menu);
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
                    if (data.get(i).getMenuItem().toLowerCase().contains(s.toLowerCase())) {
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
        int id = item.getItemId();
        if (id == R.id.action_menu_type) {

            if (flag) {
                menu.getItem(1).setIcon(getResources().getDrawable(R.drawable.display_type_icon1));
                menu.getItem(1).setTitle("View in list view");
                flag = false;
                setupRecyclerView();
            } else {
                menu.getItem(1).setIcon(getResources().getDrawable(R.drawable.display_type_icon));
                menu.getItem(1).setTitle("View in grid mode");
                flag = true;
                setupRecyclerView();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
        ArrayList<MenuBean> data;

        public CustomAdapter(ArrayList<MenuBean> data) {
            this.data = data;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View v;
            if (flag) {
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.menu_item, viewGroup, false);
            } else {
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.menu_item1, viewGroup, false);
            }
            ViewHolder viewHolder = new ViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, final int i) {
            viewHolder.title.setText(data.get(i).getMenuItem());
            setupImages(viewHolder, data.get(i).getMenuItem());

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    manageItemClick(data.get(i).getId());
                }
            });
        }

        private void manageItemClick(int i) {
            switch (i) {
                case 1:
                    Intent i1 = new Intent(getActivity(), DisplayCoursesActivity.class);
                    i1.putExtra("type", "learningCalendar");
                    startActivity(i1);
                    break;
                case 2:
                    Intent i2 = new Intent(getActivity(), LearningCategoriesActivity.class);
                    startActivity(i2);
                    break;

                case 3:
                    Intent i3 = new Intent(getActivity(), LearningAmbassadors.class);
                    startActivity(i3);
                    break;
                case 4:
                    Intent i4 = new Intent(getActivity(), RegisteredCoursesActivity.class);
                    startActivity(i4);
                    break;

                case 5:
                    Intent i5 = new Intent(getActivity(), CompletedCoursesActivity.class);
                    startActivity(i5);
                    break;


                case 6:
                    Intent i6 = new Intent(getActivity(), FaqsActivity.class);
                    startActivity(i6);
                    break;

                case 7:
                    Intent i7 = new Intent(getActivity(), ContactsActivity.class);
                    startActivity(i7);
                    break;

                case 8:
                    Intent i8 = new Intent(getActivity(), LeaderboardActivity.class);
                    startActivity(i8);
                    break;

                case 9:
                    Intent i9 = new Intent(getActivity(), LoadUpcomingSessions.class);
                    startActivity(i9);
                    break;

                case 10:
                    Intent i10 = new Intent(getActivity(), LoadCompletedSessions.class);
                    startActivity(i10);
                    break;


            }
        }

        private void setupImages(ViewHolder viewHolder, String item) {
            switch (item) {
                case "Learning Calendar":
                    viewHolder.itemView.setBackgroundColor(getResources().getColor(R.color.color1));
                    viewHolder.imageview.setImageResource(R.drawable.learning_calendar_icon);
                    break;
                case "Learning Categories":
                    viewHolder.itemView.setBackgroundColor(getResources().getColor(R.color.color2));
                    viewHolder.imageview.setImageResource(R.drawable.learning_categories);
                    break;


                case "Certifications":
                    viewHolder.itemView.setBackgroundColor(getResources().getColor(R.color.color3));
                    viewHolder.imageview.setImageResource(R.drawable.certifications_icon);
                    break;

                case "FAQs":
                    viewHolder.itemView.setBackgroundColor(getResources().getColor(R.color.color1));
                    viewHolder.imageview.setImageResource(R.drawable.faqs_icon);
                    break;

                case "Contacts":
                    viewHolder.itemView.setBackgroundColor(getResources().getColor(R.color.color2));
                    viewHolder.imageview.setImageResource(R.drawable.contacts_icon);
                    break;

                case "Learning Ambassadors":
                    viewHolder.itemView.setBackgroundColor(getResources().getColor(R.color.color3));
                    viewHolder.imageview.setImageResource(R.drawable.learning_ambassadors);
                    break;

                case "Registered Courses":
                    viewHolder.itemView.setBackgroundColor(getResources().getColor(R.color.color4));
                    viewHolder.imageview.setImageResource(R.drawable.registered_courses_icon);
                    break;
                case "Completed Courses":
                    viewHolder.itemView.setBackgroundColor(getResources().getColor(R.color.color5));
                    viewHolder.imageview.setImageResource(R.drawable.completed_courses_icon);
                    break;

                case "Leaderboard":
                    viewHolder.itemView.setBackgroundColor(getResources().getColor(R.color.color3));
                    viewHolder.imageview.setImageResource(R.drawable.trophy_icon);
                    break;

                case "Upcoming Sessions":
                    viewHolder.itemView.setBackgroundColor(getResources().getColor(R.color.color4));
                    viewHolder.imageview.setImageResource(R.drawable.learning_calendar_icon);
                    break;

                case "Completed Sessions":
                    viewHolder.itemView.setBackgroundColor(getResources().getColor(R.color.color3));
                    viewHolder.imageview.setImageResource(R.drawable.completed_courses_icon);
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
