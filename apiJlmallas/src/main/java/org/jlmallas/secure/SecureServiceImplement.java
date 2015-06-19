/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlmallas.secure;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 *
 * @author jorge-luis
 */
public class SecureServiceImplement implements SecureService {

    private static Cipher encryptC;
    private static Cipher decryptC;

    @Override
    public String encrypt(final Secure secureDTO) {
        try {
            encryptC = Cipher.getInstance("DES");
            KeySpec ks = new DESKeySpec(secureDTO.getKey().getBytes("UTF-8"));
            SecretKeyFactory kf = SecretKeyFactory.getInstance("DES");
            SecretKey ky = kf.generateSecret(ks);
            encryptC.init(Cipher.ENCRYPT_MODE, ky);
            return encrypt(secureDTO.getPassword());

        } catch (UnsupportedEncodingException | InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException e) {
            System.out.println(e);
        }

        return null;
    }

    @Override
    public String decrypt(final Secure secureDTO) {
        try {
            decryptC = Cipher.getInstance("DES");
            KeySpec ks = new DESKeySpec(secureDTO.getKey().getBytes("UTF-8"));
            SecretKeyFactory kf = SecretKeyFactory.getInstance("DES");
            SecretKey ky = kf.generateSecret(ks);
            decryptC.init(Cipher.DECRYPT_MODE, ky);
            return decrypt(secureDTO.getPassword());
        } catch (UnsupportedEncodingException | InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException e) {
            System.out.println(e);
        }
        return null;
    }

    public static String encrypt(String password) {
        try {
            byte[] utf8 = password.getBytes("UTF8");
            byte[] enc = encryptC.doFinal(utf8);
            return new sun.misc.BASE64Encoder().encode(enc);

        } catch (UnsupportedEncodingException | BadPaddingException | IllegalBlockSizeException e) {
            System.out.println(e);
        }
        return null;

    }

    public static String decrypt(String password) {
        try {
            byte[] dec = new sun.misc.BASE64Decoder().decodeBuffer(password);
            byte[] utf8 = decryptC.doFinal(dec);
            return new String(utf8, "UTF8");
        } catch (IOException | BadPaddingException | IllegalBlockSizeException e) {
            System.out.println(e);
        }
        return null;
    }
}
