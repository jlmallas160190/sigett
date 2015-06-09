/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.service;

import edu.unl.sigett.entity.Renuncia;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jorge-luis
 */
@Local
public interface RenunciaService {

    void guardar(final Renuncia renuncia);

    void actualizar(final Renuncia renuncia);

    void eliminar(final Renuncia renuncia);

    Renuncia buscarPorId(final Renuncia renuncia);

    List<Renuncia> buscar(final Renuncia renuncia);
}
