package com.upen.personalityapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.personality_insights.v3.PersonalityInsights;
import com.ibm.watson.personality_insights.v3.model.Profile;
import com.ibm.watson.personality_insights.v3.model.ProfileOptions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;


public class MainActivity1 extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    EditText plainText;
    Button queryButton;
    ProgressBar progressBar;
    String netStatus;
    TextView netStatusView, progressUpdateView;
    public static String textToAnalyze;
    public static ResponseList<twitter4j.Status> list, list1, list2, list3, list4;
    public static ArrayList<String> tweetList;
    public static String behavior;
    public static String consumptionPreferences;
    public static String needs;
    public static String personality;
    public static String processedLanguage;
    public static String values1;
    public static String warnings;
    public static String wordCount;
    public static String wordCountMessage;
    public static String userID, languageSelected;
    public IamAuthenticator authenticator;
    public PersonalityInsights service;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        try {
            netStatus = chkStatus();
            plainText = (EditText) findViewById(R.id.plainText);
            //plainText.setText("__upen");
            progressBar = (ProgressBar) findViewById(R.id.progressBar);
            netStatusView = (TextView) findViewById(R.id.conntextview);
            progressUpdateView = (TextView) findViewById(R.id.progressUpdate);
            netStatusView.setTextColor(Color.parseColor("#009688"));
            netStatusView.setText(netStatus);
            progressBar.setVisibility(View.GONE);
            queryButton = (Button) findViewById(R.id.button1);
            if (netStatus.equalsIgnoreCase("You are not connected to the Internet.")) {
                queryButton.setEnabled(false);
            } else {
                queryButton.setEnabled(true);
                progressUpdateView.setTextColor(Color.parseColor("#009688"));
                progressUpdateView.setText("Ready.");

            }
            languageSelected = "en";
            Spinner dropdown = findViewById(R.id.spinner);
            String[] items = new String[]{"English", "Spanish", "Arabic", "Japanese", "Korean"};
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
            dropdown.setAdapter(adapter);
            dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int position, long id) {
                    //Log.v("item", (String) parent.getItemAtPosition(position));
                    languageSelected = (String) parent.getItemAtPosition(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    languageSelected = "en";
                }
            });

            final InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

            queryButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                    if (languageSelected.equalsIgnoreCase("Spanish")) {
                        languageSelected = "es";
                    } else if (languageSelected.equalsIgnoreCase("Japanese")) {
                        languageSelected = "ja";
                    } else if (languageSelected.equalsIgnoreCase("Korean")) {
                        languageSelected = "ko";
                    } else if (languageSelected.equalsIgnoreCase("Arabic")) {
                        languageSelected = "ar";
                    } else {
                        languageSelected = "en";
                    }

                    userID = plainText.getText().toString();

                    String regexp = "^[a-zA-Z0-9_]{1,15}$"; //your regexp here

                    if (userID.matches(regexp)) {
                        //It's valid
                        new MainActivity1.RetrieveTweetTask(getApplicationContext()).execute();
                        Log.e("One", "Done");
                        progressBar.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(getApplicationContext(), "This User ID may not be valid. Enter the Twitter handle without the '@' symbol.", Toast.LENGTH_LONG).show();
                    }
                }
            });

        } catch (NullPointerException e) {
            Log.e("Exception: ", e.getMessage());
            Toast.makeText(getApplicationContext(), "There may be an issue. Please reload the app.", Toast.LENGTH_LONG).show();
        } catch (final Exception e) {
            Log.e("Exception: ", e.getMessage());
            Toast.makeText(getApplicationContext(), "There may be an issue. Please reload the app.", Toast.LENGTH_LONG).show();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_share) {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = "Did you know #IBMWatson AI can analyze Twitter profiles? Get the app (#Persona): https://play.google.com/store/apps/details?id=com.upen.personalityapp";
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareBody);
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));
            return true;
        } else if (id == R.id.action_info) {
            Intent donAct = new Intent(this, com.upen.personalityapp.InformationActivity.class);
            startActivity(donAct);
            return true;
        } else if (id == R.id.refresh) {
            Intent intent = getIntent();
            overridePendingTransition(0, 0);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            finish();
            overridePendingTransition(0, 0);
            startActivity(intent);
        } else if (id == R.id.exit) {
            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
            homeIntent.addCategory(Intent.CATEGORY_HOME);
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.home) {
        } else if (id == R.id.results) {
            if (personality != null) {
                Intent resultsAct = new Intent(this, com.upen.personalityapp.ResultsActivity.class);
                startActivity(resultsAct);
            } else {
                Toast.makeText(getApplicationContext(), "Previous result unavailable. Click on Analyze to view results.", Toast.LENGTH_LONG).show();
            }
        } else if (id == R.id.tweets) {
            if (personality != null) {
                //View tweets from textToAnalyze
                Intent tweetsAct = new Intent(this, com.upen.personalityapp.ViewTweetsActivity.class);
                startActivity(tweetsAct);
            } else {
                Toast.makeText(getApplicationContext(), "Analyzed tweets unavailable. Click on Analyze to view results. Then view analyzed tweets.", Toast.LENGTH_LONG).show();
            }

        } else if (id == R.id.nav_how) {
            Intent howAct = new Intent(this, com.upen.personalityapp.HowWorks.class);
            startActivity(howAct);

        } else if (id == R.id.nav_share) {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = "Did you know #IBMWatson AI can analyze Twitter profiles? Get the app (#Persona): https://play.google.com/store/apps/details?id=com.upen.personalityapp";
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareBody);
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    class RetrieveTweetTask extends AsyncTask<Void, Void, String> {
        Context context;
        final int SUCCESS = 0;
        final int FAILURE = SUCCESS + 1;

        private RetrieveTweetTask(Context context) {
            this.context = context.getApplicationContext();
        }

        protected void onPreExecute() {
            try {
                progressBar.setVisibility(View.VISIBLE);
                progressUpdateView.setTextColor(Color.parseColor("#009688"));
                progressUpdateView.setText("Reading the latest 1000 Tweets if available.");

            } catch (final Exception e) {
                Log.e("Exception: ", e.getMessage());
                Toast.makeText(getApplicationContext(), "There may be an issue. Please reload the app.", Toast.LENGTH_LONG).show();
            }
        }

        protected String doInBackground(Void... urls) {
            String textToFile = "";
            try {
                ConfigurationBuilder cb = new ConfigurationBuilder().setTweetModeExtended(true);
                cb.setOAuthAuthenticationURL("https://api.twitter.com/oauth2/token");
                cb.setOAuthAccessTokenURL(" https://api.twitter.com/oauth/access_token");
                cb.setOAuthAuthorizationURL("https://api.twitter.com/oauth/authorize");
                cb.setOAuthRequestTokenURL("https://api.twitter.com/oauth/request_token");
                cb.setRestBaseURL("https://api.twitter.com/1.1/");
                cb.setOAuthConsumerKey(YOUR_KEY);
                cb.setOAuthConsumerSecret(YOUR_SECRET);
                cb.setOAuthAccessToken(YOUR_ACCESS_TOKEN);
                cb.setOAuthAccessTokenSecret(YOUR_ACCESS_TOKEN_SECRET);

                Twitter twitter = new TwitterFactory(cb.build()).getInstance();
                tweetList = new ArrayList<>();

                // The factory instance is re-useable and thread safe.
                list = twitter.getUserTimeline(userID, new Paging(1, 200));
                list1 = twitter.getUserTimeline(userID, new Paging(2, 200));
                list2 = twitter.getUserTimeline(userID, new Paging(3, 200));
                list3 = twitter.getUserTimeline(userID, new Paging(4, 200));
                list4 = twitter.getUserTimeline(userID, new Paging(5, 200));
                User user = twitter.showUser(userID);

                /*Log.e("Twitter list length", String.valueOf(list.size()));
                Log.e("Twitter list 1 length", String.valueOf(list1.size()));
                Log.e("Twitter list 2 length", String.valueOf(list2.size()));
                Log.e("Twitter list 2 length", String.valueOf(list3.size()));
                Log.e("Twitter Rate Limit?", user.getRateLimitStatus().toString());*/

                for (twitter4j.Status each : list) {
                    /*System.out.println("Sent by: @" + each.getUser().getScreenName() + " - " + each.getUser().getName() + "\n"
                            + each.getText() + "\n");*/
                    writeStringAsFile(getApplicationContext(), each.getText() + "\n", "config.txt");
                    /*System.out.println("1."+each.getText() + "\n");*/
                    tweetList.add(each.getText());
                }
                for (twitter4j.Status each : list1) {
                    writeStringAsFile(getApplicationContext(), each.getText() + "\n", "config.txt");
                    tweetList.add(each.getText());
                }

                for (twitter4j.Status each : list2) {
                    writeStringAsFile(getApplicationContext(), each.getText() + "\n", "config.txt");
                    tweetList.add(each.getText());
                }
                for (twitter4j.Status each : list3) {
                    writeStringAsFile(getApplicationContext(), each.getText() + "\n", "config.txt");
                    tweetList.add(each.getText());
                }
                for (twitter4j.Status each : list4) {
                    writeStringAsFile(getApplicationContext(), each.getText() + "\n", "config.txt");
                    tweetList.add(each.getText());
                }
                //String text1 = readFileAsString(getApplicationContext(), "config.txt");
                //System.out.println(text1);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return "Done";
        }

        protected void onPostExecute(String response) {
            try {
                textToAnalyze = readFileAsString(getApplicationContext(), "config.txt");
                new MainActivity1.RetrieveFeedTask(getApplicationContext()).execute();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "There may be an issue. Please reload the app.", Toast.LENGTH_LONG).show();
            }
        }
    }

    class RetrieveFeedTask extends AsyncTask<Void, Void, String> {
        Context context;

        private RetrieveFeedTask(Context context) {
            this.context = context.getApplicationContext();
        }

        private Exception exception;

        protected void onPreExecute() {
            try {
                progressBar.setVisibility(View.VISIBLE);
                progressUpdateView.setTextColor(Color.parseColor("#009688"));
                progressUpdateView.setText("Contacting Watson AI.");

            } catch (final Exception e) {
                Log.e("Exception: ", e.getMessage());
                Toast.makeText(getApplicationContext(), "There may be an issue. Please reload the app.", Toast.LENGTH_LONG).show();
            }
        }

        protected String doInBackground(Void... urls) {
            try {
                String text = textToAnalyze;
                int numOfWords = countWords(text);
                //Log.e("numOfWords", String.valueOf(numOfWords));
                if (numOfWords < 200) {
                    //Log.e("personality", "null");
                    return null;
                } else {

                    authenticator = new IamAuthenticator(YOUR_KEY_1);
                    //PersonalityInsights personalityInsights = new PersonalityInsights("2017-10-13", authenticator);

                    service = new PersonalityInsights("2017-10-13", authenticator);
                    service.setServiceUrl("https://api.us-south.personality-insights.watson.cloud.ibm.com/instances/b6ccc311-635d-4f19-ae1d-7f6ba9312b29");
                    
                    //ContentItem cItem = new ContentItem().content(text).created(new Date());
                    //ProfileOptions options = new ProfileOptions().contentItems(Arrays.asList(cItem));
                    ProfileOptions options = new ProfileOptions.Builder()
                            .text(text).consumptionPreferences(true).contentLanguage(languageSelected)
                            .rawScores(false).build();
                    //Profile profile = service.profile(options).execute();
                    Profile profile = service.profile(options).execute().getResult();

                    //String endpoint1=service.getEndPoint().toString();
                    if (profile.getBehavior() != null) {
                        behavior = profile.getBehavior().toString();
                    }
                    if (profile.getConsumptionPreferences() != null)
                        consumptionPreferences = "{\"conspref\":" + profile.getConsumptionPreferences().toString() + "}";
                    if (profile.getNeeds() != null)
                        needs = "{\"needs\":" + profile.getNeeds().toString() + "}";
                    if (profile.getPersonality() != null)
                        personality = "{\"personality\":" + profile.getPersonality().toString() + "}";
                    if (profile.getProcessedLanguage() != null)
                        processedLanguage = profile.getProcessedLanguage();
                    if (profile.getValues() != null)
                        values1 = "{\"values\":" + profile.getValues().toString() + "}";
                    if (profile.getWarnings() != null) warnings = profile.getWarnings().toString();
                    if (profile.getWordCount() != null)
                        wordCount = profile.getWordCount().toString();
                    if (profile.getWordCountMessage() != null)
                        wordCountMessage = profile.getWordCountMessage();
                }

            } catch (RuntimeException e) {
                Log.e("RuntimeException", e.getMessage(), e);
                return null;
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                //Toast.makeText(getApplicationContext(),"There seems to be an issue connecting with the Watson AI.", Toast.LENGTH_LONG).show();
                return null;
            }
            return personality;
        }

        protected void onPostExecute(String response) {
            try {
                if (response == null) {
                    Toast.makeText(getApplicationContext(), "There may be an issue with the input provided. An input of at least 6000 words is ideal. Minimum: 200.", Toast.LENGTH_LONG).show();
                    progressUpdateView.setTextColor(Color.parseColor("#009688"));
                    progressUpdateView.setText("Done.");
                    progressUpdateView.setText("");
                    progressBar.setVisibility(View.GONE);
                } else {
                    progressUpdateView.setTextColor(Color.parseColor("#009688"));
                    progressUpdateView.setText("Done.");
                    progressUpdateView.setText("");
                    progressBar.setVisibility(View.GONE);
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(getApplicationContext().openFileOutput("config.txt", Context.MODE_PRIVATE));
                    outputStreamWriter.write("Done.");
                    outputStreamWriter.close();

                    Intent resAct = new Intent(MainActivity1.this, com.upen.personalityapp.ResultsActivity.class);
                    startActivity(resAct);
                    //context.startActivity(new Intent(context, ResultsActivity.class));
                }
            } catch (final Exception e) {
                Log.e("Exception: ", e.getMessage());
                Toast.makeText(getApplicationContext(), "There may be an issue. Please reload the app.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private String chkStatus() {
        String status = "";
        try {
            final ConnectivityManager connMgr = (ConnectivityManager)
                    this.getSystemService(Context.CONNECTIVITY_SERVICE);
            final android.net.NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            final android.net.NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (wifi.isConnectedOrConnecting()) {
                status = "You are connected with a WiFi network.";
            } else if (mobile.isConnectedOrConnecting()) {
                status = "You are connected with mobile data";
            } else {
                status = "You are not connected to the Internet.";
            }
        } catch (final Exception e) {
            Log.e("Exception: ", e.getMessage());
            Toast.makeText(getApplicationContext(), "There may be an issue. Please reload the app.", Toast.LENGTH_LONG).show();
        }
        return status;
    }

    public static void writeStringAsFile(Context context, final String fileContents, String fileName) {
        try {
            FileWriter out = new FileWriter(new File(context.getFilesDir(), fileName), true);
            out.write(fileContents);
            out.close();
        } catch (IOException e) {
            Log.e("Writing error", e.getMessage());
        }
    }

    public static String readFileAsString(Context context, String fileName) {

        StringBuilder stringBuilder = new StringBuilder();
        String line;
        BufferedReader in = null;

        try {
            in = new BufferedReader(new FileReader(new File(context.getFilesDir(), fileName)));
            while ((line = in.readLine()) != null) {
                stringBuilder.append(line);
            }

        } catch (FileNotFoundException e) {
            Log.e("FileNotFoundException", e.getMessage());
        } catch (IOException e) {
            Log.e("IOException", e.getMessage());
        }
        return stringBuilder.toString();
    }

    private static int countWords(String s) {

        int wordCount = 0;

        boolean word = false;
        int endOfLine = s.length() - 1;

        for (int i = 0; i < s.length(); i++) {
            // if the char is a letter, word = true.
            if (Character.isLetter(s.charAt(i)) && i != endOfLine) {
                word = true;
                // if char isn't a letter and there have been letters before,
                // counter goes up.
            } else if (!Character.isLetter(s.charAt(i)) && word) {
                wordCount++;
                word = false;
                // last word of String; if it doesn't end with a non letter, it
                // wouldn't count without this.
            } else if (Character.isLetter(s.charAt(i)) && i == endOfLine) {
                wordCount++;
            }
        }
        return wordCount;
    }
}
