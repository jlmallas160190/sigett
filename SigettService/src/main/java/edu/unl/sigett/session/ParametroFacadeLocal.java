/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.session;

import edu.unl.sigett.entity.Parametro;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jorge-luis
 */
@Local
public interface ParametroFacadeLocal {

    void create(Parametro parametro);

    void edit(Parametro parametro);

    void remove(Parametro parametro);

    Parametro find(Object id);

    List<Parametro> findAll();

    List<Parametro> findRange(int[] range);

    int count();

}
