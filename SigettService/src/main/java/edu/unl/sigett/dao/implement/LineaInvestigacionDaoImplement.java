/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao.implement;

import edu.unl.sigett.dao.AbstractDao;
import edu.unl.sigett.dao.LineaInvestigacionDao;
import edu.unl.sigett.entity.LineaInvestigacion;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author JorgeLuis
 */
@Stateless
public class LineaInvestigacionDaoImplement extends AbstractDao<LineaInvestigacion> implements LineaInvestigacionDao {

    public LineaInvestigacionDaoImplement() {
        super(LineaInvestigacion.class);
    }

    @Override
    public List<LineaInvestigacion> buscarPorCriterio(String nombre) {
        try {
            Query query = em.createQuery("SELECT l FROM LineaInvestigacion l WHERE " + " (LOWER(l.nombre) like concat('%',LOWER(:nombre),'%'))");
            query.setParameter("nombre", nombre);
            return query.getResultList();
        } catch (Exception e) {
        }
        return null;
    }
}
