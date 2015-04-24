/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.session;

import edu.unl.sigett.entity.RenunciaAutor;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author JorgeLuis
 */
@Local
public interface RenunciaAutorFacadeLocal {

    void create(RenunciaAutor renunciaAutor);

    void edit(RenunciaAutor renunciaAutor);

    void remove(RenunciaAutor renunciaAutor);

    RenunciaAutor find(Object id);

    List<RenunciaAutor> findAll();

    List<RenunciaAutor> findRange(int[] range);

    List<RenunciaAutor> buscarPorAutorProyecto(Long autorId);

    int count();

}
