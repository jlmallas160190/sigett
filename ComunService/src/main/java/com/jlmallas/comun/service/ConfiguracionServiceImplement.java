/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jlmallas.comun.service;

import com.jlmallas.comun.dao.ConfiguracionDao;
import com.jlmallas.comun.entity.Configuracion;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author jorge-luis
 */
@Stateless
public class ConfiguracionServiceImplement implements ConfiguracionService {

    @EJB
    private ConfiguracionDao configuracionDao;

    @Override
    public void guardar(final Configuracion configuracion) {
        this.configuracionDao.create(configuracion);
    }

    @Override
    public void actualizar(final Configuracion configuracion) {
        this.configuracionDao.edit(configuracion);
    }

    @Override
    public void eliminar(final Configuracion configuracion) {
        this.configuracionDao.remove(configuracion);
    }

    @Override
    public Configuracion buscarPorId(final Configuracion configuracion) {
        return this.configuracionDao.find(configuracion.getId());
    }

    @Override
    public List<Configuracion> buscar(final Configuracion configuracion) {
        return this.configuracionDao.buscar(configuracion);
    }

}
