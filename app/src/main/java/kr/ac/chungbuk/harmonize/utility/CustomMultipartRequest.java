package kr.ac.chungbuk.harmonize.utility;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CustomMultipartRequest extends Request<String> {

    private final String boundary = "multipart_boundary_" + System.currentTimeMillis();
    private final String mimeType = "multipart/form-data;boundary=" + boundary;
    private final Response.Listener<String> listener;
    private final File file;
    private final Map<String, String> headers;

    public CustomMultipartRequest(String url, File file, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, url, errorListener);
        this.listener = listener;
        this.file = file;
        this.headers = new HashMap<>();
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        headers.put("Content-Type", mimeType);
        return headers;
    }

    @Override
    public String getBodyContentType() {
        return mimeType;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

        try {
            // 파일 추가
            addFilePart(dataOutputStream);

            // 마무리
            dataOutputStream.writeBytes("--" + boundary + "--\r\n");
            dataOutputStream.flush();

            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            VolleyLog.e("IOException writing to ByteArrayOutputStream");
        } finally {
            try {
                byteArrayOutputStream.close();
            } catch (IOException ignored) { }
        }
        return null;
    }

    private void addFilePart(DataOutputStream dataOutputStream) throws IOException {
        String fileName = file.getName();
        dataOutputStream.writeBytes("--" + boundary + "\r\n");
        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\"" + fileName + "\"\r\n");
        dataOutputStream.writeBytes("Content-Type: " + "application/octet-stream" + "\r\n\r\n");

        FileInputStream fileInputStream = new FileInputStream(file);
        int bytesRead;
        byte[] buffer = new byte[1024];
        while ((bytesRead = fileInputStream.read(buffer)) != -1) {
            dataOutputStream.write(buffer, 0, bytesRead);
        }
        fileInputStream.close();
        dataOutputStream.writeBytes("\r\n");
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        try {
            String responseString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
        } catch (IOException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(String response) {
        listener.onResponse(response);
    }
}