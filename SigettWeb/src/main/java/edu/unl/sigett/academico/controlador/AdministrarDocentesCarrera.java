/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.academico.controlador;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.jlmallas.comun.entity.Item;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;
import edu.unl.sigett.academico.managed.session.SessionDocenteCarrera;
import edu.unl.sigett.seguridad.controlador.AdministrarUsuarios;
import edu.unl.sigett.seguridad.managed.session.SessionUsuario;
import edu.unl.sigett.seguridad.managed.session.SessionUsuarioCarrera;
import edu.jlmallas.academico.entity.Carrera;
import edu.unl.sigett.entity.Director;
import edu.jlmallas.academico.entity.Docente;
import edu.jlmallas.academico.entity.DocenteCarrera;
import edu.jlmallas.academico.entity.EstadoLaboral;
import edu.unl.sigett.entity.LineaInvestigacion;
import edu.unl.sigett.entity.LineaInvestigacionCarrera;
import edu.unl.sigett.entity.LineaInvestigacionDocente;
import com.jlmallas.comun.entity.Persona;
import com.jlmallas.comun.enumeration.CatalogoEnum;
import com.jlmallas.comun.service.ItemFacadeLocal;
import edu.jlmallas.academico.entity.TituloDocente;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
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
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;
import edu.unl.sigett.session.ConfiguracionGeneralFacadeLocal;
import edu.unl.sigett.session.DirectorFacadeLocal;
import edu.jlmallas.academico.service.DocenteCarreraFacadeLocal;
import edu.jlmallas.academico.service.DocenteFacadeLocal;
import edu.jlmallas.academico.service.EstadoLaboralFacadeLocal;
import edu.unl.sigett.session.LineaInvestigacionCarreraFacadeLocal;
import edu.unl.sigett.session.LineaInvestigacionDocenteFacadeLocal;
import edu.unl.sigett.session.LineaInvestigacionFacadeLocal;
import org.jlmallas.seguridad.dao.LogDao;
import com.jlmallas.comun.service.PersonaFacadeLocal;
import edu.jlmallas.academico.service.TituloDocenteFacadeLocal;
import org.jlmallas.seguridad.dao.UsuarioDao;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
@URLMappings(mappings = {
    @URLMapping(
            id = "editarDocenteCarrera",
            pattern = "/editarDocenteCarrera/#{sessionDocenteCarrera.docenteCarrera.id}",
            viewId = "/faces/pages/academico/editarDocenteCarrera.xhtml"
    ),
    @URLMapping(
            id = "crearDocenteCarrera",
            pattern = "/crearDocenteCarrera/",
            viewId = "/faces/pages/academico/editarDocenteCarrera.xhtml"
    ),
    @URLMapping(
            id = "docentesCarrera",
            pattern = "/docentesCarrera/",
            viewId = "/faces/pages/academico/buscarDocentesCarrera.xhtml"
    )
})
public class AdministrarDocentesCarrera implements Serializable {

    @Inject
    private SessionDocenteCarrera sessionDocenteCarrera;
    @Inject
    private SessionUsuario sessionUsuario;
    @Inject
    private SessionUsuarioCarrera sessionUsuarioCarrera;
    @Inject
    private AdministrarUsuarios administrarUsuarios;

    @EJB
    private LogDao logFacadeLocal;
    @EJB
    private DocenteCarreraFacadeLocal docenteCarreraFacadeLocal;
    @EJB
    private DocenteFacadeLocal docenteFacadeLocal;
    @EJB
    private PersonaFacadeLocal personaFacadeLocal;
    @EJB
    private ItemFacadeLocal itemFacadeLocal;
    @EJB
    private EstadoLaboralFacadeLocal estadoLaboralFacadeLocal;
    @EJB
    private UsuarioDao usuarioFacadeLocal;
    @EJB
    private LineaInvestigacionDocenteFacadeLocal lineaInvestigacionDocenteFacadeLocal;
    @EJB
    private LineaInvestigacionCarreraFacadeLocal lineaInvestigacionCarreraFacadeLocal;
    @EJB
    private LineaInvestigacionFacadeLocal lineaInvestigacionFacadeLocal;
    @EJB
    private TituloDocenteFacadeLocal tituloDocenteFacadeLocal;
    @EJB
    private ConfiguracionGeneralFacadeLocal configuracionGeneralFacadeLocal;
    @EJB
    private DirectorFacadeLocal directorFacadeLocal;
    
    private List<DocenteCarrera> docenteCarreras;
    private List<LineaInvestigacionDocente> lineaInvestigacionDocentesRemovidos;
    private DualListModel<LineaInvestigacion> lineasInvestigacionDualList;

