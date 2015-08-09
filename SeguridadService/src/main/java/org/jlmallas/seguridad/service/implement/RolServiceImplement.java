/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlmallas.seguridad.service.implement;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import org.jlmallas.seguridad.dao.RolDao;
import org.jlmallas.seguridad.entity.Rol;
import org.jlmallas.seguridad.service.RolService;

/**
 *
 * @author jorge-luis
 */
@Stateless
public class RolServiceImplement implements RolService {

    @EJB
    private RolDao rolDao;

    @Override
    public void guardar(final Rol rol) {
        this.rolDao.create(rol);
    }

    @Override
    public void actualizar(final Rol rol) {
        this.rolDao.edit(rol);
    }

    @Override
    public void eliminar(final Rol rol) {
        this.rolDao.remove(rol);
    }

    @Override
    public Rol buscarPorId(final Rol rol) {
        return this.rolDao.find(rol.getId());
    }

    @Override
    public List<Rol> buscar(final Rol rol) {
        return this.rolDao.buscar(rol);
    }

}
