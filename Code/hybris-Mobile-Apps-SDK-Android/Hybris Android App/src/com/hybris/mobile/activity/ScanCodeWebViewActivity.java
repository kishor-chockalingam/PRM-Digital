package com.hybris.mobile.activity;

import com.accenture.hybris.mobile.beacons.Constants;
import com.accenture.hybris.mobile.beacons.CustomerUuidPost;
import com.hybris.mobile.R;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class ScanCodeWebViewActivity extends Activity {



	private final String TAG = ScanCodeWebViewActivity.class.getSimpleName(); 
	private WebView browserView;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.app_web_view);
		browserView = (WebView)findViewById(R.id.webview);
	    browserView.setWebViewClient(new MyWebViewClient());
	    WebSettings webSettings = browserView.getSettings();
	    webSettings.setJavaScriptEnabled(true);
	    webSettings.setDomStorageEnabled(true);
	    
	    browserView.getSettings().setJavaScriptEnabled(true);
	    browserView.getSettings().setDomStorageEnabled(true);
	    browserView.getSettings().setPluginState(PluginState.ON);
	    
	    Bundle extras = getIntent().getExtras();
	    String url = extras.getString(Constants.DATA);
	    Log.d(TAG,"URL to load in webview is: " + url);
	    browserView.loadUrl(url);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if( (keyCode == KeyEvent.KEYCODE_BACK) && browserView.canGoBack() ) {
			browserView.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	private class MyWebViewClient extends WebViewClient {
        @Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			// TODO Auto-generated method stub
        	Log.d(TAG,"Error recived: Code " + errorCode + "\n desc: " + description + "\n failingURL: " + failingUrl);
			super.onReceivedError(view, errorCode, description, failingUrl);
		}

		@Override
		public void onReceivedSslError(WebView view, SslErrorHandler handler,
				SslError error) {
			// TODO Auto-generated method stub
			Log.d(TAG," SSL Error recived:  " + error.getPrimaryError());
			if(error.getPrimaryError()==2) {
				handler.proceed();
			} else {
				super.onReceivedSslError(view, handler, error);
			}
		}

		@Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
        }
        
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url); 
			return true;
			}
		
		@Override
		public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
			return null;
		}
	}
	
	
	
}
