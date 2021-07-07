package com.ipgallery.esp.params;

import java.util.HashMap;
import java.util.Map;

public class CommandParamsBuilder {
    CommandParams commandParams = new CommandParams();
    Map<String, String> requestParamsMap = null;

    public CommandParamsBuilder() {
    }

    public CommandParamsBuilder setEntity(String entity) {
        this.commandParams.setEntity(entity);
        return this;
    }

    public CommandParamsBuilder setParams(String[] params) {
        if (this.commandParams.getParamsString() == null) {
            this.commandParams.setParams(params);
        }

        return this;
    }

    public CommandParamsBuilder setParamsString(String paramsString) {
        if (this.commandParams.getParams() == null) {
            this.commandParams.setParamsString(paramsString);
        }

        return this;
    }

    public CommandParamsBuilder setRequestParams(String requestParams) {
        this.commandParams.setRequestParams(requestParams);
        return this;
    }

    public CommandParamsBuilder setContent(String content) {
        this.commandParams.setContent(content);
        return this;
    }

    public CommandParamsBuilder setHeadersMap(Map<String, String> headersMap) {
        this.commandParams.setHeadersMap(headersMap);
        return this;
    }

    public CommandParamsBuilder setRequestParamsMap(Map<String, String> requestParamsMap) {
        this.commandParams.setRequestParamsMap(requestParamsMap);
        return this;
    }

    public CommandParamsBuilder addRequestParam(String key, String value) {
        if (this.requestParamsMap == null) {
            this.requestParamsMap = new HashMap();
        }

        this.requestParamsMap.put(key, value);
        this.commandParams.setRequestParamsMap(this.requestParamsMap);
        return this;
    }

    public CommandParams build() {
        return this.commandParams;
    }

}
