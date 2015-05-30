/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlmallas.api.date;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 *
 * @author JorgeLuis
 */
public class DateResource implements Serializable {

    public Double calculaDuracionEnDias(Date fechaInicio, Date fechaFin, int diasNoContabilizados) {
        Integer ai, af, mi, mf, di, df = 0;
        ai = fechaInicio.getYear();
        af = fechaFin.getYear();
        mi = fechaInicio.getMonth();
        mf = fechaFin.getMonth();
        di = fechaInicio.getDate();
        df = fechaFin.getDate();
        Integer mesActual = mi;
        Integer anio = ai;
        Double dias = 0.0;
        boolean fin = true;
        while (fin) {
            if (mesActual == mi && ai == anio) {
                if (mi == mf && ai == af && df > di) {
                    dias += (df - di) - diasNoContabilizados;
                    fin = false;
                } else {
                    if ((af == ai && mf > mi) || (af > ai)) {
                        Calendar cal = new GregorianCalendar(anio, mesActual, 01);
                        dias += (cal.getActualMaximum(Calendar.DAY_OF_MONTH) - di) - diasNoContabilizados;
                        if (mesActual == 11) {
                            mesActual = 0;
                            anio++;
                        } else {
                            mesActual++;
                        }
                    } else {
                        fin = false;
                    }
                }
            } else {
                if (((mesActual > mi && anio >= ai) || (mesActual < mi && anio > ai)) && ((mesActual < mf && anio <= af) || (mesActual > mf && anio < af))) {
                    Calendar cal = new GregorianCalendar(anio, mesActual, 01);
                    dias += cal.getActualMaximum(Calendar.DAY_OF_MONTH) - diasNoContabilizados;
                    if (mesActual == 11) {
                        mesActual = 0;
                        anio++;
                    } else {
                        mesActual++;
                    }
                } else {
                    if (mesActual == mf && anio == af) {
                        dias += df - diasNoContabilizados;
                        fin = false;
                    } else {
                        Calendar cal = new GregorianCalendar(anio, mesActual, 01);
                        dias += cal.getActualMaximum(Calendar.DAY_OF_MONTH) - diasNoContabilizados;
                        if (mesActual == 11) {
                            mesActual = 0;
                            anio++;
                        } else {
                            mesActual++;
                        }
                    }
                }
            }

        }
        return dias;
    }

    public String deDateAString(Date fecha, String formato) {
        String fechaFormateada = "Fecha sin formato...";
        try {
            SimpleDateFormat formatoFecha = new SimpleDateFormat(formato);
            if (fecha != null) {
                fechaFormateada = formatoFecha.format(fecha);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fechaFormateada;
    }

    public Date DeStringADate(String fecha, String formato) {
        Date fechaDate = null;
        try {
            SimpleDateFormat formatoFecha = new SimpleDateFormat(formato);
            String strFecha = fecha;
            fechaDate = formatoFecha.parse(strFecha);
            return fechaDate;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
