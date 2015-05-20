/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.comun.controlador;

import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;
import edu.unl.sigett.comun.managed.session.SessionLineaInvestigacion;
import edu.unl.sigett.seguridad.managed.session.SessionUsuario;
import edu.jlmallas.academico.entity.Carrera;
import edu.unl.sigett.entity.LineaInvestigacion;
import edu.unl.sigett.entity.LineaInvestigacionCarrera;
import org.jlmallas.seguridad.entity.Usuario;
import edu.unl.sigett.entity.UsuarioCarrera;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;
import edu.jlmallas.academico.service.CarreraService;
import edu.unl.sigett.dao.LineaInvestigacionCarreraDao;
import edu.unl.sigett.dao.LineaInvestigacionDao;
import org.jlmallas.seguridad.dao.LogDao;
import org.jlmallas.seguridad.dao.UsuarioDao;
import edu.unl.sigett.dao.UsuarioCarreraDao;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
@URLMappings(mappings = {
    @URLMapping(
            id = "editarLineaInvestigacion",
            pattern = "/editarLineaInvestigacion/#{sessionLineaInvestigacion.lineaInvestigacion.id}",
            viewId = "/faces/pages/sigett/editarLineaInvestigacion.xhtml"
    ),
    @URLMapping(
            id = "crearLineaInvestigacion",
            pattern = "/crearLineaInvestigacion/",
            viewId = "/faces/pages/sigett/editarLineaInvestigacion.xhtml"
    ),
    @URLMapping(
            id = "lineasInvestigacion",
            pattern = "/lineasInvestigacion/",
            viewId = "/faces/pages/sigett/buscarLineasInvestigacion.xhtml"
    )})
public class AdministrarLineaInvestigacion implements Serializable {

    @Inject
    private SessionUsuario sessionUsuario;
    @Inject
    private SessionLineaInvestigacion sessionLineaInvestigacion;
    @EJB
    private LineaInvestigacionDao lineaInvestigacionFacadeLocal;
    @EJB
    private LogDao logFacadeLocal;
    private DualListModel<Carrera> carrerasDualList;
    private String criterio;
    private List<LineaInvestigacionCarrera> licsRemovidos;
    private List<LineaInvestigacionCarrera> lineasInvestigacionCarreraGrabar;
    @EJB
    private LineaInvestigacionCarreraDao lineaInvestigacionCarreraFacadeLocal;
    @EJB
    private CarreraService carreraFacadeLocal;
    @EJB
    private UsuarioDao usuarioFacadeLocal;
    @EJB
    private UsuarioCarreraDao usuarioCarreraFacadeLocal;

    private List<LineaInvestigacion> lineasInvestigacion;
    private boolean renderedNoEditar;
    private boolean renderedBuscar;

    public AdministrarLineaInvestigacion() {
        renderedBuscar = false;
        this.carrerasDualList = new DualListModel<>();
    }

