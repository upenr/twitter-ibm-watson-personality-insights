package com.upen.personalityapp;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

public class HowWorks extends AppCompatActivity {
    LinearLayout layout2;
    private WebView myWebView;
    private InterstitialAd interstitialAd;

    @SuppressLint({"JavascriptInterface", "NewApi"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_works);
        try {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            crateInterstitial();

            AdView mAdView = (AdView) findViewById(R.id.adView);
            AdRequest request = new AdRequest.Builder()
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)        // All emulators
                    .addTestDevice("2A21F56C0A302C5DCBBC4E7E6BFBFF93")
                    .build();
            mAdView.loadAd(request);

            Toast.makeText(getApplicationContext(), "Loading.", Toast.LENGTH_LONG).show();

            myWebView = (WebView) findViewById(R.id.webView);
            //myWebView.setWebChromeClient(new WebChromeClient());
            myWebView.loadUrl("https://cloud.ibm.com/docs/personality-insights?topic=personality-insights-science");
            myWebView.setWebViewClient(new WebViewClient() {
                public void onLoadResource(WebView view, String url) {
                    super.onLoadResource(view, url);
                }
            });

            myWebView.setScrollY(400);

            ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo mData = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (!mWifi.isConnected() && !mData.isConnected()) {
                Log.e("2", "LOADED");
                Toast.makeText(getApplicationContext(), "Please enable WiFi or data from the device's settings.", Toast.LENGTH_LONG).show();
            } else {
                Log.e("3", "LOADED");
                //enable Javascript
                myWebView.getSettings().setJavaScriptEnabled(true);

                //loads the WebView completely zoomed out
                myWebView.getSettings().setLoadWithOverviewMode(true);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    WebView.setWebContentsDebuggingEnabled(true);
                }

                //true makes the Webview have a normal viewport such as a normal desktop browser
                //when false the webview will have a viewport constrained to it's own dimensions
                myWebView.getSettings().setUseWideViewPort(true);

                //override the web client to open all links in the same webview
                // myWebView.setWebViewClient(new MyWebViewClient());
                myWebView.setWebChromeClient(new MyWebChromeClient());

                //enable popups
                myWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
                //myWebView.getSettings().setSupportMultipleWindows(true);
                myWebView.getSettings().setSupportZoom(true);
                myWebView.getSettings().setBuiltInZoomControls(true);
                myWebView.getSettings().setUseWideViewPort(true);
                myWebView.getSettings().setDisplayZoomControls(false);
                //Injects the supplied Java object into this WebView. The object is injected into the
                //JavaScript context of the main frame, using the supplied name. This allows the
                //Java object's public methods to be accessed from JavaScript.
                //myWebView.addJavascriptInterface(new JavaScriptInterface(this), "Android");
                //load the home page URL

            }
        } catch (final Exception e) {
            Log.e("Exception: ", e.getMessage());
            Toast.makeText(getApplicationContext(), "There may be an issue. Please reload the app.", Toast.LENGTH_LONG).show();
        }
    }

    private class MyWebViewClient extends WebViewClient {
        @JavascriptInterface
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return false;
        }

        @SuppressWarnings("deprecation")
        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            //view.clearView();
            myWebView.loadUrl("about:blank");
            Toast.makeText(getApplicationContext(), "Please turn on Wifi or data from the device's settings", Toast.LENGTH_LONG).show();
        }
    }

    private class MyWebChromeClient extends WebChromeClient {
        @JavascriptInterface
        //display alert message in Web View
        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {

            new AlertDialog.Builder(view.getContext())
                    .setMessage(message).setCancelable(true).show();
            result.confirm();
            return true;
        }
    }

    public class JavaScriptInterface {

        Context mContext;

        // Instantiate the interface and set the context
        JavaScriptInterface(Context c) {
            mContext = c;
        }

        //using Javascript to call the finish activity
        public void closeMyActivity() {
            finish();
        }
    }

    //Web view has record of all pages visited so you can go back and forth
    //just override the back button to go back in history if there is page
    //available for display
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if ((keyCode == KeyEvent.KEYCODE_BACK) && myWebView.canGoBack()) {
            myWebView.goBack();
            return true;
        } else {
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
        Log.e("Loading another", "Ad");
    }

    private void showInterstitial() {
        if (interstitialAd.isLoaded()) {
            interstitialAd.show();
            Log.e("Showing", "Ad");
        } else {
            loadInterstitial();
            Log.e("Loading", "Ad");
        }
    }
}