# FLINK OKHTTP SINK
OkHttp3 based async http sink for Apache Flink
 

## Use:
```
.map(postJson -> {
    String rcid = new UUID().toString();
    String apiPath = "/base_api/specific_api";
    CommandParams commandParams = new CommandParamsBuilder()
            .setEntity(HOSTPORT) // "localhost:3000 / http://example.com / https:secure-domain.com"
            .setParamsString(apiPath)
            .setContent(postJson.toString())
            .build();
    return commandParams.setRcid(rcid);
})
.addSink(new AsyncHttpSink("POST"))
.name("post-sink");
```
