/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.academico.controlador;

import org.jlmallas.api.date.DateResource;
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
import edu.unl.sigett.academico.managed.session.SessionEstudianteCarrera;
import edu.unl.sigett.seguridad.controlador.AdministrarUsuarios;
import edu.unl.sigett.seguridad.managed.session.SessionUsuario;
import edu.unl.sigett.seguridad.managed.session.SessionUsuarioCarrera;
import edu.unl.sigett.entity.Aspirante;
import edu.jlmallas.academico.entity.Carrera;
import edu.jlmallas.academico.entity.EstadoEstudianteCarrera;
import edu.jlmallas.academico.entity.EstadoMatricula;
import edu.jlmallas.academico.entity.Estudiante;
import edu.jlmallas.academico.entity.EstudianteCarrera;
import edu.jlmallas.academico.entity.OfertaAcademica;
import com.jlmallas.comun.entity.Persona;
import com.jlmallas.comun.enumeration.CatalogoEnum;
import com.jlmallas.comun.dao.ItemDao;
import edu.jlmallas.academico.entity.ReporteMatricula;
import org.jlmallas.seguridad.entity.Usuario;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
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
import edu.unl.sigett.dao.AspiranteFacadeLocal;
import edu.unl.sigett.dao.ConfiguracionCarreraDao;
import edu.unl.sigett.dao.ConfiguracionGeneralDao;
import edu.jlmallas.academico.service.EstadoEstudianteCarreraFacadeLocal;
import edu.jlmallas.academico.service.EstadoMatriculaFacadeLocal;
import edu.jlmallas.academico.service.EstudianteCarreraFacadeLocal;
import edu.jlmallas.academico.service.EstudianteFacadeLocal;
import org.jlmallas.seguridad.dao.LogDao;
import edu.jlmallas.academico.service.OfertaAcademicaService;
import com.jlmallas.comun.dao.PersonaDao;
import edu.jlmallas.academico.service.ReporteMatriculaFacadeLocal;
import org.jlmallas.seguridad.dao.UsuarioDao;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
@URLMappings(mappings = {
    @URLMapping(
            id = "editarEstudianteCarrera",
            pattern = "/editarEstudianteCarrera/#{sessionEstudianteCarrera.estudianteCarrera.id}",
            viewId = "/faces/pages/academico/editarEstudianteCarrera.xhtml"
    ),
    @URLMapping(
            id = "crearEstudianteCarrera",
            pattern = "/crearEstudianteCarrera/",
            viewId = "/faces/pages/academico/editarEstudianteCarrera.xhtml"
    ),
    @URLMapping(
            id = "estudiantesCarrera",
            pattern = "/estudiantesCarrera/",
            viewId = "/faces/pages/academico/buscarEstudiantesCarrera.xhtml"
    )})
public class AdministrarEstudiantesCarrera implements Serializable {

    @Inject
    private SessionUsuario sessionUsuario;
    @Inject
    private AdministrarEstudiantes administrarEstudiantes;
    @Inject
    private SessionEstudianteCarrera sessionEstudianteCarrera;
    @Inject
    private SessionUsuarioCarrera sessionUsuarioCarrera;
    @Inject
    private AdministrarUsuarios administrarUsuarios;

    @EJB
    private PersonaDao personaFacadeLocal;
    @EJB
    private EstudianteFacadeLocal estudianteFacadeLocal;
    @EJB
    private LogDao logFacadeLocal;
    @EJB
    private EstudianteCarreraFacadeLocal estudianteCarreraFacadeLocal;
    @EJB
    private UsuarioDao usuarioFacadeLocal;
    @EJB
    private ItemDao itemFacadeLocal;
    @EJB
    private EstadoEstudianteCarreraFacadeLocal estadoEstudianteCarreraFacadeLocal;
    @EJB
    private AspiranteFacadeLocal aspiranteFacadeLocal;
    @EJB
    private ConfiguracionCarreraDao configuracionCarreraFacadeLocal;
    @EJB
    private ConfiguracionGeneralDao configuracionGeneralFacadeLocal;
    @EJB
    private ReporteMatriculaFacadeLocal reporteMatriculaFacadeLocal;
    @EJB
    private OfertaAcademicaService ofertaAcademicaFacadeLocal;
    @EJB
    private EstadoMatriculaFacadeLocal estadoMatriculaFacadeLocal;

    private List<EstudianteCarrera> estudianteCarreras;
    private List<ReporteMatricula> reporteMatriculas;

    private boolean renderedNoEditar;
    private boolean esNuevoReporte;

    private String criterio;
    private String key;
    private int keyEntero;
    private String keyEstadoEstudiantesParalelo;
    private String keyRm;
    private int keyEnteroRm;
    private int i = 0;
    private int keyEnteroEstadoEstudiantesParelelo;
    private ReporteMatricula reporteMatricula = null;
    private String tipoDocumento;
    private String genero;

    public AdministrarEstudiantesCarrera() {
    }

    //<editor-fold defaultstate="collapsed" desc="MÉTODOS CRUD">
    public String viewEstudiantesCarrera() {
        this.estudianteCarreras = new ArrayList<>();
        return "pretty:estudiantesCarrera";
    }

