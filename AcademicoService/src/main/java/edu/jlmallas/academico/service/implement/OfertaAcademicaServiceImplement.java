/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.jlmallas.academico.service.implement;

import edu.jlmallas.academico.dao.OfertaAcademicaDao;
import edu.jlmallas.academico.entity.OfertaAcademica;
import edu.jlmallas.academico.service.OfertaAcademicaFacadeLocal;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author JorgeLuis
 */
@Stateless
public class OfertaAcademicaServiceImplement implements OfertaAcademicaFacadeLocal {

    @EJB
    private OfertaAcademicaDao ofertaAcademicaDao;

    @Override
    public void create(OfertaAcademica ofertaAcademica) {
        ofertaAcademicaDao.create(ofertaAcademica);
    }

    @Override
    public void edit(OfertaAcademica ofertaAcademica) {
       ofertaAcademicaDao.edit(ofertaAcademica);
    }

    @Override
    public void remove(OfertaAcademica ofertaAcademica) {
       ofertaAcademicaDao.remove(ofertaAcademica);
    }

    @Override
    public OfertaAcademica find(Object id) {
       return ofertaAcademicaDao.find(id);
    }

    @Override
    public List<OfertaAcademica> findAll() {
      return ofertaAcademicaDao.findAll();
    }


    @Override
    public OfertaAcademica buscarPorIdSga(String id) {
        return ofertaAcademicaDao.buscarPorIdSga(id);
    }

    @Override
    public List<OfertaAcademica> buscarPorPeriodoActual() {
       return ofertaAcademicaDao.buscarPorPeriodoActual();
    }

    @Override
    public OfertaAcademica ultimaOfertaPorFechaYPeriodoLectivo(Integer periodoId) {
      return ofertaAcademicaDao.ultimaOfertaPorFechaYPeriodoLectivo(periodoId);
    }

    @Override
    public OfertaAcademica primerOfertaPorFechaYPeriodoLectivo(Integer periodoId) {
        return ofertaAcademicaDao.primerOfertaPorFechaYPeriodoLectivo(periodoId);
    }
}
