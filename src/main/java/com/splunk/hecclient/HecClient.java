package com.splunk.hecclient;

/**
 * Created by kchen on 10/17/17.
 */
public abstract class HecClient {
    public abstract void send(EventBatch batch);
}