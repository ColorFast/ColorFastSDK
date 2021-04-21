package com.colorfast.mediation.admob;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.mediation.MediationAdRequest;
import com.google.android.gms.ads.mediation.customevent.CustomEventBanner;
import com.google.android.gms.ads.mediation.customevent.CustomEventBannerListener;
import com.colorfast.kern.callback.AdEventListener;
import com.colorfast.kern.core.CFNative;
import com.colorfast.kern.core.ColorFastSDK;
import com.colorfast.kern.vo.AdsNativeVO;

public class ZCustomEventBanner implements CustomEventBanner {

    private static final String TAG = "ZCustomEventBanner";
    private CustomEventBannerListener customEventBannerListener;

    @Override
    public void requestBannerAd(Context context, CustomEventBannerListener customEventBannerListener,
                                String serverParameter, AdSize adSize, MediationAdRequest mediationAdRequest, Bundle bundle) {
        this.customEventBannerListener = customEventBannerListener;
        Log.i(TAG, "requestBannerAd: Admob -> " + serverParameter);

        ColorFastSDK.initialize(context, serverParameter);
        Log.i(TAG, "requestBannerAd: " + serverParameter);

        com.colorfast.kern.enums.AdSize ctAdSize = com.colorfast.kern.enums.AdSize.AD_SIZE_320X50;
        ColorFastSDK.getBannerAd(context, serverParameter, ctAdSize, adEventListener);
    }


    @Override
    public void onDestroy() {

    }


    @Override
    public void onPause() {

    }


    @Override
    public void onResume() {

    }


    private AdEventListener adEventListener = new AdEventListener() {
        @Override
        public void onReceiveAdSucceed(CFNative result) {
            if (customEventBannerListener != null) {
                customEventBannerListener.onAdLoaded(result);
                customEventBannerListener.onAdOpened();
            }
        }


        @Override
        public void onReceiveAdVoSucceed(AdsNativeVO result) {

        }


        @Override
        public void onInterstitialLoadSucceed(CFNative result) {

        }


        @Override
        public void onReceiveAdFailed(CFNative result) {
            if (customEventBannerListener != null) {
                customEventBannerListener.onAdFailedToLoad(AdRequest.ERROR_CODE_NO_FILL);
            }
        }


        @Override
        public void onLandpageShown(CFNative result) {

        }


        @Override
        public void onAdClicked(CFNative result) {
            if (customEventBannerListener != null) {
                customEventBannerListener.onAdClicked();
            }
        }


        @Override
        public void onAdClosed(CFNative result) {
            if (customEventBannerListener != null) {
                customEventBannerListener.onAdClosed();
            }
        }
    };


    private int dpToPx(int dp) {
        DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();
        return (int) (dp * displayMetrics.density + .5f);
    }
}
