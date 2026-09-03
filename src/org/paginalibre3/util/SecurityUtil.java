package org.paginalibre3.util;
 
import java.security.MessageDigest;
 
public class SecurityUtil {

    public static String hashSHA256(String base) {

        try {

            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            byte[] hash = digest.digest(base.getBytes("UTF-8"));

            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {

                String hex = Integer.toHexString(0xff & b);

                if (hex.length() == 1) hexString.append('0');

                hexString.append(hex);

            }

            return hexString.toString();

        } catch (Exception ex) {

            throw new RuntimeException(ex);

        }

    }

    public static String hashSHA256Password(String password) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
 