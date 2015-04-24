/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.jlmallas.academico.service;

import edu.jlmallas.academico.dao.AbstractFacade;
import edu.jlmallas.academico.entity.EstadoEstudianteCarrera;
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
public class EstadoEstudianteCarreraFacade extends AbstractFacade<EstadoEstudianteCarrera> implements EstadoEstudianteCarreraFacadeLocal {

    public EstadoEstudianteCarreraFacade() {
        super(EstadoEstudianteCarrera.class);
    }

    @Override
    public EstadoEstudianteCarrera buscarPorCodigo(String codigo) {
        List<EstadoEstudianteCarrera> estados = new ArrayList<>();
        try {
            Query query = em.createQuery("SELECT e FROM EstadoEstudianteCarrea e WHERE" + " (e.codigo=:codigo)");
            query.setParameter("codigo", codigo);
            estados = query.getResultList();
            return !estados.isEmpty() ? estados.get(0) : null;
        } catch (Exception e) {
        }
        return null;
    }
}
