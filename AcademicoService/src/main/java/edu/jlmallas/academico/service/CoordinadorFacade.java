/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.jlmallas.academico.service;

import edu.jlmallas.academico.dao.AbstractFacade;
import edu.jlmallas.academico.entity.Coordinador;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author JorgeLuis
 */
@Stateless
public class CoordinadorFacade extends AbstractFacade<Coordinador> implements CoordinadorFacadeLocal {

    public CoordinadorFacade() {
        super(Coordinador.class);
    }

}
