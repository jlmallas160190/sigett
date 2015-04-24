/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.comun.controlador;

import com.jlmallas.api.email.Mail;
import com.jlmallas.comun.entity.Persona;
import com.jlmallas.comun.service.PersonaFacadeLocal;
import edu.jlmallas.academico.entity.DocenteCarrera;
import edu.jlmallas.academico.entity.EstudianteCarrera;
import edu.jlmallas.academico.service.DocenteCarreraFacadeLocal;
import edu.jlmallas.academico.service.EstudianteCarreraFacadeLocal;
import edu.unl.sigett.adjudicacion.session.SessionDirectorProyecto;
import edu.unl.sigett.entity.AutorProyecto;
import edu.unl.sigett.entity.Cronograma;
import edu.unl.sigett.entity.DirectorProyecto;
import edu.unl.sigett.entity.DocenteProyecto;
import edu.unl.sigett.enumeration.EstadoAutorEnum;
import edu.unl.sigett.finalizacion.managed.session.SessionMiembro;
import edu.unl.sigett.postulacion.managed.session.SessionDocenteProyecto;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import edu.unl.sigett.session.AutorProyectoFacadeLocal;
import edu.unl.sigett.session.ConfiguracionGeneralFacadeLocal;
import edu.unl.sigett.session.DirectorProyectoFacadeLocal;
import javax.inject.Inject;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
public class AdministrarNotificaciones implements Serializable {

    @Inject
    private SessionMiembro sessionMiembro;
    private String notificacion = "";
    @EJB
    private ConfiguracionGeneralFacadeLocal configuracionGeneralFacadeLocal;
    private int numeroNotificacionesParaDirector = 0;
    @EJB
    private DirectorProyectoFacadeLocal directorProyectoFacadeLocal;
    @EJB
    private AutorProyectoFacadeLocal autorProyectoFacadeLocal;
    @EJB
    private PersonaFacadeLocal personaFacadeLocal;
    @EJB
    private DocenteCarreraFacadeLocal docenteCarreraFacadeLocal;
    @EJB
    private EstudianteCarreraFacadeLocal estudianteCarreraFacadeLocal;

    public AdministrarNotificaciones() {

    }

