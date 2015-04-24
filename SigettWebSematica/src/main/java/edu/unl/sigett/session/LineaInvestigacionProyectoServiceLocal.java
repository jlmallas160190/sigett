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
public interface LineaInvestigacionProyectoServiceLocal {

    void read(String path);

    void write(Map parametros);
}
