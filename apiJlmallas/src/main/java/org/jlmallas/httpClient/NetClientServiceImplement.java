/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlmallas.httpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;

/**
 *
 * @author jorge-luis
 */
public class NetClientServiceImplement implements NetClientService{

    private static final Logger LOG = Logger.getLogger(NetClientServiceImplement.class.getName());
    
    @Override
    public String response(final ConexionDTO conexionDTO) {
        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(conexionDTO.getUrl() != null ? conexionDTO.getUrl() : "");
            String loginAdminPassword = conexionDTO.getUsuario() + ":"
                    + conexionDTO.getClave();
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
        } catch (IOException e) {
            LOG.info(e.getMessage());
        }
        
        return sb.toString();
    }
}
