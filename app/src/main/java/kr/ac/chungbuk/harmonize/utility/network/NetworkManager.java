package kr.ac.chungbuk.harmonize.utility.network;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.ac.chungbuk.harmonize.config.Domain;
import kr.ac.chungbuk.harmonize.entity.SimpleMusic;
import kr.ac.chungbuk.harmonize.utility.network.event.RequestListener;

public class NetworkManager {

    private static final String TAG = "NetworkManager";
    private static NetworkManager instance = null;

    public RequestQueue queue;
    private Gson gson;

    private NetworkManager(Context context) {
        queue = Volley.newRequestQueue(context.getApplicationContext());
        gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    }

    public static synchronized NetworkManager getInstance(Context context) {
        if (instance == null)
            instance = new NetworkManager(context);
        return instance;
    }

    public static synchronized NetworkManager getInstance() {
        if (instance == null) {
            throw  new IllegalStateException(NetworkManager.class.getSimpleName() +
                    " is not initialized, call getInstance(...) first");
        }
        return instance;
    }

    /**
     * 음악 검색
     * @param listener 
     */
    public void getSearch(final RequestListener<ArrayList<SimpleMusic>> listener) {

        StringRequest request = new StringRequest(Request.Method.GET, Domain.url("/api/search"),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            ArrayList<SimpleMusic> musics = gson.fromJson(response, TypeToken.getParameterized(ArrayList.class, SimpleMusic.class).getType());

                            listener.getResult(musics);

                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.d(TAG, "ERROR: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //error.printStackTrace();
                        listener.getResult(null);
                    }
                }) {

            @Override
                    protected Response<String> parseNetworkResponse(NetworkResponse response) {
                        try {
                            String utf8String = new String(response.data, "UTF-8");
                            return Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response));
                        } catch (UnsupportedEncodingException e) {
                            // log error
                            return Response.error(new ParseError(e));
                        } catch (Exception e) {
                            // log error
                            return Response.error(new ParseError(e));
                }
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //params.put("Content-Type", "application/json; charset=UTF-8");
                //params.put("token", "welkfjlwejflwe");
                return params;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                //params.put("uid", uid);
                return params;
            }
        };

        queue.add(request);
    }









}
