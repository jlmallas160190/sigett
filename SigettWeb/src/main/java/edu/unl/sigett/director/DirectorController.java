/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.director;

import com.jlmallas.comun.entity.Persona;
import com.jlmallas.comun.service.PersonaService;
import edu.unl.sigett.academico.docenteCarrera.DocenteCarreraDTO;
import edu.unl.sigett.directorProyecto.DirectorProyectoDM;
import edu.unl.sigett.docenteProyecto.SessionDocenteProyecto;
import edu.unl.sigett.entity.Director;
import edu.unl.sigett.entity.LineaInvestigacion;
import edu.unl.sigett.entity.LineaInvestigacionDocente;
import edu.unl.sigett.proyecto.SessionProyecto;
import edu.unl.sigett.service.DirectorService;
import edu.unl.sigett.service.LineaInvestigacionDocenteService;
import edu.unl.sigett.usuarioCarrera.SessionUsuarioCarrera;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.primefaces.context.RequestContext;

/**
 *
 * @author jorge-luis
 */
@Named(value = "directorController")
@SessionScoped
public class DirectorController implements Serializable {

    //<editor-fold defaultstate="collapsed" desc="MANAGED BEANS">
    @Inject
    private DirectorDM directorDM;
    @Inject
    private SessionUsuarioCarrera sessionUsuarioCarrera;
    @Inject
    private SessionProyecto sessionProyecto;
    @Inject
    private SessionDocenteProyecto sessionDocenteProyecto;
    @Inject
    private DirectorProyectoDM directorProyectoDM;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="SERVICIOS">
    @EJB(lookup = "java:global/SigettService/LineaInvestigacionDocenteServiceImplement!edu.unl.sigett.service.LineaInvestigacionDocenteService")
    private LineaInvestigacionDocenteService lineaInvestigacionDocenteService;
    @EJB(lookup = "java:global/SigettService/DirectorServiceImplement!edu.unl.sigett.service.DirectorService")
    private DirectorService directorService;
    @EJB(lookup = "java:global/ComunService/PersonaServiceImplement!com.jlmallas.comun.service.PersonaService")
    private PersonaService personaService;

    //</editor-fold>
    private static final Logger LOG = Logger.getLogger(DirectorController.class.getName());
    
    public DirectorController() {
    }

    /**
     * LISTADO DE DIRECTORES DISPONIBLES DE ACUERDO A LAS LINEAS DE
     * INVESTIGACIÓN SELECCIONADAS EN EL PROYECTO. LOS DIRECTORES DEBEN SER
     * DOCENTES DE LA CARRERA QUE ADMINISTRA EL USUARIO ESTAR VIGENTES POSEER
     * LAS MISMA LINEAS DE INVESTIGACIÓN DEL PROYECTO SELECCCIONADO.
     */
    public void listadoDirectoresDiponibles() {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("tipo");
            directorProyectoDM.setRenderedPnlDirectoresDisponibles(Boolean.FALSE);
            sessionDocenteProyecto.setRenderedPnlDocentesDisponibles(Boolean.FALSE);
            if (param.equals("director")) {
                directorProyectoDM.setRenderedPnlDirectoresDisponibles(Boolean.TRUE);
                RequestContext.getCurrentInstance().execute("PF('dlgDirectoresDisponibles').show()");
            } else {
                sessionDocenteProyecto.setRenderedPnlDocentesDisponibles(Boolean.TRUE);
                RequestContext.getCurrentInstance().execute("PF('dlgDocentesDisponibles').show()");
            }
            this.directorDM.getDirectoresDTO().clear();
            this.directorDM.getFilterDirectoresDTO().clear();
            for (DocenteCarreraDTO docenteCarreraDTO : sessionUsuarioCarrera.getDocentesCarreraDTO()) {
                List<LineaInvestigacionDocente> lineaInvestigacionDocentes = lineaInvestigacionDocenteService.buscar(
                        new LineaInvestigacionDocente(docenteCarreraDTO.getDocenteCarrera().getDocenteId().getId(), null));
                if (lineaInvestigacionDocentes.isEmpty()) {
                    continue;
                }
                for (LineaInvestigacion lineaInvestigacion : sessionProyecto.getLineasInvestigacionSeleccionadas()) {
                    for (LineaInvestigacionDocente lid : lineaInvestigacionDocentes) {
                        if (!lineaInvestigacion.equals(lid.getLineaInvestigacionId())) {
                            continue;
                        }
                        DirectorDTO directorDTO = new DirectorDTO(directorService.buscarPorId(new Director(
                                docenteCarreraDTO.getDocenteCarrera().getId())), docenteCarreraDTO.getDocenteCarrera(), personaService.buscarPorId(
                                        new Persona(docenteCarreraDTO.getDocenteCarrera().getDocenteId().getId())));
                        if (!containDirector(directorDTO)) {
                            directorDM.getDirectoresDTO().add(directorDTO);
                        }
                    }
                }
            }
            
            directorDM.setFilterDirectoresDTO(directorDM.getDirectoresDTO());
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }
    }

    private Boolean containDirector(DirectorDTO directorDTO) {
        for (DirectorDTO director : directorDM.getDirectoresDTO()) {
            if (director.getDirector().equals(directorDTO.getDirector())) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }
}
