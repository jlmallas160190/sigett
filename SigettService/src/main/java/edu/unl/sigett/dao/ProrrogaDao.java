/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao;

import edu.unl.sigett.entity.Prorroga;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author JorgeLuis
 */
@Local
public interface ProrrogaDao {

    void create(Prorroga prorroga);

    void edit(Prorroga prorroga);

    void remove(Prorroga prorroga);

    Prorroga find(Object id);

    List<Prorroga> findAll();

    List<Prorroga> findRange(int[] range);

    List<Prorroga> buscar(final Prorroga prorroga);

    Prorroga obtenerPorFechaMayor(final Prorroga prorroga);

    int count();

}
