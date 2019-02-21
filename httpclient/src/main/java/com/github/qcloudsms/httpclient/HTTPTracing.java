package com.github.qcloudsms.httpclient;


public class HTTPTracing {

    // type: timestamp, unit: milliseconds
    protected long beginTime;
    protected long writeBeginTime;
    protected long readBeginTime;
    protected long endTime;

    public HTTPTracing() {
        beginTime = 0;
        writeBeginTime = 0;
        readBeginTime = 0;
        endTime = 0;
    }

    public HTTPTracing(long beginTime) {
        assert beginTime > 0;

        this.beginTime = beginTime;

        writeBeginTime = 0;
        readBeginTime = 0;
        endTime = 0;
    }

    public void setBeginTime(long beginTime) {
        this.beginTime = beginTime;
    }

    public void setWriteBeginTime(long writeBeginTime) {
        this.writeBeginTime = writeBeginTime;
    }

    public void setReadBeginTime(long readBeginTime) {
        this.readBeginTime = readBeginTime;
    }

    public long getReadBeginTime() {
        return this.readBeginTime;
    }

    public long getWriteBeginTime() {
        return this.writeBeginTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long totalTime() {
        if (beginTime <= 0 || endTime <= 0) {
            return -1;
        }
        return endTime - beginTime;
    }

    public long readTime() {
        if (readBeginTime <= 0 || endTime <= 0) {
            return -1;
        }
        return endTime - readBeginTime;
    }

    public long writeTime() {
        if (writeBeginTime <= 0 || readBeginTime <= 0) {
            return -1;
        }
        return readBeginTime - writeBeginTime;
    }

    public long beforeWriteTime() {
        if (writeBeginTime <= 0) {
            return -1;
        }
        return writeBeginTime - beginTime;
    }
}
