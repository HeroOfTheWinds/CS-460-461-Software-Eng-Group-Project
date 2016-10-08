package com.upsight.android.internal.logger;

import com.google.gson.annotations.Expose;
import com.upsight.android.logger.UpsightLogger.Level;
import com.upsight.android.persistence.annotation.UpsightStorableIdentifier;
import com.upsight.android.persistence.annotation.UpsightStorableType;

@UpsightStorableType("com.upsight.message.log")
public final class LogMessage {
    @UpsightStorableIdentifier
    public String id;
    @Expose
    private Level level;
    @Expose
    private String message;
    @Expose
    private String tag;
    @Expose
    private String throwableString;

    LogMessage() {
    }

    LogMessage(LogMessage logMessage) {
        this(logMessage.tag, logMessage.level, logMessage.message, logMessage.throwableString);
    }

    LogMessage(String str, Level level, String str2, String str3) {
        this.tag = str;
        this.level = level;
        this.message = str2;
        this.throwableString = str3;
    }

    public boolean equals(Object obj) {
        if (this != obj) {
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            LogMessage logMessage = (LogMessage) obj;
            if (this.id != null) {
                if (!this.id.equals(logMessage.id)) {
                    return false;
                }
            } else if (logMessage.id != null) {
                return false;
            }
        }
        return true;
    }

    public Level getLevel() {
        return this.level;
    }

    public String getMessage() {
        return this.message;
    }

    public String getTag() {
        return this.tag;
    }

    public String getThrowableString() {
        return this.throwableString;
    }

    public int hashCode() {
        return this.id != null ? this.id.hashCode() : 0;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public void setMessage(String str) {
        this.message = str;
    }

    public void setTag(String str) {
        this.tag = str;
    }

    public void setThrowableString(String str) {
        this.throwableString = str;
    }
}
