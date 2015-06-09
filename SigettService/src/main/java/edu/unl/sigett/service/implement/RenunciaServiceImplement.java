/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.service.implement;

import edu.unl.sigett.dao.RenunciaDao;
import edu.unl.sigett.entity.Renuncia;
import edu.unl.sigett.service.RenunciaService;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author jorge-luis
 */
@Stateless
public class RenunciaServiceImplement implements RenunciaService {

    @EJB
    private RenunciaDao renunciaDao;

    @Override
    public void guardar(final Renuncia renuncia) {
        this.renunciaDao.create(renuncia);
    }

    @Override
    public void actualizar(final Renuncia renuncia) {
        this.renunciaDao.edit(renuncia);
    }

    @Override
    public void eliminar(final Renuncia renuncia) {
        this.renunciaDao.remove(renuncia);
    }

    @Override
    public Renuncia buscarPorId(final Renuncia renuncia) {
        return renunciaDao.find(renuncia.getId());
    }

    @Override
    public List<Renuncia> buscar(Renuncia renuncia) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
