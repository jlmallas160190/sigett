/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.jlmallas.academico.service.implement;

import edu.jlmallas.academico.dao.AbstractDao;
import edu.jlmallas.academico.dao.CarreraDao;
import edu.jlmallas.academico.entity.Carrera;
import edu.jlmallas.academico.service.CarreraService;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author JorgeLuis
 */
@Stateless
public class CarreraServiceImplement implements CarreraService {

    @EJB
    private CarreraDao carreraDao;

    @Override
    public void create(Carrera carrera) {
        carreraDao.create(carrera);
    }

    @Override
    public void edit(Carrera carrera) {
        carreraDao.edit(carrera);
    }

    @Override
    public void remove(Carrera carrera) {
        carreraDao.remove(carrera);
    }

    @Override
    public Carrera find(Object id) {
        return carreraDao.find(id);
    }

    @Override
    public Carrera buscarIdSga(String id) {
        return carreraDao.buscarIdSga(id);
    }

    @Override
    public List<Carrera> findAll() {
        return carreraDao.findAll();
    }

    @Override
    public List<Carrera> buscarPorCriterio(Carrera carrera) {
        return carreraDao.buscarPorCriterio(carrera);
    }

    @Override
    public List<Carrera> findRange(int[] range) {
        return carreraDao.findRange(range);
    }

    @Override
    public int count() {
        return carreraDao.count();
    }

}
