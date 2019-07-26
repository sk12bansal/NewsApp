package com.example.newsapp.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.newsapp.BuildConfig;
import com.example.newsapp.R;
import com.example.newsapp.adapter.NewsListAdapter;
import com.example.newsapp.model.NewsDetail;
import com.example.newsapp.rest.APIClient;
import com.example.newsapp.rest.NewsApiEndPoints;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    List<NewsDetail> mNewsDetailsList = new ArrayList<>();
    final String apiKey = BuildConfig.APIKEY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar);


        Spinner dropdown = findViewById(R.id.spinner1);
        String[] items = new String[]{"in", "us", "my", "ua", "ma"};
        String countryCode = items[0];
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);

        if (isNetworkConnectionAvailable()) {
            mRecyclerView = findViewById(R.id.newsList_recycler_view);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            mAdapter = new NewsListAdapter(mNewsDetailsList, R.layout.news_list_item, this);
            mRecyclerView.setAdapter(mAdapter);
            dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int position, long id) {
                    String cc = (String) parent.getItemAtPosition(position);
                    loadNewsList(cc);
                    Log.v("item", (String) parent.getItemAtPosition(position));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // TODO Auto-generated method stub
                }
            });
            loadNewsList(countryCode);
        }
    }


    public void loadNewsList(String countryCode) {
        NewsApiEndPoints apiService = APIClient.getClient().create(NewsApiEndPoints.class);
        Call<NewsDetail> call = apiService.getTopHeadLine(countryCode, apiKey);

        call.enqueue(new Callback<NewsDetail>() {

            @Override
            public void onResponse(Call<NewsDetail> call, Response<NewsDetail> response) {
                if (response != null && response.body() != null) {
                    List<NewsDetail> list = response.body().getNewsListModel();
                    mNewsDetailsList.clear();
                    mNewsDetailsList.addAll(list);
                    mAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Call<NewsDetail> call, Throwable t) {
                // Log error here since request failed
                Log.e("News List", t.toString());
            }
        });

    }

    public boolean isNetworkConnectionAvailable() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnected();
        if (isConnected) {
            Log.d("Network", "Connected");
            return true;
        } else {
            checkNetworkConnection();
            Log.d("Network", "Not Connected");
            return false;
        }
    }


    public void checkNetworkConnection() {
        try {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();

            alertDialog.setTitle("Info");
            alertDialog.setMessage("Internet not available, Cross check your internet connectivity and try again");
            alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,"OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    finish();

                }
            });

            alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE,"Go to Settings", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getApplication().startActivity(intent);

                }
            });

            alertDialog.show();
        } catch (Exception e) {
            Log.d("MainActivity", "Show Dialog: " + e.getMessage());
        }

    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Ready to leave?")
                .setMessage("Are you sure you want to exit this application?")
                .setPositiveButton("Exit", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                })
                .setNegativeButton("Cancel", null)
                .show();
    }



}
