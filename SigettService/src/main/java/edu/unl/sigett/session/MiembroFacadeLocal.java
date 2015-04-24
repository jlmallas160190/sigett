/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.session;

import edu.unl.sigett.entity.Miembro;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author JorgeLuis
 */
@Local
public interface MiembroFacadeLocal {

    void create(Miembro miembro);

    void edit(Miembro miembro);

    void remove(Miembro miembro);

    Miembro find(Object id);

    List<Miembro> findAll();

    List<Miembro> findRange(int[] range);

    List<Miembro> buscarPorTribunal(Long tribunalId);

    List<Miembro> buscarPorDocente(Long docenteId);

    int count();

}
