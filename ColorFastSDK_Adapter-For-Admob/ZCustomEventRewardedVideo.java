package com.colorfast.mediation.admob;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.colorfast.kern.core.CFError;
import com.colorfast.kern.core.CFVideo;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.mediation.MediationAdRequest;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.mediation.MediationRewardedVideoAdAdapter;
import com.google.android.gms.ads.reward.mediation.MediationRewardedVideoAdListener;
import com.colorfast.kern.callback.VideoAdLoadListener;
import com.colorfast.kern.core.ColorFastSDK;
import com.colorfast.video.core.RewardedVideoAdListener;
import com.colorfast.video.core.ColorFastVideo;

public class ZCustomEventRewardedVideo implements MediationRewardedVideoAdAdapter {

    private static final String TAG = "ZCEventRewardedVideo";

    private static String slotId = "";

    private MediationRewardedVideoAdListener mAdmobListener;
    private Context context;

    private CFVideo zcVideo;

    private boolean isInitialized;


    @Override
    public void initialize(Context context,
                           MediationAdRequest mediationAdRequest,
                           String unUsed,
                           MediationRewardedVideoAdListener listener,
                           Bundle serverParameters,
                           Bundle mediationExtras) {
        Log.e(TAG, "initialize: serverParameters -> " + serverParameters);
        mAdmobListener = listener;
        slotId = serverParameters.getString("parameter");
        ColorFastSDK.initialize(context, slotId);
        if (!(context instanceof Activity)) {
            Log.w("  ZcoupAdapter", "Context must be of type Activity.");
            listener.onInitializationFailed(
                ZCustomEventRewardedVideo.this, AdRequest.ERROR_CODE_INVALID_REQUEST);
            return;
        }
        this.context = context;
        mAdmobListener.onInitializationSucceeded(ZCustomEventRewardedVideo.this);
        isInitialized = true;
    }


    @Override
    public void loadAd(MediationAdRequest mediationAdRequest, Bundle bundle, Bundle bundle1) {
        if (mAdmobListener != null) {
            ColorFastVideo.preloadRewardedVideo(context, slotId, loadListener);
        }
    }


    @Override
    public void showVideo() {
        ColorFastVideo.showRewardedVideo(zcVideo, cloudRVListener);
    }


    @Override
    public boolean isInitialized() {
        return isInitialized;
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


    private VideoAdLoadListener loadListener = new VideoAdLoadListener() {
        @Override
        public void onVideoAdLoadSucceed(CFVideo video) {
            if (video != null) {
                zcVideo = video;
                mAdmobListener.onAdLoaded(ZCustomEventRewardedVideo.this);
                return;
            }
            mAdmobListener.onAdFailedToLoad(ZCustomEventRewardedVideo.this,
                Integer.parseInt(CFError.ERR_CODE_VIDEO));
        }


        @Override
        public void onVideoAdLoadFailed(CFError error) {
            mAdmobListener.onAdFailedToLoad(ZCustomEventRewardedVideo.this,
                Integer.parseInt(CFError.ERR_CODE_VIDEO));
        }

    };

    private RewardedVideoAdListener cloudRVListener = new RewardedVideoAdListener() {

        @Override
        public void videoStart() {
            if (mAdmobListener != null) {
                mAdmobListener.onAdOpened(ZCustomEventRewardedVideo.this);
                mAdmobListener.onVideoStarted(ZCustomEventRewardedVideo.this);
            }
        }


        @Override
        public void videoFinish() {

        }


        @Override
        public void videoError(Exception e) {
            if (mAdmobListener != null) {
                mAdmobListener.onAdFailedToLoad(
                    ZCustomEventRewardedVideo.this, Integer.parseInt(CFError.ERR_CODE_VIDEO));
            }
        }


        @Override
        public void videoClosed() {
            if (mAdmobListener != null) {
                mAdmobListener.onAdClosed(ZCustomEventRewardedVideo.this);
            }
        }


        @Override
        public void videoClicked() {
            if (mAdmobListener != null) {
                mAdmobListener.onAdClicked(ZCustomEventRewardedVideo.this);
            }
        }


        @Override
        public void videoRewarded(final String rewardName, final String rewardAmount) {
            if (mAdmobListener != null) {
                mAdmobListener.onRewarded(ZCustomEventRewardedVideo.this, new RewardItem() {
                    @Override
                    public String getType() {
                        return rewardName;
                    }


                    @Override
                    public int getAmount() {
                        int amount = 0;
                        try {
                            amount = Integer.parseInt(rewardAmount);
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                        return amount;
                    }
                });
            }
        }

    };
}