    public String crear(Usuario usuario) {
        String navegacion = "";
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "crear_estudiante_carrera");
            if (tienePermiso == 1) {
                sessionEstudianteCarrera.setEstudianteCarrera(new EstudianteCarrera());
                sessionEstudianteCarrera.setPersona(new Persona());
                sessionEstudianteCarrera.setAspirante(new Aspirante());
                sessionEstudianteCarrera.getEstudianteCarrera().setEstudianteId(new Estudiante());
//                sessionEstudianteCarrera.getEstudianteCarrera().setCarreraId(sessionUsuarioCarrera.getCarrera());
                administrarEstudiantes.renderedFotos(new Estudiante());
                administrarEstudiantes.renderedInformacionEstudio(new Estudiante());
                habilitaCampoEsAptoAspirante(usuario);
                navegacion = "pretty:crearEstudianteCarrera";
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

    public String editar(EstudianteCarrera estudianteCarrera, Usuario usuario) {
        String navegacion = "";
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "editar_estudiante_carrera");
            if (tienePermiso == 1) {
                sessionEstudianteCarrera.setEstudianteCarrera(estudianteCarrera);
                sessionEstudianteCarrera.setPersona(personaFacadeLocal.find(estudianteCarrera.getEstudianteId().getId()));
                sessionEstudianteCarrera.setAspirante(aspiranteFacadeLocal.find(estudianteCarrera.getId()));
                habilitaCampoEsAptoAspirante(usuario);
                administrarEstudiantes.renderedFotos(sessionEstudianteCarrera.getEstudianteCarrera().getEstudianteId());
                administrarEstudiantes.renderedInformacionEstudio(sessionEstudianteCarrera.getEstudianteCarrera().getEstudianteId());
                tipoDocumento = itemFacadeLocal.find(sessionEstudianteCarrera.getPersona().getTipoDocumentoIdentificacionId()).toString();
                genero = itemFacadeLocal.find(sessionEstudianteCarrera.getPersona().getGeneroId()).toString();
                navegacion = "pretty:editarEstudianteCarrera";
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
        this.estudianteCarreras = new ArrayList<>();
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "buscar_estudiante_carrera");
            if (tienePermiso == 1) {
//                for (EstudianteCarrera estudianteCarrera : estudianteCarreraFacadeLocal.buscarPorCarrera(sessionUsuarioCarrera.getCarrera().getId())) {
//                    Persona persona = personaFacadeLocal.find(estudianteCarrera.getEstudianteId().getId());
//                    if (persona.getApellidos().toLowerCase().contains(criterio.toLowerCase()) || persona.getNombres().toLowerCase().contains(criterio.toLowerCase())
//                            || persona.getNumeroIdentificacion().contains(criterio)) {
//                        if (!estudianteCarreras.contains(estudianteCarrera)) {
//                            this.estudianteCarreras.add(estudianteCarrera);
//                        }
//                    }
//                }
            } else {
                if (tienePermiso == 2) {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_buscar") + ". " + bundle.getString("lbl.msm_consulte"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
            }
        } catch (Exception e) {
        }
    }

    public void selectEstadoEstudianteCarrera(EstudianteCarrera estudianteCarrera) {
        if (estudianteCarrera.getEstadoId() == null) {
            EstadoEstudianteCarrera estadoEstudianteCarrera = estadoEstudianteCarreraFacadeLocal.find((int) 1);
            if (estadoEstudianteCarrera != null) {
                estudianteCarrera.setEstadoId(estadoEstudianteCarrera);
            } else {
                estadoEstudianteCarrera = new EstadoEstudianteCarrera();
                estadoEstudianteCarrera.setNombre("ESTUDIANTE");
                estadoEstudianteCarrera.setCodigo("ESTUDIANTE");
                estadoEstudianteCarreraFacadeLocal.create(estadoEstudianteCarrera);
                estudianteCarrera.setEstadoId(estadoEstudianteCarrera);
            }
        }
    }

