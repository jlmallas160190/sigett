/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.service.implement;

import edu.unl.sigett.dao.TemaProyectoDao;
import edu.unl.sigett.entity.TemaProyecto;
import edu.unl.sigett.service.TemaProyectoService;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author jorge-luis
 */
@Stateless
public class TemaProyectoServiceImplement implements TemaProyectoService {

    @EJB
    private TemaProyectoDao temaProyectoDao;

    @Override
    public void guardar(final TemaProyecto temaProyecto) {
        this.temaProyectoDao.create(temaProyecto);
    }

    @Override
    public void actualizar(final TemaProyecto temaProyecto) {
        this.temaProyectoDao.edit(temaProyecto);
    }

    @Override
    public void eliminar(final TemaProyecto temaProyecto) {
        this.temaProyectoDao.remove(temaProyecto);
    }

    @Override
    public TemaProyecto buscarPorId(final TemaProyecto temaProyecto) {
        return this.temaProyectoDao.find(temaProyecto.getId());
    }

    @Override
    public List<TemaProyecto> buscar(final TemaProyecto temaProyecto) {
        return this.temaProyectoDao.buscar(temaProyecto);
    }

}
