/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlmallas.util;

import java.io.File;
import java.util.Date;

/**
 *
 * @author jorge-luis
 */
public interface UtilService {

    byte[] obtenerBytes(final File file);

    Double calculaDuracion(Date fechaInicio, Date fechaFin, int diasNoContabilizados);

    Date getFecha(final String fecha, String formato);
}
