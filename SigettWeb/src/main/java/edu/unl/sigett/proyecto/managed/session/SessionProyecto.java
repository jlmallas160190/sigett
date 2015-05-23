/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.proyecto.managed.session;

import com.jlmallas.comun.entity.Item;
import edu.jlmallas.academico.entity.Carrera;
import edu.jlmallas.academico.entity.OfertaAcademica;
import edu.unl.sigett.autor.dto.AutorProyectoDTO;
import edu.unl.sigett.entity.ConfiguracionProyecto;
import edu.unl.sigett.entity.DocenteProyecto;
import edu.unl.sigett.entity.LineaInvestigacion;
import edu.unl.sigett.entity.LineaInvestigacionProyecto;
import edu.unl.sigett.entity.Proyecto;
import edu.unl.sigett.entity.TemaProyecto;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import org.primefaces.model.DualListModel;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
public class SessionProyecto implements Serializable {

    private Proyecto proyecto;
    private TemaProyecto temaProyecto;
    private AutorProyectoDTO autorProyectoDTOSeleccionado;

    private List<Proyecto> proyectos;
    private List<Proyecto> filterProyectos;
    private Item estadoSeleccionado;
    private Carrera carreraSeleccionada;
    private OfertaAcademica ofertaAcademicaSeleccionada;
    private LineaInvestigacionProyecto lineaInvestigacionProyectoSeleccionada;
    private List<Carrera> carreras;
    private List<OfertaAcademica> ofertaAcademicas;
    private List<OfertaAcademica> filterOfertaAcademicas;
    private List<Carrera> filterCarreras;
    private List<Carrera> carrerasProyecto;
    private List<Carrera> filterCarrerasProyecto;
    private List<AutorProyectoDTO> autoresProyectoDTO;
    private List<AutorProyectoDTO> filterAutoresProyectoDTO;

    private List<LineaInvestigacionProyecto> lineasInvestigacionProyecto;
    private List<LineaInvestigacionProyecto> filterLineasInvestigacionProyecto;
    private List<ConfiguracionProyecto> configuracionProyectos;
    private List<Item> estados;
    private List<Item> categorias;
    private List<Item> tipos;

    private Boolean renderedEditar;
    private Boolean renderedCrear;
    private Boolean renderedEditarDatosProyecto;
    private Boolean renderedEditarCronograma;
    private Boolean renderedDialogoAP;

    private String tipo;
    private String catalogo;
    private String estado;

    private DualListModel<LineaInvestigacion> lineasInvestigacionDualList;
    private DualListModel<Carrera> carrerasDualList;

    public SessionProyecto() {
        this.autorProyectoDTOSeleccionado = new AutorProyectoDTO();
        this.filterAutoresProyectoDTO = new ArrayList<>();
        this.autoresProyectoDTO = new ArrayList<>();
        this.carrerasProyecto = new ArrayList<>();
        this.filterCarrerasProyecto = new ArrayList<>();
        this.estados = new ArrayList<>();
        this.categorias = new ArrayList<>();
        this.tipos = new ArrayList<>();
        this.temaProyecto = new TemaProyecto();
        this.configuracionProyectos = new ArrayList<>();
        this.lineasInvestigacionDualList = new DualListModel<>();
        this.lineasInvestigacionProyecto = new ArrayList<>();
        this.filterLineasInvestigacionProyecto = new ArrayList<>();
        this.filterCarreras = new ArrayList<>();
        this.filterOfertaAcademicas = new ArrayList<>();
        this.carreras = new ArrayList<>();
        this.ofertaAcademicas = new ArrayList<>();
        this.ofertaAcademicaSeleccionada = new OfertaAcademica();
        this.carreraSeleccionada = new Carrera();
        this.estadoSeleccionado = new Item();
        this.proyectos = new ArrayList<>();
        this.filterProyectos = new ArrayList<>();
        this.proyecto = new Proyecto();
    }

    public Proyecto getProyecto() {
        return proyecto;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }

    public List<Proyecto> getProyectos() {
        return proyectos;
    }

    public void setProyectos(List<Proyecto> proyectos) {
        this.proyectos = proyectos;
    }

    public List<Proyecto> getFilterProyectos() {
        return filterProyectos;
    }

    public void setFilterProyectos(List<Proyecto> filterProyectos) {
        this.filterProyectos = filterProyectos;
    }

    public Item getEstadoSeleccionado() {
        return estadoSeleccionado;
    }

    public void setEstadoSeleccionado(Item estadoSeleccionado) {
        this.estadoSeleccionado = estadoSeleccionado;
    }

    public Carrera getCarreraSeleccionada() {
        return carreraSeleccionada;
    }

    public void setCarreraSeleccionada(Carrera carreraSeleccionada) {
        this.carreraSeleccionada = carreraSeleccionada;
    }

    public OfertaAcademica getOfertaAcademicaSeleccionada() {
        return ofertaAcademicaSeleccionada;
    }

    public void setOfertaAcademicaSeleccionada(OfertaAcademica ofertaAcademicaSeleccionada) {
        this.ofertaAcademicaSeleccionada = ofertaAcademicaSeleccionada;
    }

    public List<Carrera> getCarreras() {
        return carreras;
    }

    public void setCarreras(List<Carrera> carreras) {
        this.carreras = carreras;
    }

    public List<OfertaAcademica> getOfertaAcademicas() {
        return ofertaAcademicas;
    }

    public void setOfertaAcademicas(List<OfertaAcademica> ofertaAcademicas) {
        this.ofertaAcademicas = ofertaAcademicas;
    }

    public List<OfertaAcademica> getFilterOfertaAcademicas() {
        return filterOfertaAcademicas;
    }

