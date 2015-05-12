/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao;

import edu.unl.sigett.entity.EstadoAutor;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author JorgeLuis
 */
@Local
public interface EstadoAutorFacadeLocal {

    void create(EstadoAutor estadoAutor);

    void edit(EstadoAutor estadoAutor);

    void remove(EstadoAutor estadoAutor);

    EstadoAutor find(Object id);

    List<EstadoAutor> findAll();

    List<EstadoAutor> findRange(int[] range);

    EstadoAutor buscarPorCodigo(String codigo);

    int count();

}
