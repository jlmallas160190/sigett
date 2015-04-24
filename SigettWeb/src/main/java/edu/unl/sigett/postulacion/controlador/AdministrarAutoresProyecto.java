/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.postulacion.controlador;

import com.jlmallas.comun.entity.Persona;
import com.jlmallas.comun.service.PersonaFacadeLocal;
import edu.unl.sigett.adjudicacion.session.SessionRenunciaAutor;
import edu.unl.sigett.postulacion.managed.session.SessionAutorProyecto;
import edu.unl.sigett.postulacion.managed.session.SessionProyecto;
import edu.unl.sigett.seguridad.managed.session.SessionUsuario;
import edu.unl.sigett.entity.Aspirante;
import edu.unl.sigett.entity.AutorProyecto;
import edu.unl.sigett.entity.EstadoAutor;
import edu.unl.sigett.entity.Proyecto;
import edu.unl.sigett.entity.RenunciaAutor;
import com.jlmallas.seguridad.entity.Usuario;
import edu.unl.sigett.entity.UsuarioCarrera;
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
import org.primefaces.context.RequestContext;
import org.primefaces.event.TabChangeEvent;
import edu.unl.sigett.session.AspiranteFacadeLocal;
import edu.unl.sigett.session.AutorProyectoFacadeLocal;
import edu.unl.sigett.session.EstadoAutorFacadeLocal;
import com.jlmallas.seguridad.session.UsuarioFacadeLocal;
import edu.jlmallas.academico.entity.EstudianteCarrera;
import edu.jlmallas.academico.service.EstudianteCarreraFacadeLocal;
import edu.unl.sigett.enumeration.EstadoAutorEnum;
import edu.unl.sigett.enumeration.EstadoProyectoEnum;
import edu.unl.sigett.session.UsuarioCarreraFacadeLocal;
import edu.unl.sigett.util.MessageView;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
public class AdministrarAutoresProyecto implements Serializable {

    @Inject
    private SessionUsuario sessionUsuario;
    @Inject
    private SessionProyecto sessionProyecto;
    @Inject
    private SessionAutorProyecto sessionAutorProyecto;
    @Inject
    private AdministrarExpedientes administrarExpedientes;
    @Inject
    private AdministrarDocumentosExpediente administrarDocumentosExpediente;
    @Inject
    private SessionRenunciaAutor sessionRenunciaAutor;

    private MessageView view;

    @EJB
    private AspiranteFacadeLocal aspiranteFacadeLocal;
    @EJB
    private EstadoAutorFacadeLocal estadoAutorFacadeLocal;
    @EJB
    private UsuarioFacadeLocal usuarioFacadeLocal;
    @EJB
    private AutorProyectoFacadeLocal autorProyectoFacadeLocal;
    @EJB
    private EstudianteCarreraFacadeLocal estudianteCarreraFacadeLocal;
    @EJB
    private PersonaFacadeLocal personaFacadeLocal;
    @EJB
    private UsuarioCarreraFacadeLocal usuarioCarreraFacadeLocal;

    private List<Aspirante> aspirantes;
    private List<AutorProyecto> autorProyectos;
    private List<AutorProyecto> listadoAutores;
    private List<AutorProyecto> directorioAutoresDocenteProyecto;
    private List<AutorProyecto> autorProyectosDirectorProyecto;

    private boolean renderedNoEditar;
    private boolean renderedEditar;
    private boolean renderedEliminar;
    private boolean renderedCrear;
    private boolean renderedBuscar;
    private boolean renderedBuscarAspirante;
    private boolean renderedSeleccionar;
    private boolean renderedDlgEditarAutorProyecto;

    private AutorProyecto viewAutor;

    private String criterioDirectorioAutor;
    private String criteriodirectorioAutoresDocenteProyecto;
    private String criteriodirectorioAutoresDirectorProyecto;
    private String criterioBusquedaAutor;
    private String criterioBusquedaAspirante;

