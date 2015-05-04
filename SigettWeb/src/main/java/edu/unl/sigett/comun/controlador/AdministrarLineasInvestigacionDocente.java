/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.comun.controlador;

import edu.jlmallas.academico.entity.Docente;
import edu.jlmallas.academico.entity.DocenteCarrera;
import edu.unl.sigett.entity.LineaInvestigacion;
import edu.unl.sigett.entity.LineaInvestigacionCarrera;
import edu.unl.sigett.entity.LineaInvestigacionDocente;
import org.jlmallas.seguridad.entity.Usuario;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;
import edu.unl.sigett.session.LineaInvestigacionCarreraFacadeLocal;
import edu.unl.sigett.session.LineaInvestigacionDocenteFacadeLocal;
import edu.unl.sigett.session.LineaInvestigacionFacadeLocal;
import org.jlmallas.seguridad.dao.LogDao;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
public class AdministrarLineasInvestigacionDocente implements Serializable {

    private DualListModel<LineaInvestigacion> lineasInvestigacionDualList;
    @EJB
    private LineaInvestigacionCarreraFacadeLocal lineaInvestigacionCarreraFacadeLocal;
    @EJB
    private LineaInvestigacionDocenteFacadeLocal lineaInvestigacionDocenteFacadeLocal;
    @EJB
    private LineaInvestigacionFacadeLocal lineaInvestigacionFacadeLocal;
    @EJB
    private LogDao logFacadeLocal;
    private List<LineaInvestigacionDocente> lineaInvestigacionDocentesRemovidos;
    private List<LineaInvestigacionDocente> lineaInvestigacionDocentesAgregados;

    public AdministrarLineasInvestigacionDocente() {
        this.lineasInvestigacionDualList = new DualListModel<>();
    }

    //<editor-fold defaultstate="collapsed" desc="MÉTODOS CRUD">
    public String buscar(Docente docente) {
        String navegacion = "";
        try {
            this.lineaInvestigacionDocentesAgregados = new ArrayList<>();
            this.lineaInvestigacionDocentesRemovidos = new ArrayList<>();
            listadoLineasInvestigacion(docente);
            navegacion = "editarDocenteLineasInvestigacion?faces-redirect=true";
        } catch (Exception e) {
        }
        return navegacion;
    }

    public String grabarEliminar(Docente docente, Usuario usuario) {
        String navegacion = "";
        FacesContext facesContext = FacesContext.getCurrentInstance();
        String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");

        try {
            grabar(docente, usuario, lineaInvestigacionDocentesAgregados);
            eliminar(docente, usuario, lineaInvestigacionDocentesRemovidos);
            if (param.equalsIgnoreCase("grabar")) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Docente editado exitosamente.", "");
                FacesContext.getCurrentInstance().addMessage(null, message);
                navegacion = "principalDocente?faces-redirect=true";
            } else {
                if (param.equalsIgnoreCase("grabar-editar")) {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Docente editado exitosamente.", "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
            }
        } catch (Exception e) {
        }
        return navegacion;
    }

