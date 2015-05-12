/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao;

import edu.unl.sigett.entity.PlazoEvaluacionTribunal;
import javax.ejb.Stateless;

/**
 *
 * @author jorge-luis
 */
@Stateless
public class PlazoEvaluacionTribunalFacade extends AbstractDao<PlazoEvaluacionTribunal> implements PlazoEvaluacionTribunalFacadeLocal {

    public PlazoEvaluacionTribunalFacade() {
        super(PlazoEvaluacionTribunal.class);
    }

}
