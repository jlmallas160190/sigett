/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao;

import edu.unl.sigett.entity.EstudianteUsuario;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author JorgeLuis
 */
@Stateless
public class EstudianteUsuarioFacade extends AbstractDao<EstudianteUsuario> implements EstudianteUsuarioFacadeLocal {


    public EstudianteUsuarioFacade() {
        super(EstudianteUsuario.class);
    }

    @Override
    public EstudianteUsuario buscarPorEstudiante(Long id) {
        List<EstudianteUsuario> estudianteUsuarios = new ArrayList<>();
        try {
            Query query = em.createQuery("Select e from EstudianteUsuario e where " + "e.estudianteId.id=:id");
            query.setParameter("id", id);
            estudianteUsuarios = query.getResultList();
            return !estudianteUsuarios.isEmpty() ? estudianteUsuarios.get(0) : null;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
}
