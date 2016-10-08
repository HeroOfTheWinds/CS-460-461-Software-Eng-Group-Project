package com.upsight.mediation.log;

import java.util.ArrayList;

public class LogBuffer {
    public final String[] buffer;
    public final int bufferSize;
    public int end;
    public final int msgLength;

    public LogBuffer(int i, int i2) {
        if (i <= 0) {
            throw new IllegalArgumentException("Buffer size must be greater than 0");
        } else if (i2 <= 0) {
            throw new IllegalArgumentException("Message length must be greater than 0");
        } else {
            this.bufferSize = i;
            this.buffer = new String[i];
            this.msgLength = i2;
            this.end = -1;
        }
    }

    public void append(String str) {
        if (str == null) {
            throw new IllegalArgumentException("Value may not be null");
        }
        int i = this.end + 1;
        this.end = i;
        if (i >= this.bufferSize) {
            this.end = 0;
        }
        if (str.length() > this.msgLength) {
            this.buffer[this.end] = str.substring(0, this.msgLength);
        } else {
            this.buffer[this.end] = str;
        }
    }

    public void append(String str, String str2, String str3) {
        append("," + str2 + "," + str3);
    }

    public String[] getLog() {
        int startIndex = getStartIndex();
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < this.bufferSize; i++) {
            Object obj = this.buffer[(startIndex + i) % this.bufferSize];
            if (obj == null) {
                break;
            }
            arrayList.add(obj);
        }
        return (String[]) arrayList.toArray(new String[arrayList.size()]);
    }

    public int getStartIndex() {
        int i = this.buffer[this.buffer.length + -1] == null ? 0 : this.end + 1;
        return i >= this.bufferSize ? 0 : i;
    }
}
