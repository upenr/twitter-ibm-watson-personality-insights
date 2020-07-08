package com.upen.personalityapp;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.upen.personalityapp.ResultsActivity.personality1List;
import static com.upen.personalityapp.ResultsActivity.personality1SubLength;

public class PersonalityDescActivity extends AppCompatActivity {
    ListView personaInfoView;
    ListAdapter personaInfoAdapter;
    int positionClicked;
    public static String title1 = "";
    ArrayList<HashMap<String, String>> personaInfoList;
    ArrayList<String> list1, list2;
    public String[] personaTitleArray, personaInfoArray;
    public int[] cumulSubLength = new int[8];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personality_desc);
        try {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null)
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            if (getIntent() != null && getIntent().hasExtra("position"))
                positionClicked = getIntent().getExtras().getInt("position");
            if (getIntent() != null && getIntent().hasExtra("title")) {
                title1 = getIntent().getExtras().getString("title");
            }
            getSupportActionBar().setTitle(title1);
            personaTitleArray = new String[33];
            personaInfoArray = new String[33];
            personaInfoView = (ListView) findViewById(R.id.personalityDesc);

            cumulSubLength = makeCumul(personality1SubLength);
            Log.e("cumulSubLength", cumulSubLength.toString());

            for (int i = 0; i < personality1List.size(); i++) {
                Map<String, Object> entry0 = personality1List.get(i);
                for (String key : entry0.keySet()) {
                    personaTitleArray[i] = key;
                    personaInfoArray[i] = entry0.get(key).toString();
                }
            }

            list1 = new ArrayList<String>();
            for (String s : personaTitleArray)
                if (s != null)
                    list1.add(s);
            personaTitleArray = list1.toArray(new String[list1.size()]);
            list2 = new ArrayList<String>();
            for (String s : personaInfoArray)
                if (s != null)
                    list2.add(s);
            personaInfoArray = list2.toArray(new String[list2.size()]);
            Log.e("personaInfo size", String.valueOf(personaInfoArray.length));

            personaInfoList = new ArrayList<>();

            for (int i = 0; i < personaTitleArray.length; i++) {
                HashMap<String, String> personaInfoHashMap = new HashMap<>();
                personaInfoHashMap.put("TA", personaTitleArray[i]);
                personaInfoHashMap.put("IA", personaInfoArray[i]);
                ////Log.e("NIMH",personaInfoHashMap.toString());
                personaInfoList.add(personaInfoHashMap);
                ////Log.e("NIL",personaInfoList.toString());
            }

            if (positionClicked == 0) {
                personaInfoAdapter = new SimpleAdapter(getBaseContext(), personaInfoList.subList(0, cumulSubLength[0]),
                        R.layout.content, new String[]{"TA", "IA"},
                        new int[]{R.id.name1, R.id.percentile1});
                ////Log.e("List contents",  personaInfoList.subList(0,cumulSubLength[0]).toString());
            } else if (positionClicked == 1) {
                personaInfoAdapter = new SimpleAdapter(getBaseContext(), personaInfoList.subList(cumulSubLength[0], cumulSubLength[1]),
                        R.layout.content, new String[]{"TA", "IA"},
                        new int[]{R.id.name1, R.id.percentile1});
            } else if (positionClicked == 2) {
                personaInfoAdapter = new SimpleAdapter(getBaseContext(), personaInfoList.subList(cumulSubLength[1], cumulSubLength[2]),
                        R.layout.content, new String[]{"TA", "IA"},
                        new int[]{R.id.name1, R.id.percentile1});
            } else if (positionClicked == 3) {
                personaInfoAdapter = new SimpleAdapter(getBaseContext(), personaInfoList.subList(cumulSubLength[2], cumulSubLength[3]),
                        R.layout.content, new String[]{"TA", "IA"},
                        new int[]{R.id.name1, R.id.percentile1});
            } else if (positionClicked == 4) {
                personaInfoAdapter = new SimpleAdapter(getBaseContext(), personaInfoList.subList(cumulSubLength[3], cumulSubLength[4]),
                        R.layout.content, new String[]{"TA", "IA"},
                        new int[]{R.id.name1, R.id.percentile1});
            } else {
                personaInfoAdapter = new SimpleAdapter(getBaseContext(), personaInfoList,
                        R.layout.content, new String[]{"TA", "IA"},
                        new int[]{R.id.name1, R.id.percentile1});
            }
            personaInfoView.setVerticalScrollBarEnabled(true);
            personaInfoView.setAdapter(personaInfoAdapter);

            personaInfoView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               /* Object o = prestListView.getItemAtPosition(position);
                prestationEco str = (prestationEco)o; //As you are using Default String Adapter
                Toast.makeText(getBaseContext(),str.getTitle(),Toast.LENGTH_SHORT).show();*/
                    Intent i = new Intent(getApplicationContext(), PersonalityInfoActivity.class);
                    i.putExtra("position", positionClicked);
                    Object o = personaInfoView.getItemAtPosition(position);
                    i.putExtra("title", o.toString().split(",")[0].split("=")[1]);
                    startActivity(i);
                }
            });
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
                    String shareBody = "Analyze Twitter personalities on Android with #IBMWatson AI using #Persona: https://play.google.com/store/apps/details?id=com.upen.personaapp";
                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareBody);
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                    startActivity(Intent.createChooser(sharingIntent, "Share via"));
                }
            });

        } catch (NullPointerException e) {
            Log.e( "Exception: ", e.getMessage());
            Toast.makeText(getApplicationContext(), "There may be an issue. Please reload this page or the app.", Toast.LENGTH_LONG).show();
        } catch (final Exception e) {
            Log.e( "Exception: ", e.getMessage());
            Toast.makeText(getApplicationContext(), "There may be an issue. Please reload this page or the app.", Toast.LENGTH_LONG).show();
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