    private String tipoDocumento;
    private String genero;
    private String estadoLaboral;
    private String titulo;
    private String criterio;
    private String keyUnidadesDocenteParalelo;
    private int i = 0;
    private int keyEnteroUnidadesDocenteParelelo;
    private String key;
    private int keyEntero;
    private String tituloDocente;

    private boolean renderedNoEditar;

    public AdministrarDocentesCarrera() {
        this.lineasInvestigacionDualList = new DualListModel<>();
    }

    //<editor-fold defaultstate="collapsed" desc="MÉTODOS CRUD">
    public String viewDocentesCarrera() {
        this.docenteCarreras = new ArrayList<>();
        return "pretty:docentesCarrera";
    }

    public String crear() {
        String navegacion = "";
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "crear_docente_carrera");
            if (tienePermiso == 1) {
                sessionDocenteCarrera.setDocenteCarrera(new DocenteCarrera());
                sessionDocenteCarrera.getDocenteCarrera().setDocenteId(new Docente());
                sessionDocenteCarrera.setPersona(new Persona());
                sessionDocenteCarrera.setDirector(new Director());
//                sessionDocenteCarrera.getDocenteCarrera().setCarreraId(sessionUsuarioCarrera.getUsuarioCarrera().getCarreraId());
                estadoLaboral = "";
                titulo = "";
                this.lineaInvestigacionDocentesRemovidos = new ArrayList<>();
                listadoLineasInvestigacion(new Docente());
                navegacion = "pretty:crearDocenteCarrera";
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

    public String editar(DocenteCarrera docenteCarrera) {
        String navegacion = "";
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "editar_docente_carrera");
            if (tienePermiso == 1) {
                this.lineaInvestigacionDocentesRemovidos = new ArrayList<>();
                sessionDocenteCarrera.setDocenteCarrera(docenteCarrera);
                sessionDocenteCarrera.setPersona(personaFacadeLocal.find(docenteCarrera.getDocenteId().getId()));
                sessionDocenteCarrera.setDirector(directorFacadeLocal.find(sessionDocenteCarrera.getDocenteCarrera().getId()));
                listadoLineasInvestigacion(docenteCarrera.getDocenteId());
                estadoLaboral = docenteCarrera.getDocenteId().getEstadoLaboralId().toString();
                titulo = docenteCarrera.getDocenteId().getTituloDocenteId().toString();
                tipoDocumento = itemFacadeLocal.find(sessionDocenteCarrera.getPersona().getTipoDocumentoIdentificacionId()).toString();
                genero = itemFacadeLocal.find(sessionDocenteCarrera.getPersona().getGeneroId()).toString();
                navegacion = "pretty:editarDocenteCarrera";
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

    public void buscar(String criterio) {
        this.docenteCarreras = new ArrayList<>();
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "buscar_docente_carrera");
            if (tienePermiso == 1) {
                for (DocenteCarrera docenteCarrera : docenteCarreraFacadeLocal.buscarPorCarrera(sessionUsuarioCarrera.getUsuarioCarrera().getCarreraId())) {
                    Persona persona = personaFacadeLocal.find(docenteCarrera.getDocenteId().getId());
                    if (persona.getApellidos().toLowerCase().contains(criterio.toLowerCase())
                            || persona.getNombres().toLowerCase().contains(criterio.toLowerCase())
                            || persona.getNumeroIdentificacion().contains(criterio)) {
                        this.docenteCarreras.add(docenteCarrera);
                    }
                }
            } else {
                if (tienePermiso == 2) {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_buscar") + ". " + bundle.getString("lbl.msm_consulte"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
            }
        } catch (Exception e) {
        }
    }

    public String grabar() {
        String navegacion = "";
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
        try {
            int posTitulo = titulo.indexOf(":");
            int posEstadoLaboral = estadoLaboral.indexOf(":");
            EstadoLaboral el = estadoLaboralFacadeLocal.find(Long.parseLong(estadoLaboral.substring(0, posEstadoLaboral)));
            Item g = itemFacadeLocal.buscarPorCodigo(CatalogoEnum.GENERO.getTipo(), genero);
            Item tdi = itemFacadeLocal.buscarPorCodigo(CatalogoEnum.TIPODOCUMENTOIDENTIFICACION.getTipo(), tipoDocumento);
            TituloDocente td = tituloDocenteFacadeLocal.find(Long.parseLong(titulo.substring(0, posTitulo)));

            if (g != null) {
                sessionDocenteCarrera.getPersona().setGeneroId(g.getId());
            }
            if (tdi != null) {
                sessionDocenteCarrera.getPersona().setTipoDocumentoIdentificacionId(tdi.getId());
            }
            if (el != null) {
                sessionDocenteCarrera.getDocenteCarrera().getDocenteId().setEstadoLaboralId(el);
            }
            if (td != null) {
                sessionDocenteCarrera.getDocenteCarrera().getDocenteId().setTituloDocenteId(td);
            }
            if (sessionDocenteCarrera.getDocenteCarrera().getId() == null) {
                int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "crear_docente_carrera");
                if (tienePermiso == 1) {
                    if (personaFacadeLocal.esUnico(sessionDocenteCarrera.getPersona().getNumeroIdentificacion(), sessionDocenteCarrera.getPersona().getId())) {
//                        docenteCarrera.getDocenteId().getPersona().setDocente((Docente) null);
                        personaFacadeLocal.create(sessionDocenteCarrera.getPersona());
//                        docenteCarrera.getDocenteId().getPersona().setDocente(docenteCarrera.getDocenteId());
                        sessionDocenteCarrera.getDocenteCarrera().getDocenteId().setId(sessionDocenteCarrera.getPersona().getId());
                        docenteFacadeLocal.create(sessionDocenteCarrera.getDocenteCarrera().getDocenteId());
                        logFacadeLocal.create(logFacadeLocal.crearLog("Docente", sessionDocenteCarrera.getDocenteCarrera().getDocenteId().getId() + "", "CREAR", "|Nombres= "
                                + sessionDocenteCarrera.getPersona().getNombres() + "|Apellidos= " + sessionDocenteCarrera.getPersona().getApellidos()
                                + "|Cédula= " + sessionDocenteCarrera.getPersona().getNumeroIdentificacion() + "|Email= " + sessionDocenteCarrera.getPersona().getEmail(), sessionUsuario.getUsuario()));
                        sessionDocenteCarrera.getDirector().setEsActivo(true);
                        sessionDocenteCarrera.getDocenteCarrera().setEsActivo(true);
                        docenteCarreraFacadeLocal.create(sessionDocenteCarrera.getDocenteCarrera());
//                        director.setDocenteCarrera(docenteCarrera);
                        sessionDocenteCarrera.getDirector().setId(sessionDocenteCarrera.getDocenteCarrera().getId());
                        directorFacadeLocal.create(sessionDocenteCarrera.getDirector());
//                        docenteCarrera.setDirector(director);
                        administrarUsuarios.grabarUsuarioDocente(sessionDocenteCarrera.getDocenteCarrera().getDocenteId());
                        logFacadeLocal.create(logFacadeLocal.crearLog("DocenteCarrera", sessionDocenteCarrera.getDocenteCarrera().getId() + "",
                                "CREAR", "|Carrera=" + sessionDocenteCarrera.getDocenteCarrera().getCarreraId() + "|Docente="
                                + sessionDocenteCarrera.getDocenteCarrera().getDocenteId() + "|EsActivo= " + sessionDocenteCarrera.getDocenteCarrera().isEsActivo(), sessionUsuario.getUsuario()));
                        grabarLineasInvestigacionDocentes(sessionDocenteCarrera.getDocenteCarrera().getDocenteId());
                        if (param.equalsIgnoreCase("grabar")) {
                            navegacion = "pretty:docentesCarrera";
                            sessionDocenteCarrera.setDocenteCarrera(new DocenteCarrera());
                        } else {
                            if (param.equalsIgnoreCase("grabar-editar")) {
                                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.docente") + " " + bundle.getString("lbl.msm_grabar"), "");
                                FacesContext.getCurrentInstance().addMessage(null, message);
                            }
                        }
                    } else {
                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.docente") + " " + bundle.getString("lbl.msm_existe"), "");
                        FacesContext.getCurrentInstance().addMessage(null, message);
                    }
                    buscar("");
                } else {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear") + ". " + bundle.getString("lbl.msm_consulte"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
            } else {
                int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "editar_docente_carrera");
                if (tienePermiso == 1) {
                    if (personaFacadeLocal.esUnico(sessionDocenteCarrera.getPersona().getNumeroIdentificacion(), sessionDocenteCarrera.getPersona().getId())) {
//                        docenteCarrera.getDocenteId().getPersona().setDocente((Docente) null);
                        personaFacadeLocal.edit(sessionDocenteCarrera.getPersona());
//                      sessionDocenteCarrera.getPersona().setDocente(sessionDocenteCarrera.getDocenteCarrera().getId());
                        sessionDocenteCarrera.getDocenteCarrera().getDocenteId().setId(sessionDocenteCarrera.getPersona().getId());
                        docenteFacadeLocal.edit(sessionDocenteCarrera.getDocenteCarrera().getDocenteId());
                        logFacadeLocal.create(logFacadeLocal.crearLog("Docente", sessionDocenteCarrera.getDocenteCarrera().getDocenteId().getId() + "",
                                "EDITAR", "|Nombres= " + sessionDocenteCarrera.getPersona().getNombres() + "|Apellidos= " + sessionDocenteCarrera.getPersona().getApellidos()
                                + "|Cédula= " + sessionDocenteCarrera.getPersona().getNumeroIdentificacion() + "|Email= " + sessionDocenteCarrera.getPersona().getEmail(), sessionUsuario.getUsuario()));

                        sessionDocenteCarrera.getDirector().setEsActivo(true);
                        docenteCarreraFacadeLocal.edit(sessionDocenteCarrera.getDocenteCarrera());
                        logFacadeLocal.create(logFacadeLocal.crearLog("DocenteCarrera", sessionDocenteCarrera.getDocenteCarrera().getId() + "", "EDITAR",
                                "|Carrera=" + sessionDocenteCarrera.getDocenteCarrera().getCarreraId() + "|Docente=" + sessionDocenteCarrera.getDocenteCarrera().getDocenteId()
                                + "|EsActivo= " + sessionDocenteCarrera.getDocenteCarrera().isEsActivo(), sessionUsuario.getUsuario()));
                        grabarLineasInvestigacionDocentes(sessionDocenteCarrera.getDocenteCarrera().getDocenteId());
                        removerDocenteLineasInvestigacion(sessionDocenteCarrera.getDocenteCarrera().getDocenteId());
                        if (param.equalsIgnoreCase("grabar")) {
                            navegacion = "pretty:docentesCarrera";
                            sessionDocenteCarrera.setDocenteCarrera(new DocenteCarrera());
                        } else {
                            if (param.equalsIgnoreCase("grabar-editar")) {
                                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.docente") + " " + bundle.getString("lbl.msm_editar"), "");
                                FacesContext.getCurrentInstance().addMessage(null, message);
                            }
                        }
                    } else {
                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.docente") + " " + bundle.getString("lbl.msm_existe"), "");
                        FacesContext.getCurrentInstance().addMessage(null, message);
                    }
                    buscar("");
                } else {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_editar") + ". " + bundle.getString("lbl.msm_consulte"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
            }
        } catch (Exception e) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
        return navegacion;
    }

    public void listadoLineasInvestigacion(Docente docente) {
        List<LineaInvestigacion> lineaInvestigacionDocentes = new ArrayList<>();
        List<LineaInvestigacion> lineaInvestigaciones = new ArrayList<>();
        try {
            if (sessionUsuarioCarrera.getUsuarioCarrera().getId() != null) {
                if (docente.getId() != null) {
                    for (LineaInvestigacionDocente lid : lineaInvestigacionDocenteFacadeLocal.buscarPorDocenteId(docente.getId())) {
                        lineaInvestigacionDocentes.add(lid.getLineaInvestigacionId());
                    }
                    List<LineaInvestigacionCarrera> lics = new ArrayList<>();
                    lics = lineaInvestigacionCarreraFacadeLocal.buscarPorCarrera(sessionUsuarioCarrera.getUsuarioCarrera().getCarreraId());
                    for (LineaInvestigacionCarrera lic : lics) {
                        if (!lineaInvestigacionDocentes.contains(lic.getLineaInvestigacionId())) {
                            lineaInvestigaciones.add(lic.getLineaInvestigacionId());
                        }
                    }

                } else {
                    List<LineaInvestigacionCarrera> lics = new ArrayList<>();
                    lics = lineaInvestigacionCarreraFacadeLocal.buscarPorCarrera(sessionUsuarioCarrera.getUsuarioCarrera().getCarreraId());
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

    public Long devuelveLineaInvestigacionEliminar(List<LineaInvestigacionDocente> docenteLineasInv, LineaInvestigacionDocente ld) {
        Long var = (long) 0;
        for (LineaInvestigacionDocente lineaInvestigacionDocente : docenteLineasInv) {
            if (lineaInvestigacionDocente.getLineaInvestigacionId().equals(ld.getLineaInvestigacionId())) {
                var = lineaInvestigacionDocente.getId();
            }
        }
        return var;
    }

    public Persona getPersona(DocenteCarrera docenteCarrera) {
        try {
            return personaFacadeLocal.find(docenteCarrera.getDocenteId().getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
                    lineaInvestigacionDocentesRemovidos.add(lid);
                } else {
                    lineaInvestigacionDocentesRemovidos.remove(lid);
                }
            }
        } catch (Exception e) {
        }
    }

    public void removerDocenteLineasInvestigacion(Docente docente) {
        try {
            if (docente.getId() != null) {
                for (LineaInvestigacionDocente ld : lineaInvestigacionDocentesRemovidos) {
                    Long id = devuelveLineaInvestigacionEliminar(lineaInvestigacionDocenteFacadeLocal.buscarPorDocenteId(docente.getId()), ld);
                    LineaInvestigacionDocente lid = null;
                    lid = lineaInvestigacionDocenteFacadeLocal.find(id);
                    if (lid != null) {
                        logFacadeLocal.create(logFacadeLocal.crearLog("LineaInvestigacionDocente", ld.getId() + "", "DELETE", "LineaInvestigacion=" + ld.getLineaInvestigacionId() + "|Docente=" + ld.getDocenteId(), sessionUsuario.getUsuario()));
                        lineaInvestigacionDocenteFacadeLocal.remove(lid);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public void grabarLineasInvestigacionDocentes(Docente docente) {
        List<LineaInvestigacionDocente> lids = new ArrayList<>();
        for (Object o : lineasInvestigacionDualList.getTarget()) {
            int v = o.toString().indexOf(":");
            Long id = Long.parseLong(o.toString().substring(0, v));
            LineaInvestigacion li = lineaInvestigacionFacadeLocal.find(id);
            LineaInvestigacionDocente ld = new LineaInvestigacionDocente();
            ld.setDocenteId(docente.getId());
            ld.setLineaInvestigacionId(li);
            lids.add(ld);
        }
        for (LineaInvestigacionDocente lineaInvestigacionDocente : lids) {
            if (contieneLineaInvestigacion(lineaInvestigacionDocenteFacadeLocal.buscarPorDocenteId(docente.getId()), lineaInvestigacionDocente) == false) {
                lineaInvestigacionDocenteFacadeLocal.create(lineaInvestigacionDocente);
                logFacadeLocal.create(logFacadeLocal.crearLog("LineaInvestigacionDocente", lineaInvestigacionDocente.getId() + "", "CREAR", "|LineaInvestigacion= " + lineaInvestigacionDocente.getLineaInvestigacionId() + "|Docente=" + lineaInvestigacionDocente.getDocenteId(), sessionUsuario.getUsuario()));
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

    public void crearPDF(Carrera carrera) throws IOException, DocumentException {
        Document pdf = new Document();
        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        String logo = servletContext.getRealPath("") + File.separator + "resources/img" + File.separator + "selloInstitucion.png";
        Image image = Image.getInstance(logo);
        image.scaleToFit(50, 50);
        image.setAbsolutePosition(50f, 775f);
        Image carreraLogo = Image.getInstance(carrera.getLogo());
        carreraLogo.scaleToFit(50, 50);
        carreraLogo.setAbsolutePosition(450f, 775f);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter.getInstance(pdf, baos);
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
        response.setContentType("application/pdf");
        response.addHeader("Content-disposition", "attachment; filename=Docentes.pdf");
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        pdf.setPageSize(PageSize.A4.rotate());
        pdf.setMargins(20f, 20f, 20f, 20f);
        pdf.setPageSize(PageSize.A4);
        Font fontHeader = new Font(Font.TIMES_ROMAN, 16, Font.BOLD);
        Font fontTitle = FontFactory.getFont("Times-Roman", 18, Font.BOLD);
        Paragraph title = new Paragraph(bundle.getString("lbl.listado") + " " + bundle.getString("lbl.de") + " " + bundle.getString("lbl.docentes"), fontTitle);
        Paragraph titleCarrera = new Paragraph(bundle.getString("lbl.carrera") + " " + bundle.getString("lbl.de") + " " + carrera.getNombre(), fontTitle);
        Paragraph titleArea = new Paragraph(carrera.getAreaId().getNombre(), fontTitle);
        title.setSpacingAfter(20);
        title.setAlignment(1);
        titleArea.setSpacingAfter(20);
        titleArea.setAlignment(1);
        titleCarrera.setSpacingAfter(20);
        titleCarrera.setAlignment(1);
        pdf.open();
        pdf.add(image);
        pdf.add(carreraLogo);
        pdf.add(title);
        pdf.add(titleArea);
        pdf.add(titleCarrera);
        PdfPTable pdfTable = new PdfPTable(5);
        pdfTable.setWidthPercentage(100f);
        pdfTable.setHorizontalAlignment(0);
        PdfPCell cell1 = new PdfPCell(new Phrase(bundle.getString("lbl.nombres"), fontHeader));
        cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
        pdfTable.addCell(cell1);
        cell1 = new PdfPCell(new Phrase(bundle.getString("lbl.apellidos"), fontHeader));
        cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
        pdfTable.addCell(cell1);

        cell1 = new PdfPCell(new Phrase(bundle.getString("lbl.ci"), fontHeader));
        cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
        pdfTable.addCell(cell1);
        cell1 = new PdfPCell(new Phrase(bundle.getString("lbl.titulo"), fontHeader));
        cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
        pdfTable.addCell(cell1);
        cell1 = new PdfPCell(new Phrase(bundle.getString("lbl.email"), fontHeader));
        cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
        pdfTable.addCell(cell1);
        pdfTable.setHeaderRows(1);
        for (DocenteCarrera docenteCarrera : docenteCarreras) {
            Persona personaDocente = personaFacadeLocal.find(docenteCarrera.getDocenteId().getId());
            pdfTable.addCell(personaDocente.getNombres());
            pdfTable.addCell(personaDocente.getApellidos());
            pdfTable.addCell(personaDocente.getNumeroIdentificacion());
            pdfTable.addCell(docenteCarrera.getDocenteId().getTituloDocenteId().getTituloId().getNombre());
            pdfTable.addCell(personaDocente.getEmail());
        }
        pdf.add(pdfTable);
        pdf.close();
        // the contentlength
        response.setContentLength(baos.size());
        // write ByteArrayOutputStream to the ServletOutputStream
        OutputStream os = response.getOutputStream();
        baos.writeTo(os);
        os.flush();
        os.close();
    }

//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="MÉTODOS RENDERED">
    public boolean renderedEditar() {
        boolean var = false;
        int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "editar_docente_carrera");
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
        int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "eliminar_docente_carrera");
        if (tienePermiso == 1) {
            var = true;
        }
        return var;
    }

    public boolean renderedCrear() {
        boolean var = false;
        int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "crear_docente_carrera");
        if (tienePermiso == 1) {
            var = true;
        }
        return var;
    }
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="WEB SERVICES">

    public void sgaWebServicesUnidadesDocenteParalelo(String paraleloId, Carrera carrera) {
//        FacesContext facesContext = FacesContext.getCurrentInstance();
//        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
//        ConexionServicio conexionServicio = new ConexionServicio();
//        String serviceUrl = configuracionGeneralFacadeLocal.find((int) 43).getValor();
//        String passwordService = configuracionGeneralFacadeLocal.find((int) 5).getValor();
//        String userService = configuracionGeneralFacadeLocal.find((int) 6).getValor();
//        try {
//            String s = serviceUrl + "?id_paralelo=" + paraleloId;
//            String datosJson = conexionServicio.conectar(s, userService, passwordService);
//            if (!datosJson.equalsIgnoreCase("")) {
//                JsonParser parser = new JsonParser();
//                JsonElement datos = parser.parse(datosJson);
//                i = 0;
//                recorrerElementosJsonUnidadesDocenteParalelo(datos, carrera);
//            } else {
//                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.no_sincronizar_web_services"), "");
//                FacesContext.getCurrentInstance().addMessage(null, message);
//            }
//
//        } catch (Exception e) {
//            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.permiso_denegado_sincronizar"), "");
//            FacesContext.getCurrentInstance().addMessage(null, message);
//        }
    }

    private void recorrerElementosJsonUnidadesDocenteParalelo(JsonElement elemento, Carrera carrera) throws Exception {
        try {
            if (elemento.isJsonObject()) {
                JsonObject obj = elemento.getAsJsonObject();
                java.util.Set<java.util.Map.Entry<String, JsonElement>> entradas = obj.entrySet();
                java.util.Iterator<java.util.Map.Entry<String, JsonElement>> iter = entradas.iterator();
                while (iter.hasNext()) {
                    java.util.Map.Entry<String, JsonElement> entrada = iter.next();
                    keyUnidadesDocenteParalelo = entrada.getKey();
                    try {
                        String e = new String(entrada.getValue().getAsString().getBytes(), "UTF-8");
                        JsonParser jp = new JsonParser();
                        JsonElement jsonElement = jp.parse(e);
                        recorrerElementosJsonUnidadesDocenteParalelo(jsonElement, carrera);

                    } catch (Exception e) {
                        recorrerElementosJsonUnidadesDocenteParalelo(entrada.getValue(), carrera);
                    }
                }

            } else if (elemento.isJsonArray()) {
                JsonArray array = elemento.getAsJsonArray();
                keyEnteroUnidadesDocenteParelelo = 0;
                java.util.Iterator<JsonElement> iter = array.iterator();
                while (iter.hasNext()) {
                    JsonElement entrada = iter.next();
                    recorrerElementosJsonUnidadesDocenteParalelo(entrada, carrera);
                }
            } else if (elemento.isJsonPrimitive()) {
                JsonPrimitive valor = elemento.getAsJsonPrimitive();
                if (i > 5) {
                    if (keyEnteroUnidadesDocenteParelelo == 7) {
                        if (valor.isString()) {
                            Persona persona = personaFacadeLocal.buscarPorNumeroIdentificacion(valor.getAsString());
                            Docente docente = null;
                            if (persona != null) {
                                docente = docenteFacadeLocal.find(persona.getId());
                            } else {
                                persona = new Persona();
                                docente = new Docente();
                                persona.setNumeroIdentificacion(valor.getAsString());
                            }
                            grabarDesdeWebServices(docente, persona, carrera);
                        }
                    }
                    keyEnteroUnidadesDocenteParelelo++;
                }
                i++;
            }

        } catch (Exception e) {

        }
    }

    private void grabarDesdeWebServices(Docente docente, Persona persona, Carrera c) {
////        FacesContext facesContext = FacesContext.getCurrentInstance();
////        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
////        if (usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "sga_ws_datos_docente") == 1) {
////            try {
////                ConexionServicio conexionServicio = new ConexionServicio();
////                String serviceUrl = configuracionGeneralFacadeLocal.find((int) 26).getValor();
////                String passwordService = configuracionGeneralFacadeLocal.find((int) 5).getValor();
////                String userService = configuracionGeneralFacadeLocal.find((int) 6).getValor();
////                String s = serviceUrl + "?cedula=" + persona.getNumeroIdentificacion();
////                String datosJson = conexionServicio.conectar(s, userService, passwordService);
////                persona.setEmail("ejemplo@gmail.com");
////                if (!datosJson.equalsIgnoreCase("")) {
////                    JsonParser parser = new JsonParser();
////                    JsonElement datos = parser.parse(datosJson);
////                    recorrerElementosJson(datos, docente, persona);
////                    if (docente.getId() == null) {
////                        Calendar fechaActual = Calendar.getInstance();
////                        TipoDocumentoIdentificacion tp = tipoDocumentoIdentificacionFacadeLocal.find(1);
////                        Genero g = generoFacadeLocal.find(1);
////                        Nacionalidad nacionalidad = nacionalidadFacadeLocal.find(1);
////                        persona.setTipoDocumentoIdentificacionId(tp);
////                        persona.setGeneroId(g);
////                        persona.setFechaNacimiento(fechaActual.getTime());
////                        persona.setNacionalidadId(nacionalidad);
////                        personaFacadeLocal.create(persona);
////                        docente.setId(persona.getId());
////                        docenteFacadeLocal.create(docente);
////                        administrarUsuarios.grabarUsuarioDocente(docente);
////                        DocenteCarrera docenteCarrera = new DocenteCarrera();
////                        Director director = new Director();
////                        docenteCarrera.setCarreraId(c);
////                        director.setEsActivo(true);
////                        docenteCarrera.setEsActivo(true);
////                        docenteCarrera.setDocenteId(docente);
////                        docenteCarreraFacadeLocal.create(docenteCarrera);
////                        director.setId(docenteCarrera.getId());
////                        directorFacadeLocal.create(director);
////                    } else {
////                        Persona datosDocente = personaFacadeLocal.find(docente.getId());
////                        personaFacadeLocal.edit(datosDocente);
////                        docenteFacadeLocal.edit(docente);
////                    }
////                } else {
////                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.no_sincronizar_web_services"), "");
////                    FacesContext.getCurrentInstance().addMessage(null, message);
////                }
////            } catch (Exception e) {
////                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.no_sincronizar_web_services"), "");
////                FacesContext.getCurrentInstance().addMessage(null, message);
////            }
//
//        } else {
//            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.permiso_denegado_sincronizar") + ". " + bundle.getString("lbl.msm_consulte"), "");
//            FacesContext.getCurrentInstance().addMessage(null, message);
//        }
    }

    public void sgaWebServicesDatosDocente() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        if (usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "sga_ws_datos_docente") == 1) {
            try {
                Map parametros = new HashMap();
                parametros.put("persona", sessionDocenteCarrera.getPersona());
                parametros.put("docente", sessionDocenteCarrera.getDocenteCarrera().getDocenteId());
                String serviceUrl = configuracionGeneralFacadeLocal.find((int) 26).getValor();
                String passwordService = configuracionGeneralFacadeLocal.find((int) 5).getValor();
                String userService = configuracionGeneralFacadeLocal.find((int) 6).getValor();
                String s = serviceUrl + "?cedula=" + sessionDocenteCarrera.getPersona().getNumeroIdentificacion();
                parametros.put("url", s);
                parametros.put("clave", passwordService);
                parametros.put("usuario", userService);
                parametros.put("msm_sincronizado", bundle.getString("lbl.sincronizado"));
                parametros.put("msm_no_sincronizado", bundle.getString("lbl.no_sincronizar_web_services"));
//                Map resultado = docenteConsumeService.getDatosDocente(parametros);
//                ConexionServicio conexionServicio = new ConexionServicio();
//                String serviceUrl = configuracionGeneralFacadeLocal.find((int) 26).getValor();
//                String passwordService = configuracionGeneralFacadeLocal.find((int) 5).getValor();
//                String userService = configuracionGeneralFacadeLocal.find((int) 6).getValor();
//                String s = serviceUrl + "?cedula=" + sessionDocenteCarrera.getPersona().getNumeroIdentificacion();
//                String datosJson = conexionServicio.conectar(s, userService, passwordService);
//                sessionDocenteCarrera.getPersona().setEmail("ejemplo@gmail.com");
//                sessionDocenteCarrera.getDocenteCarrera().setDocenteId((Docente) resultado.get("docente"));
//                sessionDocenteCarrera.setPersona((Persona) resultado.get("persona"));
                tituloDocente = sessionDocenteCarrera.getDocenteCarrera().getDocenteId().getTituloDocenteId().getTituloId().toString();
                estadoLaboral = sessionDocenteCarrera.getDocenteCarrera().getDocenteId().getEstadoLaboralId().toString();
//                if (!datosJson.equalsIgnoreCase("")) {
//                    JsonParser parser = new JsonParser();
//                    JsonElement datos = parser.parse(datosJson);
//                    recorrerElementosJson(datos, sessionDocenteCarrera.getDocenteCarrera().getDocenteId(), sessionDocenteCarrera.getPersona());
//                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.sincronizado"), "");
//                    FacesContext.getCurrentInstance().addMessage(null, message);
//                } else {
//                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.no_sincronizar_web_services"), "");
//                    FacesContext.getCurrentInstance().addMessage(null, message);
//                }
            } catch (Exception e) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.no_sincronizar_web_services"), "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }

        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.permiso_denegado_sincronizar") + ". " + bundle.getString("lbl.msm_consulte"), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="SET Y GET">
    public SessionDocenteCarrera getSessionDocenteCarrera() {
        return sessionDocenteCarrera;
    }

    public void setSessionDocenteCarrera(SessionDocenteCarrera sessionDocenteCarrera) {
        this.sessionDocenteCarrera = sessionDocenteCarrera;
    }

    public SessionUsuarioCarrera getSessionUsuarioCarrera() {
        return sessionUsuarioCarrera;
    }

    public void setSessionUsuarioCarrera(SessionUsuarioCarrera sessionUsuarioCarrera) {
        this.sessionUsuarioCarrera = sessionUsuarioCarrera;
    }

    public List<DocenteCarrera> getDocenteCarreras() {
        return docenteCarreras;
    }

    public void setDocenteCarreras(List<DocenteCarrera> docenteCarreras) {
        this.docenteCarreras = docenteCarreras;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getEstadoLaboral() {
        return estadoLaboral;
    }

    public void setEstadoLaboral(String estadoLaboral) {
        this.estadoLaboral = estadoLaboral;
    }

    public boolean isRenderedNoEditar() {
        return renderedNoEditar;
    }

    public void setRenderedNoEditar(boolean renderedNoEditar) {
        this.renderedNoEditar = renderedNoEditar;
    }

    public List<LineaInvestigacionDocente> getLineaInvestigacionDocentesRemovidos() {
        return lineaInvestigacionDocentesRemovidos;
    }

    public void setLineaInvestigacionDocentesRemovidos(List<LineaInvestigacionDocente> lineaInvestigacionDocentesRemovidos) {
        this.lineaInvestigacionDocentesRemovidos = lineaInvestigacionDocentesRemovidos;
    }

    public DualListModel<LineaInvestigacion> getLineasInvestigacionDualList() {
        return lineasInvestigacionDualList;
    }

    public void setLineasInvestigacionDualList(DualListModel<LineaInvestigacion> lineasInvestigacionDualList) {
        this.lineasInvestigacionDualList = lineasInvestigacionDualList;
    }

    public String getCriterio() {
        return criterio;
    }

    public void setCriterio(String criterio) {
        this.criterio = criterio;
    }

    public SessionUsuario getSessionUsuario() {
        return sessionUsuario;
    }

    public void setSessionUsuario(SessionUsuario sessionUsuario) {
        this.sessionUsuario = sessionUsuario;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
//</editor-fold>
}
