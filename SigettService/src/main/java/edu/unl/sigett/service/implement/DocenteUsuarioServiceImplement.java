/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.service.implement;

import edu.unl.sigett.dao.DocenteUsuarioDao;
import edu.unl.sigett.entity.DocenteUsuario;
import edu.unl.sigett.service.DocenteUsuarioService;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author jorge-luis
 */
@Stateless
public class DocenteUsuarioServiceImplement implements DocenteUsuarioService {
    
    @EJB
    private DocenteUsuarioDao docenteUsuarioDao;
    
    @Override
    public DocenteUsuario buscarPorDocente(final DocenteUsuario docenteUsuario) {
        List<DocenteUsuario> docenteUsuarios = docenteUsuarioDao.buscar(docenteUsuario);
        if (docenteUsuarios == null) {
            return null;
        }
        return !docenteUsuarios.isEmpty() ? docenteUsuarios.get(0) : null;
    }
    
    @Override
    public void guardar(final DocenteUsuario docenteUsuario) {
        docenteUsuarioDao.create(docenteUsuario);
    }
    
    @Override
    public void actualizar(final DocenteUsuario docenteUsuario) {
        this.docenteUsuarioDao.edit(docenteUsuario);
    }
    
    @Override
    public DocenteUsuario buscarPorId(final Long docenteUsuarioId) {
        return this.docenteUsuarioDao.find(docenteUsuarioId);
    }
    
}
