package com.tcs.maverick.talentdevelopment.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tcs.maverick.talentdevelopment.R;

/**
 * Created by abhi on 3/1/2016.
 */
public class AboutAppFragment extends Fragment {
    private TextView textView1, textView2;
    private String messageText = "<body>\n" +
            "\t\t\n" +
            "<strong>Talent Development</strong> app version 1.0 enables user to check schedules for the <strong>Talent Development Program.</strong></p>\n" +
            "\t\t<br><br>\n" +
            "Users can tap on Learning Calender to view schedule for various courses and also get registered for the courses of their interest.Users can find their registered courses in the&nbsp;<strong>Registered Courses&nbsp;</strong>option.</p>\n" +
            "\t\t<br><br>\n" +
            "Users can also mark attendance on the day of course by selecting the course from&nbsp;<strong>Registered Courses&nbsp;</strong>option and then selecting the&nbsp;<strong>Mark Attendance&nbsp;</strong>option.</p>\n" +
            "\t\t<br><br>\n" +
            "After marking the attendance for the course user can also give feedback to the respective courses, After successful submission of the feedback the course can be found in <strong>Completed Courses&nbsp;</strong>option.</p>\n" +
            "\t</body>";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.app_info_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textView1 = (TextView) view.findViewById(R.id.messageText);
        textView2 = (TextView) view.findViewById(R.id.copyrightText);

        textView1.setText(Html.fromHtml(messageText));
        textView2.setText(getResources().getString(R.string.copyright_text));
    }
}
