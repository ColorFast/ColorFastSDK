package com.colorfast.mediation.admob;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.mediation.MediationAdRequest;
import com.google.android.gms.ads.mediation.customevent.CustomEventInterstitial;
import com.google.android.gms.ads.mediation.customevent.CustomEventInterstitialListener;
import com.colorfast.kern.callback.AdEventListener;
import com.colorfast.kern.core.ColorFastSDK;
import com.colorfast.kern.core.CFNative;
import com.colorfast.kern.vo.AdsNativeVO;

public class ZCustomEventInterstitial implements CustomEventInterstitial {
    private CFNative result;
    private static final String TAG = "ZCEventInterstitial";
    private CustomEventInterstitialListener interstitialListener;


    @Override
    public void requestInterstitialAd(Context context, CustomEventInterstitialListener customEventInterstitialListener, String serverParameter, MediationAdRequest mediationAdRequest, Bundle bundle) {
        interstitialListener = customEventInterstitialListener;
        Log.e(TAG, "requestInterstitialAd: Admob -> " + serverParameter);
        if (!(context instanceof Activity)) {
            Log.w(TAG, "Context must be of type Activity.");
            if (interstitialListener != null) {
                interstitialListener.onAdFailedToLoad(AdRequest.ERROR_CODE_NO_FILL);
            }
            return;
        }
        ColorFastSDK.initialize(context, serverParameter);
        ColorFastSDK.preloadInterstitialAd(context, serverParameter, ctAdEventListener);
    }


    @Override
    public void showInterstitial() {
        if (!ColorFastSDK.isInterstitialAvailable(result)) {
            if (interstitialListener != null) {
                interstitialListener.onAdFailedToLoad(AdRequest.ERROR_CODE_INTERNAL_ERROR);
            }
            return;
        }
        ColorFastSDK.showInterstitialAd(result);
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


    private AdEventListener ctAdEventListener = new AdEventListener() {
        @Override
        public void onReceiveAdSucceed(CFNative result) {
            if (interstitialListener != null) {
                interstitialListener.onAdLoaded();
            }
            ZCustomEventInterstitial.this.result = result;
        }


        @Override
        public void onReceiveAdVoSucceed(AdsNativeVO result) {

        }


        @Override
        public void onInterstitialLoadSucceed(CFNative result) {
            if (interstitialListener != null) {
                interstitialListener.onAdOpened();
            }
        }


        @Override
        public void onReceiveAdFailed(CFNative result) {
            if (interstitialListener != null) {
                interstitialListener.onAdFailedToLoad(AdRequest.ERROR_CODE_NO_FILL);
            }
        }


        @Override
        public void onLandpageShown(CFNative result) {

        }


        @Override
        public void onAdClicked(CFNative result) {
            if (interstitialListener != null) {
                interstitialListener.onAdClicked();
            }
        }


        @Override
        public void onAdClosed(CFNative result) {
            if (interstitialListener != null) {
                interstitialListener.onAdClosed();
            }
        }

    };
}