    //<editor-fold defaultstate="collapsed" desc="MÉTODOS CRUD">
    public String crear(Usuario usuario) {
        String navegacion = "";
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "crear_linea_investigacion");
            if (tienePermiso == 1) {
                sessionLineaInvestigacion.setLineaInvestigacion(new LineaInvestigacion());
                listadoCarreras(new LineaInvestigacion());
                licsRemovidos = new ArrayList<>();
                navegacion = "pretty:crearLineaInvestigacion";
            } else {
                if (tienePermiso == 2) {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear") + ". " + bundle.getString("lbl.msm_consulte"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                } else {
                    navegacion = "pretty:login";
                }
            }
        } catch (Exception e) {
        }
        return navegacion;
    }

    public String editar(LineaInvestigacion lineaInvestigacion) {
        String navegacion = "";
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "editar_linea_investigacion");
            if (tienePermiso == 1) {
                sessionLineaInvestigacion.setLineaInvestigacion(lineaInvestigacion);
                licsRemovidos = new ArrayList<>();
                listadoCarreras(lineaInvestigacion);
                navegacion = "pretty:editarLineaInvestigacion";
            } else {
                if (tienePermiso == 2) {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_editar") + ". " + bundle.getString("lbl.msm_consulte"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                } else {
                    navegacion = "pretty:login";
                }
            }
        } catch (Exception e) {
        }
        return navegacion;
    }

    public void eliminar(LineaInvestigacion lineaInvestigacion) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "eliminar_linea_investigacion");
            if (tienePermiso == 1) {
                if (lineaInvestigacion.getLineaInvestigacionCarreraList().isEmpty() && lineaInvestigacion.getLineaInvestigacionDocenteList().isEmpty() && lineaInvestigacion.getLineaInvestigacionProyectoList().isEmpty()) {
                    logFacadeLocal.create(logFacadeLocal.crearLog("LineaInvestigacion", lineaInvestigacion.getId() + "", "ELIMINAR", "|Nombre= " + lineaInvestigacion.getNombre() + "|Descripción= " + lineaInvestigacion.getDescripcion(), sessionUsuario.getUsuario()));
                    lineaInvestigacionFacadeLocal.remove(lineaInvestigacion);
                    buscarLineasInvestigacion("");
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_eliminar"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                } else {
                    if (lineaInvestigacion.getEsActivo()) {
                        lineaInvestigacion.setEsActivo(false);
                        logFacadeLocal.create(logFacadeLocal.crearLog("LineaInvestigacion", lineaInvestigacion.getId() + "", "DESACTIVAR", "|Nombre= " + lineaInvestigacion.getNombre() + "|Descripción= " + lineaInvestigacion.getDescripcion(), sessionUsuario.getUsuario()));
                    } else {
                        lineaInvestigacion.setEsActivo(true);
                        logFacadeLocal.create(logFacadeLocal.crearLog("LineaInvestigacion", lineaInvestigacion.getId() + "", "ACTIVAR", "|Nombre= " + lineaInvestigacion.getNombre() + "|Descripción= " + lineaInvestigacion.getDescripcion(), sessionUsuario.getUsuario()));
                    }
                    lineaInvestigacionFacadeLocal.edit(lineaInvestigacion);
                    buscarLineasInvestigacion("");
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_eliminar"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
            }
        } catch (Exception e) {
        }
    }

    public boolean disabledEditar(LineaInvestigacion li) {
        boolean var = false;
        if (li.getEsActivo() == false) {
            var = true;
        }
        return var;
    }

    public String grabar(LineaInvestigacion lineaInvestigacion) {
        String navegacion = "";
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
            if (lineaInvestigacion.getId() == null) {
                int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "crear_linea_investigacion");//guardar Usuario
                if (tienePermiso == 1) {
                    if (param.equalsIgnoreCase("guardar")) {
                        lineaInvestigacionFacadeLocal.create(lineaInvestigacion);
                        logFacadeLocal.create(logFacadeLocal.crearLog("LineaInvestigacion", lineaInvestigacion.getId() + "", "CREAR", "|Nombre= " + lineaInvestigacion.getNombre() + "|Descripción= " + lineaInvestigacion.getDescripcion(), sessionUsuario.getUsuario()));
                        grabarLineasInvestigacionCarrera(lineaInvestigacion);
                        removerLineasInvestigacionCarreras(lineaInvestigacion);
                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_grabar"), "");
                        FacesContext.getCurrentInstance().addMessage(null, message);
                        navegacion = "pretty:lineasInvestigacion";
                    } else {
                        if (param.equalsIgnoreCase("guardar y editar")) {
                            lineaInvestigacionFacadeLocal.create(lineaInvestigacion);
                            logFacadeLocal.create(logFacadeLocal.crearLog("LineaInvestigacion", lineaInvestigacion.getId() + "", "CREAR", "|Nombre= " + lineaInvestigacion.getNombre() + "|Descripción= " + lineaInvestigacion.getDescripcion(), sessionUsuario.getUsuario()));
                            grabarLineasInvestigacionCarrera(lineaInvestigacion);
                            removerLineasInvestigacionCarreras(lineaInvestigacion);
                            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_grabar"), "");
                            FacesContext.getCurrentInstance().addMessage(null, message);
                        } else {
                            lineaInvestigacionFacadeLocal.create(lineaInvestigacion);
                            logFacadeLocal.create(logFacadeLocal.crearLog("LineaInvestigacion", lineaInvestigacion.getId() + "", "CREAR", "|Nombre= " + lineaInvestigacion.getNombre() + "|Descripción= " + lineaInvestigacion.getDescripcion(), sessionUsuario.getUsuario()));
                            grabarLineasInvestigacionCarrera(lineaInvestigacion);
                            removerLineasInvestigacionCarreras(lineaInvestigacion);
                            sessionLineaInvestigacion.setLineaInvestigacion(new LineaInvestigacion());
                        }
                    }
                    buscarLineasInvestigacion("");
                } else {
                    if (tienePermiso == 2) {
                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear") + ". " + bundle.getString("lbl.msm_consulte"), "");
                        FacesContext.getCurrentInstance().addMessage(null, message);
                    } else {
                        navegacion = "pretty:login";
                    }
                }
            } else {
                int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "editar_linea_investigacion");//editar
                if (tienePermiso == 1) {
                    if (param.equalsIgnoreCase("guardar")) {
                        lineaInvestigacionFacadeLocal.edit(lineaInvestigacion);
                        logFacadeLocal.create(logFacadeLocal.crearLog("LineaInvestigacion", lineaInvestigacion.getId() + "", "EDITAR", "|Nombre= " + lineaInvestigacion.getNombre() + "|Descripción= " + lineaInvestigacion.getDescripcion(), sessionUsuario.getUsuario()));
                        grabarLineasInvestigacionCarrera(lineaInvestigacion);
                        removerLineasInvestigacionCarreras(lineaInvestigacion);
                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_editar"), "");
                        FacesContext.getCurrentInstance().addMessage(null, message);
                        navegacion = "pretty:lineasInvestigacion";
                    } else {
                        if (param.equalsIgnoreCase("guardar y editar")) {
                            lineaInvestigacionFacadeLocal.edit(lineaInvestigacion);
                            logFacadeLocal.create(logFacadeLocal.crearLog("LineaInvestigacion", lineaInvestigacion.getId() + "", "EDITAR", "|Nombre= " + lineaInvestigacion.getNombre() + "|Descripción= " + lineaInvestigacion.getDescripcion(), sessionUsuario.getUsuario()));
                            grabarLineasInvestigacionCarrera(lineaInvestigacion);
                            removerLineasInvestigacionCarreras(lineaInvestigacion);
                            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_editar"), "");
                            FacesContext.getCurrentInstance().addMessage(null, message);
                        } else {
                            lineaInvestigacionFacadeLocal.edit(lineaInvestigacion);
                            logFacadeLocal.create(logFacadeLocal.crearLog("LineaInvestigacion", lineaInvestigacion.getId() + "", "EDITAR", "|Nombre= " + lineaInvestigacion.getNombre() + "|Descripción= " + lineaInvestigacion.getDescripcion(), sessionUsuario.getUsuario()));
                            grabarLineasInvestigacionCarrera(lineaInvestigacion);
                            removerLineasInvestigacionCarreras(lineaInvestigacion);
                            sessionLineaInvestigacion.setLineaInvestigacion(new LineaInvestigacion());
                        }
                    }
                } else {
                    if (tienePermiso == 2) {
                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_editar") + ". " + bundle.getString("lbl.msm_consulte"), "");
                        FacesContext.getCurrentInstance().addMessage(null, message);
                    } else {
                        navegacion = "pretty:login";
                    }
                }
                buscarLineasInvestigacion("");
            }
        } catch (Exception e) {
        }

        return navegacion;
    }

    public void buscarLineasInvestigacion(String criterio) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            this.lineasInvestigacion = new ArrayList<>();
            int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "buscar_linea_investigacion");
            if (tienePermiso == 1) {
                this.lineasInvestigacion = lineaInvestigacionFacadeLocal.buscarPorCriterio(criterio);
            } else {
                if (tienePermiso == 2) {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_buscar") + ". " + bundle.getString("lbl.msm_consulte"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                } else {
                    FacesContext.getCurrentInstance().getExternalContext().redirect("pretty:login");
                }
            }
        } catch (Exception e) {
        }
    }

    public void grabarLineasInvestigacionCarrera(LineaInvestigacion li) {
        List<LineaInvestigacionCarrera> lics = new ArrayList<>();
        for (Object o : carrerasDualList.getTarget()) {
            int v = o.toString().indexOf(":");
            Integer id = Integer.parseInt(o.toString().substring(0, v));
            Carrera c = carreraFacadeLocal.find(id);
            LineaInvestigacionCarrera lic = new LineaInvestigacionCarrera();
            lic.setCarreraId(c.getId());
            lic.setLineaInvestigacionId(li);
            lics.add(lic);
        }
        for (LineaInvestigacionCarrera lineaInvestigacionCarrera : lics) {
//            if (contieneCarrera(lineaInvestigacionCarreraFacadeLocal.buscarPorLineaInvestigacion(li.getId()), lineaInvestigacionCarrera) == false) {
//                lineaInvestigacionCarreraFacadeLocal.create(lineaInvestigacionCarrera);
//                logFacadeLocal.create(logFacadeLocal.crearLog("LineaInvestigacionCarrera", lineaInvestigacionCarrera.getId() + "", "CREAR", "|Carrera=" + lineaInvestigacionCarrera.getCarreraId() + "|LineaInvestigacion=" + lineaInvestigacionCarrera.getLineaInvestigacionId(), sessionUsuario.getUsuario()));
//            }
        }
    }

    public void removerLineasInvestigacionCarreras(LineaInvestigacion li) {
//        if (li.getId() != null) {
//            for (LineaInvestigacionCarrera lic : licsRemovidos) {
//                Long id = devuelveCarreraEliminar(lineaInvestigacionCarreraFacadeLocal.buscarPorLineaInvestigacion(li.getId()), lic);
//                LineaInvestigacionCarrera lineaInvestigacionCarrera = null;
//                lineaInvestigacionCarrera = lineaInvestigacionCarreraFacadeLocal.find(id);
//                if (lineaInvestigacionCarrera != null) {
//                    logFacadeLocal.create(logFacadeLocal.crearLog("LineaInvestigacionCarrera", lineaInvestigacionCarrera.getId() + "", "DELETE", "ELIMINAR: |Carrera=" + lineaInvestigacionCarrera.getCarreraId() + "|LineaInvestigacion=" + lineaInvestigacionCarrera.getLineaInvestigacionId(), sessionUsuario.getUsuario()));
//                    lineaInvestigacionCarreraFacadeLocal.remove(lineaInvestigacionCarrera);
//                }
//            }
//        }
    }

    public void listadoCarreras(LineaInvestigacion li) {
        List<Carrera> lineasInvestigacionCarreras = new ArrayList<>();
        List<Carrera> carreras = new ArrayList<>();
        try {
//            if (li.getId() != null) {
//                for (LineaInvestigacionCarrera lic : lineaInvestigacionCarreraFacadeLocal.buscarPorLineaInvestigacion(li.getId())) {
//                    Carrera carrera = carreraFacadeLocal.find(lic.getCarreraId());
//                    lineasInvestigacionCarreras.add(carrera);
//                }
//                for (UsuarioCarrera usuarioCarrera : usuarioCarreraFacadeLocal.buscarPorUsuario(sessionUsuario.getUsuario().getId())) {
//                    Carrera carrera = carreraFacadeLocal.find(usuarioCarrera.getCarreraId());
//                    if (!lineasInvestigacionCarreras.contains(carrera)) {
//                        carreras.add(carrera);
//
//                    }
//                }
//            } else {
//                for (UsuarioCarrera usuarioCarrera : usuarioCarreraFacadeLocal.buscarPorUsuario(sessionUsuario.getUsuario().getId())) {
//                    Carrera carrera = carreraFacadeLocal.find(usuarioCarrera.getCarreraId());
//                    carreras.add(carrera);
//                }
//            }
            carrerasDualList = new DualListModel<>(carreras, lineasInvestigacionCarreras);
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public boolean contieneCarrera(List<LineaInvestigacionCarrera> lics, LineaInvestigacionCarrera lic) {
        boolean var = false;
        for (LineaInvestigacionCarrera lineaInvestigacionCarrera : lics) {
            Carrera carrera = carreraFacadeLocal.find(lineaInvestigacionCarrera.getCarreraId());
            if (carrera.equals(lic.getCarreraId())) {
                var = true;
                break;
            }
        }
        return var;
    }

    public Long devuelveCarreraEliminar(List<LineaInvestigacionCarrera> lics, LineaInvestigacionCarrera lic) {
        Long var = (long) 0;
        for (LineaInvestigacionCarrera lineaInvestigacionCarrera : lics) {
            Carrera carrera = carreraFacadeLocal.find(lineaInvestigacionCarrera.getCarreraId());
            if (carrera.equals(lic.getCarreraId())) {
                var = lineaInvestigacionCarrera.getId();
                break;
            }
        }
        return var;
    }

    public void transferLineaInvestigacionCarrera(TransferEvent event) {
        try {
            for (Object item : event.getItems()) {
                int v = item.toString().indexOf(":");
                Integer id = Integer.parseInt(item.toString().substring(0, v));
                Carrera c = carreraFacadeLocal.find(id);
                LineaInvestigacionCarrera lic = new LineaInvestigacionCarrera();
                lic.setCarreraId(c.getId());
                if (event.isRemove()) {
                    licsRemovidos.add(lic);
                    lineasInvestigacionCarreraGrabar.add(lic);
                } else {
                    lineasInvestigacionCarreraGrabar.add(lic);
                    licsRemovidos.remove(lic);
                }
            }
        } catch (Exception e) {
        }
    }
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="MÉTODOS RENDERED">

    public boolean renderedEditar() {
        boolean var = false;
        int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "editar_linea_investigacion");
        if (tienePermiso == 1) {
            var = true;
            renderedNoEditar = false;
        } else {
            renderedNoEditar = true;
        }
        return var;
    }

    public boolean renderedEliminar() {
        boolean var = false;
        int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "eliminar_linea_investigacion");
        if (tienePermiso == 1) {
            var = true;
        }
        return var;
    }

    public void renderedBuscar(Usuario usuario) {
        int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "buscar_linea_investigacion");
        if (tienePermiso == 1) {
            renderedBuscar = true;
        } else {
            renderedBuscar = false;
        }
    }

    public boolean renderedCrear() {
        boolean var = false;
        int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "crear_linea_investigacion");
        if (tienePermiso == 1) {
            var = true;
        }
        return var;
    }

