package com.example.courses.data.model;

public class DataBinding {
    private static String uuidUser = "";
    private static String bearerToken = "";

    public static String getUuidUser() {
        return uuidUser;
    }

    public static void setUuidUser(String uuid) {
        uuidUser = uuid;
    }

    public static String getBearerToken() {
        return bearerToken;
    }

    public static void setBearerToken(String token) {
        bearerToken = "Bearer " + token;
    }
}
