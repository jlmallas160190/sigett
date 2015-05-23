/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.service.implement;

import edu.unl.sigett.dao.ProyectoOfertaCarreraDao;
import edu.unl.sigett.entity.ProyectoCarreraOferta;
import edu.unl.sigett.service.ProyectoCarreraOfertaService;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author jorge-luis
 */
@Stateless
public class ProyectoCarreraOfertaServiceImplement implements ProyectoCarreraOfertaService {

    @EJB
    private ProyectoOfertaCarreraDao proyectoCarreraDao;

    @Override
    public void guardar(final ProyectoCarreraOferta proyectoCarreraOferta) {
        this.proyectoCarreraDao.create(proyectoCarreraOferta);
    }

    @Override
    public void editar(final ProyectoCarreraOferta proyectoCarreraOferta) {
        this.proyectoCarreraDao.edit(proyectoCarreraOferta);
    }

    @Override
    public void eliminar(final ProyectoCarreraOferta proyectoCarreraOferta) {
        this.proyectoCarreraDao.remove(proyectoCarreraOferta);
    }

    @Override
    public ProyectoCarreraOferta buscarPorId(final ProyectoCarreraOferta proyectoCarreraOferta) {
        return this.proyectoCarreraDao.find(proyectoCarreraOferta.getId());
    }

    @Override
    public List<ProyectoCarreraOferta> buscar(final ProyectoCarreraOferta proyectoCarreraOferta) {
        return this.proyectoCarreraDao.buscar(proyectoCarreraOferta);
    }

}
