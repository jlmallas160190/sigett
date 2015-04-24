/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.session;

import edu.unl.sigett.entity.LineaInvestigacionDocente;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author JorgeLuis
 */
@Local
public interface LineaInvestigacionDocenteFacadeLocal {

    void create(LineaInvestigacionDocente lineaInvestigacionDocente);

    void edit(LineaInvestigacionDocente lineaInvestigacionDocente);

    void remove(LineaInvestigacionDocente lineaInvestigacionDocente);

    LineaInvestigacionDocente find(Object id);

    List<LineaInvestigacionDocente> findAll();

    List<LineaInvestigacionDocente> findRange(int[] range);

    List<LineaInvestigacionDocente> buscarPorDocenteCi(String numeroIdentificacion);

    List<LineaInvestigacionDocente> buscarPorDocenteId(Long docenteId);

    int count();

}
