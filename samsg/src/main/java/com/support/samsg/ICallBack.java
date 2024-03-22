package com.support.samsg;

import org.json.JSONException;

public interface ICallBack {
    /**
     * Fires on request finished
     * @param response - response body
     * @return true if event was handled, otherwise onSuccess or onError will fire
     */
    boolean onFinished(String response);
    /**
     * Fires if server response != "ok"
     */
    void onError(String error);
    /**
     * Fires if server response == "ok"
     */
    void onSuccess(Object resp) throws JSONException;
}