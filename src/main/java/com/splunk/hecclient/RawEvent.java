package com.splunk.hecclient;

import com.splunk.hecclient.errors.InvalidEventException;

import java.io.UnsupportedEncodingException;

/**
 * Created by kchen on 10/17/17.
 */
public class RawEvent extends Event {
    public RawEvent(Object data, Object tied) {
        super(data, tied);
    }

    @Override
    public byte[] getBytes() {
        if (bytes != null) {
            return bytes;
        }

        if (data instanceof String) {
            String s = (String) data;
            try {
                bytes = s.getBytes("UTF-8");
            } catch (UnsupportedEncodingException ex) {
                log.error("failed to encode as UTF-8: " + ex);
                throw new InvalidEventException("Not UTF-8 encodable: " + ex.getMessage());
            }
        } else if (data instanceof byte[]) {
            bytes = (byte[]) data;
        } else {
            // JSON object
            try {
                bytes = jsonMapper.writeValueAsBytes(data);
            } catch (Exception ex) {
                log.error("Invalid json data:" + ex);
                throw new InvalidEventException("Failed to json marshal the data: " + ex.getMessage());
            }
        }
        return bytes;
    }

    @Override
    public String toString() {
        try {
            return new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            log.error("failed to decode as UTF-8: ", ex);
            throw new InvalidEventException("Not UTF-8 decodable: " + ex.getMessage());
        }
    }
}
