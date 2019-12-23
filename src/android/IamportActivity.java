package kr.iamport.cordova;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.util.Log;

import org.json.JSONObject;

import kr.iamport.cordova.example.R;

public class IamportActivity extends Activity {
    WebView webview;
    IamportWebViewClient webViewClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Application application = getApplication();
        String packageName = application.getPackageName();

        Integer identifier = application.getResources().getIdentifier("iamport_activity", "layout", packageName);
        setContentView(identifier);

        webview = (WebView) findViewById(R.id.webview);
        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);

        webview.loadUrl(IamportCordova.WEBVIEW_PATH);
        webview.setWebChromeClient(new IamportWebChromeClient());

        Bundle extras = getIntent().getExtras();
        /* SET ACTION BAR */
        String title = extras.getString("title");
        setActionBar(title);

        /* SET WEBVIEW */
        String type = extras.getString("type");
        String params = extras.getString("params");
        switch (type) {
            case "nice": { // 나이스 && 실시간 계좌이체
                webViewClient = new IamportNiceWebViewClient(this, params);
                break;
            }
            case "certification": {
                webViewClient = new IamportCertificationWebViewClient(this, params);
                break;
            }
            default: { // 결제
                webViewClient = new IamportPaymentWebViewClient(this, params);
                break;
            }
        }
        webview.setWebViewClient(webViewClient);
    }

    private void setActionBar(String title) {
        ActionBar ab = getActionBar();

        if (title.equals("{}")) {
            ab.hide();
        } else {
            try {
                JSONObject titleParams = new JSONObject(title);

                String name = titleParams.getString("name");
                String color = titleParams.getString("color");

                ab.setTitle(name);
                ab.setBackgroundDrawable(new ColorDrawable(Color.parseColor(color)));
                ab.setDisplayHomeAsUpEnabled(true);

            } catch (Exception e) {

            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        webview.clearHistory();
        webview.clearCache(true);
        webview.destroy();
        webview = null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IamportCordova.REQUEST_CODE_FOR_NICE_TRANS) {
            webViewClient.bankPayPostProcess(requestCode, resultCode, data);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
                break;
            }
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}