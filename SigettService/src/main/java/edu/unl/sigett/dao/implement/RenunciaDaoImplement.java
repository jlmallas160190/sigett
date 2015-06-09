/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao.implement;

import edu.unl.sigett.dao.AbstractDao;
import edu.unl.sigett.dao.RenunciaDao;
import edu.unl.sigett.entity.Renuncia;
import javax.ejb.Stateless;

/**
 *
 * @author JorgeLuis
 */
@Stateless
public class RenunciaDaoImplement extends AbstractDao<Renuncia> implements RenunciaDao {

    public RenunciaDaoImplement() {
        super(Renuncia.class);
    }

}
