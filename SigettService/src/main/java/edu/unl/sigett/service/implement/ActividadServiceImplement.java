/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.service.implement;

import edu.unl.sigett.dao.ActividadDao;
import edu.unl.sigett.entity.Actividad;
import edu.unl.sigett.service.ActividadService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author jorge-luis
 */
@Stateless
public class ActividadServiceImplement implements ActividadService {

    @EJB
    private ActividadDao actividadDao;

    @Override
    public void guardar(final Actividad actividad) {
        this.actividadDao.create(actividad);
    }

    @Override
    public void actualizar(final Actividad actividad) {
        this.actividadDao.edit(actividad);
    }

    @Override
    public void eliminar(final Actividad actividad) {
        this.actividadDao.remove(actividad);
    }

    @Override
    public Actividad buscarPorId(final Actividad actividad) {
        return this.actividadDao.find(actividad.getId());
    }

    @Override
    public List<Actividad> buscar(final Actividad actividad) {
        List<Actividad> actividades = actividadDao.buscar(actividad);
        if (actividades == null) {
            return new ArrayList<>();
        }
        return actividades;
    }

    @Override
    public BigDecimal sumatoriaDuracion(final Actividad actividad) {
        return this.actividadDao.sumatoriaDuracion(actividad);
    }

}
