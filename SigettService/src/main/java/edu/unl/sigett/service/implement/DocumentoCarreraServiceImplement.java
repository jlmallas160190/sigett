/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.service.implement;

import edu.unl.sigett.dao.DocumentoCarreraDao;
import edu.unl.sigett.entity.DocumentoCarrera;
import edu.unl.sigett.service.DocumentoCarreraService;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author jorge-luis
 */
@Stateless
public class DocumentoCarreraServiceImplement implements DocumentoCarreraService {

    @EJB
    private DocumentoCarreraDao docenteCarreraDao;

    @Override
    public void guardar(final DocumentoCarrera oficioCarrera) {
        this.docenteCarreraDao.create(oficioCarrera);
    }

    @Override
    public void actualizar(final DocumentoCarrera oficioCarrera) {
        this.docenteCarreraDao.edit(oficioCarrera);
    }

    @Override
    public void remover(final DocumentoCarrera oficioCarrera) {
        this.docenteCarreraDao.remove(oficioCarrera);
    }

    @Override
    public List<DocumentoCarrera> buscar(final DocumentoCarrera oficioCarrera) {
        return this.docenteCarreraDao.buscar(oficioCarrera);
    }

}
