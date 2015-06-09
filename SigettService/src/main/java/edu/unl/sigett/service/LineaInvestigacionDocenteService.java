/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.service;

import edu.unl.sigett.entity.LineaInvestigacionDocente;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jorge-luis
 */
@Local
public interface LineaInvestigacionDocenteService {

    void guardar(final LineaInvestigacionDocente lineaInvestigacionDocente);

    void actualizar(final LineaInvestigacionDocente lineaInvestigacionDocente);

    void eliminar(final LineaInvestigacionDocente lineaInvestigacionDocente);

    LineaInvestigacionDocente buscarPorId(final LineaInvestigacionDocente lineaInvestigacionDocente);

    List<LineaInvestigacionDocente> buscar(final LineaInvestigacionDocente lineaInvestigacionDocente);
}
