package com.android.volley;

import com.android.volley.request.Request;

/** 网络接口，处理网络请求 */
public interface Network {
    NetworkResponse performRequest(Request<?> request) throws VolleyError;
}
