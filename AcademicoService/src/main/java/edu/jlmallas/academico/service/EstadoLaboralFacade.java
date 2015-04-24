/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.jlmallas.academico.service;

import edu.jlmallas.academico.dao.AbstractFacade;
import edu.jlmallas.academico.entity.EstadoLaboral;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author JorgeLuis
 */
@Stateless
public class EstadoLaboralFacade extends AbstractFacade<EstadoLaboral> implements EstadoLaboralFacadeLocal {

    public EstadoLaboralFacade() {
        super(EstadoLaboral.class);
    }

    @Override
    public EstadoLaboral buscarPorTipoContratoNombre(String nombre) {
        List<EstadoLaboral> estados = new ArrayList<>();
        try {
            Query query = em.createQuery("SELECT e FROM EstadoLaboral e WHERE" + " (LOWER(e.tipoContratoId.nombre) =LOWER(:nombre))");
            query.setParameter("nombre", nombre);
            estados = query.getResultList();
            return !estados.isEmpty() ? estados.get(0) : null;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
}
