/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jlmallas.comun.dao.implement;

import com.jlmallas.comun.dao.ConfiguracionDao;
import com.jlmallas.comun.entity.Configuracion;
import com.jlmallas.comun.enumeration.ConfiguracionEnum;
import com.jlmallas.comun.dao.AbstractDao;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author jorge-luis
 */
@Stateless
public class ConfiguracionDaoImplement extends AbstractDao<Configuracion> implements ConfiguracionDao {

    private static Cipher encrypt;
    private static Cipher decrypt;

    public ConfiguracionDaoImplement() {
        super(Configuracion.class);
    }

    @Override
    public String getSecretKey() {
        BufferedReader br = null;
        String secretKey = "";
        try {
            String pathSecretKey = this.buscarPorCodigo(ConfiguracionEnum.SECRETKEY.getTipo()).getValor();
            String sCurrentLine;
            br = new BufferedReader(new FileReader(pathSecretKey));
            int count = 0;
            while ((sCurrentLine = br.readLine()) != null) {
                count++;
                if (count == 1) {
                    StringTokenizer st = new StringTokenizer(sCurrentLine, "='");
                    String value = "";
                    while (st.hasMoreElements()) {
                        value = st.nextToken();
                    }
                    secretKey = value;
                    break;
                }
            }

        } catch (IOException e) {
            System.out.println(e);
        }
        return secretKey;
    }

    @Override
    public Configuracion buscarPorCodigo(String codigo) {
        List<Configuracion> configuracionGenerals = new ArrayList<>();
        try {
            Query query = em.createNamedQuery("Configuracion.findByCodigo");
            query.setParameter("codigo", codigo);
            configuracionGenerals = query.getResultList();
            return !configuracionGenerals.isEmpty() ? configuracionGenerals.get(0) : null;
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    public String encriptaClave(String clave) {
        try {
            encrypt = Cipher.getInstance("DES");
            KeySpec ks = new DESKeySpec(getSecretKey().getBytes("UTF-8"));
            SecretKeyFactory kf = SecretKeyFactory.getInstance("DES");
            SecretKey ky = kf.generateSecret(ks);
            encrypt.init(Cipher.ENCRYPT_MODE, ky);
            return encrypt(clave);

        } catch (Exception e) {
            System.out.println(e);
        }

        return null;
    }

    @Override
    public String desencriptaClave(String clave) {
        try {
            decrypt = Cipher.getInstance("DES");
            KeySpec ks = new DESKeySpec(getSecretKey().getBytes("UTF-8"));
            SecretKeyFactory kf = SecretKeyFactory.getInstance("DES");
            SecretKey ky = kf.generateSecret(ks);
            decrypt.init(Cipher.DECRYPT_MODE, ky);
            return decrypt(clave);
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public static String encrypt(String password) {
        try {
            byte[] utf8 = password.getBytes("UTF8");
            byte[] enc = encrypt.doFinal(utf8);
            return new sun.misc.BASE64Encoder().encode(enc);

        } catch (Exception e) {
            System.out.println(e);
        }
        return null;

    }

    public static String decrypt(String password) {
        try {
            byte[] dec = new sun.misc.BASE64Decoder().decodeBuffer(password);
            byte[] utf8 = decrypt.doFinal(dec);
            return new String(utf8, "UTF8");
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
}
