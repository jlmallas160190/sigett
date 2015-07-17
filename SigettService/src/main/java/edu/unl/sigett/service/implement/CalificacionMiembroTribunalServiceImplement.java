/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.unl.sigett.service.implement;

import edu.unl.sigett.dao.CalificacionMiembroDao;
import edu.unl.sigett.entity.CalificacionMiembro;
import edu.unl.sigett.service.CalificacionMiembroTribunalService;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author jorge-luis
 */
@Stateless
public class CalificacionMiembroTribunalServiceImplement implements CalificacionMiembroTribunalService {

    @EJB
    private CalificacionMiembroDao calificacionMiembroDao;

    @Override
    public void guardar(final CalificacionMiembro calificacionMiembro) {
        this.calificacionMiembroDao.create(calificacionMiembro);
    }
    
    @Override
    public void actualizar(final CalificacionMiembro calificacionMiembro) {
        calificacionMiembroDao.edit(calificacionMiembro);
    }
    
    @Override
    public void eliminar(final CalificacionMiembro calificacionMiembro) {
        this.calificacionMiembroDao.remove(calificacionMiembro);
    }
    
    @Override
    public CalificacionMiembro buscarPorId(final CalificacionMiembro calificacionMiembro) {
        return this.calificacionMiembroDao.find(calificacionMiembro.getId());
    }
    
    @Override
    public List<CalificacionMiembro> buscar(final CalificacionMiembro calificacionMiembro) {
        return this.calificacionMiembroDao.buscar(calificacionMiembro);
    }
    
}
