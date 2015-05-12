/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jlmallas.comun.dao;

import com.jlmallas.comun.entity.Foto;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author JorgeLuis
 */
@Local
public interface FotoFacadeLocal {

    void create(Foto foto);

    void edit(Foto foto);

    void remove(Foto foto);

    Foto find(Object id);

    List<Foto> findAll();

    List<Foto> findRange(int[] range);

    List<Foto> buscarPorPersona(Long id);

    Foto fotoActual(Long personaId);

    int count();

}
