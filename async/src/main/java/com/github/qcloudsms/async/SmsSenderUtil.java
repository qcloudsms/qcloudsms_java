package com.github.qcloudsms.async;

import java.lang.StringBuffer;
import java.util.Random;
import java.util.ArrayList;
import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;


public class SmsSenderUtil {

    public static boolean isNotEmpty(String s) {
        if (s == null || s.isEmpty())
            return false;
        return true;
    }

    public static long getCurrentTime() {
        return System.currentTimeMillis() / 1000;
    }

    public static long getRandom() {
        return (new Random(System.currentTimeMillis())).nextInt(900000) + 100000;
    }

    public static String calculateSignature(String appkey, long random, long time,
            String phoneNumber) {

        StringBuffer buffer = new StringBuffer("appkey=")
            .append(appkey)
            .append("&random=")
            .append(random)
            .append("&time=")
            .append(time)
            .append("&mobile=")
            .append(phoneNumber);

        return sha256(buffer.toString());
    }

    public static String calculateSignature(String appkey, long random, long time,
            String[] phoneNumbers) {

        StringBuffer buffer = new StringBuffer("appkey=")
            .append(appkey)
            .append("&random=")
            .append(random)
            .append("&time=")
            .append(time)
            .append("&mobile=");

        if (phoneNumbers.length > 0) {
            buffer.append(phoneNumbers[0]);
            for (int i = 1; i < phoneNumbers.length; i++) {
                buffer.append(",");
                buffer.append(phoneNumbers[i]);
            }
        }

        return sha256(buffer.toString());
    }

    public static String calculateSignature(String appkey, long random, long time,
            ArrayList<String> phoneNumbers) {
        return calculateSignature(appkey, random, time, phoneNumbers.toArray(new String[0]));
    }

    public static String calculateSignature(String appkey, long random, long time) {

        StringBuffer buffer = new StringBuffer("appkey=")
            .append(appkey)
            .append("&random=")
            .append(random)
            .append("&time=")
            .append(time);

        return sha256(buffer.toString());
    }

    public static String calculateFStatusSignature(String appkey, long random,
            long time, String fid) {

        StringBuffer buffer = new StringBuffer("appkey=")
            .append(appkey)
            .append("&random=")
            .append(random)
            .append("&time=")
            .append(time)
            .append("&fid=")
            .append(fid);

        return sha256(buffer.toString());
    }

    public static String calculateAuth(String appkey, long random, long time, String fileSha1Sum) {
        StringBuffer buffer = new StringBuffer("appkey=")
            .append(appkey)
            .append("&random=")
            .append(random)
            .append("&time=")
            .append(time)
            .append("&content-sha1=")
            .append(fileSha1Sum);

        return sha256(buffer.toString());
    }

    public static String sha1sum(String rawString) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            byte[] hash = digest.digest(rawString.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hash);
        } catch (NoSuchAlgorithmException e) {
            return "";
        }
    }

    public static String sha1sum(byte[] bytes) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            byte[] hash = digest.digest(bytes);
            return bytesToHex(hash);
        } catch (NoSuchAlgorithmException e) {
            return "";
        }
    }

    private static String bytesToHex(byte[] hash) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1)
                buf.append('0');
            buf.append(hex);
        }
        return buf.toString();
    }

    public static String sha256(String rawString) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(rawString.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hash);
        } catch (NoSuchAlgorithmException e) {
            return "";
        }
    }
}
