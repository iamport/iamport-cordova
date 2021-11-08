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
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import org.json.JSONObject;

public class IamportActivity extends Activity {
    Application application;
    String packageName;
    Resources resources;
    WebView webview;
    IamportWebViewClient webViewClient;
    String titleOptions;
    int actionCloseItemId;
    int actionBackItemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        application = getApplication();
        packageName = application.getPackageName();
        resources = application.getResources();

        actionCloseItemId = resources.getIdentifier("action_close", "id", packageName);
        actionBackItemId = resources.getIdentifier("action_back", "id", packageName);

        Integer identifier = resources.getIdentifier("iamport_activity", "layout", packageName);
        setContentView(identifier);

        int webViewId= resources.getIdentifier("webview", "id", packageName);
        webview = (WebView) findViewById(webViewId);

        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            cookieManager.setAcceptThirdPartyCookies(webview, true);
        }

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
                Float textSize= Float.parseFloat(titleObj.getString("textSize"));
                String textAlignment = titleObj.getString("textAlignment");
                String backgroundColor = titleObj.getString("backgroundColor");

                ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
                ab.setCustomView(resources.getIdentifier("iamport_actionbar", "layout", packageName));

                TextView title = (TextView)findViewById(resources.getIdentifier("action_bar_title", "id", packageName));
                title.setText(text);
                title.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);

                title.setTextColor(Color.parseColor(textColor));

                title.setTextAlignment(getActionBarTitleAlignment(textAlignment));

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

                String rightButtonType = titleObj.getString("rightButtonType");
                if (textAlignment.equals("center")) {
                    LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                    boolean isLeftButtonHidden = leftButtonType.equals("hide");
                    boolean isRightButtonHidden = rightButtonType.equals("hide");
                    if (isLeftButtonHidden && !isRightButtonHidden) {
                        layoutParams.setMarginStart(120);
                    }
                    if (!isLeftButtonHidden && isRightButtonHidden) {
                        layoutParams.setMarginEnd(120);
                    }
                    title.setLayoutParams(layoutParams);
                }
            } else {
                ab.hide();
            }
        } catch (Exception e) {

        }
    }

    private int getActionBarTitleAlignment(String textAlignment) {
        switch (textAlignment) {
            case "center":
                return View.TEXT_ALIGNMENT_CENTER;
            case "right":
                return View.TEXT_ALIGNMENT_TEXT_END;
            default:
                return View.TEXT_ALIGNMENT_VIEW_START;

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
                MenuItem menuItem = menu.findItem(iconId);
                menuItem.setVisible(true);
                menuItem.getIcon().setColorFilter(Color.parseColor(rightButtonColor), PorterDuff.Mode.SRC_IN);
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
                onCancelPayment();
                break;
            }
            default: {
                if (itemId.equals(actionBackItemId) || itemId.equals(actionCloseItemId)) {
                    onCancelPayment();
                }
                break;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void onCancelPayment() {
        Intent data = new Intent();
        String url = "http://detectchangingwebview/iamport/cor?imp_success=false&error_code=IAMPORT_CORDOVA";

        data.putExtra("url", url);

        setResult(IamportCordova.REQUEST_CODE, data);

        finish();
    }

    private int getLeftIconId(String buttonType) {
        if (buttonType.equals("back")) {
            return resources.getIdentifier("ic_action_back", "drawable", packageName);
        }
        return resources.getIdentifier("ic_action_close", "drawable", packageName);
    }

    private int getRightIconId(String buttonType) {
        if (buttonType.equals("back")) {
            return actionBackItemId;
        }
        return actionCloseItemId;
    }
}