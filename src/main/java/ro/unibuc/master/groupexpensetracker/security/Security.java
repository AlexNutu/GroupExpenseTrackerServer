//package ro.unibuc.master.groupexpensetracker.security;
//
//import java.nio.charset.Charset;
//import java.security.MessageDigest;
//import java.security.NoSuchAlgorithmException;
//
//public class Security {
//    public static String computeMD5(String input) throws NoSuchAlgorithmException {
//        MessageDigest md = MessageDigest.getInstance("MD5");
//        byte[] data = input.getBytes(Charset.forName("UTF-8"));
//        md.update(data);
//        byte[] digest = md.digest();
//        StringBuffer sb = new StringBuffer();
//        for (byte b : digest) {
//            sb.append(String.format("%02x", b & 0xff));
//        }
//        return new String(sb);
//    }
//}
