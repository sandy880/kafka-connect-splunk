package com.splunk.hecclient;

import com.splunk.hecclient.errors.InvalidEventTypeException;

import org.apache.http.client.utils.URIBuilder;

/**
 * Created by kchen on 10/18/17.
 */
public class RawEventBatch extends EventBatch {
    private String index;
    private String source;
    private String sourcetype;
    private String host;
    private long time = -1;

    // index, source etc metadata is for the whole raw batch
    public RawEventBatch(String index, String source, String sourcetype, String host, long time) {
        this.index = index;
        this.source = source;
        this.sourcetype = sourcetype;
        this.host = host;
        this.time = time;
    }

    public void add(Event event) throws InvalidEventTypeException {
        if (event instanceof RawEvent) {
            events.add(event);
            len += event.length();
        } else {
            throw new InvalidEventTypeException("only RawEvent can be add to RawEventBatch");
        }
    }

    @Override
    public final String getRestEndpoint() {
        return "/services/collector/raw" + getMetadataParams();
    }

    @Override
    public String getContentType() {
        return "text/plain; profile=urn:splunk:event:1.0; charset=utf-8";
    }

    private String getMetadataParams() {
        URIBuilder params = new URIBuilder();
        putIfPresent(index, "index=", params);
        putIfPresent(sourcetype,"sourcetype=", params);
        putIfPresent(source,"source=", params);
        putIfPresent(host,"host=", params);

        if (time != -1) {
            params.addParameter("time",  String.valueOf(time));
        }

        return params.toString();
    }

    private static void putIfPresent(String val, String tag, URIBuilder params) {
        if (val != null && !val.isEmpty()) {
            params.addParameter(tag,  val);
        }
    }
}