/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlmallas.util.service;

import java.io.File;
import java.util.Date;

/**
 *
 * @author jorge-luis
 */
public interface UtilService {

    byte[] obtenerBytes(final File file);

    void generaDocumento(final File file, final byte[] bytes);

    Double calculaDuracion(Date fechaInicio, Date fechaFin, Integer diasNoContabilizados);

    Date parserFecha(final String fecha, String formato);

    String formatoFecha(final Date fecha, String formato);
}
