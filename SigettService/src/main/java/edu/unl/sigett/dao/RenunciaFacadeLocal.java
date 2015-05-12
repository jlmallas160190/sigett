/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao;

import edu.unl.sigett.entity.Renuncia;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author JorgeLuis
 */
@Local
public interface RenunciaFacadeLocal {

    void create(Renuncia renuncia);

    void edit(Renuncia renuncia);

    void remove(Renuncia renuncia);

    Renuncia find(Object id);

    List<Renuncia> findAll();

    List<Renuncia> findRange(int[] range);

    int count();

}
