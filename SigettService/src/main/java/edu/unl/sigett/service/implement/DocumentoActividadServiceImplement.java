/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.service.implement;

import edu.unl.sigett.dao.DocumentoActividadDao;
import edu.unl.sigett.entity.DocumentoActividad;
import edu.unl.sigett.service.DocumentoActividadService;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author jorge-luis
 */
@Stateless
public class DocumentoActividadServiceImplement implements DocumentoActividadService {

    @EJB
    private DocumentoActividadDao documentoActividadDao;

    @Override
    public void guardar(final DocumentoActividad documentoActividad) {
        this.documentoActividadDao.create(documentoActividad);
    }

    @Override
    public void actualizar(final DocumentoActividad documentoActividad) {
        this.documentoActividadDao.edit(documentoActividad);
    }

    @Override
    public void eliminar(final DocumentoActividad documentoActividad) {
        this.documentoActividadDao.remove(documentoActividad);
    }

    @Override
    public DocumentoActividad buscarPorId(final DocumentoActividad documentoActividad) {
        return this.documentoActividadDao.find(documentoActividad.getId());
    }

    @Override
    public List<DocumentoActividad> buscar(final DocumentoActividad documentoActividad) {
        List<DocumentoActividad> documentoActividads = documentoActividadDao.buscar(documentoActividad);
        if (documentoActividads == null) {
            return new ArrayList<>();
        }
        return documentoActividads;
    }

}
