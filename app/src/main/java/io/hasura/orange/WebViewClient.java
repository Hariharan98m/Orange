package io.hasura.orange;

import android.webkit.WebView;

/**
 * Created by HARIHARAN on 14-10-2017.
 */

public class WebViewClient extends android.webkit.WebViewClient {
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;
    }
}
