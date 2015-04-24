/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.jlmallas.academico.service;

import edu.jlmallas.academico.dao.AbstractFacade;
import edu.jlmallas.academico.entity.EstadoMatricula;
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
public class EstadoMatriculaFacade extends AbstractFacade<EstadoMatricula> implements EstadoMatriculaFacadeLocal {

    public EstadoMatriculaFacade() {
        super(EstadoMatricula.class);
    }

    public EstadoMatricula buscarPorNombre(String nombre) {
        List<EstadoMatricula> estadoMatriculas = new ArrayList<>();
        try {
            Query query = em.createNamedQuery("EstadoMatricula.findByNombre");
            query.setParameter("nombre", nombre);
            estadoMatriculas = query.getResultList();
            return !estadoMatriculas.isEmpty() ? estadoMatriculas.get(0) : null;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
}
