package com.huihui.hotfixdemo;

import android.content.Context;
import android.support.annotation.Keep;
import android.util.Log;
import com.taobao.sophix.PatchStatus;
import com.taobao.sophix.SophixApplication;
import com.taobao.sophix.SophixEntry;
import com.taobao.sophix.SophixManager;
import com.taobao.sophix.listener.PatchLoadStatusListener;

public class SophixStubApplication extends SophixApplication {

    private final String TAG = "SophixStubApplication";

    // 此处SophixEntry应指定真正的Application，并且保证RealApplicationStub类名不被混淆。
    @Keep
    @SophixEntry(SophixStubApplication.class)
    static class RealApplicationStub {}

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //         如果需要使用MultiDex，需要在此处调用。
        //         MultiDex.install(this);
        initSophix();
    }

    private void initSophix() {
        String appVersion = "1.0";
        try {
            appVersion =
                this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName;
        } catch (Exception e) {
        }
        final SophixManager instance = SophixManager.getInstance();
        instance.setContext(this)
                .setAppVersion(appVersion)
                .setSecretMetaData("24969858-1", "c7858199bab5d62cbe148251d6330f10",
                                   "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDtpgM9XjOhhpLucWP7t5NquAbhbgxUciU+xkr6fPQwBtFE5NBln8EN2h4oQHiOJK38XjJO1392CViXqtBDYQaRRQBj+cGA8EhooHV62kxYWCZDdB+kkShWf6uavlpMy9JuBdi6y6KIQtmDCVYkGNXZmrPcXwKdn0sd6DNURPv2xecQpdIEmlXWweKczXeFA5OvgenpuOA2eyPz8LrRX2y5Joxvqxrnot/MRrS4PvSVIvN0ulE9jdDyjfJ7Guu8k/HvIkWDwWNxKfDQI8jplD7xItASx72Q/GTHAL2bG4J0Sk5XDvXahOoO9vnM0lauF8ENZFkvgW8f9Pese2QkWNtRAgMBAAECggEAEvFwwKuJog6AF9GiDqaLca5Jnh6PjQusPFzCDUV4kXt5ouw6UF9znA8O6FZ7RZlW5Q79I5u55UbJcQ2EnFpXk6zUYyO9cPG2U+C2fJx6CXC9XBMdkpifhoWWwDb8K7LziyGcLQeeZQ4vGGreGvJ/2WCjhaVtJ/NSoYeoz8bP7tOgSzQN48z10VaL0N6Kdpf3sPW0s62BtahSPTRmDdMy1IJ7vR7uWHsK8+/UwKujKQSrC6X025dbKO4L7gsXGaUw4Lg5pNP0NM7Lrl6YeEmEqhvpXVm47fOXTZZQ+vemnGAXcT80aWhebh4pYqA8+rdvybDd44OFHuV+ug78+++yoQKBgQD7WWbY5EWyHjoqzY1cp7NAOc8ZMS+adGab1PKbZFsWZKf1bcVJjc6mzOFrIDvLv9TPrwx8hHDVjr0gc77ZhNzftbq5qafjJnXfitGJgmTSePyYB5OJCayZDY6A6ltj3qi6X6fB950tNWsuI51DFsT9PdYKz/ImqLi6OwhP5Ki2HwKBgQDyC7Z9pE6t/0ZjKITwqjrZGU4tLHiK/lq2HFCuzWWHvbEmgyOW91wXVR2CznyA0DeYMQxJeWWR6wJe9ih2WNP1UN9kyu2K1ufK19TaT/Uo3HuDQML9qM4qiiBfIHMz8v/fLACakelQVGusQmoFVT6l9fGDFWVN1fri55CHXUDgjwKBgEUThdNXsNvJuNyojxAGupmNCaB3xwhgsw5ikKYqB2p5ETu/Bjg3s7JdnhnNywEs4DSPfBQAZe4bA1kW1j17aSRGEmISASTJHh3oTbvHzSOWwnyZoG9J7FhHxIEjZyhkOeS2gykofta81o7glMiqSkr6oodoLLl5JwNvU9J5GfCrAoGAZSqAHAOQ3yjTQp6s8eo0rojlI/xyZaXqRDORhX5T8WhXosEtHuxEkXwB2ap0X2Kn2fSmLg+PZG3Vj9tTDiQSc3ZZ6Mbogh9Qwndzf8vrrCh7d2CaH2tuwFj7LsdhX1UxnRXHKtPRsMGYP2eC6Le4d6VUTHWk4eON+UjQH8PaWgECgYEAqizCo6RzQr7WTvi8FmXGayr6Dbeiiuw+gW6A0I/iihrBIN4srgyABOZopqm7KkGNXSqHh3/73NRRkcmVJnXwER7fxmlT82nJAn8puHrytKlV5uVJGXqDSH7/2tJh2ncqAk+5HWtplSDmX/rIX9XQJ7tgIC/7iiZd9yIKBSpMrp4=")
                .setEnableDebug(true)
                .setEnableFullLog()
                .setPatchLoadStatusStub(new PatchLoadStatusListener() {
                    @Override
                    public void onLoad(final int mode, final int code, final String info,
                        final int handlePatchVersion) {

                        Log.e(TAG,"返回状态吗："+code);
                        if (code == PatchStatus.CODE_LOAD_SUCCESS) {//表明补丁加载成功
                            Log.e(TAG, "sophix load patch success!");
                        } else if (code == PatchStatus.CODE_LOAD_RELAUNCH) {
                            // 如果需要在后台重启，建议此处用SharePreference保存状态。
                            //// 表明新补丁生效需要重启. 开发者可提示用户或者强制重启;
                            Log.e(TAG, "sophix preload patch success. restart app to make effect.");
                            instance.killProcessSafely();
                        } else {

                        }
                    }
                })
                .initialize();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // queryAndLoadNewPatch不可放在attachBaseContext 中，否则无网络权限，建议放在后面任意时刻，如onCreate中
        SophixManager.getInstance().queryAndLoadNewPatch();
        /** 补丁在后台发布之后, 并不会主动下行推送到客户端, 客户端通过调用queryAndLoadNewPatch方法查询后台补丁是否可用*/
    }

}
