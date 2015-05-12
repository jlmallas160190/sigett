/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao;

import edu.unl.sigett.entity.Expediente;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author JorgeLuis
 */
@Local
public interface ExpedienteFacadeLocal {

    void create(Expediente expediente);

    void edit(Expediente expediente);

    void remove(Expediente expediente);

    Expediente find(Object id);

    List<Expediente> findAll();

    List<Expediente> findRange(int[] range);

    int count();

}
