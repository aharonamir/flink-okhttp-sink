package com.ipgallery.esp.params;
import java.util.Map;

public class CommandParams {

    String scheme = null;
    String entity = null;
    String[] params;
    String paramsString;
    String requestParams;
    String content;
    Map<String, String> headersMap = null;
    Map<String, String> requestParamsMap = null;
    String rcid = null;

    public CommandParams() {
    }

    public CommandParams(String entity, String paramsString, String requestParams, String content, Map<String, String> headersMap) {
        this.entity = entity;
        this.params = null;
        this.paramsString = paramsString;
        this.requestParams = requestParams;
        this.content = content;
        this.headersMap = headersMap;
    }

    public CommandParams(String entity, String[] params, String requestParams, String content, Map<String, String> headersMap) {
        this.entity = entity;
        this.params = params;
        this.paramsString = null;
        this.requestParams = requestParams;
        this.content = content;
        this.headersMap = headersMap;
    }

    public String getScheme() {
        return this.scheme;
    }

    public CommandParams setScheme(String scheme) {
        this.scheme = scheme;
        return this;
    }

    public String getEntity() {
        return this.entity;
    }

    public String[] getParams() {
        return this.params;
    }

    public String getParamsString() {
        return this.paramsString;
    }

    public String getRequestParams() {
        return this.requestParams;
    }

    public Map<String, String> getRequestParamsMap() {
        return this.requestParamsMap;
    }

    public String getContent() {
        return this.content;
    }

    public Map<String, String> getHeadersMap() {
        return this.headersMap;
    }

    public CommandParams setEntity(String entity) {
        this.entity = entity;
        return this;
    }

    public CommandParams setParams(String[] params) {
        this.params = params;
        return this;
    }

    public CommandParams setParamsString(String paramsString) {
        this.paramsString = paramsString;
        return this;
    }

    public CommandParams setRequestParams(String requestParams) {
        this.requestParams = requestParams;
        return this;
    }

    public CommandParams setRequestParamsMap(Map<String, String> requestParamsMap) {
        this.requestParamsMap = requestParamsMap;
        return this;
    }

    public CommandParams setContent(String content) {
        this.content = content;
        return this;
    }

    public CommandParams setHeadersMap(Map<String, String> headersMap) {
        this.headersMap = headersMap;
        return this;
    }

    public CommandParams setRcid(String rcid) {
        this.rcid = rcid;
        return this;
    }

    public String getRcid() {
        return this.rcid;
    }

  }