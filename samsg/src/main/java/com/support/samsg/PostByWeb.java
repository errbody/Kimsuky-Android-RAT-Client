package com.support.samsg;


import android.content.Context;
import android.util.Log;


import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by RAIN on 2021-02-09.
 */

public class PostByWeb extends Thread {

    protected final int _SIZE;
    protected final Context mContext;
    protected final String mData;
    protected final PostType mType;
    protected final ICallBack mICallBack;
    protected final String mID;

    protected URL mUrl;
    protected PrintWriter mPrintWriter;

    public PostByWeb(Context context, PostType nType, String strID, String strData, ICallBack callBack) {
        mContext = context;
        mType = nType;
        mData = strData;
        mICallBack = callBack;
        _SIZE = 8192;
        mID = strID;
        request();
    }

    private void request() {

        try {
            if (mType == PostType.DATA) {
                mUrl = new URL(_Global.DATA_URL + "?ati=" + mID);
            } else if (mType == PostType.FILE) {
                mUrl = new URL(_Global.FILE_URL + "?ati=" + mID);
            }
            HttpURLConnection connection = (HttpURLConnection) mUrl.openConnection();
            connection.setConnectTimeout(_Global.READ_TIME_OUT * 3);
            connection.setDoInput(true);

            if (mType == PostType.DATA) {
                connection.setDoOutput(true);
                connection.setRequestMethod(MyCrypt.crypt("t\u001Ew\u0005", 48));//"POST"
                connection.setRequestProperty(MyCrypt.crypt("g>J%A?P|p(T4", 48), MyCrypt.crypt("E!T=M2E%M>J~N\"K?", 48));//"Content-Type", "application/json"
                connection.setReadTimeout(_Global.READ_TIME_OUT);
            } else if (mType == PostType.FILE) {
                connection.setDoOutput(true);
                connection.setRequestProperty(MyCrypt.crypt("g>J?A2P8K?", 48), MyCrypt.crypt("o4A!\t\u0010H8R4", 48));//"Connection", "Keep-Alive"
                connection.setRequestProperty(MyCrypt.crypt("g>J%A?P|p(T4", 48), MyCrypt.crypt("<Q=P8T0V%\u000B7K#I|@0P0\u001F3K$J5E#]l", 48) + _Global.Boundary);//"Content-Type", "multipart/form-data;boundary="
            } else if (mType == PostType.PATCH) {
                connection.setRequestMethod(MyCrypt.crypt("\u0016a\u0005", 48));//"GET"
            }

            connection.connect();

            if (mType == PostType.DATA) {
                OutputStreamWriter mOutputStreamWriter = new OutputStreamWriter(connection.getOutputStream());
                mOutputStreamWriter.write(mData);
                mOutputStreamWriter.flush();
                mOutputStreamWriter.close();
            } else if (mType == PostType.FILE) {
                mPrintWriter = new PrintWriter(new OutputStreamWriter(connection.getOutputStream(), _Global.CharSet), true);

                /*for (int i = 0; i < myFormDataArray.size(); i++)
                    addFormField(myFormDataArray.get(i).getParamName(), myFormDataArray.get(i).getParamValue());
                for (int i = 0; i < myFileArray.size(); i++)
                    addFilePart(myFileArray.getParamName(), new File(myFileArray.getFileName()));*/

                mPrintWriter.append(_Global.CRLF).flush();
                mPrintWriter.append(_Global.TwoHyphens).append(_Global.Boundary).append(_Global.TwoHyphens).append(_Global.CRLF);
                mPrintWriter.close();
            }

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                Log.e(PostByWeb.class.getName(), MyCrypt.crypt("\n\t\f\u0004\u0002A?@qv4W!\u0004\u0012K5Aq\u0019q", 48) + connection.getResponseCode());//"[-] Send Resp Code = "
                return;
            }
            connection.disconnect();
            if (mType == PostType.PATCH) {
                int nRead;
                int len = connection.getContentLength();
                byte[] bytesTemp = new byte[_SIZE];
                InputStream inputStream = connection.getInputStream();

                int position = 0;
                byte[] bytesRecv = new byte[len];
                for (;;) {
                    nRead = inputStream.read(bytesTemp);
                    if (nRead <= 0) {
                        break;
                    }
                    System.arraycopy(bytesTemp, 0, bytesRecv, position, nRead);
                    position += nRead;
                }
                inputStream.close();
                connection.disconnect();

                if (new String(bytesRecv).equals(MyCrypt.crypt("B0M=", 48))) {//fail
                    mICallBack.onFinished(MyCrypt.crypt("B0M=", 48));
                } else {
                    mICallBack.onSuccess(ByteBuffer.wrap(bytesRecv));
                }
                bytesRecv = null;
            } else {
                String line;
                BufferedReader mBufferReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder mStringBuilder = new StringBuilder();
                while ((line = mBufferReader.readLine()) != null) {
                    mStringBuilder.append(line);
                }

                String response = mStringBuilder.toString();
                connection.disconnect();

                if (mICallBack == null) {
                    Log.e(PostByWeb.class.getName(), MyCrypt.crypt("\u007F|yqw4J5\u0004\u0012E=H\u0013E2Oq\u0019qJ$H=", 48));//"[-] Send CallBack = null"
                    return;
                }
                if (mICallBack.onFinished(response)) {
                    Log.d(PostByWeb.class.getName(), MyCrypt.crypt("\u007Fzyqw4J5\u0004\u0015E%Eqw$G2A\"Wp\u0005p\bqt0W\"\u0004\u0017M?M\"Lqg0H=f0G:", 48));//"[+] Send Data Success!!!, Pass Finish CallBack"
                }
                mICallBack.onSuccess(response);
            }
        } catch (IOException e) {
            Log.d(PostByWeb.class.getName(), MyCrypt.crypt("\n\t\f\u0004#A Q4W%\u0004\u0018k\u0014\\2A!P8K?\bqAq\u001Eq", 48) + e.getMessage());//"[-] request IOException, e : "
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        interrupt();
    }
}
