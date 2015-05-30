/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.service;

import edu.unl.sigett.dao.DocumentoProyectoDao;
import edu.unl.sigett.entity.DocumentoProyecto;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author jorge-luis
 */
@Stateless
public class DocumentoProyectoServiceImplement implements DocumentoProyectoService {

    @EJB
    private DocumentoProyectoDao documentoProyectoDao;

    @Override
    public void guardar(final DocumentoProyecto documentoProyecto) {
        this.documentoProyectoDao.create(documentoProyecto);
    }

    @Override
    public void actualizar(final DocumentoProyecto documentoProyecto) {
        this.documentoProyectoDao.edit(documentoProyecto);
    }

    @Override
    public void eliminar(final DocumentoProyecto documentoProyecto) {
        this.documentoProyectoDao.remove(documentoProyecto);
    }

    @Override
    public DocumentoProyecto buscarPorId(final DocumentoProyecto documentoProyecto) {
        return this.documentoProyectoDao.find(documentoProyecto.getId());
    }

    @Override
    public List<DocumentoProyecto> buscar(final DocumentoProyecto documentoProyecto) {
        return this.documentoProyectoDao.buscar(documentoProyecto);
    }

}
