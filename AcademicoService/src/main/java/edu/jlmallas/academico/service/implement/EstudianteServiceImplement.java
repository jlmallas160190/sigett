/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.jlmallas.academico.service.implement;

import edu.jlmallas.academico.dao.EstudianteDao;
import edu.jlmallas.academico.entity.Estudiante;
import edu.jlmallas.academico.service.EstudianteService;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author jorge-luis
 */
@Stateless
public class EstudianteServiceImplement implements EstudianteService {
@EJB
private EstudianteDao estudianteDao;
    @Override
    public void guardar(final Estudiante estudiante) {
        this.estudianteDao.create(estudiante);
    }

    @Override
    public void actualizar(final Estudiante estudiante) {
        this.estudianteDao.edit(estudiante);
    }

    @Override
    public void eliminar(final Estudiante estudiante) {
        this.estudianteDao.remove(estudiante);
    }

    @Override
    public Estudiante buscarPorId(final Estudiante estudiante) {
        return this.estudianteDao.find(estudiante.getId());
    }

    @Override
    public List<Estudiante> buscar(final Estudiante estudiante) {
        return this.estudianteDao.buscar(estudiante);
    }

}
