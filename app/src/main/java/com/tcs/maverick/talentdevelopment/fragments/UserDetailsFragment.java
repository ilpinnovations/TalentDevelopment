package com.tcs.maverick.talentdevelopment.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.tcs.maverick.talentdevelopment.R;
import com.tcs.maverick.talentdevelopment.activities.LoginActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by abhi on 3/1/2016.
 */
public class UserDetailsFragment extends Fragment {
    private TextView empId;
    private TextView empName;
    private TextView empEmail;
    private TextView empRole;
    private CircleImageView userImg;
    private Button mButton;

    private String employeeName, employeeId, employeeEmail, employeeImage, employeeRole;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.user_details_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        empId = (TextView) view.findViewById(R.id.employeeId);
        empName = (TextView) view.findViewById(R.id.employeeName);
        empEmail = (TextView) view.findViewById(R.id.employeeEmail);
        empRole = (TextView) view.findViewById(R.id.employeeRole);
        userImg = (CircleImageView) view.findViewById(R.id.userImage);
        mButton = (Button) view.findViewById(R.id.logoutButton);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Talent Development", Context.MODE_PRIVATE);
        employeeId = sharedPreferences.getString("emp_id", "");
        employeeName = sharedPreferences.getString("emp_name", "");
        employeeEmail = sharedPreferences.getString("emp_email", "");
        employeeImage = sharedPreferences.getString("emp_image", "");
        employeeRole = sharedPreferences.getString("emp_role", "");

        empId.setText(employeeId);
        empName.setText(employeeName);
        empEmail.setText(employeeEmail);
        empRole.setText(employeeRole);
        loadImageFromStorage(employeeImage, userImg);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Logout")
                        .setMessage("Are you sure you want to logout?")
                                //.setIcon(getResources().getDrawable(android.R.drawable.ic_dialog_alert))
                        .setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Talent Development", getActivity().MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();

                                        editor.putString("emp_id", "");
                                        editor.putString("emp_name", "");
                                        editor.putString("emp_email", "");
                                        editor.putString("emp_image", "");
                                        editor.commit();

                                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                                        startActivity(intent);
                                        getActivity().finish();

                                    }
                                })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
            }
        });
    }

    private void loadImageFromStorage(String path, CircleImageView imageView) {

        try {
            File f = new File(path, "profile.jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            imageView.setImageBitmap(b);
        } catch (FileNotFoundException e) {
            imageView.setImageResource(R.drawable.user_image);
        }

    }
}
