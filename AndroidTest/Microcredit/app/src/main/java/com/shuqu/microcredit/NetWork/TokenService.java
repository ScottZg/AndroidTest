package com.shuqu.microcredit.NetWork;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Class Info ：update fresh token
 * Created by Lyndon on 16/6/21.
 */
public class TokenService extends Service implements FreshTokenService.onFreshToken{

    @Override public void onCreate() {
        super.onCreate();
        FreshTokenService.freshToken(this, FreshTokenService.REFRESH, this);
    }


    @Nullable @Override public IBinder onBind(Intent intent) {
        return null;
    }

    @Override public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }


    @Override public void onTokenFreshed() {
        stopSelf();
    }
}
