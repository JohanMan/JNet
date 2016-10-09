package com.johan.jnet.http;

import com.google.gson.Gson;
import com.johan.jnet.WorkerHandler;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 使用OkHttpClient实现访问网络
 * Created by Johan on 2016/10/9.
 */
public enum  OkHttpExecutor implements HttpExecutor {

    INSTANCE(new OkHttpClient());

    private ConcurrentHashMap<com.squareup.okhttp.Request, WorkerHandler> handlerMap;
    private OkHttpClient okHttpClient;

    OkHttpExecutor(OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
        this.handlerMap = new ConcurrentHashMap<>();
    }

    @Override
    public void executeRequest(final Request request, final Callback callback, final Type respondType) {
        // JNet request parse to OkHttp request
        com.squareup.okhttp.Request okHttpRequest = BuildOkHttpRequestFactory.buildRequest(request);
        // set handler
        WorkerHandler handler = WorkerHandler.workerHandler();
        if (okHttpRequest == null) {
            handler.workFailure(callback, "JNet request parse to OkHttp request error");
            return;
        }
        handlerMap.put(okHttpRequest, handler);
        // call network
        okHttpClient.newCall(okHttpRequest).enqueue(new com.squareup.okhttp.Callback() {
            @Override
            public void onFailure(com.squareup.okhttp.Request request, IOException e) {
                WorkerHandler handler = handlerMap.get(request);
                handlerMap.remove(request);
                if (handler != null) {
                    handler.workFailure(callback, e.getMessage());
                }
            }
            @Override
            public void onResponse(Response response) throws IOException {
                WorkerHandler handler = handlerMap.get(response.request());
                handlerMap.remove(request);
                if (handler == null) return;
                int code = response.code();
                Map<String, List<String>> header = response.headers().toMultimap();
                com.johan.jnet.http.Response realResponse = null;
                if (respondType instanceof InputStream) {
                   realResponse = new com.johan.jnet.http.Response(code, response.body().byteStream(), header);
                } else if (Utils.isBaseDataType(respondType)) {
                    String className = respondType.toString();
                    if (className.endsWith(String.class.getName())) {
                        realResponse = new com.johan.jnet.http.Response(code, response.body().string(), header);
                    } else if (className.endsWith(Integer.class.getName()) || className.endsWith("int")) {
                        realResponse = new com.johan.jnet.http.Response(code, Integer.parseInt(response.body().string()), header);
                    } else if (className.endsWith(Long.class.getName()) || className.endsWith("long")) {
                        realResponse = new com.johan.jnet.http.Response(code, Long.parseLong(response.body().string()), header);
                    } else if (className.endsWith(Double.class.getName()) || className.endsWith("double")) {
                        realResponse = new com.johan.jnet.http.Response(code, Double.parseDouble(response.body().string()), header);
                    } else if (className.endsWith(Float.class.getName()) || className.endsWith("float")) {
                        realResponse = new com.johan.jnet.http.Response(code, Float.parseFloat(response.body().string()), header);
                    } else if (className.endsWith(Boolean.class.getName()) || className.endsWith("boolean")) {
                        realResponse = new com.johan.jnet.http.Response(code, Boolean.parseBoolean(response.body().string()), header);
                    }
                } else {
                    String jsonBody = response.body().string();
                    Gson gson = new Gson();
                    realResponse = new com.johan.jnet.http.Response(code, gson.fromJson(jsonBody, respondType), header);
                }
                if (realResponse == null) {
                    handler.workFailure(callback, "data parse response error");
                    return;
                }
                handler.workResponse(callback, realResponse);
            }
        });
    }

    @Override
    public void cancelRequest(String tag) {
        okHttpClient.cancel(tag);
        // remove request with tag from handlerMap
        Iterator<Map.Entry<com.squareup.okhttp.Request, WorkerHandler>> iterator = handlerMap.entrySet().iterator();
        while (iterator.hasNext()) {
            com.squareup.okhttp.Request request = iterator.next().getKey();
            if (request.tag() instanceof String) {
                String requestTag = (String) request.tag();
                if (requestTag.equals(tag)) {
                    iterator.remove();
                }
            }
        }
    }

