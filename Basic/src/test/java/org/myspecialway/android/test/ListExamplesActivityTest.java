package org.myspecialway.android.test;

import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.myspecialway.android.sharelocation.channel.ConversionUtils;
import org.myspecialway.android.sharelocation.channel.LocationSource;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;




public class ListExamplesActivityTest  {

    @Test
    public void TautologyIsCool() throws JSONException{
        assertEquals(12, new LocationSource("123",12).color);
    }
}