    public void setFilterOfertaAcademicas(List<OfertaAcademica> filterOfertaAcademicas) {
        this.filterOfertaAcademicas = filterOfertaAcademicas;
    }

    public List<Carrera> getFilterCarreras() {
        return filterCarreras;
    }

    public void setFilterCarreras(List<Carrera> filterCarreras) {
        this.filterCarreras = filterCarreras;
    }

    public Boolean getRenderedEditar() {
        return renderedEditar;
    }

    public void setRenderedEditar(Boolean renderedEditar) {
        this.renderedEditar = renderedEditar;
    }

    public Boolean getRenderedCrear() {
        return renderedCrear;
    }

    public void setRenderedCrear(Boolean renderedCrear) {
        this.renderedCrear = renderedCrear;
    }

    public List<LineaInvestigacionProyecto> getLineasInvestigacionProyecto() {
        return lineasInvestigacionProyecto;
    }

    public void setLineaInvestigacionProyecto(List<LineaInvestigacionProyecto> lineasInvestigacionProyecto) {
        this.lineasInvestigacionProyecto = lineasInvestigacionProyecto;
    }

    public List<LineaInvestigacionProyecto> getFilterLineasInvestigacionProyecto() {
        return filterLineasInvestigacionProyecto;
    }

    public void setFilterLineaInvestigacionProyecto(List<LineaInvestigacionProyecto> filterLineasInvestigacionProyecto) {
        this.filterLineasInvestigacionProyecto = filterLineasInvestigacionProyecto;
    }

    public LineaInvestigacionProyecto getLineaInvestigacionProyectoSeleccionada() {
        return lineaInvestigacionProyectoSeleccionada;
    }

    public void setLineaInvestigacionProyectoSeleccionada(LineaInvestigacionProyecto lineaInvestigacionProyectoSeleccionada) {
        this.lineaInvestigacionProyectoSeleccionada = lineaInvestigacionProyectoSeleccionada;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getCatalogo() {
        return catalogo;
    }

    public void setCatalogo(String catalogo) {
        this.catalogo = catalogo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public DualListModel<LineaInvestigacion> getLineasInvestigacionDualList() {
        return lineasInvestigacionDualList;
    }

    public void setLineasInvestigacionDualList(DualListModel<LineaInvestigacion> lineasInvestigacionDualList) {
        this.lineasInvestigacionDualList = lineasInvestigacionDualList;
    }

    public DualListModel<Carrera> getCarrerasDualList() {
        return carrerasDualList;
    }

    public void setCarrerasDualList(DualListModel<Carrera> carrerasDualList) {
        this.carrerasDualList = carrerasDualList;
    }

    public List<ConfiguracionProyecto> getConfiguracionProyectos() {
        return configuracionProyectos;
    }

    public void setConfiguracionProyectos(List<ConfiguracionProyecto> configuracionProyectos) {
        this.configuracionProyectos = configuracionProyectos;
    }

    public Boolean getRenderedEditarDatosProyecto() {
        return renderedEditarDatosProyecto;
    }

    public void setRenderedEditarDatosProyecto(Boolean renderedEditarDatosProyecto) {
        this.renderedEditarDatosProyecto = renderedEditarDatosProyecto;
    }

    public TemaProyecto getTemaProyecto() {
        return temaProyecto;
    }

    public void setTemaProyecto(TemaProyecto temaProyecto) {
        this.temaProyecto = temaProyecto;
    }

    public Boolean getRenderedEditarCronograma() {
        return renderedEditarCronograma;
    }

    public void setRenderedEditarCronograma(Boolean renderedEditarCronograma) {
        this.renderedEditarCronograma = renderedEditarCronograma;
    }

    public List<Item> getEstados() {
        return estados;
    }

    public void setEstados(List<Item> estados) {
        this.estados = estados;
    }

    public List<Item> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<Item> categorias) {
        this.categorias = categorias;
    }

    public List<Item> getTipos() {
        return tipos;
    }

    public void setTipos(List<Item> tipos) {
        this.tipos = tipos;
    }

    public List<Carrera> getCarrerasProyecto() {
        return carrerasProyecto;
    }

    public void setCarrerasProyecto(List<Carrera> carrerasProyecto) {
        this.carrerasProyecto = carrerasProyecto;
    }

    public List<Carrera> getFilterCarrerasProyecto() {
        return filterCarrerasProyecto;
    }

    public void setFilterCarrerasProyecto(List<Carrera> filterCarrerasProyecto) {
        this.filterCarrerasProyecto = filterCarrerasProyecto;
    }

    public List<AutorProyectoDTO> getAutoresProyectoDTO() {
        return autoresProyectoDTO;
    }

    public void setAutoresProyectoDTO(List<AutorProyectoDTO> autoresProyectoDTO) {
        this.autoresProyectoDTO = autoresProyectoDTO;
    }

    public List<AutorProyectoDTO> getFilterAutoresProyectoDTO() {
        return filterAutoresProyectoDTO;
    }

    public void setFilterAutoresProyectoDTO(List<AutorProyectoDTO> filterAutoresProyectoDTO) {
        this.filterAutoresProyectoDTO = filterAutoresProyectoDTO;
    }

    public AutorProyectoDTO getAutorProyectoDTOSeleccionado() {
        return autorProyectoDTOSeleccionado;
    }

    public void setAutorProyectoDTOSeleccionado(AutorProyectoDTO autorProyectoDTOSeleccionado) {
        this.autorProyectoDTOSeleccionado = autorProyectoDTOSeleccionado;
    }

    public Boolean isRenderedDialogoAP() {
        return renderedDialogoAP;
    }

    public void setRenderedDialogoAP(Boolean renderedDialogoAP) {
        this.renderedDialogoAP = renderedDialogoAP;
    }

}
