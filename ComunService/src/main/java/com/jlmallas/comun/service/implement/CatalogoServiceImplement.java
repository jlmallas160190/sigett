/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jlmallas.comun.service.implement;

import com.jlmallas.comun.dao.CatalogoDao;
import com.jlmallas.comun.entity.Catalogo;
import com.jlmallas.comun.service.CatalogoService;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author jorge-luis
 */
@Stateless
public class CatalogoServiceImplement implements CatalogoService {

    @EJB
    private CatalogoDao catalogoDao;

    @Override
    public Catalogo buscarPorCodigo(final String codigo) {
        List<Catalogo> catalogos = catalogoDao.buscar(new Catalogo(null, null, codigo, null, null));
        if (catalogos == null) {
            return null;
        }
        return !catalogos.isEmpty() ? catalogos.get(0) : null;
    }

}
