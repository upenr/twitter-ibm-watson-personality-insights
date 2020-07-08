package com.upen.personalityapp;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.HashMap;

public class ValuesInfoActivity extends AppCompatActivity {
    ListView valuesInfoView;
    ListAdapter valuesInfoAdapter;
    String title;
    ArrayList<HashMap<String, String>> valuesInfoList;
    String[] valuesTitleArray = {"Values", "Conservation", "Openness to change", "Hedonism", "Self-enhancement", "Self-transcendence"};
    String[] valuesInfoArray = {"Values describe motivating factors that influence the author's decision-making. The following table describes the five values that the service infers."
            , "Emphasize self-restriction, order, and resistance to change."
            , "Emphasize independent action, thought, and feeling, as well as a readiness for new experiences."
            , "Seek pleasure and sensuous gratification for themselves."
            , "Seek personal success for themselves."
            , "Show concern for the welfare and interests of others."
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_values_info);
        try {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            if (getIntent() != null && getIntent().hasExtra("title"))
                title = getIntent().getExtras().getString("title");
            getSupportActionBar().setTitle(title);
            valuesInfoView = (ListView) findViewById(R.id.valuesInfo);
            valuesInfoList = new ArrayList<>();

            AdView mAdView = (AdView) findViewById(R.id.adView);
            AdRequest request = new AdRequest.Builder()
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)        // All emulators
                    .addTestDevice("2A21F56C0A302C5DCBBC4E7E6BFBFF93")
                    .build();
            mAdView.loadAd(request);

            for (int i = 0; i < valuesTitleArray.length; i++) {
                HashMap<String, String> valuesInfoHashMap = new HashMap<>();
                valuesInfoHashMap.put("TA", valuesTitleArray[i]);
                valuesInfoHashMap.put("IA", valuesInfoArray[i]);
                ////Log.e("NIMH", valuesInfoHashMap.toString());
                valuesInfoList.add(valuesInfoHashMap);
                ////Log.e("NIL", valuesInfoList.toString());

                valuesInfoAdapter = new SimpleAdapter(getBaseContext(), valuesInfoList,
                        R.layout.needsinfocontent, new String[]{"TA", "IA"},
                        new int[]{R.id.ta, R.id.ia});
                valuesInfoView.setVerticalScrollBarEnabled(true);
                valuesInfoView.setAdapter(valuesInfoAdapter);
            }

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    String shareBody = "Analyze Twitter Personalities on Android with #IBMWatson AI using #Persona: https://play.google.com/store/apps/details?id=com.upen.personalityapp";
                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareBody);
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                    startActivity(Intent.createChooser(sharingIntent, "Share via"));
                }
            });

        } catch (NullPointerException e) {
            Log.e( "Exception: ", e.getMessage());
            Toast.makeText(getApplicationContext(), "There may be an issue. Please reload the app.", Toast.LENGTH_LONG).show();
        } catch (final Exception e) {
            Log.e( "Exception: ", e.getMessage());
            Toast.makeText(getApplicationContext(), "There may be an issue. Please reload the app.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return (super.onOptionsItemSelected(item));
    }

}
