/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.jlmallas.academico.service.implement;

import edu.jlmallas.academico.dao.CoordinadorDao;
import edu.jlmallas.academico.entity.Coordinador;
import edu.jlmallas.academico.service.CoordinadorService;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author jorge-luis
 */
@Stateless
public class CoordinadorServiceImplement implements CoordinadorService {

    @EJB
    private CoordinadorDao coordinadorDao;

    @Override
    public void guardar(final Coordinador coordinador) {
        this.coordinadorDao.create(coordinador);
    }

    @Override
    public void actualizar(final Coordinador coordinador) {
        this.coordinadorDao.edit(coordinador);
    }

    @Override
    public void eliminar(final Coordinador coordinador) {
        this.coordinadorDao.remove(coordinador);
    }

    @Override
    public Coordinador buscarPorId(final Coordinador coordinador) {
        return this.coordinadorDao.find(coordinador.getId());
    }

    @Override
    public List<Coordinador> buscar(final Coordinador coordinador) {
        return null;
    }

}
