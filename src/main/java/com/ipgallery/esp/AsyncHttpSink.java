package com.ipgallery.esp;

import okhttp3.*;
import org.apache.flink.api.common.accumulators.Histogram;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import com.ipgallery.esp.defs.Constants;
import com.ipgallery.esp.params.CommandParams;

public class AsyncHttpSink extends RichSinkFunction<CommandParams> {

    /**
     *
     */
    private static final long serialVersionUID = -2260707049749352200L;
    public static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
    private final int HTTP_SOCKET_READ_TIMEOUT = Integer.valueOf(System.getProperty("http.client.read.timeout.seconds","30"));
    private final int HTTP_SOCKET_CONNECT_TIMEOUT = Integer.valueOf(System.getProperty("http.client.connect.timeout.seconds","10"));

    String scheme = Constants.HTTP_SCHEME; // default scheme
    OkHttpClient client = null;
    final String method;
    private Histogram httpStatusesAccumulator;
    private static final Logger logger = LoggerFactory.getLogger(AsyncHttpSink.class);
    public AsyncHttpSink(String method) {
        this.method = method;
    }

    @Override
    public void open(Configuration parameters) {
        initClient();
        httpStatusesAccumulator = getRuntimeContext().getHistogram("http_statuses");
    }

    @Override
    public void close() {
        httpStatusesAccumulator.resetLocal();
    }

    private void initClient() {
        client = new OkHttpClient.Builder()
                .connectTimeout(HTTP_SOCKET_CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(HTTP_SOCKET_READ_TIMEOUT, TimeUnit.SECONDS)
                .build();

    }

    @Override
    public void invoke(CommandParams commandParams, Context context)  {

        try {
            Request request = createRequest(method,commandParams);
            final String requestUrl = request.url().toString();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException ex) {
                    logger.error(String.format("AsyncHttpSink >> Error: %s",ex.toString()));
                    httpStatusesAccumulator.add(-1);
                }

                @Override
                public void onResponse(Call call, Response response)  {
                    logger.info(String.format("AsyncHttpSink >> Reuqest to: %s, succeed",requestUrl));
                    httpStatusesAccumulator.add(response.code());
                    response.close();
                }
            });

        } catch (Exception exp){
            if (logger != null)
                logger.error(getClass().getName() + " >> " + exp.toString());
        }

    }

    private Request createRequest(String method, CommandParams commandParams) {
        final Request.Builder builder = new Request.Builder();
        // Add url
        URI uri = buildUri(commandParams);
        builder.url(uri.toString());
        // method + body , if exists
        RequestBody requestBody = null;
        if (commandParams.getContent() != null) // currently assuming json
            requestBody = RequestBody.create(MEDIA_TYPE_JSON,commandParams.getContent());

        builder.method(method,requestBody);
        // headers
        if (commandParams.getHeadersMap() != null) {
            commandParams.getHeadersMap().entrySet().forEach(header -> builder.addHeader(header.getKey(),header.getValue()));
        }
        // add command-id
        if (commandParams.getRcid() != null)
            builder.addHeader(Constants.RCID_HEADER,commandParams.getRcid());
        return builder.build();
    }

    private URI buildUri(CommandParams cmdParams) throws NumberFormatException, IllegalArgumentException{
        Objects.requireNonNull(cmdParams.getEntity(), "entity must not be null");
        try {
            final StringBuilder fullPath = buildUrlNoQueryParams(cmdParams);
            // checking for request(query) params
            if (cmdParams.getRequestParams() !=  null){
                fullPath.append('?').append(cmdParams.getRequestParams());
            }
            URIBuilder uriBuilder = new URIBuilder(fullPath.toString());

            /**
             * add query params
             */
            if (cmdParams.getRequestParamsMap() != null) {
                cmdParams.getRequestParamsMap()
                        .entrySet()
                        .forEach(stringStringEntry -> uriBuilder.addParameter(stringStringEntry.getKey(),stringStringEntry.getValue()));
            }
            return uriBuilder.build();
        } catch(URISyntaxException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    private StringBuilder buildUrlNoQueryParams(CommandParams cmdParams) {
        final StringBuilder fullPath = new StringBuilder(Constants.STRING_INITIAL_CAPACITY);
        // scheme
        if (cmdParams.getScheme() != null)
            fullPath.append(cmdParams.getScheme());
        else
            fullPath.append(this.scheme);
        fullPath.append("://").append(cmdParams.getEntity());
        if (cmdParams.getParamsString() != null) {
            if (cmdParams.getParamsString().startsWith("/")) {
                fullPath.append(cmdParams.getParamsString());
            } else {
                fullPath.append('/').append(cmdParams.getParamsString());
            }
        } else if( cmdParams.getParams() != null && cmdParams.getParams().length > 0) {
            for(int i = 0; i < cmdParams.getParams().length; ++i) {
                fullPath.append('/').append(cmdParams.getParams()[i]);
            }
        }
        return fullPath;
    }

}
