/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.session;

import edu.unl.sigett.entity.EstructuraDocumentoProyecto;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jorge-luis
 */
@Local
public interface EstructuraDocumentoProyectoFacadeLocal {

    void create(EstructuraDocumentoProyecto estructuraDocumentoProyecto);

    void edit(EstructuraDocumentoProyecto estructuraDocumentoProyecto);

    void remove(EstructuraDocumentoProyecto estructuraDocumentoProyecto);

    EstructuraDocumentoProyecto find(Object id);

    List<EstructuraDocumentoProyecto> findAll();

    List<EstructuraDocumentoProyecto> findRange(int[] range);

    int count();

}
