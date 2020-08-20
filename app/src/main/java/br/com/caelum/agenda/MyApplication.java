package br.com.caelum.agenda;

import android.app.Application;
import android.content.IntentFilter;
import br.com.caelum.broadcast.SmsReceiver;

public class MyApplication extends Application {
    private static IntentFilter intentFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");

    @Override
    public void onCreate() {
        super.onCreate();
        intentFilter.setPriority(1);
        registerReceiver(new SmsReceiver(), intentFilter);
    }

    public void onTerminate() {
        super.onTerminate();
        intentFilter.setPriority(1);
        registerReceiver(new SmsReceiver(), intentFilter);
        //unregisterReceiver(new SmsReceiver());
    }
}
