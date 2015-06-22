/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.service.implement;

import edu.unl.sigett.dao.RevisionDao;
import edu.unl.sigett.entity.Revision;
import edu.unl.sigett.service.RevisionService;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author jorge-luis
 */
@Stateless
public class RevisionServiceImplement implements RevisionService {

    @EJB
    private RevisionDao revisionDao;

    @Override
    public void guardar(final Revision revision) {
        this.revisionDao.create(revision);
    }

    @Override
    public void actualizar(final Revision revision) {
        this.revisionDao.edit(revision);
    }

    @Override
    public void eliminar(final Revision revision) {
        this.revisionDao.remove(revision);
    }

    @Override
    public Revision buscarPorId(Revision revision) {
        return this.revisionDao.find(revision.getId());
    }

    @Override
    public List<Revision> buscar(final Revision revision) {
        List<Revision> revisiones = this.revisionDao.buscar(revision);
        if (revisiones == null) {
            return new ArrayList<>();
        }
        return revisiones;
    }

}
