package com.murey.poster.postermaster.communication.message;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.murey.poster.postermaster.utils.LogUtils;

import java.lang.reflect.Type;

public class WebMessageFactory {

    public static class WebMessageDeserializer implements JsonDeserializer {
        @Override
        public WebMessage deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            WebMessage webMessage = null;
            LogUtils.d(LogUtils.DEBUG_TAG,"RESPONSE BODY ==> "+json.toString());
            try{
                webMessage = new Gson().fromJson(json.toString(),WebMessage.class);
            }catch(Exception e){
                LogUtils.d(LogUtils.DEBUG_TAG,"Exception SESSION ==> "+e.toString());
            }
            return webMessage;
        }
    }
}
