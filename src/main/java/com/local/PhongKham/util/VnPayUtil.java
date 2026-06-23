package com.local.PhongKham.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class VnPayUtil {

    public static String hmacSHA512(
            String key,
            String data
    ) {
        try {

            Mac hmac512 = Mac.getInstance("HmacSHA512");

            SecretKeySpec secretKey =
                    new SecretKeySpec(
                            key.getBytes(),
                            "HmacSHA512"
                    );

            hmac512.init(secretKey);

            byte[] bytes =
                    hmac512.doFinal(data.getBytes());

            StringBuilder hash = new StringBuilder();

            for (byte b : bytes) {
                hash.append(String.format("%02x", b));
            }

            return hash.toString();

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static String getPaymentURL(
            Map<String, String> params,
            boolean encodeKey
    ) {

        List<String> fieldNames =
                new ArrayList<>(params.keySet());

        Collections.sort(fieldNames);

        StringBuilder sb = new StringBuilder();

        Iterator<String> itr = fieldNames.iterator();

        while (itr.hasNext()) {

            String fieldName = itr.next();

            String fieldValue = params.get(fieldName);

            if ((fieldValue != null)
                    && (fieldValue.length() > 0)) {

                sb.append(fieldName);
                sb.append("=");

                sb.append(
                        URLEncoder.encode(
                                fieldValue,
                                StandardCharsets.UTF_8
                        )
                );

                if (itr.hasNext()) {
                    sb.append("&");
                }
            }
        }

        return sb.toString();
    }
}