    public void activarDesactivar(EstudianteCarrera estudianteCarrera) {
        try {
            int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "eliminar_estudiante_carrera");
            if (tienePermiso == 1) {
                estudianteCarreraFacadeLocal.edit(estudianteCarrera);
                logFacadeLocal.create(logFacadeLocal.crearLog("EstudianteCarrera", estudianteCarrera.getId() + "", "EDITAR", "|Carrera=" + estudianteCarrera.getCarreraId() + "|Estudiante=" + estudianteCarrera.getEstudianteId() + "|EsActivo= " + sessionEstudianteCarrera.getEstudianteCarrera().getEsActivo(), sessionUsuario.getUsuario()));
            }
        } catch (Exception e) {
        }
    }

    public String grabar(EstudianteCarrera estudianteCarrera) {
        String navegacion = "";
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
            int posTipoDoc = tipoDocumento.indexOf(":");
            int posGenero = genero.indexOf(":");
            Item g = itemFacadeLocal.buscarPorCodigo(CatalogoEnum.GENERO.getTipo(), genero);
            Item tdi = itemFacadeLocal.buscarPorCodigo(CatalogoEnum.TIPODOCUMENTOIDENTIFICACION.getTipo(), tipoDocumento);
            if (g != null) {
                sessionEstudianteCarrera.getPersona().setGeneroId(g.getId());
            }
            if (tdi != null) {
                sessionEstudianteCarrera.getPersona().setTipoDocumentoIdentificacionId(tdi.getId());
            }
            if (estudianteCarrera.getId() == null) {
                int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "crear_estudiante_carrera");
                if (tienePermiso == 1) {
                    if (personaFacadeLocal.esUnico(sessionEstudianteCarrera.getPersona().getNumeroIdentificacion(), sessionEstudianteCarrera.getPersona().getId())) {
//                        estudianteCarrera.getEstudianteId().getPersona().setEstudiante((Estudiante) null);
                        personaFacadeLocal.create(sessionEstudianteCarrera.getPersona());
                        estudianteCarrera.getEstudianteId().setId(sessionEstudianteCarrera.getPersona().getId());
                        estudianteFacadeLocal.create(estudianteCarrera.getEstudianteId());
                        logFacadeLocal.create(logFacadeLocal.crearLog("Estudiante", estudianteCarrera.getEstudianteId().getId() + "", "CREAR", "|Nombres= " + sessionEstudianteCarrera.getPersona().getNombres() + "|Apellidos= " + sessionEstudianteCarrera.getPersona().getApellidos()
                                + "|Cédula= " + sessionEstudianteCarrera.getPersona().getNumeroIdentificacion() + "|Email= " + sessionEstudianteCarrera.getPersona().getEmail(), sessionUsuario.getUsuario()));
                        Aspirante aspirante = sessionEstudianteCarrera.getAspirante();
                        selectEstadoEstudianteCarrera(estudianteCarrera);
                        estudianteCarreraFacadeLocal.create(sessionEstudianteCarrera.getEstudianteCarrera());
                        aspirante.setId(sessionEstudianteCarrera.getEstudianteCarrera().getId());
                        aspiranteFacadeLocal.create(aspirante);
                        administrarUsuarios.grabarUsuarioEstudiante(estudianteCarrera.getEstudianteId());
                        administrarEstudiantes.renderedFotos(sessionEstudianteCarrera.getEstudianteCarrera().getEstudianteId());
                        administrarEstudiantes.renderedInformacionEstudio(sessionEstudianteCarrera.getEstudianteCarrera().getEstudianteId());
                        logFacadeLocal.create(logFacadeLocal.crearLog("EstudianteCarrera", estudianteCarrera.getId() + "", "CREAR", "|Carrera=" + estudianteCarrera.getCarreraId() + "|Estudiante=" + estudianteCarrera.getEstudianteId() + "|EsActivo= " + sessionEstudianteCarrera.getEstudianteCarrera().getEsActivo(), sessionUsuario.getUsuario()));
                        if (param.equalsIgnoreCase("grabar")) {
                            sessionEstudianteCarrera.setEstudianteCarrera(new EstudianteCarrera());
                            navegacion = "pretty:estudiantesCarrera";
                        } else {
                            if (param.equalsIgnoreCase("grabar-editar")) {
                                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.estudiante") + " " + bundle.getString("lbl.msm_grabar"), "");
                                FacesContext.getCurrentInstance().addMessage(null, message);
                            }
                        }
                    } else {
                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.estudiante") + " " + bundle.getString("lbl.msm_existe"), "");
                        FacesContext.getCurrentInstance().addMessage(null, message);
                    }
                    buscar("");
                } else {
                    if (tienePermiso == 2) {
                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear") + ". " + bundle.getString("lbl.msm_consulte"), "");
                        FacesContext.getCurrentInstance().addMessage(null, message);
                    }
                }
            } else {
                int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "editar_estudiante_carrera");
                if (tienePermiso == 1) {
                    if (personaFacadeLocal.esUnico(sessionEstudianteCarrera.getPersona().getNumeroIdentificacion(), sessionEstudianteCarrera.getPersona().getId())) {
//                       sessionEstudianteCarrera.getPersona().setEstudiante((Estudiante) null);
                        personaFacadeLocal.edit(sessionEstudianteCarrera.getPersona());
//                        estudianteCarrera.getEstudianteId().getPersona().setEstudiante(estudianteCarrera.getEstudianteId());
                        estudianteCarrera.getEstudianteId().setId(sessionEstudianteCarrera.getPersona().getId());
                        estudianteFacadeLocal.edit(sessionEstudianteCarrera.getEstudianteCarrera().getEstudianteId());
                        logFacadeLocal.create(logFacadeLocal.crearLog("Estudiante", estudianteCarrera.getEstudianteId().getId() + "", "EDITAR", "|Nombres= " + sessionEstudianteCarrera.getPersona().getNombres()
                                + "|Apellidos= " + sessionEstudianteCarrera.getPersona().getApellidos()
                                + "|Cédula= " + sessionEstudianteCarrera.getPersona().getNumeroIdentificacion() + "|Email= " + sessionEstudianteCarrera.getPersona().getEmail(), sessionUsuario.getUsuario()));
                        selectEstadoEstudianteCarrera(estudianteCarrera);
                        estudianteCarreraFacadeLocal.edit(estudianteCarrera);
                        logFacadeLocal.create(logFacadeLocal.crearLog("EstudianteCarrera", estudianteCarrera.getId() + "", "EDITAR", "|Carrera=" + estudianteCarrera.getCarreraId() + "|Estudiante=" + estudianteCarrera.getEstudianteId() + "|EsActivo= " + sessionEstudianteCarrera.getEstudianteCarrera().getEsActivo(), sessionUsuario.getUsuario()));
                        administrarEstudiantes.renderedFotos(sessionEstudianteCarrera.getEstudianteCarrera().getEstudianteId());
                        administrarEstudiantes.renderedInformacionEstudio(sessionEstudianteCarrera.getEstudianteCarrera().getEstudianteId());
                        if (param.equalsIgnoreCase("grabar")) {
                            sessionEstudianteCarrera.setEstudianteCarrera(new EstudianteCarrera());
                            navegacion = "pretty:estudiantesCarrera";
                        } else {
                            if (param.equalsIgnoreCase("grabar-editar")) {
                                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.estudiante") + " " + bundle.getString("lbl.msm_editar"), "");
                                FacesContext.getCurrentInstance().addMessage(null, message);
                            }
                        }
                    } else {
                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.estudiante") + " " + bundle.getString("lbl.msm_existe"), "");
                        FacesContext.getCurrentInstance().addMessage(null, message);
                    }
                    buscar("");
                } else {
                    if (tienePermiso == 2) {
                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_editar") + ". " + bundle.getString("lbl.msm_consulte"), "");
                        FacesContext.getCurrentInstance().addMessage(null, message);
                    }
                }
            }

        } catch (Exception e) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
        return navegacion;
    }

    public void esAptoEstudiante() {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            if (sessionEstudianteCarrera.getAspirante().getEsApto()) {
                sessionEstudianteCarrera.getAspirante().setEsApto(false);
            } else {
                sessionEstudianteCarrera.getAspirante().setEsApto(true);
            }
            if (sessionEstudianteCarrera.getAspirante().getId() != null) {
                aspiranteFacadeLocal.edit(sessionEstudianteCarrera.getAspirante());
                logFacadeLocal.create(logFacadeLocal.crearLog("Aspirante", sessionEstudianteCarrera.getAspirante().getId() + "", "EDITAR", "|EsApto=" + sessionEstudianteCarrera.getAspirante().getEsApto(), sessionUsuario.getUsuario()));
            }
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.estudiante") + " " + bundle.getString("lbl.msm_editar"), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        } catch (Exception e) {
        }
    }

    public ReporteMatricula obtenerMatriculaUltima(EstudianteCarrera estudianteCarrera) {
        ReporteMatricula rm = new ReporteMatricula();
        try {
            rm = reporteMatriculaFacadeLocal.buscarUltimaMatriculaEstudiante(estudianteCarrera.getId());
        } catch (Exception e) {
        }
        return rm;
    }

    public ReporteMatricula obtenerPrimerMatricula(EstudianteCarrera estudianteCarrera) {
        ReporteMatricula rm = new ReporteMatricula();
        try {
            rm = reporteMatriculaFacadeLocal.buscarPrimerMatriculaEstudiante(estudianteCarrera.getId());
        } catch (Exception e) {
        }
        return rm;
    }
    private boolean renderedEsAptoAspirante;

    public void habilitaCampoEsAptoAspirante(Usuario usuario) {
        try {
            if (usuarioFacadeLocal.tienePermiso(usuario, "habilitar_apto_aspirante") == 1) {
                renderedEsAptoAspirante = true;
            } else {
                renderedEsAptoAspirante = false;
            }
        } catch (Exception e) {
        }

    }

    public void grabarReporteMatriculas() {
        try {
            for (ReporteMatricula rm : reporteMatriculas) {
                rm.setEstudianteCarreraId(sessionEstudianteCarrera.getEstudianteCarrera());
                ReporteMatricula reporte = reporteMatriculaFacadeLocal.buscarPorMatriculaId(rm.getMatriculaId());
                if (reporte == null) {
                    sgaWebServicesReporteMatricula(rm);
                    reporteMatriculaFacadeLocal.create(rm);
                    logFacadeLocal.create(logFacadeLocal.crearLog("ReporteMatricula", rm.getId() + "", "CREAR", "NUEVO: |MatriculaId" + rm.getMatriculaId() + "|NombreModulo= " + rm.getModuloMatriculado() + "|Oferta= " + rm.getOfertaAcademicaId().getId() + "|Estado= " + rm.getEstadoMatriculaId(), sessionUsuario.getUsuario()));
                } else {
                    reporte.setEsAprobado(rm.getEsAprobado());
                    reporte.setMatriculaId(rm.getMatriculaId());
                    reporte.setEstudianteCarreraId(sessionEstudianteCarrera.getEstudianteCarrera());
                    reporte.setEstadoMatriculaId(rm.getEstadoMatriculaId());
                    reporte.setModuloMatriculado(rm.getModuloMatriculado());
                    reporte.setOfertaAcademicaId(rm.getOfertaAcademicaId());
                    reporte.setParalelo(rm.getParalelo());
                    sgaWebServicesReporteMatricula(reporte);
                    reporteMatriculaFacadeLocal.edit(reporte);
                    logFacadeLocal.create(logFacadeLocal.crearLog("ReporteMatricula", reporte.getId() + "", "CREAR", "NUEVO: |MatriculaId" + reporte.getMatriculaId() + "|NombreModulo= " + reporte.getModuloMatriculado() + "|Oferta= " + reporte.getOfertaAcademicaId().getId() + "|Estado= " + reporte.getEstadoMatriculaId(), sessionUsuario.getUsuario()));

                }
            }
        } catch (Exception e) {
        }
    }

    public List<ReporteMatricula> listadoMatriculasEstudiante(EstudianteCarrera estudianteCarrera) {
        List<ReporteMatricula> reporteMatriculas1 = new ArrayList<>();
        for (ReporteMatricula rm : reporteMatriculaFacadeLocal.buscarPorEstudianteCarrera(estudianteCarrera.getId())) {
            reporteMatriculas1.add(rm);
        }
        return reporteMatriculas1;
    }

    public void compruebaEsAspiranteApto() {
        boolean esApto = false;
        String mensaje = "";
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        DateResource calculo = new DateResource();
        if (sessionEstudianteCarrera.getEstudianteCarrera().getCarreraId() != null) {
            String moduloMaxAprobado = configuracionCarreraFacadeLocal.buscarPorCarreraId(sessionEstudianteCarrera.getEstudianteCarrera().getCarreraId().getId(), "MA").getValor();
            String moduloEgresado = configuracionCarreraFacadeLocal.buscarPorCarreraId(sessionEstudianteCarrera.getEstudianteCarrera().getCarreraId().getId(), "ME").getValor();
            int tiempoGracia = Integer.parseInt(configuracionGeneralFacadeLocal.find((int) 21).getValor());
            ReporteMatricula reporte = obtenerMatriculaUltima(sessionEstudianteCarrera.getEstudianteCarrera());
            if (reporte != null) {
                if (Integer.parseInt(reporte.getNumeroModuloMatriculado()) > Integer.parseInt(moduloMaxAprobado)) {
                    Calendar fechaActual = Calendar.getInstance();
                    int tiempo = calculo.calculaDuracionEnDias(reporte.getOfertaAcademicaId().getFechaFin(), fechaActual.getTime(), 0);
                    if (tiempo >= 0) {
                        if (tiempo <= tiempoGracia) {
                            esApto = true;
                        } else {
                            mensaje = bundle.getString("lbl.msm_tiempo_apto_aspirante");
                        }
                    }
                } else {
                    mensaje = bundle.getString("lbl.msm_modulo_apto_aspirante");
                }

                if (Integer.parseInt(reporte.getNumeroModuloMatriculado()) >= Integer.parseInt(moduloEgresado)) {
                    EstadoEstudianteCarrera estadoEstudianteCarrera = estadoEstudianteCarreraFacadeLocal.find((int) 2);
                    if (estadoEstudianteCarrera != null) {
                        sessionEstudianteCarrera.getEstudianteCarrera().setEstadoId(estadoEstudianteCarrera);
                    }
                }
                if (esApto) {
                    sessionEstudianteCarrera.getAspirante().setEsApto(true);
                    if (sessionEstudianteCarrera.getAspirante().getId() != null) {
                        aspiranteFacadeLocal.edit(sessionEstudianteCarrera.getAspirante());
                        estudianteCarreraFacadeLocal.edit(sessionEstudianteCarrera.getEstudianteCarrera());
                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_es_apto_aspirante"), "");
                        FacesContext.getCurrentInstance().addMessage(null, message);
                    } else {
                        sessionEstudianteCarrera.getAspirante().setEsApto(false);
                        if (sessionEstudianteCarrera.getAspirante().getId() != null) {
                            aspiranteFacadeLocal.edit(sessionEstudianteCarrera.getAspirante());
                            logFacadeLocal.create(logFacadeLocal.crearLog("Aspirante", sessionEstudianteCarrera.getAspirante().getId() + "", "EDITAR", "NUEVO: |EsApto=" + sessionEstudianteCarrera.getAspirante().getEsApto(), sessionUsuario.getUsuario()));
                        }
                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, mensaje, "");
                        FacesContext.getCurrentInstance().addMessage(null, message);
                    }
                } else {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.estudiante") + " " + bundle.getString("lbl.reporte_matriculas"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.estudiante") + " " + bundle.getString("lbl.msm_no_pertenece_carrera"), "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        }
    }

    public void crearPdf(Carrera carrera) throws IOException, DocumentException {
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
        response.addHeader("Content-disposition", "attachment; filename=Estudiantes.pdf");
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        pdf.setPageSize(PageSize.A4.rotate());
        pdf.setMargins(20f, 20f, 20f, 20f);
        pdf.setPageSize(PageSize.A4);
        Font fontHeader = new Font(Font.TIMES_ROMAN, 16, Font.BOLD);
        Font fontTitle = FontFactory.getFont("Times-Roman", 18, Font.BOLD);
        Paragraph title = new Paragraph(bundle.getString("lbl.listado") + " " + bundle.getString("lbl.de") + " " + bundle.getString("lbl.estudiantes"), fontTitle);
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
        PdfPTable pdfTable = new PdfPTable(4);
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

        cell1 = new PdfPCell(new Phrase(bundle.getString("lbl.email"), fontHeader));
        cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
        pdfTable.addCell(cell1);
        pdfTable.setHeaderRows(1);
        for (EstudianteCarrera estudianteCarrera : estudianteCarreras) {
            Persona persona = personaFacadeLocal.find(estudianteCarrera.getEstudianteId().getId());
            pdfTable.addCell(persona.getNombres());
            pdfTable.addCell(persona.getApellidos());
            pdfTable.addCell(persona.getNumeroIdentificacion());
            pdfTable.addCell(persona.getEmail());
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
        int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "editar_estudiante_carrera");
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
        int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "eliminar_estudiante_carrera");
        if (tienePermiso == 1) {
            var = true;
        }
        return var;
    }

    public boolean renderedCrear() {
        boolean var = false;
        int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "crear_estudiante_carrera");
        if (tienePermiso == 1) {
            var = true;
        }
        return var;
    }
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="WEB SERVICES">

    public void sgaWebServicesEstudianteMatriculas(Usuario usuario) {
//        FacesContext facesContext = FacesContext.getCurrentInstance();
//        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
//        int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "sga_ws_estudiante_carrera");
//        if (tienePermiso == 1) {
//            reporteMatriculas = new ArrayList<>();
//            reporteMatricula = null;
//            ConexionServicio conexionServicio = new ConexionServicio();
//            String serviceUrl = configuracionGeneralFacadeLocal.find((int) 16).getValor();
//            String passwordService = configuracionGeneralFacadeLocal.find((int) 5).getValor();
//            String userService = configuracionGeneralFacadeLocal.find((int) 6).getValor();
//            try {
//                String s = serviceUrl + "?id_carrera=" +sessionEstudianteCarrera.getEstudianteCarrera().getCarreraId().getIdSga() + ";cedula=" + sessionEstudianteCarrera.getPersona().getNumeroIdentificacion();
//                String datosJson = conexionServicio.conectar(s, userService, passwordService);
//                if (!datosJson.equalsIgnoreCase("")) {
//                    JsonParser parser = new JsonParser();
//                    JsonElement datos = parser.parse(datosJson);
//                    recorrerElementosJson(datos);
//                    grabarReporteMatriculas();
//                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.sincronizado"), "");
//                    FacesContext.getCurrentInstance().addMessage(null, message);
//                } else {
//                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.no_sincronizar_web_services"), "");
//                    FacesContext.getCurrentInstance().addMessage(null, message);
//                }
//
//            } catch (Exception e) {
//                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.permiso_denegado_sincronizar"), "");
//                FacesContext.getCurrentInstance().addMessage(null, message);
//            }
//        } else {
//            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.no_sincronizar_web_services"), "");
//            FacesContext.getCurrentInstance().addMessage(null, message);
//        }
    }

    public void sgaWebServicesReporteMatricula(ReporteMatricula rm) {
//        FacesContext facesContext = FacesContext.getCurrentInstance();
//        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
//        ConexionServicio conexionServicio = new ConexionServicio();
//        String serviceUrl = configuracionGeneralFacadeLocal.find((int) 44).getValor();
//        String passwordService = configuracionGeneralFacadeLocal.find((int) 5).getValor();
//        String userService = configuracionGeneralFacadeLocal.find((int) 6).getValor();
//        try {
//            Persona p= personaFacadeLocal.find(rm.getEstudianteCarreraId().getEstudianteId().getId());
//            String s = serviceUrl + "?cedula=" + p.getNumeroIdentificacion() + ";id_carrera=" + rm.getEstudianteCarreraId().getCarreraId().getIdSga() + ";id_oferta=" + rm.getOfertaAcademicaId().getIdSga();
//            String datosJson = conexionServicio.conectar(s, userService, passwordService);
//            if (!datosJson.equalsIgnoreCase("")) {
//                JsonParser parser = new JsonParser();
//                JsonElement datos = parser.parse(datosJson);
//                recorreJsonReporteMatricula(datos, rm);
//            }
//
//        } catch (Exception e) {
//            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.permiso_denegado_sincronizar"), "");
//            FacesContext.getCurrentInstance().addMessage(null, message);
//        }

    }

    private void recorreJsonReporteMatricula(JsonElement elemento, ReporteMatricula rm) throws Exception {
        try {
            if (elemento.isJsonObject()) {
                JsonObject obj = elemento.getAsJsonObject();
                java.util.Set<java.util.Map.Entry<String, JsonElement>> entradas = obj.entrySet();
                java.util.Iterator<java.util.Map.Entry<String, JsonElement>> iter = entradas.iterator();
                while (iter.hasNext()) {
                    java.util.Map.Entry<String, JsonElement> entrada = iter.next();
                    keyRm = entrada.getKey();
                    try {
                        String e = new String(entrada.getValue().getAsString().getBytes(), "UTF-8");
                        JsonParser jp = new JsonParser();
                        JsonElement jsonElement = jp.parse(e);
                        recorreJsonReporteMatricula(jsonElement, rm);

                    } catch (Exception e) {
                        recorreJsonReporteMatricula(entrada.getValue(), rm);
                    }
                }
            } else if (elemento.isJsonArray()) {
                JsonArray array = elemento.getAsJsonArray();
                keyEnteroRm = 0;

                java.util.Iterator<JsonElement> iter = array.iterator();
                while (iter.hasNext()) {
                    JsonElement entrada = iter.next();
                    recorreJsonReporteMatricula(entrada, rm);
                }
            } else if (elemento.isJsonPrimitive()) {
                JsonPrimitive valor = elemento.getAsJsonPrimitive();
                if (keyEnteroRm == 3) {
                    double nota = Math.round(valor.getAsDouble() * 100);
                    nota = nota / 100;
                    rm.setNota(nota);
                }
                keyEnteroRm++;
            }
        } catch (Exception e) {
        }
    }

    private void recorrerElementosJson(JsonElement elemento) throws Exception {
        try {
            if (elemento.isJsonObject()) {
                reporteMatricula = new ReporteMatricula();
                JsonObject obj = elemento.getAsJsonObject();
                java.util.Set<java.util.Map.Entry<String, JsonElement>> entradas = obj.entrySet();
                java.util.Iterator<java.util.Map.Entry<String, JsonElement>> iter = entradas.iterator();
                while (iter.hasNext()) {
                    java.util.Map.Entry<String, JsonElement> entrada = iter.next();
                    key = entrada.getKey();
                    try {
                        String e = new String(entrada.getValue().getAsString().getBytes(), "UTF-8");
                        JsonParser jp = new JsonParser();
                        JsonElement jsonElement = jp.parse(e);
                        recorrerElementosJson(jsonElement);

                    } catch (Exception e) {
                        recorrerElementosJson(entrada.getValue());
                    }

                }

            } else if (elemento.isJsonArray()) {
                JsonArray array = elemento.getAsJsonArray();
                keyEntero = 0;
                reporteMatricula = new ReporteMatricula();
                esNuevoReporte = true;

                java.util.Iterator<JsonElement> iter = array.iterator();
                while (iter.hasNext()) {
                    JsonElement entrada = iter.next();
                    recorrerElementosJson(entrada);
                }
            } else if (elemento.isJsonPrimitive()) {
                JsonPrimitive valor = elemento.getAsJsonPrimitive();
                if (valor.isBoolean()) {
                    if (!key.equalsIgnoreCase("result")) {

                    } else {
                        keyEntero++;
                    }

                } else if (valor.isNumber()) {
                    if (!key.equalsIgnoreCase("result")) {

                    } else {
                        if (keyEntero == 0) {
                            OfertaAcademica ofertaAcademica = ofertaAcademicaFacadeLocal.buscarPorIdSga(valor.getAsString());
                            if (ofertaAcademica != null) {
                                reporteMatricula.setOfertaAcademicaId(ofertaAcademica);
                            }
                        } else {
                            if (keyEntero == 1) {
                                reporteMatricula.setMatriculaId(Long.parseLong(valor.getAsString()));
                            } else {
                                if (keyEntero == 3) {
                                    reporteMatricula.setNumeroModuloMatriculado(valor.getAsString());
                                }
                            }
                        }
                        keyEntero++;
                    }
                } else if (valor.isString()) {
                    if (!key.equalsIgnoreCase("result")) {
                        if (key.equalsIgnoreCase("Sigla")) {

                        } else {
                            if (key.equalsIgnoreCase("Nombre")) {

                            }
                        }
                    } else {
                        if (keyEntero == 2) {
                            reporteMatricula.setParalelo(valor.getAsString());
                        } else {
                            if (keyEntero == 4) {
                                reporteMatricula.setModuloMatriculado(valor.getAsString());
                            } else {
                                if (keyEntero == 5) {
                                    EstadoMatricula estadoMatricula = estadoMatriculaFacadeLocal.buscarPorNombre(valor.getAsString());
                                    if (estadoMatricula != null) {
                                        reporteMatricula.setEstadoMatriculaId(estadoMatricula);
                                        if (estadoMatricula.getId() == 1) {
                                            reporteMatricula.setEsAprobado(true);
                                        }
                                    } else {
                                        estadoMatricula = new EstadoMatricula();
                                        estadoMatricula.setNombre(valor.getAsString());
                                        estadoMatriculaFacadeLocal.create(estadoMatricula);
                                        reporteMatricula.setEstadoMatriculaId(estadoMatricula);
                                    }
                                } else {
                                    if (keyEntero == 3) {
                                        reporteMatricula.setNumeroModuloMatriculado(valor.getAsString());
                                    }
                                }
                            }
                        }
                        keyEntero++;
                    }

                }
            }
            if (reporteMatricula.getEstadoMatriculaId() != null && reporteMatricula.getOfertaAcademicaId() != null && reporteMatricula.getMatriculaId() != null
                    && reporteMatricula.getModuloMatriculado() != null && reporteMatricula.getNumeroModuloMatriculado() != null && reporteMatricula.getParalelo() != null) {
                reporteMatriculas.add(reporteMatricula);
            }
        } catch (Exception e) {

        }
    }

    public void sgaWebServicesEstadoEstudiantesParalelo(String paraleloId, Carrera carrera) {
//        FacesContext facesContext = FacesContext.getCurrentInstance();
//        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
//        ConexionServicio conexionServicio = new ConexionServicio();
//        String serviceUrl = configuracionGeneralFacadeLocal.find((int) 42).getValor();
//        String passwordService = configuracionGeneralFacadeLocal.find((int) 5).getValor();
//        String userService = configuracionGeneralFacadeLocal.find((int) 6).getValor();
//        try {
//            String s = serviceUrl + "?id_paralelo=" + paraleloId;
//            String datosJson = conexionServicio.conectar(s, userService, passwordService);
//            if (!datosJson.equalsIgnoreCase("")) {
//                JsonParser parser = new JsonParser();
//                JsonElement datos = parser.parse(datosJson);
//                i = 0;
//                recorrerElementosJsonEstadoEstudiantesParalelo(datos, carrera);
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

    private void recorrerElementosJsonEstadoEstudiantesParalelo(JsonElement elemento, Carrera carrera) throws Exception {
        try {
            if (elemento.isJsonObject()) {
                JsonObject obj = elemento.getAsJsonObject();
                java.util.Set<java.util.Map.Entry<String, JsonElement>> entradas = obj.entrySet();
                java.util.Iterator<java.util.Map.Entry<String, JsonElement>> iter = entradas.iterator();
                while (iter.hasNext()) {
                    java.util.Map.Entry<String, JsonElement> entrada = iter.next();
                    keyEstadoEstudiantesParalelo = entrada.getKey();
                    try {
                        String e = new String(entrada.getValue().getAsString().getBytes(), "UTF-8");
                        JsonParser jp = new JsonParser();
                        JsonElement jsonElement = jp.parse(e);
                        recorrerElementosJsonEstadoEstudiantesParalelo(jsonElement, carrera);

                    } catch (Exception e) {
                        recorrerElementosJsonEstadoEstudiantesParalelo(entrada.getValue(), carrera);
                    }
                }

            } else if (elemento.isJsonArray()) {
                JsonArray array = elemento.getAsJsonArray();
                keyEnteroEstadoEstudiantesParelelo = 0;
                java.util.Iterator<JsonElement> iter = array.iterator();
                while (iter.hasNext()) {
                    JsonElement entrada = iter.next();
                    recorrerElementosJsonEstadoEstudiantesParalelo(entrada, carrera);
                }
            } else if (elemento.isJsonPrimitive()) {
                JsonPrimitive valor = elemento.getAsJsonPrimitive();
                if (i > 4) {
                    if (valor.isString()) {
                        if (keyEnteroEstadoEstudiantesParelelo == 3) {
                            Persona persona = personaFacadeLocal.buscarPorNumeroIdentificacion(valor.getAsString());
                            Estudiante estudiante = null;
                            if (persona != null) {
                                estudiante = estudianteFacadeLocal.find(persona.getId());
                            } else {
                                persona = new Persona();
                                persona.setNumeroIdentificacion(valor.getAsString());

                            }
//                            sgaWebServicesDatosEstudiante();
                        }
                    }
                    keyEnteroEstadoEstudiantesParelelo++;
                }
                i++;
            }

        } catch (Exception e) {

        }
    }

//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="SET Y GET">
    public SessionUsuario getSessionUsuario() {
        return sessionUsuario;
    }

    public void setSessionUsuario(SessionUsuario sessionUsuario) {
        this.sessionUsuario = sessionUsuario;
    }

    public SessionEstudianteCarrera getSessionEstudianteCarrera() {
        return sessionEstudianteCarrera;
    }

    public void setSessionEstudianteCarrera(SessionEstudianteCarrera sessionEstudianteCarrera) {
        this.sessionEstudianteCarrera = sessionEstudianteCarrera;
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

    public String getCriterio() {
        return criterio;
    }

    public void setCriterio(String criterio) {
        this.criterio = criterio;
    }

    public List<EstudianteCarrera> getEstudianteCarreras() {
        return estudianteCarreras;
    }

    public void setEstudianteCarreras(List<EstudianteCarrera> estudianteCarreras) {
        this.estudianteCarreras = estudianteCarreras;
    }

    public SessionUsuarioCarrera getSessionUsuarioCarrera() {
        return sessionUsuarioCarrera;
    }

    public void setSessionUsuarioCarrera(SessionUsuarioCarrera sessionUsuarioCarrera) {
        this.sessionUsuarioCarrera = sessionUsuarioCarrera;
    }

    public boolean isRenderedNoEditar() {
        return renderedNoEditar;
    }

    public void setRenderedNoEditar(boolean renderedNoEditar) {
        this.renderedNoEditar = renderedNoEditar;
    }

    public boolean isRenderedEsAptoAspirante() {
        return renderedEsAptoAspirante;
    }

    public void setRenderedEsAptoAspirante(boolean renderedEsAptoAspirante) {
        this.renderedEsAptoAspirante = renderedEsAptoAspirante;
    }
//</editor-fold>
}
