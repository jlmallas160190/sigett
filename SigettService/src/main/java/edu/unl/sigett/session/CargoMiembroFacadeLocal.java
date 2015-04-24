/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.session;

import edu.unl.sigett.entity.CargoMiembro;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author JorgeLuis
 */
@Local
public interface CargoMiembroFacadeLocal {

    void create(CargoMiembro cargoMiembro);

    void edit(CargoMiembro cargoMiembro);

    void remove(CargoMiembro cargoMiembro);

    CargoMiembro find(Object id);

    List<CargoMiembro> findAll();

    List<CargoMiembro> findRange(int[] range);

    List<CargoMiembro> buscarActivos();

    CargoMiembro buscarPorCodigo(String codigo);

    int count();

}
