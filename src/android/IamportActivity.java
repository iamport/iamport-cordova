package kr.iamport.cordova;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;

import org.json.JSONObject;

import kr.iamport.cordova.example.R;

public class IamportActivity extends Activity {
    Application application;
    String packageName;
    Resources resources;
    WebView webview;
    IamportWebViewClient webViewClient;
    String titleOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        application = getApplication();
        packageName = application.getPackageName();
        resources = application.getResources();

        Integer identifier = resources.getIdentifier("iamport_activity", "layout", packageName);
        setContentView(identifier);

        int webViewId= resources.getIdentifier("webview", "id", packageName);
        webview = (WebView) findViewById(webViewId);

        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);

        webview.loadUrl(IamportCordova.WEBVIEW_PATH);
        webview.setWebChromeClient(new IamportWebChromeClient());

        Bundle extras = getIntent().getExtras();
        /* SET ACTION BAR */
        titleOptions = extras.getString("titleOptions");
        setActionBar();

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

    private void setActionBar() {
        ActionBar ab = getActionBar();

        try {
            JSONObject titleObj = new JSONObject(titleOptions);

            String show = titleObj.getString("show");
            if (show.equals("true")) {
                String text = titleObj.getString("text");
                String textColor= titleObj.getString("textColor");
                String backgroundColor = titleObj.getString("backgroundColor");

                ab.setTitle(Html.fromHtml("<font color='" + textColor + "'>" + text + "</font>"));
                ab.setBackgroundDrawable(new ColorDrawable(Color.parseColor(backgroundColor)));

                String leftButtonColor = titleObj.getString("leftButtonColor");
                String leftButtonType = titleObj.getString("leftButtonType");
                if (!leftButtonType.equals("hide")) {
                    ab.setDisplayHomeAsUpEnabled(true);

                    int iconId = getLeftIconId(leftButtonType);
                    final Drawable closeIcon = getResources().getDrawable(iconId);
                    closeIcon.setColorFilter(Color.parseColor(leftButtonColor), PorterDuff.Mode.SRC_IN);
                    ab.setHomeAsUpIndicator(closeIcon);
                }
            } else {
                ab.hide();
            }
        } catch (Exception e) {

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
    public boolean onCreateOptionsMenu(Menu menu) {
        try {
            JSONObject titleObj = new JSONObject(titleOptions);

            String rightButtonType = titleObj.getString("rightButtonType");
            String rightButtonColor = titleObj.getString("rightButtonColor");

            int menuId = resources.getIdentifier("iamport_actionbar_actions", "menu", packageName);
            getMenuInflater().inflate(menuId, menu);
            if (!rightButtonType.equals("hide")) {
                int iconId = getRightIconId(rightButtonType);
                MenuItem meniItem = menu.findItem(iconId);
                meniItem.setVisible(true);
                meniItem.getIcon().setColorFilter(Color.parseColor(rightButtonColor), PorterDuff.Mode.SRC_IN);
            }
        } catch (Exception e) {

        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Integer itemId = item.getItemId();
        switch (itemId) {
            case android.R.id.home: {
                finish();
                break;
            }
            default: {
                if (itemId.equals(R.id.action_close)) {
                    finish();
                }
                break;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private int getLeftIconId(String buttonType) {
        if (buttonType.equals("back")) {
            return R.drawable.ic_action_back;
        }
        return R.drawable.ic_action_close;
    }

    private int getRightIconId(String buttonType) {
        if (buttonType.equals("back")) {
            return R.id.action_back;
        }
        return R.id.action_close;
    }
}