    public void eliminar(Docente docente, Usuario usuario, List<LineaInvestigacionDocente> lineaInvestigacionDocentes) {
        try {
            if (docente.getId() != null) {
                for (LineaInvestigacionDocente ld : lineaInvestigacionDocentes) {
                    Long id = devuelveLineaInvestigacionEliminar(lineaInvestigacionDocenteFacadeLocal.buscarPorDocenteId(docente.getId()), ld);
                    LineaInvestigacionDocente lid = null;
                    lid = lineaInvestigacionDocenteFacadeLocal.find(id);
                    if (lid != null) {
                        logFacadeLocal.create(logFacadeLocal.crearLog("LineaInvestigacionDocente", ld.getId() + "", "ELIMINAR", " LineaInvestigacion=" + ld.getLineaInvestigacionId() + "|Docente=" + ld.getDocenteId(), usuario));
                        lineaInvestigacionDocenteFacadeLocal.remove(lid);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public Long devuelveLineaInvestigacionEliminar(List<LineaInvestigacionDocente> docenteLineasInv, LineaInvestigacionDocente ld) {
        Long var = (long) 0;
        for (LineaInvestigacionDocente lineaInvestigacionDocente : docenteLineasInv) {
            if (lineaInvestigacionDocente.getLineaInvestigacionId().equals(ld.getLineaInvestigacionId())) {
                var = lineaInvestigacionDocente.getId();
            }
        }
        return var;
    }

    public void grabar(Docente docente, Usuario usuario, List<LineaInvestigacionDocente> lineaInvestigacionDocentes) {
        for (LineaInvestigacionDocente lineaInvestigacionDocente : lineaInvestigacionDocentes) {
            if (contieneLineaInvestigacion(lineaInvestigacionDocenteFacadeLocal.buscarPorDocenteId(docente.getId()), lineaInvestigacionDocente) == false) {
                lineaInvestigacionDocente.setDocenteId(docente.getId());
                lineaInvestigacionDocenteFacadeLocal.create(lineaInvestigacionDocente);
                logFacadeLocal.create(logFacadeLocal.crearLog("LineaInvestigacionDocente", lineaInvestigacionDocente.getId() + "", "CREAR", "|LineaInvestigacion= " + lineaInvestigacionDocente.getLineaInvestigacionId() + "|Docente=" + lineaInvestigacionDocente.getDocenteId(), usuario));
            }
        }
    }

    public boolean contieneLineaInvestigacion(List<LineaInvestigacionDocente> docenteLineasInv, LineaInvestigacionDocente ld) {
        boolean var = false;
        for (LineaInvestigacionDocente lineaInvestigacionDocente : docenteLineasInv) {
            if (lineaInvestigacionDocente.getLineaInvestigacionId().equals(ld.getLineaInvestigacionId())) {
                var = true;
                break;
            }
        }
        return var;
    }

    public void listadoLineasInvestigacion(Docente docente) {
        List<LineaInvestigacion> lineaInvestigacionDocentes = new ArrayList<>();
        List<LineaInvestigacion> lineaInvestigaciones = new ArrayList<>();
        try {
            if (docente.getId() != null) {
                for (LineaInvestigacionDocente lid : lineaInvestigacionDocenteFacadeLocal.buscarPorDocenteId(docente.getId())) {
                    lineaInvestigacionDocentes.add(lid.getLineaInvestigacionId());
                }
                for (DocenteCarrera docenteCarrera : docente.getDocenteCarreraList()) {
                    List<LineaInvestigacionCarrera> lics = new ArrayList<>();
                    lics = lineaInvestigacionCarreraFacadeLocal.buscarPorCarrera(docenteCarrera.getCarreraId().getId());
                    for (LineaInvestigacionCarrera lic : lics) {
                        if (!lineaInvestigacionDocentes.contains(lic.getLineaInvestigacionId())) {
                            lineaInvestigaciones.add(lic.getLineaInvestigacionId());
                        }
                    }
                }
            } else {
                for (DocenteCarrera docenteCarrera : docente.getDocenteCarreraList()) {
                    List<LineaInvestigacionCarrera> lics = new ArrayList<>();
                    lics = lineaInvestigacionCarreraFacadeLocal.buscarPorCarrera(docenteCarrera.getCarreraId().getId());
                    for (LineaInvestigacionCarrera lic : lics) {
                        lineaInvestigaciones.add(lic.getLineaInvestigacionId());
                    }
                }
            }
            lineasInvestigacionDualList = new DualListModel<>(lineaInvestigaciones, lineaInvestigacionDocentes);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void transferDocenteLineaInvestigacion(TransferEvent event) {
        try {
            for (Object item : event.getItems()) {
                int v = item.toString().indexOf(":");
                Long id = Long.parseLong(item.toString().substring(0, v));
                LineaInvestigacion li = lineaInvestigacionFacadeLocal.find(id);
                LineaInvestigacionDocente lid = new LineaInvestigacionDocente();
                lid.setLineaInvestigacionId(li);
                if (event.isRemove()) {
                    lineaInvestigacionDocentesAgregados.remove(lid);
                    lineaInvestigacionDocentesRemovidos.add(lid);
                } else {
                    lineaInvestigacionDocentesAgregados.add(lid);
                    if (contieneLineaInvestigacion(lineaInvestigacionDocentesRemovidos, lid)) {
                        lineaInvestigacionDocentesRemovidos.remove(lid);
                    }
                }
            }
        } catch (Exception e) {
        }
    }
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="SET Y GET">

    public DualListModel<LineaInvestigacion> getLineasInvestigacionDualList() {
        return lineasInvestigacionDualList;
    }

    public void setLineasInvestigacionDualList(DualListModel<LineaInvestigacion> lineasInvestigacionDualList) {
        this.lineasInvestigacionDualList = lineasInvestigacionDualList;
    }

    public List<LineaInvestigacionDocente> getLineaInvestigacionDocentesRemovidos() {
        return lineaInvestigacionDocentesRemovidos;
    }

    public void setLineaInvestigacionDocentesRemovidos(List<LineaInvestigacionDocente> lineaInvestigacionDocentesRemovidos) {
        this.lineaInvestigacionDocentesRemovidos = lineaInvestigacionDocentesRemovidos;
    }

    public List<LineaInvestigacionDocente> getLineaInvestigacionDocentesAgregados() {
        return lineaInvestigacionDocentesAgregados;
    }

    public void setLineaInvestigacionDocentesAgregados(List<LineaInvestigacionDocente> lineaInvestigacionDocentesAgregados) {
        this.lineaInvestigacionDocentesAgregados = lineaInvestigacionDocentesAgregados;
    }
//</editor-fold>
}
