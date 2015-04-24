/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.session;

import edu.unl.sigett.entity.ConfiguracionGeneral;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author JorgeLuis
 */
@Stateless
public class ConfiguracionGeneralFacade extends AbstractFacade<ConfiguracionGeneral> implements ConfiguracionGeneralFacadeLocal {

    @PersistenceContext(unitName = "sigettPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ConfiguracionGeneralFacade() {
        super(ConfiguracionGeneral.class);
    }

    @Override
    public String getSecretKey() {
        BufferedReader br = null;
        String secretKey = "";
        try {
            String pathSecretKey = find(1).getValor();
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
    public String timestampFormat(Date fecha) {
        BufferedReader br = null;
        String formato = "";
        String fechaFormateada = "Fecha sin formato...";
        try {
            String pathFormatFecha = find(1).getValor();
            String sCurrentLine;
            br = new BufferedReader(new FileReader(pathFormatFecha));
            int count = 0;
            while ((sCurrentLine = br.readLine()) != null) {
                count++;
                if (count == 2) {
                    StringTokenizer st = new StringTokenizer(sCurrentLine, "='");
                    String value = "";
                    while (st.hasMoreElements()) {
                        value = st.nextToken();
                    }
                    formato = value;
                    break;
                }
            }
            SimpleDateFormat formatoFecha = new SimpleDateFormat(formato);
            if (fecha != null) {
                fechaFormateada = formatoFecha.format(fecha);
            }
        } catch (IOException e) {
            System.out.println(e);
        }
        return fechaFormateada;
    }

    @Override
    public String dateFormat(Date fecha) {
        BufferedReader br = null;
        String formato = "";
        String fechaFormateada = "Fecha sin formato...";
        try {
            String pathFormatFecha = find(1).getValor();
            String sCurrentLine;
            br = new BufferedReader(new FileReader(pathFormatFecha));
            int count = 0;
            while ((sCurrentLine = br.readLine()) != null) {
                count++;
                if (count == 3) {
                    StringTokenizer st = new StringTokenizer(sCurrentLine, "='");
                    String value = "";
                    while (st.hasMoreElements()) {
                        value = st.nextToken();
                    }
                    formato = value;
                    break;
                }
            }
            SimpleDateFormat formatoFecha = new SimpleDateFormat(formato);
            if (fecha != null) {
                fechaFormateada = formatoFecha.format(fecha);
            }
        } catch (IOException e) {
            System.out.println(e);
        }
        return fechaFormateada;
    }

    @Override
    public String timeFormat(Date fecha) {
        BufferedReader br = null;
        String formato = "";
        String fechaFormateada = "Fecha sin formato...";
        try {
            String pathFormatFecha = find(1).getValor();
            String sCurrentLine;
            br = new BufferedReader(new FileReader(pathFormatFecha));
            int count = 0;
            while ((sCurrentLine = br.readLine()) != null) {
                count++;
                if (count == 4) {
                    StringTokenizer st = new StringTokenizer(sCurrentLine, "='");
                    String value = "";
                    while (st.hasMoreElements()) {
                        value = st.nextToken();
                    }
                    formato = value;
                    break;
                }
            }
            SimpleDateFormat formatoFecha = new SimpleDateFormat(formato);
            if (fecha != null) {
                fechaFormateada = formatoFecha.format(fecha);
            }
        } catch (IOException e) {
            System.out.println(e);
        }
        return fechaFormateada;
    }

    @Override
    public Date DeStringADate(String fecha) {
        Date fechaDate = null;
        try {
            SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
            String strFecha = fecha;
            fechaDate = formatoFecha.parse(strFecha);
            return fechaDate;
        } catch (Exception e) {
            return fechaDate;
        }
    }

    @Override
    public List<ConfiguracionGeneral> buscarPorNombre(String nombre) {
        try {
            Query query = em.createNamedQuery("ConfiguracionGeneral.findByNombre");
            query.setParameter("nombre", nombre);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String usuarioBd() {
        BufferedReader br = null;
        String usuario = "";
        try {
            String pathSecretKey = find(1).getValor();
            String sCurrentLine;
            br = new BufferedReader(new FileReader(pathSecretKey));
            int count = 0;
            while ((sCurrentLine = br.readLine()) != null) {
                count++;
                if (count == 6) {
                    StringTokenizer st = new StringTokenizer(sCurrentLine, "[]'");
                    String value = "";
                    while (st.hasMoreElements()) {
                        value = st.nextToken();
                    }
                    usuario = value;
                    break;
                }
            }

        } catch (IOException e) {
            System.out.println(e);
        }
        return usuario;
    }

    @Override
    public String claveBd() {
        BufferedReader br = null;
        String clave = "";
        try {
            String pathSecretKey = find(1).getValor();
            String sCurrentLine;
            br = new BufferedReader(new FileReader(pathSecretKey));
            int count = 0;
            while ((sCurrentLine = br.readLine()) != null) {
                count++;
                if (count == 7) {
                    StringTokenizer st = new StringTokenizer(sCurrentLine, "[]'");
                    String value = "";
                    while (st.hasMoreElements()) {
                        value = st.nextToken();
                    }
                    clave = value;
                    break;
                }
            }

        } catch (IOException e) {
            System.out.println(e);
        }
        return clave;
    }
}
