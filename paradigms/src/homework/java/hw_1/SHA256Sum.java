/**
 * Nariman Safiulin (woofilee)
 * File: SHA256Sum.java
 * Created on: Feb 29, 2016
 */

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.DigestInputStream;
import java.security.MessageDigest;

public class SHA256Sum {
    public static void main(String[] args) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        if (args.length > 0) {
            for (String filename : args) {
                for (byte md5byte : md.digest(Files.readAllBytes(Paths.get(filename)))) {
                    System.out.printf("%02X", md5byte);
                }
                System.out.println(" *" + filename);
            }
        } else {
            DigestInputStream dis = new DigestInputStream(System.in, md);
            byte[] buff = new byte[1 << 10];
            while (dis.read(buff) > 0);
            for (byte md5byte : md.digest()) {
                System.out.printf("%02X", md5byte);
            }
            System.out.println(" *-");
        }
    }
}
