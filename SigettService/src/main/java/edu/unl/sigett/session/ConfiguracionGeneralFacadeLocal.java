/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.session;

import edu.unl.sigett.entity.ConfiguracionGeneral;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author JorgeLuis
 */
@Local
public interface ConfiguracionGeneralFacadeLocal {

    void create(ConfiguracionGeneral configuracionGeneral);

    void edit(ConfiguracionGeneral configuracionGeneral);

    void remove(ConfiguracionGeneral configuracionGeneral);

    ConfiguracionGeneral find(Object id);

    List<ConfiguracionGeneral> findAll();

    List<ConfiguracionGeneral> findRange(int[] range);

    String getSecretKey();

    String timestampFormat(Date fecha);

    String dateFormat(Date fecha);

    String timeFormat(Date fecha);

    List<ConfiguracionGeneral> buscarPorNombre(String nombre);

    Date DeStringADate(String fecha);

    String usuarioBd();

    String claveBd();

    int count();

}
