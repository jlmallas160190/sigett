/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.jlmallas.academico.service.implement;

import edu.jlmallas.academico.dao.AbstractFacade;
import edu.jlmallas.academico.dao.NivelDao;
import edu.jlmallas.academico.entity.Nivel;
import edu.jlmallas.academico.service.NivelService;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author JorgeLuis
 */
@Stateless
public class NivelServiceImplement extends AbstractFacade<Nivel> implements NivelService {

    @EJB
    private NivelDao nivelDao;

    public NivelServiceImplement() {
        super(Nivel.class);
    }

    public Nivel buscarPorNombre(Nivel nivel) {
        List<Nivel> niveles = new ArrayList<>();
        try {
            niveles = nivelDao.buscarPorNombre(nivel);
            if (niveles == null) {
                return null;
            }
            return !niveles.isEmpty() ? niveles.get(0) : null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
