package com.example.datnsum24sd01.worker;

import java.util.UUID;

public class AutoGenCodeRandom {
    public static String genUUID(){
        return UUID.randomUUID().toString().substring(0, 6);
    }
}
