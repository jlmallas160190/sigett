/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.service.implement;

import edu.unl.sigett.dao.DocenteProyectoDao;
import edu.unl.sigett.entity.DocenteProyecto;
import edu.unl.sigett.service.DocenteProyectoService;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author jorge-luis
 */
@Stateless
public class DocenteProyectoServiceImplement implements DocenteProyectoService {

    @EJB
    private DocenteProyectoDao docenteProyectoDao;

    @Override
    public void guardar(final DocenteProyecto docenteProyecto) {
        this.docenteProyectoDao.create(docenteProyecto);
    }

    @Override
    public void actualizar(final DocenteProyecto docenteProyecto) {
        this.docenteProyectoDao.edit(docenteProyecto);
    }

    @Override
    public void eliminar(final DocenteProyecto docenteProyecto) {
        this.docenteProyectoDao.remove(docenteProyecto);
    }

    @Override
    public DocenteProyecto buscarPorId(final DocenteProyecto docenteProyecto) {
        return this.docenteProyectoDao.find(docenteProyecto.getId());
    }

    @Override
    public List<DocenteProyecto> buscar(final DocenteProyecto docenteProyecto) {
        return this.docenteProyectoDao.buscar(docenteProyecto);
    }

}
