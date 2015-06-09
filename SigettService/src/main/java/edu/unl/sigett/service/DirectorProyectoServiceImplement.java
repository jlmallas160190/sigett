/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.service;

import edu.unl.sigett.dao.DirectorProyectoDao;
import edu.unl.sigett.entity.DirectorProyecto;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author jorge-luis
 */
@Stateless
public class DirectorProyectoServiceImplement implements DirectorProyectoService {

    @EJB
    private DirectorProyectoDao directorProyectoDao;

    @Override
    public void guardar(final DirectorProyecto directorProyecto) {
        this.directorProyectoDao.create(directorProyecto);
    }

    @Override
    public void actualizar(final DirectorProyecto directorProyecto) {
        this.directorProyectoDao.edit(directorProyecto);
    }

    @Override
    public void eliminar(final DirectorProyecto directorProyecto) {
        this.directorProyectoDao.remove(directorProyecto);
    }

    @Override
    public DirectorProyecto buscarPorId(final DirectorProyecto directorProyecto) {
        return this.directorProyectoDao.find(directorProyecto.getId());
    }

    @Override
    public List<DirectorProyecto> buscar(final DirectorProyecto directorProyecto) {
        return this.directorProyectoDao.buscar(directorProyecto);
    }

}
