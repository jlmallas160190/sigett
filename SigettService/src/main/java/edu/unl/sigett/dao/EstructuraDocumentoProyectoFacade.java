/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.unl.sigett.dao;

import edu.unl.sigett.entity.EstructuraDocumentoProyecto;
import javax.ejb.Stateless;

/**
 *
 * @author jorge-luis
 */
@Stateless
public class EstructuraDocumentoProyectoFacade extends AbstractDao<EstructuraDocumentoProyecto> implements EstructuraDocumentoProyectoFacadeLocal {

    public EstructuraDocumentoProyectoFacade() {
        super(EstructuraDocumentoProyecto.class);
    }
    
}