    public AdministrarAutoresProyecto() {
        this.renderedDlgEditarAutorProyecto = false;
    }

    //<editor-fold defaultstate="collapsed" desc="MÉTODOS CRUD">
    public void editar(AutorProyecto autorProyecto) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "editar_autor_proyecto");
            if (tienePermiso == 1) {
                autorProyecto.setEsEditado(true);
                sessionAutorProyecto.setAutorProyecto(autorProyecto);
                renderedDlgEditarAutorProyecto = true;
                RequestContext.getCurrentInstance().execute("PF('dlgEditarAutor').show()");
            } else {
                if (tienePermiso == 2) {
                    view.message(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_editar") + ". " + bundle.getString("lbl.msm_consulte"), "");
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public void listadoAutores(String criterio, Proyecto proyecto) {
        listadoAutores = new ArrayList<>();
        try {
            for (AutorProyecto autorProyecto : autorProyectoFacadeLocal.buscarPorProyecto(proyecto.getId())) {
                if (!autorProyecto.getEstadoAutorId().getCodigo().equalsIgnoreCase(EstadoAutorEnum.ABANDONADO.getTipo())) {
                    EstudianteCarrera estudianteCarrera = estudianteCarreraFacadeLocal.find(autorProyecto.getAspiranteId().getId());
                    Persona personaAutor = personaFacadeLocal.find(estudianteCarrera.getEstudianteId().getId());
                    if (personaAutor.getApellidos().toUpperCase().contains(criterio.toUpperCase())
                            || personaAutor.getNombres().toUpperCase().contains(criterio.toUpperCase())
                            || personaAutor.getNumeroIdentificacion().contains(criterio)) {

                        listadoAutores.add(autorProyecto);
                    }
                }
            }

        } catch (Exception e) {
        }
    }

    public void buscar(String criterio, Proyecto proyecto, Usuario usuario) {
        int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "buscar_autor_proyecto");
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        if (tienePermiso == 1) {
            autorProyectos = new ArrayList<>();
            try {
                for (AutorProyecto autorProyecto : proyecto.getAutorProyectoList()) {
                    if (!autorProyecto.getEstadoAutorId().getCodigo().equalsIgnoreCase(EstadoAutorEnum.ABANDONADO.getTipo())) {
//                        if (autorProyecto.getAspiranteId().getEstudianteCarrera().getEstudianteId().getPersona().getApellidos().toUpperCase().contains(criterio.toUpperCase())
//                                || autorProyecto.getAspiranteId().getEstudianteCarrera().getEstudianteId().getPersona().getNombres().toUpperCase().contains(criterio.toUpperCase())
//                                || autorProyecto.getAspiranteId().getEstudianteCarrera().getEstudianteId().getPersona().getNumeroIdentificacion().contains(criterio)) {
                        autorProyectos.add(autorProyecto);
//                        }
                    }
                }

            } catch (Exception e) {
            }
        } else {
            view.message(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_buscar") + ". " + bundle.getString("lbl.msm_consulte"), "");
        }
    }

    public List<AutorProyecto> autoresProyecto(Proyecto proyecto) {
        try {
            List<AutorProyecto> aps = new ArrayList<>();
            for (AutorProyecto autorProyecto : autorProyectoFacadeLocal.buscarPorProyecto(proyecto.getId())) {
                if (!autorProyecto.getEstadoAutorId().getCodigo().equalsIgnoreCase(EstadoAutorEnum.ABANDONADO.getTipo())) {
                    aps.add(autorProyecto);
                }
            }
            return aps;
        } catch (Exception e) {
        }
        return null;
    }

    public void removerAutor(AutorProyecto autorProyecto, Proyecto proyecto, Usuario usuario) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "eliminar_autor_proyecto");
            if (tienePermiso == 1) {
                if (autorProyecto.getId() != null) {
                    autorProyecto.setEsEditado(true);
                    if (proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.INICIO.getTipo())) {
                        EstadoAutor estadoAutor = estadoAutorFacadeLocal.buscarPorCodigo(EstadoAutorEnum.ABANDONADO.getTipo());
                        if (estadoAutor != null) {
                            autorProyecto.setEstadoAutorId(estadoAutor);
                            buscar("", proyecto, usuario);
                            view.message(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_eliminar") + ". ", "");
                        }
                    } else {
                        if (!autorProyecto.getEstadoAutorId().getCodigo().equalsIgnoreCase(EstadoAutorEnum.ABANDONADO.getTipo())) {
                            if (proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.SEGUIMIENTO.getTipo())) {
                                int tienePermiso1 = usuarioFacadeLocal.tienePermiso(usuario, "crear_renuncia_autor");
                                if (tienePermiso1 == 1) {
                                    sessionRenunciaAutor.setRenunciaAutor(new RenunciaAutor());
                                    sessionAutorProyecto.setAutorProyecto(autorProyecto);
                                    RequestContext.getCurrentInstance().execute("PF('dlgEditarRenunciaAutorProyecto').show()");
                                } else {
                                    view.message(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear") + " " + bundle.getString("lbl.renuncia") + ". " + bundle.getString("lbl.msm_consulte"), "");
                                }
                            } else {
                                view.message(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear") + " " + bundle.getString("lbl.renuncia") + ". " + bundle.getString("lbl.msm_consulte"), "");
                            }
                        } else {
                            proyecto.getAutorProyectoList().remove(autorProyecto);
                            buscar("", proyecto, usuario);
                            view.message(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.autor") + " " + bundle.getString("lbl.msm_eliminar") + ". ", "");
                        }
                    }

                } else {
                    proyecto.getAutorProyectoList().remove(autorProyecto);
                    buscar("", proyecto, usuario);
                    view.message(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_eliminar"), "");
                }
            } else {
                view.message(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_eliminar") + ". " + bundle.getString("lbl.msm_consulte"), "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public AutorProyecto contieneAutorProyecto(Proyecto proyecto, AutorProyecto autorProyecto) {
        AutorProyecto ap = null;
        try {
            for (AutorProyecto autor : proyecto.getAutorProyectoList()) {
                if (autor.getAspiranteId().equals(autorProyecto.getAspiranteId())) {
                    ap = autor;
                    break;
                }
            }
        } catch (Exception e) {
        }
        return ap;
    }

    public boolean tieneAsignadoProyectoTitulacion(Aspirante aspirante) {
        boolean var = false;
        for (AutorProyecto autorProyecto : aspirante.getAutorProyectoList()) {
            if (autorProyecto.getProyectoId().getTipoProyectoId().getId() == 1
                    && !autorProyecto.getEstadoAutorId().getCodigo().equalsIgnoreCase(EstadoAutorEnum.ABANDONADO.getTipo())
                    && !autorProyecto.getProyectoId().getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.ABANDONADO.getTipo())
                    || (!autorProyecto.getProyectoId().getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.REPROBADO.getTipo()))) {
                var = true;
                break;
            }
        }
        return var;
    }

    public void agregarAutorProyecto(Aspirante aspirante, Proyecto proyecto, Usuario usuario) {
        Calendar fecha = Calendar.getInstance();
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            if (proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.INICIO.getTipo())) {
                int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "select_autor_proyecto");
                if (tienePermiso == 1) {
                    if (aspirante.getId() != null) {
                        aspirante = aspiranteFacadeLocal.find(aspirante.getId());
                    }
                    if (proyecto.getTipoProyectoId() != null) {
                        if (aspirante.getEsApto() || proyecto.getTipoProyectoId().getId() != 1) {
                            if (tieneAsignadoProyectoTitulacion(aspirante) == false) {
                                AutorProyecto autorProyecto = new AutorProyecto();
                                autorProyecto.setAspiranteId(aspirante);
                                autorProyecto.setFechaInicio(fecha.getTime());
                                if (proyecto.getCronograma().getFechaProrroga() != null) {
                                    autorProyecto.setFechaCulminacion(proyecto.getCronograma().getFechaProrroga());
                                } else {
                                    autorProyecto.setFechaCulminacion(proyecto.getCronograma().getFechaProrroga());
                                }
                                autorProyecto.setProyectoId(proyecto);
                                autorProyecto.setEsEditado(true);
                                EstadoAutor estadoAutor = estadoAutorFacadeLocal.buscarPorCodigo(EstadoAutorEnum.INICIO.getTipo());
                                if (estadoAutor != null) {
                                    autorProyecto.setEstadoAutorId(estadoAutor);
                                }
                                AutorProyecto ap = contieneAutorProyecto(proyecto, autorProyecto);
                                if (ap == null) {
                                    sessionProyecto.getProyecto().getAutorProyectoList().add(autorProyecto);
                                    buscar("", proyecto, usuario);
                                    view.message(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.autor") + " " + bundle.getString("lbl.msm_agregar"), "");
                                } else {
                                    sessionProyecto.getProyecto().getAutorProyectoList().remove(ap);
                                    ap.setAspiranteId(aspirante);
                                    if (ap.getFechaInicio() == null) {
                                        ap.setFechaInicio(proyecto.getCronograma().getFechaInicio());
                                    }
                                    if (ap.getFechaCulminacion() == null) {
                                        if (proyecto.getCronograma().getFechaProrroga() != null) {
                                            ap.setFechaCulminacion(proyecto.getCronograma().getFechaFin());
                                        } else {
                                            ap.setFechaCulminacion(fecha.getTime());
                                        }
                                    }
                                    ap.setProyectoId(proyecto);
                                    sessionProyecto.getProyecto().getAutorProyectoList().add(ap);
                                    buscar("", proyecto, usuario);
                                    view.message(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.autor") + " " + bundle.getString("lbl.msm_editar"), "");
                                }
                                RequestContext.getCurrentInstance().execute("PF('dlgBuscarAspirantes').hide()");
                            } else {
                                view.message(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.aspirante") + " " + bundle.getString("lbl.msm_tiene_autor_proyecto"), "");
                            }
                        } else {
                            view.message(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.aspirante") + " " + bundle.getString("lbl.msm_no_es_apto_tt"), "");
                        }
                    } else {
                        view.message(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl_no_select") + " " + bundle.getString("lbl.tipo_proyecto"), "");
                    }
                } else {
                    view.message(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_select") + ". " + bundle.getString("lbl.msm_consulte"), "");
                }
            } else {
                view.message(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear") + ". " + bundle.getString("lbl.msm_consulte"), "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onTabChange(TabChangeEvent event) {
        if (event.getTab().getId().equals("tabExpedientes")) {
            administrarExpedientes.buscarExpedientes(sessionAutorProyecto.getAutorProyecto());
            administrarExpedientes.renderedCrear(sessionUsuario.getUsuario());
            administrarExpedientes.renderedEditar(sessionUsuario.getUsuario());
            administrarExpedientes.renderedEliminar(sessionUsuario.getUsuario());
            administrarDocumentosExpediente.setRenderedDlgEditar(false);
            administrarDocumentosExpediente.renderedCrear(sessionUsuario.getUsuario());
            administrarDocumentosExpediente.renderedEditar(sessionUsuario.getUsuario());
            administrarDocumentosExpediente.renderedEliminar(sessionUsuario.getUsuario());
        }
    }

    public void viewAutorProyecto(AutorProyecto autorProyecto) {
        try {
            this.viewAutor = autorProyecto;
            RequestContext.getCurrentInstance().execute("PF('dlgViewAutor').show()");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="MÉTODOS LISTADOS">
    public List<AutorProyecto> listadoAutoresProyecto(Aspirante aspirante) {
        List<AutorProyecto> autorProyectos = new ArrayList<>();
        try {
            for (AutorProyecto autorProyecto : aspirante.getAutorProyectoList()) {
                autorProyectos.add(autorProyecto);
            }
            RequestContext.getCurrentInstance().execute("PF('dlgListadoAutoresProyecto').show()");
        } catch (Exception e) {
            System.out.println(e);
        }
        return autorProyectos;
    }

    public void buscarAspirantes(String criterio, Usuario usuario, Proyecto proyecto) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        this.aspirantes = new ArrayList<>();
        try {
            if (proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.INICIO.getTipo())) {
                int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "buscar_aspirante");
                if (tienePermiso == 1) {
                    for (UsuarioCarrera usuarioCarrera : usuarioCarreraFacadeLocal.buscarPorUsuario(usuario.getId())) {
                        for (Aspirante aspirante : aspiranteFacadeLocal.buscarPorCarrera(usuarioCarrera.getCarreraId())) {
                            EstudianteCarrera estudianteCarrera = estudianteCarreraFacadeLocal.find(aspirante.getId());
                            Persona personaEstudiante = personaFacadeLocal.find(estudianteCarrera.getId());
                            if (personaEstudiante.getApellidos().toUpperCase().contains(criterio.toUpperCase()) || personaEstudiante.getNombres().toUpperCase().contains(criterio.toUpperCase())
                                    || personaEstudiante.getNumeroIdentificacion().contains(criterio)) {
                                aspirantes.add(aspirante);
                            }
                        }
                    }
                } else {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_buscar") + ". " + bundle.getString("lbl.msm_consulte"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_buscar") + ". " + bundle.getString("lbl.msm_consulte"), "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        } catch (Exception e) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    public void buscarAutoresDesdeDocenteProyecto(String criterio, Proyecto proyecto) {
        directorioAutoresDocenteProyecto = new ArrayList<>();
        try {
            for (AutorProyecto autorProyecto : proyecto.getAutorProyectoList()) {
                if (!autorProyecto.getEstadoAutorId().getCodigo().equalsIgnoreCase(EstadoAutorEnum.ABANDONADO.getTipo())) {
                    EstudianteCarrera estudianteCarrera = estudianteCarreraFacadeLocal.find(autorProyecto.getAspiranteId().getId());
                    Persona personaEstudiante = personaFacadeLocal.find(estudianteCarrera.getId());
                    if (personaEstudiante.getApellidos().toUpperCase().contains(criterio.toUpperCase())
                            || personaEstudiante.getNombres().toUpperCase().contains(criterio.toUpperCase())
                            || personaEstudiante.getNumeroIdentificacion().contains(criterio)) {

                        directorioAutoresDocenteProyecto.add(autorProyecto);
                    }
                }
            }

        } catch (Exception e) {
        }
    }

    public void buscarAutoresDesdeDirectorProyecto(String criterio, Proyecto proyecto) {
        autorProyectosDirectorProyecto = new ArrayList<>();
        try {
            for (AutorProyecto autorProyecto : proyecto.getAutorProyectoList()) {
                if (!autorProyecto.getEstadoAutorId().getCodigo().equalsIgnoreCase(EstadoAutorEnum.ABANDONADO.getTipo())) {
                    EstudianteCarrera estudianteCarrera = estudianteCarreraFacadeLocal.find(autorProyecto.getAspiranteId().getId());
                    Persona personaEstudiante = personaFacadeLocal.find(estudianteCarrera.getId());
                    if (personaEstudiante.getApellidos().toUpperCase().contains(criterio.toUpperCase())
                            || personaEstudiante.getNombres().toUpperCase().contains(criterio.toUpperCase())
                            || personaEstudiante.getNumeroIdentificacion().contains(criterio.toUpperCase())) {
                        autorProyectosDirectorProyecto.add(autorProyecto);
                    }
                }
            }

        } catch (Exception e) {
        }
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="MÉTODOS RENDERED">
    public void renderedEditar(Usuario usuario) {
        int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "editar_autor_proyecto");
        if (tienePermiso == 1) {
            renderedEditar = true;
            renderedNoEditar = false;
        } else {
            renderedEditar = false;
            renderedNoEditar = true;
        }
    }

    public void renderedBuscar(Usuario usuario) {
        int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "buscar_autor_proyecto");
        if (tienePermiso == 1) {
            renderedBuscar = true;
        } else {
            renderedBuscar = false;
        }
    }

    public void renderedEliminar(Usuario usuario, Proyecto proyecto) {
        if (proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoAutorEnum.INICIO.getTipo())) {
            int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "eliminar_autor_proyecto");
            if (tienePermiso == 1) {
                renderedEliminar = true;
            } else {
                renderedEliminar = false;
            }
        } else {
            renderedEliminar = false;
        }
    }

    public void renderedBuscarAspirantes(Usuario usuario, Proyecto proyecto) {
        try {
            if (proyecto.getEstadoProyectoId() != null) {
                if (proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.INICIO.getTipo())) {
                    int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "buscar_aspirante");
                    if (tienePermiso == 1) {
                        renderedBuscarAspirante = true;
                    } else {
                        renderedBuscarAspirante = false;
                    }
                } else {
                    renderedBuscarAspirante = false;
                }
            } else {
                renderedBuscarAspirante = true;
            }
        } catch (Exception e) {
        }

    }

    public void renderedCrear(Usuario usuario) {
        int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "crear_autor_proyecto");
        if (tienePermiso == 1) {
            renderedCrear = true;
        } else {
            renderedCrear = false;
        }
    }

    public void renderedSeleccionar(Usuario usuario, Proyecto proyecto) {
        if (proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.INICIO.getTipo())) {
            int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "select_autor_proyecto");
            if (tienePermiso == 1) {
                renderedSeleccionar = true;
            } else {
                renderedSeleccionar = false;
            }
        } else {
            renderedSeleccionar = false;
        }
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="SET Y GET">
    public boolean isRenderedBuscar() {
        return renderedBuscar;
    }

    public void setRenderedBuscar(boolean renderedBuscar) {
        this.renderedBuscar = renderedBuscar;
    }

    public AutorProyecto getViewAutor() {
        return viewAutor;
    }

    public void setViewAutor(AutorProyecto viewAutor) {
        this.viewAutor = viewAutor;
    }

    public SessionUsuario getSessionUsuario() {
        return sessionUsuario;
    }

    public void setSessionUsuario(SessionUsuario sessionUsuario) {
        this.sessionUsuario = sessionUsuario;
    }

    public SessionProyecto getSessionProyecto() {
        return sessionProyecto;
    }

    public void setSessionProyecto(SessionProyecto sessionProyecto) {
        this.sessionProyecto = sessionProyecto;
    }

    public SessionAutorProyecto getSessionAutorProyecto() {
        return sessionAutorProyecto;
    }

    public void setSessionAutorProyecto(SessionAutorProyecto sessionAutorProyecto) {
        this.sessionAutorProyecto = sessionAutorProyecto;
    }

    public String getCriterioBusquedaAutor() {
        return criterioBusquedaAutor;
    }

    public void setCriterioBusquedaAutor(String criterioBusquedaAutor) {
        this.criterioBusquedaAutor = criterioBusquedaAutor;
    }

    public String getCriterioBusquedaAspirante() {
        return criterioBusquedaAspirante;
    }

    public void setCriterioBusquedaAspirante(String criterioBusquedaAspirante) {
        this.criterioBusquedaAspirante = criterioBusquedaAspirante;
    }

    public List<Aspirante> getAspirantes() {
        return aspirantes;
    }

    public void setAspirantes(List<Aspirante> aspirantes) {
        this.aspirantes = aspirantes;
    }

    public SessionRenunciaAutor getSessionRenunciaAutor() {
        return sessionRenunciaAutor;
    }

    public void setSessionRenunciaAutor(SessionRenunciaAutor sessionRenunciaAutor) {
        this.sessionRenunciaAutor = sessionRenunciaAutor;
    }

    public boolean isRenderedNoEditar() {
        return renderedNoEditar;
    }

    public void setRenderedNoEditar(boolean renderedNoEditar) {
        this.renderedNoEditar = renderedNoEditar;
    }

    public List<AutorProyecto> getAutorProyectos() {
        return autorProyectos;
    }

    public void setAutorProyectos(List<AutorProyecto> autorProyectos) {
        this.autorProyectos = autorProyectos;
    }

    public boolean isRenderedEditar() {
        return renderedEditar;
    }

    public void setRenderedEditar(boolean renderedEditar) {
        this.renderedEditar = renderedEditar;
    }

    public boolean isRenderedEliminar() {
        return renderedEliminar;
    }

    public void setRenderedEliminar(boolean renderedEliminar) {
        this.renderedEliminar = renderedEliminar;
    }

    public boolean isRenderedCrear() {
        return renderedCrear;
    }

    public void setRenderedCrear(boolean renderedCrear) {
        this.renderedCrear = renderedCrear;
    }

    public boolean isRenderedBuscarAspirante() {
        return renderedBuscarAspirante;
    }

    public void setRenderedBuscarAspirante(boolean renderedBuscarAspirante) {
        this.renderedBuscarAspirante = renderedBuscarAspirante;
    }

    public boolean isRenderedSeleccionar() {
        return renderedSeleccionar;
    }

    public void setRenderedSeleccionar(boolean renderedSeleccionar) {
        this.renderedSeleccionar = renderedSeleccionar;
    }

    public List<AutorProyecto> getListadoAutores() {
        return listadoAutores;
    }

    public void setListadoAutores(List<AutorProyecto> listadoAutores) {
        this.listadoAutores = listadoAutores;
    }

    public String getCriterioDirectorioAutor() {
        return criterioDirectorioAutor;
    }

    public void setCriterioDirectorioAutor(String criterioDirectorioAutor) {
        this.criterioDirectorioAutor = criterioDirectorioAutor;
    }

    public List<AutorProyecto> getDirectorioAutoresDocenteProyecto() {
        return directorioAutoresDocenteProyecto;
    }

    public void setDirectorioAutoresDocenteProyecto(List<AutorProyecto> directorioAutoresDocenteProyecto) {
        this.directorioAutoresDocenteProyecto = directorioAutoresDocenteProyecto;
    }

    public String getCriteriodirectorioAutoresDocenteProyecto() {
        return criteriodirectorioAutoresDocenteProyecto;
    }

    public void setCriteriodirectorioAutoresDocenteProyecto(String criteriodirectorioAutoresDocenteProyecto) {
        this.criteriodirectorioAutoresDocenteProyecto = criteriodirectorioAutoresDocenteProyecto;
    }

    public String getCriteriodirectorioAutoresDirectorProyecto() {
        return criteriodirectorioAutoresDirectorProyecto;
    }

    public void setCriteriodirectorioAutoresDirectorProyecto(String criteriodirectorioAutoresDirectorProyecto) {
        this.criteriodirectorioAutoresDirectorProyecto = criteriodirectorioAutoresDirectorProyecto;
    }

    public List<AutorProyecto> getAutorProyectosDirectorProyecto() {
        return autorProyectosDirectorProyecto;
    }

    public void setAutorProyectosDirectorProyecto(List<AutorProyecto> autorProyectosDirectorProyecto) {
        this.autorProyectosDirectorProyecto = autorProyectosDirectorProyecto;
    }

    public boolean isRenderedDlgEditarAutorProyecto() {
        return renderedDlgEditarAutorProyecto;
    }

    public void setRenderedDlgEditarAutorProyecto(boolean renderedDlgEditarAutorProyecto) {
        this.renderedDlgEditarAutorProyecto = renderedDlgEditarAutorProyecto;
    }
//</editor-fold>

}
