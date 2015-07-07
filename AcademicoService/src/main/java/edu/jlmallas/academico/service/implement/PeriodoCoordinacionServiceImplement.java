/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.jlmallas.academico.service.implement;

import edu.jlmallas.academico.dao.PeriodoCoordinacionDao;
import edu.jlmallas.academico.entity.PeriodoCoordinacion;
import edu.jlmallas.academico.service.PeriodoCoordinacionService;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author jorge-luis
 */
@Stateless
public class PeriodoCoordinacionServiceImplement implements PeriodoCoordinacionService {

    @EJB
    private PeriodoCoordinacionDao periodoCoordinacionDao;

    @Override
    public void guardar(final PeriodoCoordinacion periodoCoordinacion) {
        this.periodoCoordinacionDao.create(periodoCoordinacion);
    }

    @Override
    public void actualizar(final PeriodoCoordinacion periodoCoordinacion) {
        this.periodoCoordinacionDao.edit(periodoCoordinacion);
    }

    @Override
    public void eliminar(final PeriodoCoordinacion periodoCoordinacion) {
        this.periodoCoordinacionDao.remove(periodoCoordinacion);
    }

    @Override
    public PeriodoCoordinacion buscarPorId(final PeriodoCoordinacion periodoCoordinacion) {
        return this.periodoCoordinacionDao.find(periodoCoordinacion.getId());
    }

    @Override
    public List<PeriodoCoordinacion> buscar(final PeriodoCoordinacion periodoCoordinacion) {
        return this.periodoCoordinacionDao.buscar(periodoCoordinacion);
    }

}
