/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.jlmallas.academico.service.implement;

import edu.jlmallas.academico.dao.DocenteCarreraDao;
import edu.jlmallas.academico.entity.DocenteCarrera;
import edu.jlmallas.academico.service.DocenteCarreraService;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author jorge-luis
 */
@Stateless
public class DocenteCarreraServiceImplement implements DocenteCarreraService {

    @EJB
    private DocenteCarreraDao docenteCarreraDao;

    @Override
    public void guardar(final DocenteCarrera docenteCarrera) {
        this.docenteCarreraDao.create(docenteCarrera);
    }

    @Override
    public void actualizar(final DocenteCarrera docenteCarrera) {
        this.docenteCarreraDao.edit(docenteCarrera);
    }

    @Override
    public void eliminar(final DocenteCarrera docenteCarrera) {
        this.docenteCarreraDao.remove(docenteCarrera);
    }

    @Override
    public DocenteCarrera buscarPorId(final DocenteCarrera docenteCarrera) {
        return this.docenteCarreraDao.find(docenteCarrera.getId());
    }

    @Override
    public List<DocenteCarrera> buscar(final DocenteCarrera docenteCarrera) {
        return this.docenteCarreraDao.buscar(docenteCarrera);
    }

}
