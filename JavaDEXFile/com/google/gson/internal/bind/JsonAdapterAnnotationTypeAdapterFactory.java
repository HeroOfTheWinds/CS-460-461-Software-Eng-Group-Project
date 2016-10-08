package com.google.gson.internal.bind;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;

public final class JsonAdapterAnnotationTypeAdapterFactory implements TypeAdapterFactory {
    private final ConstructorConstructor constructorConstructor;

    public JsonAdapterAnnotationTypeAdapterFactory(ConstructorConstructor constructorConstructor) {
        this.constructorConstructor = constructorConstructor;
    }

    static TypeAdapter<?> getTypeAdapter(ConstructorConstructor constructorConstructor, Gson gson, TypeToken<?> typeToken, JsonAdapter jsonAdapter) {
        TypeAdapter<?> typeAdapter;
        Class value = jsonAdapter.value();
        if (TypeAdapter.class.isAssignableFrom(value)) {
            typeAdapter = (TypeAdapter) constructorConstructor.get(TypeToken.get(value)).construct();
        } else if (TypeAdapterFactory.class.isAssignableFrom(value)) {
            typeAdapter = ((TypeAdapterFactory) constructorConstructor.get(TypeToken.get(value)).construct()).create(gson, typeToken);
        } else {
            throw new IllegalArgumentException("@JsonAdapter value must be TypeAdapter or TypeAdapterFactory reference.");
        }
        return typeAdapter != null ? typeAdapter.nullSafe() : typeAdapter;
    }

    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
        JsonAdapter jsonAdapter = (JsonAdapter) typeToken.getRawType().getAnnotation(JsonAdapter.class);
        return jsonAdapter == null ? null : getTypeAdapter(this.constructorConstructor, gson, typeToken, jsonAdapter);
    }
}
