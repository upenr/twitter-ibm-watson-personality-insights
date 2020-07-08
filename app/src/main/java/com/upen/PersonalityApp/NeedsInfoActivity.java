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

public class NeedsInfoActivity extends AppCompatActivity {
    ListView needsInfoView;
    ListAdapter needsInfoAdapter;
    int positionClicked;
    String title;
    ArrayList<HashMap<String, String>> needsInfoList;
    String[] needsTitleArray = {"Challenge", "Closeness", "Curiosity", "Excitement",
            "Harmony", "Ideal", "Liberty", "Love", "Practicality", "Self-expression", "Stability", "Structure"};
    String[] needsInfoArray = {"A desire to achieve, succeed, compete, or pursue experiences that test one's abilities.",
            "A need to nurture or be nurtured; a feeling of belonging."
            , "A need to pursue experiences that foster learning, exploration, and growth.", "A need to pursue experiences or lead a lifestyle that arouses enthusiasm and eagerness.",
            "A need to appreciate or please other people, their viewpoints, and feelings.", "A desire to satisfy one's idea of perfection in a lifestyle or experience, oftentimes seen as pursuing a sense of community."
            , "A need to escape, a desire for new experiences, new things.", "Social contact, whether one-to-one or one-to-many.",
            "A desire for getting the job done, skill, and efficiency.", "A desire to discover and assert one's identity.",
            "A need for the sensible, tried and tested, with a good track record and a known history.", "A need for organization, planning, and things that have a clear purpose."};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_needs_info);
        try {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            if (getIntent() != null && getIntent().hasExtra("position"))
                positionClicked = getIntent().getExtras().getInt("position");
            if (getIntent() != null && getIntent().hasExtra("title"))
                title = getIntent().getExtras().getString("title");
            getSupportActionBar().setTitle(title);

            needsInfoView = (ListView) findViewById(R.id.needsInfo);
            needsInfoList = new ArrayList<>();

            AdView mAdView = (AdView) findViewById(R.id.adView);
            AdRequest request = new AdRequest.Builder()
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)        // All emulators
                    .addTestDevice("2A21F56C0A302C5DCBBC4E7E6BFBFF93")
                    .build();
            mAdView.loadAd(request);

            for (int i = 0; i < needsTitleArray.length; i++) {
                HashMap<String, String> needsInfoHashMap = new HashMap<>();
                needsInfoHashMap.put("TA", needsTitleArray[i]);
                needsInfoHashMap.put("IA", needsInfoArray[i]);
                // Log.e("NIMH",needsInfoHashMap.toString());
                needsInfoList.add(needsInfoHashMap);
                // Log.e("NIL",needsInfoList.toString());

            }
            needsInfoAdapter = new SimpleAdapter(getBaseContext(), needsInfoList,
                    R.layout.needsinfocontent, new String[]{"TA", "IA"},
                    new int[]{R.id.ta, R.id.ia});
            needsInfoView.setVerticalScrollBarEnabled(true);
            needsInfoView.setAdapter(needsInfoAdapter);
            if (positionClicked == 1) {
                needsInfoView.setSelection(needsInfoAdapter.getCount() - 11);
            } else if (positionClicked == 2) {
                needsInfoView.setSelection(needsInfoAdapter.getCount() - 10);
            } else if (positionClicked == 3) {
                needsInfoView.setSelection(needsInfoAdapter.getCount() - 9);
            } else if (positionClicked == 4) {
                needsInfoView.setSelection(needsInfoAdapter.getCount() - 8);
            } else if (positionClicked == 5) {
                needsInfoView.setSelection(needsInfoAdapter.getCount() - 7);
            } else if (positionClicked == 6) {
                needsInfoView.setSelection(needsInfoAdapter.getCount() - 6);
            } else if (positionClicked == 7) {
                needsInfoView.setSelection(needsInfoAdapter.getCount() - 5);
            } else if (positionClicked == 8) {
                needsInfoView.setSelection(needsInfoAdapter.getCount() - 4);
            } else if (positionClicked == 9) {
                needsInfoView.setSelection(needsInfoAdapter.getCount() - 3);
            } else if (positionClicked == 10) {
                needsInfoView.setSelection(needsInfoAdapter.getCount() - 2);
            } else if (positionClicked == 11) {
                needsInfoView.setSelection(needsInfoAdapter.getCount() - 1);
            } else {
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
