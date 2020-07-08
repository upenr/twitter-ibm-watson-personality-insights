package com.upen.personalityapp;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import static com.upen.personalityapp.MainActivity1.tweetList;

public class ViewTweetsActivity extends AppCompatActivity {
    ListView viewTweets;
    ListAdapter viewTweetsAdapter;
    String title;
    private InterstitialAd interstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_tweets);
        try {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            crateInterstitial();
            viewTweets = (ListView) findViewById(R.id.viewTweets);

            AdView mAdView = (AdView) findViewById(R.id.adView);
            AdRequest request = new AdRequest.Builder()
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)        // All emulators
                    .addTestDevice("2A21F56C0A302C5DCBBC4E7E6BFBFF93")
                    .build();
            mAdView.loadAd(request);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    R.layout.needsinfocontent, R.id.ta, tweetList);
            viewTweets.setVerticalScrollBarEnabled(true);
            viewTweets.setAdapter(adapter);

            viewTweets.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                    // this is textview which you used in listview row
                    TextView textView = (TextView) view.findViewById(R.id.ta);
                    ClipboardManager cm = (ClipboardManager) getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("Tweet", textView.getText());
                    cm.setPrimaryClip(clip);
                    Toast.makeText(getApplicationContext(), "Copied.", Toast.LENGTH_SHORT).show();
                    return true;
                }
            });
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (final Exception e) {
            Log.e( "Exception: ", e.getMessage());
            Toast.makeText(getApplicationContext(), "There may be an issue. Please reload the app.", Toast.LENGTH_LONG).show();
        }
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
                            onBackPressed();
                        }
                    });
                } else {
                    onBackPressed();
                }
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
                        onBackPressed();
                        //finish();
                    }
                });
            } else {
                onBackPressed();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void crateInterstitial() {

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

    private void loadInterstitial() {
        AdRequest interstitialRequest = new AdRequest.Builder()
                .addTestDevice("2A21F56C0A302C5DCBBC4E7E6BFBFF93")
                .build();
        interstitialAd.loadAd(interstitialRequest);
        //Log.e("Loading another", "Ad");
    }

    private void showInterstitial() {
        if (interstitialAd.isLoaded()) {
            interstitialAd.show();
            //Log.e("Showing", "Ad");
        } else {
            loadInterstitial();
            //Log.e("Loading", "Ad");
        }
    }


}
