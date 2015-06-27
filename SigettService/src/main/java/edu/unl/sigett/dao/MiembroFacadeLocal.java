/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao;

import edu.unl.sigett.entity.MiembroTribunal;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author JorgeLuis
 */
@Local
public interface MiembroFacadeLocal {

    void create(MiembroTribunal miembro);

    void edit(MiembroTribunal miembro);

    void remove(MiembroTribunal miembro);

    MiembroTribunal find(Object id);

    List<MiembroTribunal> findAll();

    List<MiembroTribunal> findRange(int[] range);

    List<MiembroTribunal> buscarPorTribunal(Long tribunalId);

    List<MiembroTribunal> buscarPorDocente(Long docenteId);

    int count();

}
