/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.service.implement;

import edu.unl.sigett.service.AspiranteService;
import edu.unl.sigett.dao.AspiranteDao;
import edu.unl.sigett.entity.Aspirante;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author jorge-luis
 */
@Stateless
public class AspiranteServiceImplement implements AspiranteService {

    @EJB
    private AspiranteDao aspiranteDao;

    @Override
    public void guardar(final Aspirante aspirante) {
        this.aspiranteDao.create(aspirante);
    }

    @Override
    public void actualizar(final Aspirante aspirante) {
        this.aspiranteDao.edit(aspirante);
    }

    @Override
    public void eliminar(Aspirante aspirante) {
        this.aspiranteDao.remove(aspirante);
    }

    @Override
    public List<Aspirante> buscar(Aspirante aspirante) {
        return this.aspiranteDao.buscar(aspirante);
    }

    @Override
    public Aspirante buscarPorId(Long id) {
        return this.aspiranteDao.find(id);
    }

}
