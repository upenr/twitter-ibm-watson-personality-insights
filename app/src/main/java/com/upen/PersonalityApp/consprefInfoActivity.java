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
import java.util.Map;

import static com.upen.personalityapp.ResultsActivity.consPrefSubLength;
import static com.upen.personalityapp.ResultsActivity.conspref1List;

public class consprefInfoActivity extends AppCompatActivity {
    ListView consprefInfoView;
    ListAdapter consprefInfoAdapter;
    int positionClicked;
    String title;
    ArrayList<HashMap<String, String>> consprefInfoList;
    ArrayList<String> list1, list2;
    public String[] consprefTitleArray, consprefInfoArray;
    public int[] cumulSubLength = new int[8];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conspref_info);
        try {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null)
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            if (getIntent() != null && getIntent().hasExtra("position"))
                positionClicked = getIntent().getExtras().getInt("position");
            if (getIntent() != null && getIntent().hasExtra("title"))
                title = getIntent().getExtras().getString("title");
            getSupportActionBar().setTitle(title);
            consprefTitleArray = new String[50];
            consprefInfoArray = new String[50];

            cumulSubLength = makeCumul(consPrefSubLength);
            //Log.e("cumulSubLength", cumulSubLength.toString());

            for (int i = 0; i < conspref1List.size(); i++) {
                Map<String, Object> entry0 = conspref1List.get(i);
                for (String key : entry0.keySet()) {
                    consprefTitleArray[i] = key;
                    if (entry0.get(key).toString().equals("0")) {
                        consprefInfoArray[i] = "Unlikely";
                    } else if (entry0.get(key).toString().equals("1")) {
                        consprefInfoArray[i] = "Likely";
                    } else {
                        consprefInfoArray[i] = "Neutral";
                    }
                }
            }

            list1 = new ArrayList<String>();
            for (String s : consprefTitleArray)
                if (s != null)
                    list1.add(s);
            consprefTitleArray = list1.toArray(new String[list1.size()]);
            list2 = new ArrayList<String>();
            for (String s : consprefInfoArray)
                if (s != null)
                    list2.add(s);
            consprefInfoArray = list2.toArray(new String[list2.size()]);
            //Log.e("consprefInfo size", String.valueOf(consprefInfoArray.length));

            consprefInfoView = (ListView) findViewById(R.id.consprefInfo);
            consprefInfoList = new ArrayList<>();

            for (int i = 0; i < consprefTitleArray.length; i++) {
                HashMap<String, String> consprefInfoHashMap = new HashMap<>();
                consprefInfoHashMap.put("TA", consprefTitleArray[i]);
                consprefInfoHashMap.put("IA", consprefInfoArray[i]);
                ////Log.e("NIMH",consprefInfoHashMap.toString());
                consprefInfoList.add(consprefInfoHashMap);
                ////Log.e("NIL",consprefInfoList.toString());
            }

            if (positionClicked == 0) {
                consprefInfoAdapter = new SimpleAdapter(getBaseContext(), consprefInfoList.subList(0, cumulSubLength[0]),
                        R.layout.needsinfocontent, new String[]{"TA", "IA"},
                        new int[]{R.id.ta, R.id.ia});
            } else if (positionClicked == 1) {
                consprefInfoAdapter = new SimpleAdapter(getBaseContext(), consprefInfoList.subList(cumulSubLength[0], cumulSubLength[1]),
                        R.layout.needsinfocontent, new String[]{"TA", "IA"},
                        new int[]{R.id.ta, R.id.ia});
            } else if (positionClicked == 2) {
                consprefInfoAdapter = new SimpleAdapter(getBaseContext(), consprefInfoList.subList(cumulSubLength[1], cumulSubLength[2]),
                        R.layout.needsinfocontent, new String[]{"TA", "IA"},
                        new int[]{R.id.ta, R.id.ia});
            } else if (positionClicked == 3) {
                consprefInfoAdapter = new SimpleAdapter(getBaseContext(), consprefInfoList.subList(cumulSubLength[2], cumulSubLength[3]),
                        R.layout.needsinfocontent, new String[]{"TA", "IA"},
                        new int[]{R.id.ta, R.id.ia});
            } else if (positionClicked == 4) {
                consprefInfoAdapter = new SimpleAdapter(getBaseContext(), consprefInfoList.subList(cumulSubLength[3], cumulSubLength[4]),
                        R.layout.needsinfocontent, new String[]{"TA", "IA"},
                        new int[]{R.id.ta, R.id.ia});
            } else if (positionClicked == 5) {
                consprefInfoAdapter = new SimpleAdapter(getBaseContext(), consprefInfoList.subList(cumulSubLength[4], cumulSubLength[5]),
                        R.layout.needsinfocontent, new String[]{"TA", "IA"},
                        new int[]{R.id.ta, R.id.ia});
            } else if (positionClicked == 6) {
                consprefInfoAdapter = new SimpleAdapter(getBaseContext(), consprefInfoList.subList(cumulSubLength[5], cumulSubLength[6]),
                        R.layout.needsinfocontent, new String[]{"TA", "IA"},
                        new int[]{R.id.ta, R.id.ia});
            } else if (positionClicked == 7) {
                consprefInfoAdapter = new SimpleAdapter(getBaseContext(), consprefInfoList.subList(cumulSubLength[6], cumulSubLength[7]),
                        R.layout.needsinfocontent, new String[]{"TA", "IA"},
                        new int[]{R.id.ta, R.id.ia});
            } else {
                consprefInfoAdapter = new SimpleAdapter(getBaseContext(), consprefInfoList,
                        R.layout.needsinfocontent, new String[]{"TA", "IA"},
                        new int[]{R.id.ta, R.id.ia});
            }
            consprefInfoView.setVerticalScrollBarEnabled(true);
            consprefInfoView.setAdapter(consprefInfoAdapter);

            AdView mAdView = (AdView) findViewById(R.id.adView);
            AdRequest request = new AdRequest.Builder()
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)        // All emulators
                    .addTestDevice("2A21F56C0A302C5DCBBC4E7E6BFBFF93")
                    .build();
            mAdView.loadAd(request);
            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    String shareBody = "Analyze Twitter celebrities on Android with #IBMWatson AI using #Persona: https://play.google.com/store/apps/details?id=com.upen.consprefapp";
                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareBody);
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                    startActivity(Intent.createChooser(sharingIntent, "Share via"));
                }
            });

        } catch (NullPointerException e) {
            Log.e("Exception: ", e.getMessage());
            Toast.makeText(getApplicationContext(), "There may be an issue. Please reload the app.", Toast.LENGTH_LONG).show();
        } catch (final Exception e) {
            Log.e("Exception: ", e.getMessage());
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

    private int[] makeCumul(int[] in) {
        int[] out = new int[in.length];
        int total = 0;
        for (int i = 0; i < in.length; i++) {
            total += in[i];
            out[i] = total;
        }
        return out;
    }
}
