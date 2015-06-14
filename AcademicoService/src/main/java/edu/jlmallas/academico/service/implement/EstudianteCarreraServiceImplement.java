/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.jlmallas.academico.service.implement;

import edu.jlmallas.academico.dao.EstudianteCarreraDao;
import edu.jlmallas.academico.entity.EstudianteCarrera;
import edu.jlmallas.academico.service.EstudianteCarreraService;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author jorge-luis
 */
@Stateless
public class EstudianteCarreraServiceImplement implements EstudianteCarreraService {

    @EJB
    private EstudianteCarreraDao estudianteCarreraDao;

    @Override
    public void guardar(final EstudianteCarrera estudianteCarrera) {
        this.estudianteCarreraDao.create(estudianteCarrera);
    }

    @Override
    public void actualizar(final EstudianteCarrera estudianteCarrera) {
        this.estudianteCarreraDao.edit(estudianteCarrera);
    }

    @Override
    public void eliminar(final EstudianteCarrera estudianteCarrera) {
        this.estudianteCarreraDao.remove(estudianteCarrera);
    }

    @Override
    public EstudianteCarrera buscarPorId(final EstudianteCarrera estudianteCarrera) {
        return this.estudianteCarreraDao.find(estudianteCarrera.getId());
    }

    @Override
    public List<EstudianteCarrera> buscar(final EstudianteCarrera estudianteCarrera) {
        return this.estudianteCarreraDao.buscar(estudianteCarrera);
    }

}
