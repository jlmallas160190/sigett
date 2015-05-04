/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlmallas.api.http;

import org.jlmallas.api.http.dto.SeguridadHttp;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *
 * @author jorge-luis
 */
public class UrlConexion {

    public String conectar(SeguridadHttp seguridad) {
        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(seguridad.getUrl() != null ? seguridad.getUrl() : "");
            String loginAdminPassword = seguridad.getUsuario() + ":"
                    + seguridad.getClave();
            String encoded = new sun.misc.BASE64Encoder().encode(loginAdminPassword.getBytes());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestProperty("Authorization", "Basic " + encoded);
            InputStream is = conn.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sb.toString();
    }
}
