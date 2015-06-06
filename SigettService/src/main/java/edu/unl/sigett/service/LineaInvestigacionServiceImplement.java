/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.service;

import edu.unl.sigett.dao.LineaInvestigacionCarreraDao;
import edu.unl.sigett.dao.LineaInvestigacionDao;
import edu.unl.sigett.dao.LineaInvestigacionDocenteDao;
import edu.unl.sigett.entity.LineaInvestigacion;
import edu.unl.sigett.entity.LineaInvestigacionCarrera;
import edu.unl.sigett.entity.LineaInvestigacionDocente;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author jorge-luis
 */
@Stateless
public class LineaInvestigacionServiceImplement implements LineaInvestigacionService {

    @EJB
    private LineaInvestigacionCarreraDao lineaInvestigacionCarreraDao;
    @EJB
    private LineaInvestigacionDocenteDao lineaInvestigacionDocenteDao;
    @EJB
    private LineaInvestigacionDao lineaInvestigacionDao;

    @Override
    public List<LineaInvestigacion> buscarPorCarrera(LineaInvestigacionCarrera lineaInvestigacionCarrera) {
        List<LineaInvestigacionCarrera> lics = this.lineaInvestigacionCarreraDao.buscar(lineaInvestigacionCarrera);
        List<LineaInvestigacion> lineaInvestigacions = new ArrayList<>();
        if (lics == null) {
            return new ArrayList<>();
        }
        for (LineaInvestigacionCarrera lic : lics) {
            lineaInvestigacions.add(lic.getLineaInvestigacionId());
        }
        return lineaInvestigacions;
    }

    @Override
    public List<LineaInvestigacion> buscarPorDocente(LineaInvestigacionDocente lineaInvestigacionDocente) {
        List<LineaInvestigacionDocente> lics = this.lineaInvestigacionDocenteDao.buscar(lineaInvestigacionDocente);
        List<LineaInvestigacion> lineaInvestigacions = new ArrayList<>();
        if (lics == null) {
            return new ArrayList<>();
        }
        for (LineaInvestigacionDocente lic : lics) {
            lineaInvestigacions.add(lic.getLineaInvestigacionId());
        }
        return lineaInvestigacions;
    }

    @Override
    public List<LineaInvestigacion> buscarDiferenciaDocenteCarrera(LineaInvestigacionCarrera lineaInvestigacionCarrera,
            LineaInvestigacionDocente lineaInvestigacionDocente) {
        List<LineaInvestigacion> lineaInvestigacionDocentes = this.buscarPorDocente(lineaInvestigacionDocente);
        List<LineaInvestigacion> lineasInvestigacionesCarreras = this.buscarPorCarrera(lineaInvestigacionCarrera);
        List<LineaInvestigacion> lineaInvestigacions = new ArrayList<>();
        if (lineasInvestigacionesCarreras == null) {
            return new ArrayList<>();
        }
        if (lineasInvestigacionesCarreras.isEmpty()) {
            return new ArrayList<>();
        }
        if (lineaInvestigacionDocentes == null) {
            return lineasInvestigacionesCarreras;
        }
        if (lineaInvestigacionDocentes.isEmpty()) {
            return lineasInvestigacionesCarreras;
        }
        for (LineaInvestigacion lic : lineasInvestigacionesCarreras) {
            if (!lineaInvestigacionDocentes.contains(lic)) {
                lineaInvestigacions.add(lic);
            }
        }
        return lineaInvestigacions;
    }

    @Override
    public List<LineaInvestigacion> diferenciaCarreraProyecto(List<LineaInvestigacion> lics, List<LineaInvestigacion> lips) {
        List<LineaInvestigacion> lineaInvestigacions = new ArrayList<>();
        if(lics.isEmpty()){
            return lics;
        }
        if (lips.isEmpty()) {
            return lics;
        }
        for (LineaInvestigacion lic : lics) {
            if (!lips.contains(lic)) {
                lineaInvestigacions.add(lic);
            }
        }
        return lineaInvestigacions;
    }

    @Override
    public void guardar(final LineaInvestigacion lineaInvestigacion) {
        this.lineaInvestigacionDao.create(lineaInvestigacion);
    }

    @Override
    public LineaInvestigacion buscarPorId(final LineaInvestigacion lineaInvestigacion) {
        return this.lineaInvestigacionDao.find(lineaInvestigacion.getId());
    }

}
