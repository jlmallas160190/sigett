/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jlmallas.comun.service;

import com.jlmallas.comun.entity.Catalogo;
import javax.ejb.Local;

/**
 *
 * @author jorge-luis
 */
@Local
public interface CatalogoService {

    Catalogo buscarPorCodigo(final String codigo);
    
}
