/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.unl.sigett.service.implement;

import edu.unl.sigett.dao.AutorProyectoDao;
import edu.unl.sigett.entity.AutorProyecto;
import edu.unl.sigett.service.AutorProyectoService;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author jorge-luis
 */
@Stateless
public class AutorProyectoServiceImplement implements AutorProyectoService {
@EJB
private AutorProyectoDao autorProyectoDao;
    @Override
    public void guardar(final AutorProyecto autorProyecto) {
        this.autorProyectoDao.create(autorProyecto);
    }

    @Override
    public void editar(final AutorProyecto autorProyecto) {
        this.autorProyectoDao.edit(autorProyecto);
    }

    @Override
    public void eliminar(final AutorProyecto autorProyecto) {
        this.autorProyectoDao.remove(autorProyecto);
    }

    @Override
    public AutorProyecto buscarPorId(final AutorProyecto autorProyecto) {
        return autorProyectoDao.find(autorProyecto.getId());
    }

    @Override
    public List<AutorProyecto> buscar(AutorProyecto autorProyecto) {
        return this.autorProyectoDao.buscar(autorProyecto);
    }

    
}
