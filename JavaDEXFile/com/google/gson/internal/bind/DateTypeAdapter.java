package com.google.gson.internal.bind;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.bind.util.ISO8601Utils;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Date;
import java.util.Locale;

public final class DateTypeAdapter extends TypeAdapter<Date> {
    public static final TypeAdapterFactory FACTORY;
    private final DateFormat enUsFormat;
    private final DateFormat localFormat;

    /* renamed from: com.google.gson.internal.bind.DateTypeAdapter.1 */
    static final class C06931 implements TypeAdapterFactory {
        C06931() {
        }

        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
            return typeToken.getRawType() == Date.class ? new DateTypeAdapter() : null;
        }
    }

    static {
        FACTORY = new C06931();
    }

    public DateTypeAdapter() {
        this.enUsFormat = DateFormat.getDateTimeInstance(2, 2, Locale.US);
        this.localFormat = DateFormat.getDateTimeInstance(2, 2);
    }

    private Date deserializeToDate(String str) {
        Date parse;
        synchronized (this) {
            try {
                parse = this.localFormat.parse(str);
            } catch (ParseException e) {
                try {
                    parse = this.enUsFormat.parse(str);
                } catch (ParseException e2) {
                    try {
                        parse = ISO8601Utils.parse(str, new ParsePosition(0));
                    } catch (Throwable e3) {
                        throw new JsonSyntaxException(str, e3);
                    }
                }
            }
        }
        return parse;
    }

    public Date read(JsonReader jsonReader) throws IOException {
        if (jsonReader.peek() != JsonToken.NULL) {
            return deserializeToDate(jsonReader.nextString());
        }
        jsonReader.nextNull();
        return null;
    }

    public void write(JsonWriter jsonWriter, Date date) throws IOException {
        synchronized (this) {
            if (date == null) {
                jsonWriter.nullValue();
            } else {
                jsonWriter.value(this.enUsFormat.format(date));
            }
        }
    }
}
