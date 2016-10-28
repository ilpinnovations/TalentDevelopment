package com.tcs.maverick.talentdevelopment.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.tcs.maverick.talentdevelopment.R;
import com.tcs.maverick.talentdevelopment.beans.ContactsBean;
import com.tcs.maverick.talentdevelopment.utilities.AppConstants;
import com.tcs.maverick.talentdevelopment.utilities.DividerItemDecorator;
import com.tcs.maverick.talentdevelopment.utilities.HttpManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by abhi on 3/10/2016.
 */

public class ContactsActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private ArrayList<ContactsBean> data = new ArrayList<>();
    private ArrayList<ContactsBean> searchData = new ArrayList<>();
    private RecyclerView recyclerView;
    private CustomAdapter customAdapter;
    private Menu menu;
    private TextView messageText;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_layout1);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Contacts");
        recyclerView = (RecyclerView) findViewById(R.id.list);
        customAdapter = new CustomAdapter(searchData, ContactsActivity.this);
        messageText = (TextView) findViewById(R.id.messageText);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeLayout);
        recyclerView.setLayoutManager(new LinearLayoutManager(ContactsActivity.this));
        recyclerView.addItemDecoration(new DividerItemDecorator(ContactsActivity.this, LinearLayoutManager.VERTICAL));
        onRefresh();

        swipeRefreshLayout.setOnRefreshListener(this);


    }


    public void getData() {

        String url = AppConstants.URL + "contacts.php";

        HttpManager httpManager = new HttpManager(ContactsActivity.this, new HttpManager.ServiceResponse() {
            @Override
            public void onServiceResponse(String serviceResponse) {
                if (serviceResponse != null && HttpManager.getStatusCode() == 200) {
                    data = new ArrayList<>();
                    searchData = new ArrayList<>();
                    //Log.d("My Tag", serviceResponse);
                    try {
                        JSONObject jsonObject = new JSONObject(serviceResponse);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);
                            String empName = jsonObject1.getString("contact_name");
                            String role = jsonObject1.getString("role");
                            String email = jsonObject1.getString("contact_email");
                            String contactNumber = jsonObject1.getString("contact_number");
                            ContactsBean contactBean = new ContactsBean(empName, contactNumber, email, role);
                            data.add(contactBean);
                            searchData.add(contactBean);
                        }

                        if (data.size() > 0) {
                            customAdapter = new CustomAdapter(searchData, ContactsActivity.this);
                            recyclerView.setVisibility(View.VISIBLE);
                            recyclerView.setAdapter(customAdapter);
                            swipeRefreshLayout.setRefreshing(false);
                        } else {
                            recyclerView.setVisibility(View.GONE);
                            messageText.setVisibility(View.VISIBLE);
                            messageText.setText("No Learning Ambassadors are tagged for your IOU.");
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


    private class CustomAdapter extends RecyclerSwipeAdapter<CustomAdapter.ViewHolder> {
        ArrayList<ContactsBean> data;
        Context context;
        private ArrayList<ViewHolder> viewH = new ArrayList<>();

        public CustomAdapter(ArrayList<ContactsBean> data, Context context) {
            this.data = data;
            this.context = context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View v;
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.contact_layout, viewGroup, false);
            ViewHolder viewHolder = new ViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(CustomAdapter.ViewHolder viewHolder, final int i) {
            viewH.add(viewHolder);
            viewHolder.contactName.setText(data.get(i).getName());
            viewHolder.contactNumber.setText(data.get(i).getContactNumber());
            viewHolder.role.setText("Role : " + data.get(i).getRole());
            viewHolder.email.setText(data.get(i).getEmail());

            setupImages(viewHolder, data.get(i).getName().toUpperCase().charAt(0));

            viewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);

            // Drag From Left
            viewHolder.swipeLayout.addDrag(SwipeLayout.DragEdge.Left, viewHolder.swipeLayout.findViewById(R.id.bottom_wrapper1));


            // Handling different events when swiping
            viewHolder.swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
                @Override
                public void onClose(SwipeLayout layout) {
                    //when the SurfaceView totally cover the BottomView.
                }

                @Override
                public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {
                    //you are swiping.
                }

                @Override
                public void onStartOpen(SwipeLayout layout) {

                }

                @Override
                public void onOpen(SwipeLayout layout) {
                    //when the BottomView totally show.
                }

                @Override
                public void onStartClose(SwipeLayout layout) {

                }

                @Override
                public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
                    //when user's hand released.
                }
            });

            viewHolder.callButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:" + data.get(i).getContactNumber()));
                        startActivity(intent);
                    } catch (Exception ex) {
                        Toast.makeText(context, "Call cannot be placed, please enable permission for calling from app settings.", Toast.LENGTH_LONG).show();
                    }
                }
            });

            viewHolder.messageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent intent = new Intent(Intent.ACTION_SENDTO);
                        intent.setData(Uri.parse("smsto:" + data.get(i).getContactNumber()));
                        startActivity(intent);
                    } catch (Exception ex) {
                        Toast.makeText(context, "Message cannot be sent, please enable permission for sms from app settings.", Toast.LENGTH_LONG).show();
                    }
                }
            });
            mItemManger.bindView(viewHolder.itemView, i);
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

        @Override
        public int getSwipeLayoutResourceId(int position) {
            return R.id.swipe;
        }


        public class ViewHolder extends RecyclerView.ViewHolder {

            public TextView contactName, contactNumber, role, email;
            public ImageView imageview, imageView1;
            public SwipeLayout swipeLayout;
            public ImageButton callButton, messageButton;

            public ViewHolder(View itemView) {
                super(itemView);
                swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);
                contactName = (TextView) itemView.findViewById(R.id.contactName);
                contactNumber = (TextView) itemView.findViewById(R.id.contactNumber);
                imageview = (ImageView) itemView.findViewById(R.id.userImage);
                callButton = (ImageButton) itemView.findViewById(R.id.callButton);
                messageButton = (ImageButton) itemView.findViewById(R.id.messageButton);
                email = (TextView) itemView.findViewById(R.id.email);
                role = (TextView) itemView.findViewById(R.id.role);

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
                    if (data.get(i).getName().toLowerCase().contains(s.toLowerCase())) {
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



