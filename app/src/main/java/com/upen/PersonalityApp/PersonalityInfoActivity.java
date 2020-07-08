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

public class PersonalityInfoActivity extends AppCompatActivity {
    ListView personaInfoView;
    ListAdapter personaInfoAdapter;
    int positionClicked;
    String title;
    ArrayList<HashMap<String, String>> personaInfoList;
    String[] personaTitleArray = {"Agreeableness", "Altruism", "Cooperation", "Modesty", "Straightforwardness", "Sympathy", "Trust",
            "Conscientiousness", "Achievement striving", "Cautiousness", "Dutifulness ", "Orderliness", "Self-discipline", "Self-efficacy",
            "Emotional Range", "Fiery", "Immoderation", "Melancholy", "Prone to worry", "Self-consciousness", "Sensitivity to stress",
            "Introversion/Extroversion", "Activity level", "Assertiveness", "Cheerfulness", "Excitement-seeking", "Gregariousness", "Warmth",
            "Openness", "Adventurousness", "Artistic interests", "Authority-challenging", "Emotionality", "Imagination", "Intellect"};
    String[] personaInfoArray = {
            "A person's tendency to be compassionate and cooperative toward others.\n" + "Higher Value means they get along with others. They have a more optimistic view of human nature. Lower Value means they value Self-interests over others. They are more skeptical of others' motives."
            , "Find that helping others is genuinely rewarding, that doing things for others is a form of self-fulfillment rather than self-sacrifice."
            , "Dislike confrontation. They are perfectly willing to compromise or to deny their own needs to get along with others."
            , "Are unassuming, rather self-effacing, and humble. However, they do not necessarily lack self-confidence or self-esteem."
            , "See no need for pretense or manipulation when dealing with others and are therefore candid, frank, and genuine."
            , "Are compassionate."
            , "Assume that most people are fundamentally fair, honest, and have good intentions. They take people at face value and are willing to forgive and forget."
            , "Higher value means they are more self-disciplined, dutiful, or aiming for achievement against measures or outside expectations. Lower value means they are more likely to prefer the spontaneous over the planned."
            , "Try hard to achieve excellence. Their drive to be recognized as successful keeps them on track as they work hard to accomplish their goals."
            , "Are disposed to think through possibilities carefully before acting."
            , "Have a strong sense of duty and obligation."
            , "Are well-organized, tidy, and neat."
            , "Have the self-discipline, or \"will-power,\" to persist at difficult or unpleasant tasks until they are completed."
            , "Are confident in their ability to accomplish things."
            , "This app cannot diagnose a mental illness. Higher value could mean they are more likely to have negative emotions or get upset. It could mean they are going through a tough time. Lower value could mean they are more calm and less likely to get upset. It does not mean they are positive, or happy people."
            , "Tendency to experience – but not necessarily express – anger or frustration."
            , "Tendency to act on cravings and urges rather over resisting them or delaying gratification."
            , "Normal tendency to experience feelings of guilt, sadness, hopelessness, or loneliness. This demo cannot diagnose a mental illness."
            , "Tendency to dwell on difficulty or troubles; easily experience unease or concern."
            , "Concern with rejection, embarrassment; shyness."
            , "Difficulty in coping with stress or pressure in difficult situations."
            , "Higher value could mean they are more engaged with the external world. Likes high group visibility, talking, and asserting themselves. Lower value could mean they need less stimulation and are more independent. It does not mean they are shy, un-friendly, or antisocial."
            , "Lead fast-paced and busy lives. They do things and move about quickly, energetically, and vigorously, and they are involved in many activities."
            , "Like to take charge and direct the activities of others. They tend to be leaders in groups."
            , "Experience a range of positive feelings, including happiness, enthusiasm, optimism, and joy."
            , "Are easily bored without high levels of stimulation."
            , "Find the company of others pleasantly stimulating and rewarding. They enjoy the excitement of crowds."
            , "Genuinely like other people and openly demonstrate positive feelings toward others."
            , "Openness to experience. Higher value could mean they are intellectually curious, emotionally-aware, sensitive to beauty and willing to try new things. Lower value could mean they prefer the plain, straightforward, and obvious over the complex, ambiguous, and subtle."
            , "Are eager to try new activities and experience different things. They find familiarity and routine boring."
            , "Love beauty, both in art and in nature. They become easily involved and absorbed in artistic and natural events. With intellect, this facet is one of the two most important, central aspects of this characteristic."
            , "Have a readiness to challenge authority, convention, and traditional values."
            , "Have good access to and awareness of their own feelings."
            , "View the real world as often too plain and ordinary. They use fantasy not as an escape but as a way of creating for themselves a richer and more interesting inner-world."
            , "Are intellectually curious and tend to think in symbols and abstractions. With artistic interests, this facet is one of the two most important, central aspects of this characteristic."
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personality_info);
        try {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null)
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            if (getIntent() != null && getIntent().hasExtra("position"))
                positionClicked = getIntent().getExtras().getInt("position");
            if (getIntent() != null && getIntent().hasExtra("title"))
                title = getIntent().getExtras().getString("title");
            getSupportActionBar().setTitle("Find: " + title);

            personaInfoView = (ListView) findViewById(R.id.personaInfo);
            personaInfoList = new ArrayList<>();

            for (int i = 0; i < personaTitleArray.length; i++) {
                HashMap<String, String> personaInfoHashMap = new HashMap<>();
                personaInfoHashMap.put("TA", personaTitleArray[i]);
                personaInfoHashMap.put("IA", personaInfoArray[i]);
                ////Log.e("NIMH",personaInfoHashMap.toString());
                personaInfoList.add(personaInfoHashMap);
                ////Log.e("NIL",personaInfoList.toString());

            }
            personaInfoAdapter = new SimpleAdapter(getBaseContext(), personaInfoList,
                    R.layout.needsinfocontent, new String[]{"TA", "IA"},
                    new int[]{R.id.ta, R.id.ia});
            personaInfoView.setVerticalScrollBarEnabled(true);
            personaInfoView.setAdapter(personaInfoAdapter);
            //Log.e("positionClicked", String.valueOf(positionClicked));
            if (positionClicked == 0) {
                personaInfoView.setSelection(personaInfoAdapter.getCount() - 7);
            } else if (positionClicked == 1) {
                personaInfoView.setSelection(personaInfoAdapter.getCount() - 28);
            } else if (positionClicked == 2) {
                personaInfoView.setSelection(personaInfoAdapter.getCount() - 14);
            } else if (positionClicked == 3) {
                personaInfoView.setSelection(personaInfoAdapter.getCount() - 34);
            } else if (positionClicked == 4) {
                personaInfoView.setSelection(personaInfoAdapter.getCount() - 21);
            } else {
                personaInfoView.setSelection(personaInfoAdapter.getCount() - 34);
            }
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
