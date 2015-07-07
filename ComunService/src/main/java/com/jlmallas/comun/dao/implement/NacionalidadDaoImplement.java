/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jlmallas.comun.dao.implement;

import com.jlmallas.comun.entity.Nacionalidad;
import com.jlmallas.comun.dao.AbstractDao;
import com.jlmallas.comun.dao.NacionalidadDao;
import javax.ejb.Stateless;

/**
 *
 * @author jorge-luis
 */
@Stateless
public class NacionalidadDaoImplement extends AbstractDao<Nacionalidad> implements NacionalidadDao {

    public NacionalidadDaoImplement() {
        super(Nacionalidad.class);
    }

}
