package com.aibinong.tantan.ui.activity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.ClientCertRequest;
import android.webkit.DownloadListener;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.aibinong.tantan.R;
import com.aibinong.tantan.broadcast.LocalBroadCastConst;
import com.aibinong.tantan.constant.IntentExtraKey;
import com.aibinong.tantan.ui.dialog.VipGiftSendDialog;
import com.aibinong.tantan.ui.widget.EmptyView;
import com.aibinong.tantan.util.AliulianAndroidJSInterface;
import com.aibinong.tantan.util.DialogUtil;
import com.aibinong.yueaiapi.pojo.OrderResultEntitiy;
import com.fatalsignal.util.Log;

import butterknife.Bind;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class UserAgreementActivity extends AppCompatActivity {
    private TextView mTitleView;
    protected String mUrl;
    protected boolean mCanStopLoading = false;
    @Bind(R.id.tv_toolbar_title)
    TextView mTvToolbarTitle;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.webview_common_activity)
    WebView mWebviewCommonActivity;
    @Bind(R.id.emptyview_commonweb)
    EmptyView mEmptyviewCommonweb;
    @Bind(R.id.rotate_header_list_view_frame)
    PtrClassicFrameLayout mRotateHeaderListViewFrame;

    public static Intent launchIntent(Context context, String url) {
        Intent intent = new Intent(context, UserAgreementActivity.class);
        intent.putExtra(IntentExtraKey.WEBURL_WEBACTIVITY_EXTRA, url);
        context.startActivity(intent);
        return intent;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem closeItem = menu.add(R.id.abn_yueai_default_menu_group, R.id.abn_yueai_menuItem_close, 0, "关闭");
        closeItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.abn_yueai_menuItem_close) {
            finish();
            return true;
        } else if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void setupToolbar(Toolbar toolbar, TextView titleView, boolean asBack) {
        mTitleView = titleView;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(asBack);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.abn_yueai_ic_back);
        getSupportActionBar().setTitle(null);
        if (mTitleView != null) {
            mTitleView.setText(getTitle());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.abn_yueai_activity_common_web);
        ButterKnife.bind(this);
        if (getIntent() != null) {
            mUrl = getIntent().getStringExtra(
                    IntentExtraKey.WEBURL_WEBACTIVITY_EXTRA);
        }
        setupToolbar(mToolbar, mTvToolbarTitle, true);
        requireTransStatusBar();
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_common_x);
        setupView(savedInstanceState);
        mWebviewCommonActivity.loadUrl(mUrl);

    }

    protected void requireTransStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
//                getWindow()
//                        .setStatusBarColor(
//                                getResources().getColor(
//                                        R.color.color_common_textwhite));
//                getWindow()
//                        .setNavigationBarColor(
//                                getResources().getColor(
//                                        R.color.color_common_textwhite));
//
//            } else
            {
                // 4.4的话半透明状态栏
                getWindow().addFlags(
                        WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//                getWindow().addFlags(
//                        WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            mUrl = getIntent().getStringExtra(
                    IntentExtraKey.WEBURL_WEBACTIVITY_EXTRA);
            mWebviewCommonActivity.loadUrl(mUrl);
        }
    }


    protected void setupView(@Nullable Bundle savedInstanceState) {

        mRotateHeaderListViewFrame.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mWebviewCommonActivity.reload();
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
//                return super.checkCanDoRefresh(frame, mWebviewCommonActivity, header);
                return false;  //禁止下拉刷新
            }
        });
        mWebviewCommonActivity = (WebView) findViewById(R.id.webview_common_activity);
//        if (mWebviewCommonActivity.isHardwareAccelerated()) {
//            mWebviewCommonActivity.setLayerType(View.LAYER_TYPE_HARDWARE, null);
//        } else {
//            mWebviewCommonActivity.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
//        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mWebviewCommonActivity.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        mWebviewCommonActivity.getSettings().setDomStorageEnabled(true);
        mWebviewCommonActivity.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
