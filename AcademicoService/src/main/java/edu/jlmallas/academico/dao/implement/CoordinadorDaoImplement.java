/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.jlmallas.academico.dao.implement;

import edu.jlmallas.academico.dao.AbstractDao;
import edu.jlmallas.academico.dao.AbstractDao;
import edu.jlmallas.academico.dao.CoordinadorDao;
import edu.jlmallas.academico.entity.Coordinador;
import javax.ejb.Stateless;

/**
 *
 * @author JorgeLuis
 */
@Stateless
public class CoordinadorDaoImplement extends AbstractDao<Coordinador> implements CoordinadorDao {

    public CoordinadorDaoImplement() {
        super(Coordinador.class);
    }

}
