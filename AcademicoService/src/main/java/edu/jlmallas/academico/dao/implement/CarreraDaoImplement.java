/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.jlmallas.academico.dao.implement;

import edu.jlmallas.academico.dao.AbstractFacade;
import edu.jlmallas.academico.dao.CarreraDao;
import edu.jlmallas.academico.entity.Carrera;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author jorge-luis
 */
@Stateless
public class CarreraDaoImplement extends AbstractFacade<Carrera> implements CarreraDao {

    public CarreraDaoImplement() {
        super(Carrera.class);
    }

    @Override
    public Carrera buscarIdSga(String id) {
        List<Carrera> carreras = new ArrayList<>();
        try {
            Query query = em.createQuery("SELECT c from Carrera c where (c.idSga=:idSga)");
            query.setParameter("idSga", id);
            carreras = query.getResultList();
            return !carreras.isEmpty() ? carreras.get(0) : null;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @Override
    public List<Carrera> buscarPorArea(Integer id) {
        try {
            Query query = em.createQuery("SELECT c from Carrera c where (c.areaId.id=:id)");
            query.setParameter("id", id);
            return query.getResultList();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @Override
    public List<Carrera> buscarPorCriteriosArea(String criterio, Integer areaId) {
        try {
            Query query = em.createQuery("SELECT c from Carrera c where" + " (c.nombre like concat('%',:nombre,'%')) and c.areaId.id:=areaId");
            query.setParameter("nombre", criterio);
            query.setParameter("areaId", areaId);
            return query.getResultList();

        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
}