    public void notificarEnvioActividad(Cronograma cronograma, Date fechaEnvio) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            Mail mail = new Mail();
            String usrCorreo = configuracionGeneralFacadeLocal.find((int) 28).getValor();
            String passwordCorreo = configuracionGeneralFacadeLocal.find((int) 29).getValor();
            String puerto = configuracionGeneralFacadeLocal.find((int) 31).getValor();
            String smtp = configuracionGeneralFacadeLocal.find((int) 30).getValor();
            for (DirectorProyecto directorProyecto : directorProyectoFacadeLocal.buscarPorProyecto(cronograma.getProyecto().getId())) {
                DocenteCarrera docenteCarrera = docenteCarreraFacadeLocal.find(directorProyecto.getDirectorId().getId());
                Persona personaDirector = personaFacadeLocal.find(docenteCarrera.getDocenteId().getId());
                if (directorProyecto.getEstadoDirectorId().getId() == 1) {
                    mail.enviarNotificacionCorreo(smtp, puerto, usrCorreo, passwordCorreo, bundle.getString("lbl.actividad") + " " + bundle.getString("lbl.revisar"), bundle.getString("lbl.estimado") + personaDirector.getNombres() + " "
                            + " " + personaDirector.getApellidos() + " " + bundle.getString("lbl.msm_envio_actividad") + " "
                            + "" + directorProyecto.getProyectoId().getTemaActual(), personaDirector.getEmail(), personaDirector.getNombres()
                            + " " + personaDirector.getApellidos());

                }
            }
        } catch (Exception e) {
        }
    }

    public void notificarAsignacionMiembroTribunal() {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            Mail email = new Mail();
            String usrCorreo = configuracionGeneralFacadeLocal.find((int) 28).getValor();
            String passwordCorreo = configuracionGeneralFacadeLocal.find((int) 29).getValor();
            String puerto = configuracionGeneralFacadeLocal.find((int) 31).getValor();
            String smtp = configuracionGeneralFacadeLocal.find((int) 30).getValor();
            email.enviarNotificacionCorreo(smtp, puerto, usrCorreo, passwordCorreo, bundle.getString("lbl.designacion_miembro"), bundle.getString("lbl.estimado") + ": "
                    + sessionMiembro.getPersona().getNombres() + " " + sessionMiembro.getPersona().getApellidos() + " " + bundle.getString("lbl.msm_designacion_miembro") + ": "
                    + sessionMiembro.getMiembro().getTribunalId().getProyectoId().getTemaActual() + ""
                    + " ", sessionMiembro.getPersona().getEmail(), sessionMiembro.getPersona().getNombres() + " " + sessionMiembro.getPersona().getApellidos());

        } catch (Exception e) {
        }
    }

    public void notificarAsignacionDocenteProyecto(DocenteProyecto docenteProyecto,String tiempoMaxPertinencia) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            Mail mail = new Mail();
            Persona persona=personaFacadeLocal.find(docenteProyecto.getDocenteId());
            String usrCorreo = configuracionGeneralFacadeLocal.find((int) 28).getValor();
            String passwordCorreo = configuracionGeneralFacadeLocal.find((int) 29).getValor();
            String puerto = configuracionGeneralFacadeLocal.find((int) 31).getValor();
            String smtp = configuracionGeneralFacadeLocal.find((int) 30).getValor();
            mail.enviarNotificacionCorreo(smtp, puerto, usrCorreo, passwordCorreo, "", bundle.getString("lbl.estimado") + ": "
                    +persona.getNombres() + " " + persona.getApellidos() + " "
                    + bundle.getString("lbl.msm_asignacion_docente") + " " +docenteProyecto.getProyectoId().getTemaActual() + ""
                    + "  " + ";" + bundle.getString("lbl.msm_nota_asignacion_docente") + " " + tiempoMaxPertinencia + " "
                    + bundle.getString("lbl.dias"), persona.getEmail(),persona.getNombres() + " "
                    + persona.getApellidos());

        } catch (Exception e) {
        }
    }

    public void notificarAsignacionDirectorProyecto(Persona persona,DirectorProyecto directorProyecto) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            Mail mail = new Mail();
            String usrCorreo = configuracionGeneralFacadeLocal.find((int) 28).getValor();
            String passwordCorreo = configuracionGeneralFacadeLocal.find((int) 29).getValor();
            String puerto = configuracionGeneralFacadeLocal.find((int) 31).getValor();
            String smtp = configuracionGeneralFacadeLocal.find((int) 30).getValor();
            mail.enviarNotificacionCorreo(smtp, puerto, usrCorreo, passwordCorreo, bundle.getString("lbl.director") + " " + bundle.getString("lbl.proyecto"), bundle.getString("lbl.estimado") + ": "
                    + persona.getNombres() + " " + persona.getApellidos() + " " + bundle.getString("lbl.msm_asignacion_director") + " "
                    +directorProyecto.getProyectoId().getTemaActual() + " ",persona.getEmail(),persona.getNombres() + " "
                    + persona.getApellidos());
        } catch (Exception e) {
        }
    }

    public void notificarAsignacionDocenteProyectoAutores(List<AutorProyecto> autorProyectos, Map<String, String> map) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            Mail mail = new Mail();
            String usrCorreo = configuracionGeneralFacadeLocal.find((int) 28).getValor();
            String passwordCorreo = configuracionGeneralFacadeLocal.find((int) 29).getValor();
            String puerto = configuracionGeneralFacadeLocal.find((int) 31).getValor();
            String smtp = configuracionGeneralFacadeLocal.find((int) 30).getValor();
            for (AutorProyecto autorProyecto : autorProyectos) {
                EstudianteCarrera estudianteCarrera = estudianteCarreraFacadeLocal.find(autorProyecto.getAspiranteId().getId());
                Persona persona = personaFacadeLocal.find(estudianteCarrera.getEstudianteId().getId());
                mail.enviarNotificacionCorreo(smtp, puerto, usrCorreo, passwordCorreo, bundle.getString("lbl.pertinencia"), bundle.getString("lbl.estimado") + ": "
                        + persona.getNombres() + " " + persona.getApellidos() + " " + bundle.getString("lbl.msm_informe_proyecto_autor")
                        + ": (" + autorProyecto.getProyectoId().getTemaActual() + ") "
                        + bundle.getString("lbl.msm_asignacion_docente_proyecto_autor") + map.values() + " " + bundle.getString("lbl.msm_info_asignacion_docente_proyecto_autor"), persona.getEmail(), persona.getNombres() + " " + persona.getApellidos());

            }
        } catch (Exception e) {
        }
    }

    public void notificarAsignacionDirectorProyectoAutores(List<AutorProyecto> autorProyectos, Map<String, String> map) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            Mail mail = new Mail();
            String usrCorreo = configuracionGeneralFacadeLocal.find((int) 28).getValor();
            String passwordCorreo = configuracionGeneralFacadeLocal.find((int) 29).getValor();
            String puerto = configuracionGeneralFacadeLocal.find((int) 31).getValor();
            String smtp = configuracionGeneralFacadeLocal.find((int) 30).getValor();
            for (AutorProyecto autorProyecto : autorProyectos) {
                EstudianteCarrera estudianteCarrera = estudianteCarreraFacadeLocal.find(autorProyecto.getAspiranteId().getId());
                Persona persona = personaFacadeLocal.find(estudianteCarrera.getEstudianteId().getId());
                mail.enviarNotificacionCorreo(smtp, puerto, usrCorreo, passwordCorreo, bundle.getString("lbl.director") + " "
                        + bundle.getString("lbl.proyecto"), bundle.getString("lbl.estimado") + ": " + persona.getNombres() + " "
                        + persona.getApellidos() + " " + bundle.getString("lbl.msm_informe_proyecto_autor") + ":(" + autorProyecto.getProyectoId().getTemaActual() + ") "
                        + bundle.getString("lbl.msm_asignacion_director_proyecto_autor") + ":" + map.values() + " " + bundle.getString("lbl.msm_info_asignacion_director_proyecto_autor"),
                        persona.getEmail(), persona.getNombres() + " " + persona.getApellidos());

            }
        } catch (Exception e) {
        }
    }

    public void notificarRevisionActividad(Cronograma cronograma) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            Mail mail = new Mail();
            String usrCorreo = configuracionGeneralFacadeLocal.find((int) 28).getValor();
            String passwordCorreo = configuracionGeneralFacadeLocal.find((int) 29).getValor();
            String puerto = configuracionGeneralFacadeLocal.find((int) 31).getValor();
            String smtp = configuracionGeneralFacadeLocal.find((int) 30).getValor();
            for (AutorProyecto autorProyecto : autorProyectoFacadeLocal.buscarPorProyecto(cronograma.getProyecto().getId())) {
                if (!autorProyecto.getEstadoAutorId().getCodigo().equalsIgnoreCase(EstadoAutorEnum.ABANDONADO.getTipo())) {
                     EstudianteCarrera estudianteCarrera = estudianteCarreraFacadeLocal.find(autorProyecto.getAspiranteId().getId());
                Persona persona = personaFacadeLocal.find(estudianteCarrera.getEstudianteId().getId());
               
                    mail.enviarNotificacionCorreo(smtp, puerto, usrCorreo, passwordCorreo, bundle.getString("lbl.actividad") + " " + bundle.getString("lbl.revisada"), bundle.getString("lbl.estimado") + " " + persona.getNombres() + " "
                            + " " + persona.getApellidos() + bundle.getString("lbl.msm_revision_actividad") + " "
                            + "" + autorProyecto.getProyectoId().getTemaActual(),persona.getEmail(),persona.getNombres() + " " +
                                   persona.getApellidos());
                }
            }
        } catch (Exception e) {
        }
    }

    public String getNotificacion() {
        return notificacion;
    }

    public void setNotificacion(String notificacion) {
        this.notificacion = notificacion;
    }

    public int getNumeroNotificacionesParaDirector() {
        return numeroNotificacionesParaDirector;
    }

    public void setNumeroNotificacionesParaDirector(int numeroNotificacionesParaDirector) {
        this.numeroNotificacionesParaDirector = numeroNotificacionesParaDirector;
    }
}
