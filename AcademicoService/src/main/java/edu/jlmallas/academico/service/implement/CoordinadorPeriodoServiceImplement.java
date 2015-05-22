/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.jlmallas.academico.service.implement;

import edu.jlmallas.academico.dao.CoordinadorPeriodoDao;
import edu.jlmallas.academico.entity.CoordinadorPeriodo;
import edu.jlmallas.academico.service.CoordinadorPeriodoService;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author jorge-luis
 */
@Stateless
public class CoordinadorPeriodoServiceImplement implements CoordinadorPeriodoService {

    @EJB
    private CoordinadorPeriodoDao coordinadorPeriodoDao;

    @Override
    public void guardar(final CoordinadorPeriodo coordinadorPeriodo) {
        this.coordinadorPeriodoDao.create(coordinadorPeriodo);
    }

    @Override
    public void editar(final CoordinadorPeriodo coordinadorPeriodo) {
        this.coordinadorPeriodoDao.edit(coordinadorPeriodo);
    }

    @Override
    public void eliminar(final CoordinadorPeriodo coordinadorPeriodo) {
        this.coordinadorPeriodoDao.remove(coordinadorPeriodo);
    }

    @Override
    public CoordinadorPeriodo buscarPorId(final CoordinadorPeriodo coordinadorPeriodo) {
        return this.coordinadorPeriodoDao.find(coordinadorPeriodo.getId());
    }

    @Override
    public CoordinadorPeriodo vigente(final CoordinadorPeriodo coordinadorPeriodo) {
        List<CoordinadorPeriodo> coordinadorPeriodosVigentes = new ArrayList<>();
        coordinadorPeriodosVigentes = this.coordinadorPeriodoDao.buscar(coordinadorPeriodo);
        if (coordinadorPeriodosVigentes == null) {
            return null;
        }
        return !coordinadorPeriodosVigentes.isEmpty() ? coordinadorPeriodosVigentes.get(0) : null;
    }

    @Override
    public List<CoordinadorPeriodo> buscar(CoordinadorPeriodo coordinadorPeriodo) {
        return this.coordinadorPeriodoDao.buscar(coordinadorPeriodo);
    }

}
