/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jlmallas.comun.service.implement;

import com.jlmallas.comun.dao.DocumentoDao;
import com.jlmallas.comun.entity.Documento;
import com.jlmallas.comun.service.DocumentoService;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author jorge-luis
 */
@Stateless
public class DocumentoServiceImplement implements DocumentoService {

    @EJB
    private DocumentoDao documentoDao;

    @Override
    public void guardar(final Documento documento) {
        this.documentoDao.create(documento);
    }

    @Override
    public void actualizar(final Documento documento) {
        this.documentoDao.edit(documento);
    }

    @Override
    public void eliminar(final Documento documento) {
        this.documentoDao.remove(documento);
    }

    @Override
    public Documento buscarPorId(final Documento documento) {
        return this.documentoDao.find(documento.getId());
    }

    @Override
    public List<Documento> buscar(final Documento documento) {
        return this.documentoDao.buscar(documento);
    }

    @Override
    public Documento buscarPorCatalogo(final Documento documento) {
        List<Documento> documentos = this.documentoDao.buscar(documento);
        if (documento == null) {
            return null;
        }
        return !documentos.isEmpty() ? documentos.get(0) : null;
    }
}
