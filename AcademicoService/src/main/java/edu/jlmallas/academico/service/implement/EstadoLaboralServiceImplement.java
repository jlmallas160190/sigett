/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.jlmallas.academico.service.implement;

import edu.jlmallas.academico.dao.EstadoLaboralDao;
import edu.jlmallas.academico.entity.EstadoLaboral;
import edu.jlmallas.academico.service.EstadoLaboralService;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author jorge-luis
 */
@Stateless
public class EstadoLaboralServiceImplement implements EstadoLaboralService {

    @EJB
    private EstadoLaboralDao estadoLaboralDao;

    @Override
    public EstadoLaboral buscarPorTipoContrato(final EstadoLaboral estadoLaboral) {
        List<EstadoLaboral> estadosLaborales = new ArrayList<>();
        estadosLaborales = estadoLaboralDao.buscar(estadoLaboral);
        if (estadosLaborales == null) {
            return null;
        }
        return !estadosLaborales.isEmpty() ? estadosLaborales.get(0) : null;
    }

    @Override
    public void guardar(final EstadoLaboral estadoLaboral) {
        this.estadoLaboralDao.create(estadoLaboral);
    }

    @Override
    public void actualizar(final EstadoLaboral estadoLaboral) {
        this.estadoLaboralDao.edit(estadoLaboral);
    }

    @Override
    public EstadoLaboral buscarPorId(final Long id) {
        return this.estadoLaboralDao.find(id);
    }

    @Override
    public List<EstadoLaboral> buscar(final EstadoLaboral estadoLaboral) {
        return this.estadoLaboralDao.buscar(estadoLaboral);
    }

}
