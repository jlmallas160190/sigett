/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.unl.sigett.service.implement;

import edu.unl.sigett.dao.EstudianteUsuarioDao;
import edu.unl.sigett.entity.EstudianteUsuario;
import edu.unl.sigett.service.EstudianteUsuarioService;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author jorge-luis
 */
@Stateless
public class EstudianteUsuarioServiceImplement implements EstudianteUsuarioService {

    @EJB
    private EstudianteUsuarioDao estudianteUsuarioDao;

    @Override
    public EstudianteUsuario buscarPorEstudiante(final EstudianteUsuario estudianteUsuario) {
        List<EstudianteUsuario> estudianteUsuarios = this.estudianteUsuarioDao.buscar(estudianteUsuario);
        if (estudianteUsuarios == null) {
            return null;
        }
        return !estudianteUsuarios.isEmpty() ? estudianteUsuarios.get(0) : null;
    }
    
    @Override
    public void guardar(final EstudianteUsuario estudianteUsuario) {
        this.estudianteUsuarioDao.create(estudianteUsuario);
    }
    
    @Override
    public void editar(final EstudianteUsuario estudianteUsuario) {
        this.estudianteUsuarioDao.edit(estudianteUsuario);
    }
    
    @Override
    public EstudianteUsuario buscarPorId(final Long id) {
        return this.estudianteUsuarioDao.find(id);
    }
    
}
