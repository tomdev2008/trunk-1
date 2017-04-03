package com.aibinong.tantan.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.aibinong.tantan.BuildConfig;
import com.aibinong.tantan.constant.IntentExtraKey;
import com.aibinong.tantan.push.BroadCastConst;
import com.fatalsignal.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yourfriendyang on 2016/4/15.
 */
public class IapppayH5PayActivity extends CommonWebActivity {
    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        mToolbar_common.setVisibility(View.GONE);
        mWebviewCommonActivity.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.i("shouldOverrideUrlLoading", url);
                if (!TextUtils.isEmpty(url)) {
                    try {
                        //transdata=%7B%22appid%22%3A%223000669142%22%2C%22appuserid%22%3A%22%EF%BC%9F%22%2C%22cporderid%22%3A%221427551361223%22%2C%22cpprivate%22%3A%22cpprivateinfo123456%22%2C%22currency%22%3A%22RMB%22%2C%22feetype%22%3A2%2C%22money%22%3A0.010%2C%22paytype%22%3A501%2C%22result%22%3A0%2C%22transid%22%3A%2232011503282159230691%22%2C%22transtime%22%3A%222015-03-28+22%3A00%3A09%22%2C%22transtype%22%3A0%2C%22waresid%22%3A1%7D&sign=I6eAtacd5D8LJ08zgyTvtAa%2BrtapaatWRmuhLoiiwO%2B%2BW6DY3yf3kq0qtsLIe6tzcAJHZf74KC2lGNXlOlWke%2B0NkLV%2BpZGlNE8VkZAEdnKM1Ay6Qzl%2BAR1zWv2%2FIiGzRxrsUlS6bY%2FKQ0qDV1nkZCtqAi2vvWQ0xEI0mHr%2Feng%3D&signtype=RS
                        Pattern pattern = Pattern.compile("(transdata=.*?signtype=RSA)");
                        Matcher matcher = pattern.matcher(url);
                        if (matcher.find()) {
                            String signvalue = matcher.group(0);
                            //支付成功
//                                    CallbackUtil.getCallback().onPayResult(IAppPay.PAY_SUCCESS, signvalue, "");
                            Intent intent = new Intent(BroadCastConst.IapppayWebResultBroadIntentAction);
                            intent.putExtra(IntentExtraKey.IAPPAY_WEB_PAYRESULT_TRANSDATA, signvalue);
                            LocalBroadcastManager.getInstance(IapppayH5PayActivity.this).sendBroadcast(intent);
                            finish();
                            return true;
                        }
                        String httpHead = url.substring(0, 5).toString();
                        String urlScheme = BuildConfig.DUOBAO_URL_SCHEME;
                        if (!httpHead.equals("http:")
                                && !httpHead.equals("https")) {
                            if (url.startsWith(urlScheme)) {
                                finish();
                            }
                            try {
                                Intent it = new Intent(Intent.ACTION_VIEW);
                                it.setData(Uri.parse(url));
                                startActivity(it);
                                return true;
                            } catch (Exception e) {
                                try {
                                    Uri uri = Uri.parse(url);
                                    Intent intent;
                                    intent = Intent.parseUri(url,
                                            Intent.URI_INTENT_SCHEME);
                                    intent.addCategory("android.intent.category.BROWSABLE");
                                    intent.setComponent(null);
                                    // intent.setSelector(null);
                                    startActivity(intent);

                                } catch (Exception e2) {
                                    e2.printStackTrace();
                                }
//                                Toast.makeText(IapppayH5PayActivity.this, "发生了错误，可能是微信版本过低", Toast.LENGTH_SHORT).show();
                                /**
                                 android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(IapppayH5PayActivity.this);
                                 builder.setTitle("支付提示").setCancelable(false)
                                 .setMessage("微信支付仅支持6.0.2 及以上版本，请将微信更新至最新版本")
                                 .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                dialog.cancel();
                                }

                                }).show();
                                 **/
                                e.printStackTrace();
                                return false;
                            }

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                view.loadUrl(url);// 使用当前WebView处理跳转
                return true;// true表示此事件在此处被处理，不需要再广播
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                // 有页面跳转时被回调
                if (!view.canGoBack()) {
//                    setLoadStatus(LOADING_VIEW_STATUS_LOADING, null);
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                // 页面跳转结束后被回调
//                setLoadStatus(LOADING_VIEW_STATUS_SUCCESS, null);

//                setTopTitle(mWebView.getTitle());
//                if (mWebView.canGoBack()) {
//                    getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_common_arr_left_white);
//                } else {
//                    getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_common_x);
//                }
            }

            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {

//                DialogUtil.showDialog(CommonWebActivity.this, "出错了:"
//                        + description, true);
                super.onReceivedError(view, errorCode, description, failingUrl);
            }
        });
    }

}
