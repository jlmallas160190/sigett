/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.session;

import java.util.Map;
import javax.ejb.Local;

/**
 *
 * @author jorge-luis
 */
@Local
public interface ProyectoServiceLocal {

    void read(String path);

    Map getTodo(Map parametros);

    Map getPorDocente(Map parametros);

    Map getPorCarrera(Map parametros);

    void write(Map parametros);
}
