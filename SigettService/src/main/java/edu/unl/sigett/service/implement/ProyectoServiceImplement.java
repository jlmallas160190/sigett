/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.service.implement;

import edu.unl.sigett.dao.ProyectoDao;
import edu.unl.sigett.entity.Proyecto;
import edu.unl.sigett.service.ProyectoService;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author jorge-luis
 */
@Stateless
public class ProyectoServiceImplement implements ProyectoService {

    @EJB
    private ProyectoDao proyectoDao;

    @Override
    public void guardar(final Proyecto proyecto) {
        this.proyectoDao.create(proyecto);
    }

    @Override
    public void actualizar(final Proyecto proyecto) {
        this.proyectoDao.edit(proyecto);
    }

    @Override
    public void eliminar(final Proyecto proyecto) {
        this.proyectoDao.remove(proyecto);
    }

    @Override
    public Proyecto buscarPorId(final Proyecto proyecto) {
        return this.proyectoDao.find(proyecto.getId());
    }

    @Override
    public List<Proyecto> buscar(final Proyecto proyecto) {
      return this.proyectoDao.buscar(proyecto);
    }

}
