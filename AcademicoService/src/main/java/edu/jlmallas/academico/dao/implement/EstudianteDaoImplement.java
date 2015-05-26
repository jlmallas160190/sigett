/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.jlmallas.academico.dao.implement;

import edu.jlmallas.academico.dao.AbstractDao;
import edu.jlmallas.academico.dao.AbstractDao;
import edu.jlmallas.academico.dao.EstudianteDao;
import edu.jlmallas.academico.entity.Estudiante;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author JorgeLuis
 */
@Stateless
public class EstudianteDaoImplement extends AbstractDao<Estudiante> implements EstudianteDao {

    public EstudianteDaoImplement() {
        super(Estudiante.class);
    }

    @Override
    public List<Estudiante> buscarPorCriterio(String criterio) {
        try {
            Query query = em.createQuery("Select e from Estudiante e where " + "(LOWER(e.persona.apellidos) like concat('%',LOWER(:criterio),'%')"
                    + " or LOWER(e.persona.nombres) like concat('%',LOWER(:criterio),'%') or e.persona.numeroIdentificacion like concat('%',:criterio,'%') ) order by e.persona.apellidos");
            query.setParameter("criterio", criterio);
            return query.getResultList();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
}