//		mWebviewCommonActivity.getSettings().setUserAgentString("Mozilla/5.0 (Linux; U; Android 6.0; zh-CN; Nexus 5 Build/MRA58K) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 UCBrowser/10.7.0.634 U3/0.8.0 Mobile Safari/534.30");
//		mWebviewCommonActivity.getSettings().setUserAgentString("Mozilla/5.0 (Linux; U; iPhone 6.0; zh-CN; Nexus 5 Build/MRA58K) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 UCBrowser/10.7.0.634 U3/0.8.0 Mobile Safari/534.30");
        mWebviewCommonActivity.getSettings().setBuiltInZoomControls(false);
        mWebviewCommonActivity.getSettings().setJavaScriptEnabled(true);
        mWebviewCommonActivity.getSettings().setUseWideViewPort(true);
        mWebviewCommonActivity.requestFocus();
        /*
         * WebView默认用系统自带浏览器处理页面跳转。 为了让页面跳转在当前WebView中进行，重写WebViewClient。
		 * 但是按BACK键时，不会返回跳转前的页面，而是退出本Activity。重写onKeyDown()方法来解决此问题。
		 */
        mWebviewCommonActivity.setWebViewClient(new WebViewClient() {
//            private boolean mReceivedError;

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                Log.d("shouldInterceptRequest", request.toString());
                return super.shouldInterceptRequest(view, request);
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                Log.d("onReceivedSslError", error.toString());
                super.onReceivedSslError(view, handler, error);
            }

            @Override
            public void onReceivedClientCertRequest(WebView view, ClientCertRequest request) {
                Log.d("onReceivedClientCertRequest", request.toString());
                super.onReceivedClientCertRequest(view, request);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);// 使用当前WebView处理跳转
                return true;// true表示此事件在此处被处理，不需要再广播
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                // 有页面跳转时被回调
                mEmptyviewCommonweb.startLoading();
                mCanStopLoading = true;
//                mReceivedError = false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                // 页面跳转结束后被回调
                Log.i("CommonWebActivity", "onPageFinished");
                mCanStopLoading = false;
                mRotateHeaderListViewFrame.refreshComplete();
//                if (!mReceivedError) {
                mEmptyviewCommonweb.loadingComplete();
                setTitle(mWebviewCommonActivity.getTitle());
//                }
//                mReceivedError = false;
                if (mWebviewCommonActivity.canGoBack()) {
                    getSupportActionBar().setHomeAsUpIndicator(R.mipmap.abn_yueai_ic_back);
                } else {
                    getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_common_x);
                }
            }

//            @Override
//            public void onReceivedError(WebView view, int errorCode,
//                                        String description, String failingUrl) {
//                mCanStopLoading = false;
//                setLoadStatus(LOADING_VIEW_STATUS_LOADFAILED, description);
//                Log.i("CommonWebActivity", "onReceivedError");
//                mWebView.stopLoading();
//                mReceivedError = true;
//                super.onReceivedError(view, errorCode, description, failingUrl);
//            }
        });

		/*
         * 当WebView内容影响UI时调用WebChromeClient的方法
		 */
        mWebviewCommonActivity.setWebChromeClient(new WebChromeClient() {
            /**
             * 处理JavaScript Alert事件
             */
            @Override
            public boolean onJsAlert(WebView view, String url, String message,
                                     final JsResult result) {
                // 提示是否重试
                AlertDialog dialog = new AlertDialog.Builder(
                        UserAgreementActivity.this).create();
                DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        dialog.dismiss();
                        if (which == Dialog.BUTTON_NEGATIVE) {
                            result.cancel();
                        }
                        if (which == Dialog.BUTTON_POSITIVE) {
                            result.confirm();
                        }
                    }
                };
//                dialog.setButton(Dialog.BUTTON_NEGATIVE, "取消",
//                        listener);
                dialog.setButton(Dialog.BUTTON_POSITIVE, "确定",
                        listener);
                dialog.setMessage(message);
                dialog.setCancelable(false);
                DialogUtil.showExistingDialog(UserAgreementActivity.this, dialog);
                return true;
            }
        });
        mWebviewCommonActivity.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                try {
                    Uri uri = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        /*
         * 绑定Java对象到WebView，这样可以让JS与Java通信(JS访问Java方法) 第一个参数是自定义类对象，映射成JS对象
		 * 第二个参数是第一个参数的JS别名 调用示例：
		 * mWebView.loadUrl("javascript:window.stub.jsMethod('param')");
		 */
        // mWebView.addJavascriptInterface(jsInterface, "zuiHaoKanJSInterface");

//		mWebView.getSettings()
//				.setUserAgentString(
//						"Mozilla/5.0 (Windows NT 5.2) AppleWebKit/534.30 (KHTML, like Gecko) Chrome/12.0.742.122 Safari/534.30 aliulian Android");

        // mWebView.loadUrl("file:///android_asset/untitled.html");
        // mWebView.loadUrl("file:///android_asset/untitled.html");
    }

    @Override
    public void onBackPressed() {
        if (mCanStopLoading) {
            mWebviewCommonActivity.stopLoading();
            mCanStopLoading = false;
        } else if (mWebviewCommonActivity.canGoBack()) {
            mWebviewCommonActivity.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        mWebviewCommonActivity.stopLoading();
        super.onDestroy();
    }
}
