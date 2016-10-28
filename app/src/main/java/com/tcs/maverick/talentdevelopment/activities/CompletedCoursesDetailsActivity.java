package com.tcs.maverick.talentdevelopment.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.tcs.maverick.talentdevelopment.R;
import com.tcs.maverick.talentdevelopment.fragments.CompletedSessionsAttendeesFragment;
import com.tcs.maverick.talentdevelopment.fragments.CompletedSessionsStatisticsFragment;
import com.tcs.maverick.talentdevelopment.fragments.RegistrationsCountFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abhi on 3/28/2016.
 */
public class CompletedCoursesDetailsActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private static String courseName;
    private static String courseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.completed_sessions_details_activity);
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            courseName = bundle.getString("courseName");
            courseId = bundle.getString("courseId");
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(courseName);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewPager);

        setupViewPager();
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(CompletedCoursesDetailsActivity.this, R.color.colorAccent));
        String[] titles = new String[]{
                "Attendees",
                "Statistics",
                "Numbers",
        };

        for (int i = 0; i < titles.length; i++) {
            tabLayout.getTabAt(i).setText(titles[i]);
        }

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            int currentPosition = 0;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                ViewPagerAdapter viewPagerAdapter = (ViewPagerAdapter) viewPager.getAdapter();
                Fragment f = viewPagerAdapter.getItem(position);
                f.onResume();
                Fragment c = viewPagerAdapter.getItem(currentPosition);
                c.onPause();
                currentPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void setupViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new CompletedSessionsAttendeesFragment(), "");
        adapter.addFragment(new CompletedSessionsStatisticsFragment(), "");
        adapter.addFragment(new RegistrationsCountFragment(), "");

        viewPager.setAdapter(adapter);
    }

    public static String getCourseName() {
        return courseName;
    }

    class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }

    public static String getCourseId() {
        //Log.d("Course Id", courseId);
        return courseId;


    }


}
