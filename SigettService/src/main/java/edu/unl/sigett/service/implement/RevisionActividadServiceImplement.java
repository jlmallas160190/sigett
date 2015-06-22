/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.service.implement;

import edu.unl.sigett.dao.RevisionActividadDao;
import edu.unl.sigett.entity.RevisionActividad;
import edu.unl.sigett.service.RevisionActividadService;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author jorge-luis
 */
@Stateless
public class RevisionActividadServiceImplement implements RevisionActividadService {

    @EJB
    private RevisionActividadDao revisionActividadDao;

    @Override
    public void guardar(final RevisionActividad revisionActividad) {
        this.revisionActividadDao.create(revisionActividad);
    }

    @Override
    public void actualizar(final RevisionActividad revisionActividad) {
        this.revisionActividadDao.edit(revisionActividad);
    }

    @Override
    public void eliminar(final RevisionActividad revisionActividad) {
        this.revisionActividadDao.remove(revisionActividad);
    }

    @Override
    public RevisionActividad buscarPorId(RevisionActividad revisionActividad) {
        return this.revisionActividadDao.find(revisionActividad.getId());
    }

    @Override
    public List<RevisionActividad> buscar(RevisionActividad revisionActividad) {
        List<RevisionActividad> revisionActividads = this.revisionActividadDao.buscar(revisionActividad);
        if (revisionActividads == null) {
            return new ArrayList<>();
        }
        return revisionActividads;
    }

}
