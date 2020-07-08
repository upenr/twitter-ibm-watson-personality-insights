package com.upen.personalityapp;

import android.content.Intent;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.upen.personalityapp.MainActivity1.consumptionPreferences;
import static com.upen.personalityapp.MainActivity1.needs;
import static com.upen.personalityapp.MainActivity1.personality;
import static com.upen.personalityapp.MainActivity1.processedLanguage;
import static com.upen.personalityapp.MainActivity1.userID;
import static com.upen.personalityapp.MainActivity1.values1;
import static com.upen.personalityapp.MainActivity1.warnings;
import static com.upen.personalityapp.MainActivity1.wordCount;

public class ResultsActivity extends AppCompatActivity {

    /**
     * The {@link PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    public static ArrayList<HashMap<String, Object>> personalityList, personality1List, consprefList, conspref1List;
    public static int consPrefSubLength[] = new int[8];
    public static int personality1SubLength[] = new int[5];
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private InterstitialAd interstitialAd;
    private static InterstitialAd sinterstitialAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        try {
            //responseView.setText("");

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            // Create the adapter that will return a fragment for each of the three
            // primary sections of the activity.
            MobileAds.initialize(this, "ca-app-pub-6716761053101068~2545122152");
            //Create ads here
            crateInterstitial();

            mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

            // Set up the ViewPager with the sections adapter.
            mViewPager = (ViewPager) findViewById(R.id.container);
            mViewPager.setAdapter(mSectionsPagerAdapter);

            TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
            tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
            tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

            AdView mAdView = (AdView) findViewById(R.id.adView);
            AdRequest request = new AdRequest.Builder()
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)        // All emulators
                    .addTestDevice("2A21F56C0A302C5DCBBC4E7E6BFBFF93")
                    .build();
            mAdView.loadAd(request);

            mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
            tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    String shareBody = "I just analyzed @" + userID + "'s profile with #IBMWatson AI using #Persona. Check it on this app: https://play.google.com/store/apps/details?id=com.upen.personalityapp";
                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareBody);
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                    startActivity(Intent.createChooser(sharingIntent, "Share via"));
                }
            });

            /*Toast.makeText(getApplicationContext(), "We will try to display an ad in 15 seconds.", Toast.LENGTH_LONG).show();*/
            /*final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    showInterstitial();
                }
            }, 15000);*/

        } catch (NullPointerException e) {
            Log.e("Exception: ", e.getMessage());
            Toast.makeText(getApplicationContext(), "There may be an issue. Please reload the app.", Toast.LENGTH_LONG).show();
        } catch (final Exception e) {
            Log.e("Exception: ", e.getMessage());
            Toast.makeText(getApplicationContext(), "There may be an issue. Please reload the app.", Toast.LENGTH_LONG).show();
        }
    }

    public void crateInterstitial() {

        interstitialAd = new InterstitialAd(getApplicationContext());
        interstitialAd.setAdUnitId("ca-app-pub-6716761053101068/7375557254");
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // not call show interstitial ad from here
            }

            @Override
            public void onAdClosed() {
                loadInterstitial();
            }
        });
        loadInterstitial();
    }

    public void loadInterstitial() {
        AdRequest interstitialRequest = new AdRequest.Builder()
                .addTestDevice("2A21F56C0A302C5DCBBC4E7E6BFBFF93")
                .build();
        interstitialAd.loadAd(interstitialRequest);
    }

    public void showInterstitial() {
        if (interstitialAd.isLoaded()) {
            interstitialAd.show();
            //Log.e("Showing", "Ad");
        } else {
            loadInterstitial();
            //Log.e("Loading", "Ad");
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_results, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //onBackPressed();
                if (interstitialAd.isLoaded()) {
                    interstitialAd.show();
                    interstitialAd.setAdListener(new AdListener() {
                        @Override
                        public void onAdClosed() {
                            Intent mainAct = new Intent(ResultsActivity.this, MainActivity1.class);
                            startActivity(mainAct);
                            finish();
                        }
                    });
                } else {
                    Intent mainAct = new Intent(ResultsActivity.this, MainActivity1.class);
                    startActivity(mainAct);
                }
                return true;
            case R.id.action_tweets:
                Intent tweetsAct = new Intent(this, ViewTweetsActivity.class);
                startActivity(tweetsAct);
                return true;
        }
        return (super.onOptionsItemSelected(item));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if (interstitialAd.isLoaded()) {
                interstitialAd.show();
                interstitialAd.setAdListener(new AdListener() {
                    @Override
                    public void onAdClosed() {
                        Intent mainAct = new Intent(ResultsActivity.this, MainActivity1.class);
                        startActivity(mainAct);
                        //finish();
                    }
                });
            } else {
                Intent mainAct = new Intent(ResultsActivity.this, MainActivity1.class);
                startActivity(mainAct);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            //showInterstitial();

            View rootView = inflater.inflate(R.layout.fragment_results, container, false);
            ListAdapter needsAdapter;

            ArrayList<HashMap<String, Object>> needsList;
            ListAdapter personalityAdapter, personality1Adapter, consprefAdapter;
            final ListView listView, listView2;

            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            TextView percentileTextView = (TextView) rootView.findViewById(R.id.percentile1);
            textView.setMovementMethod(new ScrollingMovementMethod());

            listView = (ListView) rootView.findViewById(R.id.listview);

            if (getArguments().getInt(ARG_SECTION_NUMBER) == 1) {
                ////Log.e("Personality", personality);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent i = new Intent(getContext(), PersonalityDescActivity.class);
                        Object o = listView.getItemAtPosition(position);
                        i.putExtra("position", position);
                        i.putExtra("title", o.toString().split(",")[0].split("=")[1]);
                        startActivity(i);
                    }
                });
                if (personality != null) {
                    try {

                        JSONObject jsonObj = new JSONObject(personality);
                        // Getting JSON Array node
                        JSONArray personalityArray = jsonObj.getJSONArray("personality");
                        personalityList = new ArrayList<>();
                        personality1List = new ArrayList<>();

                        HashMap<Object, Object> merged;
                        //Log.e("PERSONALITY LENGTH", String.valueOf(personalityArray.length()));

                        for (int i = 0; i < personalityArray.length(); i++) {
                            JSONObject c = personalityArray.getJSONObject(i);

                            String name, name1 = "test", percentile, percentile1 = "10";
                            name = c.getString("name");
                            percentile = c.getString("percentile");

                            if (c.getString("children") != null) {
                                String childrenString = "{\"children\":" + c.getJSONArray("children") + "}";
                                JSONObject jsonObj1 = new JSONObject(childrenString);
                                JSONArray personality1Array = jsonObj1.getJSONArray("children");

                                personality1SubLength[i] = personality1Array.length();
                                //Log.e("PERSONALITY 1 LENGTH", String.valueOf(personality1Array.length()));
                                for (int j = 0; j < personality1Array.length(); j++) {
                                    JSONObject c1 = personality1Array.getJSONObject(j);
                                    name1 = c1.getString("name");
                                    percentile1 = c1.getString("percentile");
                                    HashMap<String, Object> personality1 = new HashMap<>();
                                    personality1.put(name1, returnRounded(percentile1));
                             /*  personality1.put("percentile"+j+"Rounded", returnRounded(percentile1));*/
                                    personality1List.add(personality1);
                                }

                            }
                            HashMap<String, Object> personality = new HashMap<>();
                            merged = new HashMap<>();
                            // adding each child node to HashMap key => value
                            personality.put("name", name);
                            personality.put("percentileRounded", returnRounded(percentile));
                            personalityList.add(personality);
                        }
                        ////Log.e("Merged List", mergeList(personalityList,personality1List).toString());
                        personalityAdapter = new SimpleAdapter(getContext(), personalityList,
                                R.layout.content, new String[]{
                                "name",
                                "percentileRounded"
                        },
                                new int[]{
                                        R.id.name1, R.id.percentile1
                                });

                        listView.setVerticalScrollBarEnabled(true);
                        listView.setAdapter(personalityAdapter);


                    } catch (final JSONException e) {
                        Log.e("Json parsing error: ", e.getMessage());
                        textView.setText("Parsing error.");
                    } catch (final Exception e) {
                        Log.e("Exception: ", e.getMessage());
                        Toast.makeText(getContext(), "There may be an issue. Please reload the app.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    textView.setText("Watson AI has nothing on this.");
                }

            } else if (getArguments().getInt(ARG_SECTION_NUMBER) == 2) {
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
      /* Object o = prestListView.getItemAtPosition(position);
       prestationEco str = (prestationEco)o; //As you are using Default String Adapter
       Toast.makeText(getBaseContext(),str.getTitle(),Toast.LENGTH_SHORT).show();*/
                        Intent i = new Intent(getContext(), NeedsInfoActivity.class);
                        Object o = listView.getItemAtPosition(position);
                        i.putExtra("position", position);
                        i.putExtra("title", o.toString().split(",")[0].split("=")[1]);
                        startActivity(i);
                    }
                });
                if (needs != null) {
                    try {
                        JSONObject jsonObj = new JSONObject(needs);
                        // Getting JSON Array node
                        JSONArray needsArray = jsonObj.getJSONArray("needs");
                        needsList = new ArrayList<>();
                        //Log.e("NEEDS LENGTH", String.valueOf(needsArray.length()));

                        // looping through All Needs
                        for (int i = 0; i < needsArray.length(); i++) {
                            JSONObject c = needsArray.getJSONObject(i);
                            String name = c.getString("name");
                            String percentile = c.getString("percentile");
                            HashMap<String, Object> needs1 = new HashMap<>();
                            // adding each child node to HashMap key => value
                            needs1.put("name", name);
                            needs1.put("percentileRounded", returnRounded(percentile));
                            // adding Needs to Needs list
                            needsList.add(needs1);
                            needsAdapter = new SimpleAdapter(getContext(), needsList,
                                    R.layout.content, new String[]{
                                    "name",
                                    "percentileRounded"
                            },
                                    new int[]{
                                            R.id.name1, R.id.percentile1
                                    });

                            listView.setVerticalScrollBarEnabled(true);
                            listView.setAdapter(needsAdapter);
                        }
                    } catch (final JSONException e) {
                        Log.e("Json parsing error: ", e.getMessage());
                        textView.setText("Parsing error.");
                    } catch (final Exception e) {
                        Log.e("Exception: ", e.getMessage());
                        Toast.makeText(getContext(), "There may be an issue. Please reload the app.", Toast.LENGTH_LONG).show();
                    }

                } else {
                    textView.setText("Watson AI has nothing on this.");
                }
            } else if (getArguments().getInt(ARG_SECTION_NUMBER) == 3) {
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
      /* Object o = prestListView.getItemAtPosition(position);
      prestationEco str = (prestationEco)o; //As you are using Default String Adapter
      Toast.makeText(getBaseContext(),str.getTitle(),Toast.LENGTH_SHORT).show();*/
                        Intent i = new Intent(getContext(), ValuesInfoActivity.class);
                        Object o = listView.getItemAtPosition(position);
                        i.putExtra("position", position);
                        i.putExtra("title", o.toString().split(",")[0].split("=")[1]);
                        startActivity(i);
                    }
                });
                if (values1 != null) {
                    try {
                        JSONObject jsonObj = new JSONObject(values1);
                        // Getting JSON Array node
                        JSONArray needsArray = jsonObj.getJSONArray("values");
                        needsList = new ArrayList<>();
                        //Log.e("VALUES LENGTH", String.valueOf(needsArray.length()));

                        for (int i = 0; i < needsArray.length(); i++) {
                            JSONObject c = needsArray.getJSONObject(i);
                            String name = c.getString("name");
                            String percentile = c.getString("percentile");

                            HashMap<String, Object> needs1 = new HashMap<>();
                            // adding each child node to HashMap key => value
                            needs1.put("name", name);
                            needs1.put("percentileRounded", returnRounded(percentile));
                            // adding Needs to Needs list
                            needsList.add(needs1);

                            needsAdapter = new SimpleAdapter(getContext(), needsList,
                                    R.layout.content, new String[]{
                                    "name",
                                    "percentileRounded"
                            },
                                    new int[]{
                                            R.id.name1, R.id.percentile1
                                    });

                            listView.setVerticalScrollBarEnabled(true);
                            listView.setAdapter(needsAdapter);
                        }
                    } catch (final JSONException e) {
                        Log.e("Json parsing error: ", e.getMessage());
                        textView.setText("Parsing error.");
                    } catch (final Exception e) {
                        Log.e("Exception: ", e.getMessage());
                        Toast.makeText(getContext(), "There may be an issue. Please reload the app.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    textView.setText("Watson AI has nothing on this.");
                }
            } else if (getArguments().getInt(ARG_SECTION_NUMBER) == 4) {

                if (consumptionPreferences != null) {
                    try {
                        JSONObject jsonObj = new JSONObject(consumptionPreferences);
                        // Getting JSON Array node
                        JSONArray consprefArray = jsonObj.getJSONArray("conspref");
                        consprefList = new ArrayList<>();
                        conspref1List = new ArrayList<>();

                        HashMap<Object, Object> merged;
                        //Log.e("conspref LENGTH", String.valueOf(consprefArray.length()));

                        for (int i = 0; i < consprefArray.length(); i++) {
                            JSONObject c = consprefArray.getJSONObject(i);

                            String name, name1 = "test", percentile, percentile1 = "10";
                            name = c.getString("name");
                            //percentile = c.getString("name"); //Quick fix

                            if (c.getString("consumption_preferences") != null) {
                                String childrenString = "{\"consumption_preferences\":" + c.getJSONArray("consumption_preferences") + "}";
                                JSONObject jsonObj1 = new JSONObject(childrenString);
                                JSONArray conspref1Array = jsonObj1.getJSONArray("consumption_preferences");

                                consPrefSubLength[i] = conspref1Array.length();
                                //Log.e("conspref 1 LENGTH", String.valueOf(conspref1Array.length()));
                                for (int j = 0; j < conspref1Array.length(); j++) {
                                    JSONObject c1 = conspref1Array.getJSONObject(j);
                                    name1 = c1.getString("name");
                                    percentile1 = c1.getString("score");
                                    HashMap<String, Object> conspref1 = new HashMap<>();
                                    conspref1.put(name1, percentile1);
                                /*  conspref1.put("percentile"+j+"Rounded", returnRounded(percentile1));*/
                                    conspref1List.add(conspref1);
                                }
                                ////Log.e("conspref 1", conspref1List.toString());

                            }
                            HashMap<String, Object> conspref = new HashMap<>();
                            merged = new HashMap<>();
                            // adding each child node to HashMap key => value
                            conspref.put("name", name);
                            //conspref.put("score", percentile);
                            consprefList.add(conspref);
                        }

                        ////Log.e("Merged List", mergeList(consprefList,conspref1List).toString());
                        consprefAdapter = new SimpleAdapter(getContext(), consprefList,
                                R.layout.consprefcontent, new String[]{
                                "name"
                        },
                                new int[]{
                                        R.id.name1
                                });

                        listView.setVerticalScrollBarEnabled(true);
                        listView.setAdapter(consprefAdapter);


                    } catch (final JSONException e) {
                        Log.e("Json parsing error: ", e.getMessage());
                        textView.setText("Parsing error.");
                    } catch (final Exception e) {
                        Log.e("Exception: ", e.getMessage());
                        Toast.makeText(getContext(), "There may be an issue. Please reload the app.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    textView.setText("Watson AI has nothing on this.");
                }
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
      /* Object o = prestListView.getItemAtPosition(position);
       prestationEco str = (prestationEco)o; //As you are using Default String Adapter
       Toast.makeText(getBaseContext(),str.getTitle(),Toast.LENGTH_SHORT).show();*/
                        try {
                            Intent i = new Intent(getContext(), consprefInfoActivity.class);
                            Object o = listView.getItemAtPosition(position);
                            i.putExtra("position", position);
                            i.putExtra("title", o.toString().split(",")[0].split("=")[1].replaceAll("[{}]", ""));
                            startActivity(i);
                        } catch (final Exception e) {
                            Log.e("Exception: ", e.getMessage());
                            Toast.makeText(getContext(), "There may be an issue. Please reload the app.", Toast.LENGTH_LONG).show();
                        }

                    }
                });
            } else if (getArguments().getInt(ARG_SECTION_NUMBER) == 5) {
                try {
                    if (wordCount != null) {
                        textView.setVisibility(View.VISIBLE);
                        textView.setText("Number of words analyzed: " + wordCount + "." + "\nThe higher the number of words, the better the insights are." +
                                "\nA word count of 6000 or more is recommended.\nView the analyzed tweets from the menu option.");
                    } else {
                        textView.setText("Watson AI has nothing on this. Try increasing the number of words in your input.");
                    }
                } catch (final Exception e) {
                    Log.e("Exception: ", e.getMessage());
                    Toast.makeText(getContext(), "There may be an issue. Please reload the app.", Toast.LENGTH_LONG).show();
                }

            } else if (getArguments().getInt(ARG_SECTION_NUMBER) == 6) {
                try {
                    Toast.makeText(getContext(), "An ad may load here.", Toast.LENGTH_LONG).show();
                    sinterstitialAd = new InterstitialAd(getContext());
                    sinterstitialAd.setAdUnitId("ca-app-pub-6716761053101068/7375557254");
                    final AdRequest sinterstitialRequest = new AdRequest.Builder()
                            .addTestDevice("2A21F56C0A302C5DCBBC4E7E6BFBFF93")
                            .build();
                    sinterstitialAd.loadAd(sinterstitialRequest);
                    //Log.e("sinterstitial Ad", "Loading");

                    sinterstitialAd.setAdListener(new AdListener() {
                        @Override
                        public void onAdLoaded() {
                            if (sinterstitialAd.isLoaded()) {
                                sinterstitialAd.show();
                                //Log.e("Showing", "New Ad");
                            }
                        }

                        @Override
                        public void onAdClosed() {
                        }
                    });
                    if (processedLanguage != null) {
                        textView.setVisibility(View.VISIBLE);
                        if (processedLanguage.equalsIgnoreCase("en")) {
                            processedLanguage = "English";
                            textView.setText(processedLanguage);
                        } else if (processedLanguage.equalsIgnoreCase("es")) {
                            processedLanguage = "Spanish";
                            textView.setText(processedLanguage);
                        } else if (processedLanguage.equalsIgnoreCase("ar")) {
                            processedLanguage = "Arabic";
                            textView.setText(processedLanguage);
                        } else if (processedLanguage.equalsIgnoreCase("ja")) {
                            processedLanguage = "Japanese";
                            textView.setText(processedLanguage);
                        } else if (processedLanguage.equalsIgnoreCase("ko")) {
                            processedLanguage = "Korean";
                            textView.setText(processedLanguage);
                        } else{
                            textView.setVisibility(View.VISIBLE);
                            textView.setText(processedLanguage);
                        }
                    } else {
                        textView.setVisibility(View.VISIBLE);
                        textView.setText("Watson AI has nothing on this.");
                    }
                } catch (final Exception e) {
                    Log.e("Exception: ", e.getMessage());
                    Toast.makeText(getContext(), "There may be an issue. Please reload the app.", Toast.LENGTH_LONG).show();
                }
            } else if (getArguments().getInt(ARG_SECTION_NUMBER) == 7) {
                try {
                    if (warnings != null && warnings != "[]") {
                        textView.setVisibility(View.VISIBLE);
                        textView.setText(warnings);
                    } else {
                        textView.setVisibility(View.VISIBLE);
                        textView.setText("There were no warning messages.");
                    }
                } catch (final Exception e) {
                    Log.e("Exception: ", e.getMessage());
                    Toast.makeText(getContext(), "There may be an issue. Please reload the app.", Toast.LENGTH_LONG).show();
                }
            } else {

                textView.setVisibility(View.VISIBLE);
                textView.setText("Watson AI has nothing on this. Contact the app developer to understand the issue.");
            }
            return rootView;


        }

        private long returnRounded(String percentile1) {
            Double newpercentile1 = Double.parseDouble(percentile1) * 100;
            Long percentile1Rounded = Math.round(newpercentile1 * 100);
            percentile1Rounded = percentile1Rounded / 100;
            return percentile1Rounded;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 7 total pages.
            return 7;
        }
    }
}