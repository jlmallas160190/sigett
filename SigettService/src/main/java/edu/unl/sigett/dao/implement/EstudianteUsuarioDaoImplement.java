/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao.implement;

import edu.unl.sigett.dao.AbstractDao;
import edu.unl.sigett.dao.EstudianteUsuarioDao;
import edu.unl.sigett.entity.EstudianteUsuario;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author JorgeLuis
 */
@Stateless
public class EstudianteUsuarioDaoImplement extends AbstractDao<EstudianteUsuario> implements EstudianteUsuarioDao {

    public EstudianteUsuarioDaoImplement() {
        super(EstudianteUsuario.class);
    }

    @Override
    public List<EstudianteUsuario> buscar(EstudianteUsuario estudianteUsuario) {
        StringBuilder sql = new StringBuilder();
        HashMap<String, Object> parametros = new HashMap<>();
        sql.append("Select e from EstudianteUsuario e  WHERE 1=1 ");
        if (estudianteUsuario.getEstudianteId() != null) {
            sql.append(" and e.estudianteId=:estudianteId ");
            parametros.put("estudianteId", estudianteUsuario.getEstudianteId());
        }
        final Query q = em.createQuery(sql.toString());
        for (String key : parametros.keySet()) {
            q.setParameter(key, parametros.get(key));
        }
        return q.getResultList();
    }
}