//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="SET Y GET">
    public List<LineaInvestigacionCarrera> getLineasInvestigacionCarreraGrabar() {
        return lineasInvestigacionCarreraGrabar;
    }

    public void setLineasInvestigacionCarreraGrabar(List<LineaInvestigacionCarrera> lineasInvestigacionCarreraGrabar) {
        this.lineasInvestigacionCarreraGrabar = lineasInvestigacionCarreraGrabar;
    }

    public SessionUsuario getSessionUsuario() {
        return sessionUsuario;
    }

    public void setSessionUsuario(SessionUsuario sessionUsuario) {
        this.sessionUsuario = sessionUsuario;
    }

    public SessionLineaInvestigacion getSessionLineaInvestigacion() {
        return sessionLineaInvestigacion;
    }

    public void setSessionLineaInvestigacion(SessionLineaInvestigacion sessionLineaInvestigacion) {
        this.sessionLineaInvestigacion = sessionLineaInvestigacion;
    }

    public String getCriterio() {
        return criterio;
    }

    public void setCriterio(String criterio) {
        this.criterio = criterio;
    }

    public DualListModel<Carrera> getCarrerasDualList() {
        return carrerasDualList;
    }

    public void setCarrerasDualList(DualListModel<Carrera> carrerasDualList) {
        this.carrerasDualList = carrerasDualList;
    }

    public List<LineaInvestigacionCarrera> getLicsRemovidos() {
        return licsRemovidos;
    }

    public void setLicsRemovidos(List<LineaInvestigacionCarrera> licsRemovidos) {
        this.licsRemovidos = licsRemovidos;
    }

    public List<LineaInvestigacion> getLineasInvestigacion() {
        return lineasInvestigacion;
    }

    public void setLineasInvestigacion(List<LineaInvestigacion> lineasInvestigacion) {
        this.lineasInvestigacion = lineasInvestigacion;
    }

    public boolean isRenderedNoEditar() {
        return renderedNoEditar;
    }

    public void setRenderedNoEditar(boolean renderedNoEditar) {
        this.renderedNoEditar = renderedNoEditar;
    }

    public boolean isRenderedBuscar() {
        return renderedBuscar;
    }

    public void setRenderedBuscar(boolean renderedBuscar) {
        this.renderedBuscar = renderedBuscar;
    }
//</editor-fold>
}
