/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jlmallas.comun.service;

import com.jlmallas.comun.dao.NacionalidadDao;
import com.jlmallas.comun.entity.Nacionalidad;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author jorge-luis
 */
@Stateless
public class NacionalidadServiceImplement implements NacionalidadService {

    @EJB
    private NacionalidadDao nacionalidadDao;

    @Override
    public void guardar(final Nacionalidad nacionalidad) {
        this.nacionalidadDao.create(nacionalidad);
    }

    @Override
    public void actualizar(final Nacionalidad nacionalidad) {
        this.nacionalidadDao.edit(nacionalidad);
    }

    @Override
    public void eliminar(final Nacionalidad nacionalidad) {
        this.nacionalidadDao.remove(nacionalidad);
    }

    @Override
    public Nacionalidad buscarPorId(final Nacionalidad nacionalidad) {
        return this.nacionalidadDao.find(nacionalidad.getId());
    }

    @Override
    public List<Nacionalidad> buscar(Nacionalidad nacionalidad) {
        return this.nacionalidadDao.findAll();
    }

}
