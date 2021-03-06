/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlmallas.util.service;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jorge-luis
 */
public class UtilServiceImplement implements UtilService {

    @Override
    public byte[] obtenerBytes(File file) {
        ByteArrayOutputStream ous = null;
        @SuppressWarnings("UnusedAssignment")
        InputStream ios = null;
        try {
            byte[] buffer = new byte[4096];
            ous = new ByteArrayOutputStream();
            ios = new FileInputStream(file);
            @SuppressWarnings("UnusedAssignment")
            int read = 0;
            while ((read = ios.read(buffer)) != -1) {
                ous.write(buffer, 0, read);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(UtilServiceImplement.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(UtilServiceImplement.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ous.toByteArray();
    }

    @Override
    public Double calculaDuracion(Date fechaInicio, Date fechaFin,Integer diasNoContabilizados) {
        @SuppressWarnings("UnusedAssignment")
        Integer anioInicio, anioFin, mesInicio, mesFin, diaInicio, diaFin = 0;
        anioInicio = fechaInicio.getYear();
        anioFin = fechaFin.getYear();
        mesInicio = fechaInicio.getMonth();
        mesFin = fechaFin.getMonth();
        diaInicio = fechaInicio.getDate();
        diaFin = fechaFin.getDate();
        Integer mesActual = mesInicio;
        Integer anio = anioInicio;
        Double dias = 0.0;
        boolean fin = true;
        while (fin) {
            if (mesActual == mesInicio && anioInicio == anio) {
                if (mesInicio == mesFin && anioInicio == anioFin && diaFin > diaInicio) {
                    dias += (diaFin - diaInicio) - diasNoContabilizados;
                    fin = false;
                    continue;
                }
                if ((anioFin == anioInicio && mesFin > mesInicio) || (anioFin > anioInicio)) {
                    Calendar cal = new GregorianCalendar(anio, mesActual, 01);
                    dias += (cal.getActualMaximum(Calendar.DAY_OF_MONTH) - diaInicio) - diasNoContabilizados;
                    if (mesActual == 11) {
                        mesActual = 0;
                        anio++;
                        continue;
                    }
                    mesActual++;
                    continue;
                }
                fin = false;
                continue;
            }
            if (((mesActual > mesInicio && anio >= anioInicio) || (mesActual < mesInicio && anio > anioInicio)) && ((mesActual < mesFin && anio <= anioFin) || (mesActual > mesFin && anio < anioFin))) {
                Calendar cal = new GregorianCalendar(anio, mesActual, 01);
                dias += cal.getActualMaximum(Calendar.DAY_OF_MONTH) - diasNoContabilizados;
                if (mesActual == 11) {
                    mesActual = 0;
                    anio++;
                    continue;
                }
                mesActual++;
                continue;
            }

            if (mesActual == mesFin && anio == anioFin) {
                dias += diaFin - diasNoContabilizados;
                fin = false;
                continue;
            }
            Calendar cal = new GregorianCalendar(anio, mesActual, 01);
            dias += cal.getActualMaximum(Calendar.DAY_OF_MONTH) - diasNoContabilizados;
            if (mesActual == 11) {
                mesActual = 0;
                anio++;
            } else {
                mesActual++;
            }
        }
        if (dias < 0) {
            return 0.0;
        }
        return dias;
    }

    @Override
    public Date parserFecha(String fecha, String formato) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formato);
            return simpleDateFormat.parse(fecha);
        } catch (ParseException ex) {
            Logger.getLogger(UtilServiceImplement.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public void generaDocumento(File file, byte[] bytes) {
        try {
            FileOutputStream fos = new FileOutputStream(file);
            try (BufferedOutputStream bos = new BufferedOutputStream(fos)) {
                bos.write(bytes);
                bos.flush();
            }

        } catch (IOException ex) {
            Logger.getLogger(UtilServiceImplement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String formatoFecha(Date fecha, String formato) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formato);
        return simpleDateFormat.format(fecha);
    }

}