    private static class BuildOkHttpRequestFactory {
        public static com.squareup.okhttp.Request buildRequest(Request request) {
            OkHttpRequestTranslator translator = null;
            switch (request.getMethod()) {
                case Request.GET :
                    translator = new GetOkHttpRequestTranslator();
                    break;
                case Request.POST :
                    translator = new PostOkHttpRequestTranslator();
                    break;
                case Request.UPLOAD :
                    translator = new UploadOkHttpRequestTranslator();
                    break;
                case Request.NO_METHOD :
                default:
                    break;
            }
            if (translator == null) {
                return null;
            }
            return translator.translator(request);
        }
    }

    private interface OkHttpRequestTranslator {
        com.squareup.okhttp.Request translator(Request request);
    }

    private static class GetOkHttpRequestTranslator implements OkHttpRequestTranslator {
        @Override
        public com.squareup.okhttp.Request translator(Request request) {
            com.squareup.okhttp.Request.Builder builder = new com.squareup.okhttp.Request.Builder();
            // set url
            builder.url(request.getUrl());
            // set tag
            if (Utils.isNoEmptyString(request.getTag())) {
                builder.tag(request.getTag());
            }
            // set header
            if (request.getHeaders().size() > 0) {
                for (Map.Entry<String, String> entry : request.getHeaders().entrySet()) {
                    builder.addHeader(entry.getKey(), entry.getValue());
                }
            }
            // set other for child class
            addLast(builder, request);
            // return OkHttp request
            return builder.build();
        }
        protected void addLast(com.squareup.okhttp.Request.Builder builder, Request request) {

        }
    }

    private static class PostOkHttpRequestTranslator extends GetOkHttpRequestTranslator {
        @Override
        protected void addLast(com.squareup.okhttp.Request.Builder builder, Request request) {
            // set params
            if (request.getParams().size() > 0) {
                com.squareup.okhttp.FormEncodingBuilder formEncodingBuilder = new FormEncodingBuilder();
                for (Map.Entry<String, String> entry : request.getParams().entrySet()) {
                    formEncodingBuilder.add(entry.getKey(), entry.getValue());
                }
                builder.post(formEncodingBuilder.build());
            }
        }
    }

    private static class UploadOkHttpRequestTranslator extends GetOkHttpRequestTranslator {
        @Override
        protected void addLast(com.squareup.okhttp.Request.Builder builder, Request request) {
            MultipartBuilder multipartBuilder = new MultipartBuilder().type(MultipartBuilder.FORM);
            // set params
            if (request.getParams().size() > 0) {
                for (Map.Entry<String, String> entry : request.getParams().entrySet()) {
                    multipartBuilder.addFormDataPart(entry.getKey(), entry.getValue());
                }
            }
            // set file
            if (request.getFileList().size() > 0) {
                for (String filePath : request.getFileList()) {
                    MediaType MEDIA_TYPE = null;
                    // image
                    if (filePath.endsWith(".jpg")) {
                        MEDIA_TYPE = MediaType.parse("image/jpg");
                    }
                    if (filePath.endsWith(".png")) {
                        MEDIA_TYPE = MediaType.parse("image/png");
                    }
                    if (filePath.endsWith(".gif")) {
                        MEDIA_TYPE = MediaType.parse("image/gif");
                    }
                    // video
                    if (filePath.endsWith(".3gp") || filePath.endsWith(".3gpp")) {
                        MEDIA_TYPE = MediaType.parse("video/3gpp");
                    }
                    if (filePath.endsWith(".mp4")) {
                        MEDIA_TYPE = MediaType.parse("video/mp4");
                    }
                    // audio
                    if (filePath.endsWith(".mp3")) {
                        MEDIA_TYPE = MediaType.parse("audio/mpeg");
                    }
                    // txt
                    if (filePath.endsWith(".txt")) {
                        MEDIA_TYPE = MediaType.parse("text/plain");
                    }
                    // html htm
                    if (filePath.endsWith(".html") || filePath.endsWith(".htm")) {
                        MEDIA_TYPE = MediaType.parse("text/html");
                    }
                    // xml
                    if (filePath.endsWith(".xml")) {
                        MEDIA_TYPE = MediaType.parse("text/xml");
                    }
                    // zip
                    if (filePath.endsWith(".zip")) {
                        MEDIA_TYPE = MediaType.parse("application/zip");
                    }
                    if (MEDIA_TYPE == null) continue;
                    multipartBuilder.addFormDataPart(request.getFileKey(), new File(filePath).getName(), RequestBody.create(MEDIA_TYPE, new File(filePath)));
                }
            }
        }
    }

}
