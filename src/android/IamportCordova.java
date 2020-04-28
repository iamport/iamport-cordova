package kr.iamport.cordova;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import javax.security.auth.callback.Callback;

/**
 * This class echoes a string called from JavaScript.
 */
public class IamportCordova extends CordovaPlugin {
    Intent intent;

    static final int REQUEST_CODE = 6018;
    static final int REQUEST_CODE_FOR_NICE_TRANS = 4117;
    static final String WEBVIEW_PATH = "file:///android_asset/www/iamport-webview.html";

    public CallbackContext callback;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("startActivity")) {
            callback = callbackContext;

            intent = new Intent(cordova.getActivity().getApplicationContext(), IamportActivity.class);

            String type = args.getString(0);;
            JSONObject titleOptions = args.getJSONObject(1);;
            JSONObject params = args.getJSONObject(2);;
            intent.putExtra("type", type);
            intent.putExtra("titleOptions", titleOptions.toString());
            intent.putExtra("params", params.toString());

            cordova.setActivityResultCallback(this);
            cordova.getActivity().startActivityForResult(intent, REQUEST_CODE);

            return true;
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == REQUEST_CODE && intent != null) {
            Bundle extras = intent.getExtras();
            String url = extras.getString("url");
            callback.success(url);
        }
    }
}
