/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao;

import edu.unl.sigett.entity.Renuncia;
import javax.ejb.Stateless;

/**
 *
 * @author JorgeLuis
 */
@Stateless
public class RenunciaFacade extends AbstractDao<Renuncia> implements RenunciaFacadeLocal {

    public RenunciaFacade() {
        super(Renuncia.class);
    }

}
