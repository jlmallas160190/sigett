/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.proyecto;

import com.jlmallas.comun.entity.Item;
import edu.jlmallas.academico.entity.Carrera;
import edu.jlmallas.academico.entity.Coordinador;
import edu.jlmallas.academico.entity.CoordinadorPeriodo;
import edu.jlmallas.academico.entity.OfertaAcademica;
import edu.unl.sigett.academico.dto.CoordinadorPeriodoDTO;
import edu.unl.sigett.autor.AutorProyectoDTO;
import edu.unl.sigett.docenteProyecto.DocenteProyectoDTO;
import edu.unl.sigett.documentoProyecto.DocumentoProyectoDTO;
import edu.unl.sigett.entity.ConfiguracionProyecto;
import edu.unl.sigett.entity.Cronograma;
import edu.unl.sigett.entity.LineaInvestigacion;
import edu.unl.sigett.entity.LineaInvestigacionProyecto;
import edu.unl.sigett.entity.Proyecto;
import edu.unl.sigett.entity.ProyectoCarreraOferta;
import edu.unl.sigett.entity.TemaProyecto;
import edu.unl.sigett.webSemantica.dto.ProyectoOntDTO;
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

    private Proyecto proyectoSeleccionado;
    private TemaProyecto temaProyecto;
    private AutorProyectoDTO autorProyectoDTOSeleccionado;
    private Carrera carreraSeleccionada;
    private CoordinadorPeriodoDTO coordinadorPeriodoDTOCarreraSeleccionada;
    private Item estadoSeleccionado;
    private Item categoriaSeleccionada;
    private Item tipoSeleccionado;
    private Item estadoActual;
    private OfertaAcademica ofertaAcademicaSeleccionada;
    private Cronograma cronograma;

    private List<Proyecto> proyectos;
    private List<DocumentoProyectoDTO> documentosProyectoDTO;
    private List<DocumentoProyectoDTO> filterDocumentosProyectoDTO;
    private List<DocumentoProyectoDTO> documentosProyectosDTOAgregados;
    private List<Proyecto> filterProyectos;
    private LineaInvestigacionProyecto lineaInvestigacionProyectoSeleccionada;
    private List<Carrera> carreras;
    private List<OfertaAcademica> ofertaAcademicas;
    private List<OfertaAcademica> filterOfertaAcademicas;
    private List<Carrera> filterCarreras;
    private List<Carrera> carrerasProyecto;
    private List<Carrera> filterCarrerasProyecto;
    private List<AutorProyectoDTO> autoresProyectoDTO;
    private List<AutorProyectoDTO> filterAutoresProyectoDTO;
    private List<AutorProyectoDTO> autoresProyectoDTONuevos;
    private List<LineaInvestigacionProyecto> lineasInvestigacionProyecto;
    private List<LineaInvestigacionProyecto> filterLineasInvestigacionProyecto;
    private List<ConfiguracionProyecto> configuracionProyectos;
    private List<Item> estados;
    private List<Item> categorias;
    private List<Item> tipos;
    private List<ProyectoCarreraOferta> carrerasSeleccionadasTransfer;
    private List<ProyectoCarreraOferta> carrerasRemovidasTransfer;
    private List<LineaInvestigacionProyecto> lineasInvestigacionSeleccionadasTransfer;
    private List<LineaInvestigacionProyecto> lineasInvestigacionRemovidosTransfer;
    private List<LineaInvestigacion> lineasInvestigacionSeleccionadas;
    private List<DocenteProyectoDTO> docentesProyectoDTO;
    private List<DocenteProyectoDTO> filterDocentesProyectoDTO;

    private ProyectoOntDTO proyectoOntDTO;

    private Boolean renderedEditar;
    private Boolean renderedCrear;
    private Boolean renderedInicio;
    private Boolean renderedPertinente;
    private Boolean renderedDialogoAP;

    private DualListModel<LineaInvestigacion> lineasInvestigacionDualList;
    private DualListModel<Carrera> carrerasDualList;

    public SessionProyecto() {
        this.documentosProyectosDTOAgregados = new ArrayList<>();
        this.documentosProyectoDTO = new ArrayList<>();
        this.filterDocumentosProyectoDTO = new ArrayList<>();
        this.docentesProyectoDTO = new ArrayList<>();
        this.filterDocentesProyectoDTO = new ArrayList<>();
        this.autoresProyectoDTONuevos = new ArrayList<>();
        this.cronograma = new Cronograma();
        this.lineasInvestigacionSeleccionadas = new ArrayList<>();
        this.lineasInvestigacionSeleccionadasTransfer = new ArrayList<>();
        this.lineasInvestigacionRemovidosTransfer = new ArrayList<>();
        this.carrerasRemovidasTransfer = new ArrayList<>();
        this.carrerasSeleccionadasTransfer = new ArrayList<>();
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
        this.proyectoSeleccionado = new Proyecto();
    }

    public Proyecto getProyectoSeleccionado() {
        return proyectoSeleccionado;
    }

    public void setProyectoSeleccionado(Proyecto proyectoSeleccionado) {
        this.proyectoSeleccionado = proyectoSeleccionado;
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

    public Item getCategoriaSeleccionada() {
        return categoriaSeleccionada;
    }

    public void setCategoriaSeleccionada(Item categoriaSeleccionada) {
        this.categoriaSeleccionada = categoriaSeleccionada;
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

    public Boolean getRenderedInicio() {
        return renderedInicio;
    }

    public void setRenderedInicio(Boolean renderedInicio) {
        this.renderedInicio = renderedInicio;
    }

    public TemaProyecto getTemaProyecto() {
        return temaProyecto;
    }

    public void setTemaProyecto(TemaProyecto temaProyecto) {
        this.temaProyecto = temaProyecto;
    }

    public Boolean getRenderedPertinente() {
        return renderedPertinente;
    }

    public void setRenderedPertinente(Boolean renderedPertinente) {
        this.renderedPertinente = renderedPertinente;
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

    public List<ProyectoCarreraOferta> getCarrerasSeleccionadasTransfer() {
        return carrerasSeleccionadasTransfer;
    }

    public void setCarrerasSeleccionadasTransfer(List<ProyectoCarreraOferta> carrerasSeleccionadasTransfer) {
        this.carrerasSeleccionadasTransfer = carrerasSeleccionadasTransfer;
    }

    public List<ProyectoCarreraOferta> getCarrerasRemovidasTransfer() {
        return carrerasRemovidasTransfer;
    }

    public void setCarrerasRemovidasTransfer(List<ProyectoCarreraOferta> carrerasRemovidasTransfer) {
        this.carrerasRemovidasTransfer = carrerasRemovidasTransfer;
    }

    public List<LineaInvestigacionProyecto> getLineasInvestigacionSeleccionadasTransfer() {
        return lineasInvestigacionSeleccionadasTransfer;
    }

    public void setLineasInvestigacionSeleccionadasTransfer(List<LineaInvestigacionProyecto> lineasInvestigacionSeleccionadasTransfer) {
        this.lineasInvestigacionSeleccionadasTransfer = lineasInvestigacionSeleccionadasTransfer;
    }

    public List<LineaInvestigacionProyecto> getLineasInvestigacionRemovidosTransfer() {
        return lineasInvestigacionRemovidosTransfer;
    }

    public void setLineasInvestigacionRemovidosTransfer(List<LineaInvestigacionProyecto> lineasInvestigacionRemovidosTransfer) {
        this.lineasInvestigacionRemovidosTransfer = lineasInvestigacionRemovidosTransfer;
    }

    public Cronograma getCronograma() {
        return cronograma;
    }

    public void setCronograma(Cronograma cronograma) {
        this.cronograma = cronograma;
    }

    public Item getTipoSeleccionado() {
        return tipoSeleccionado;
    }

    public void setTipoSeleccionado(Item tipoSeleccionado) {
        this.tipoSeleccionado = tipoSeleccionado;
    }

    public List<AutorProyectoDTO> getAutoresProyectoDTONuevos() {
        return autoresProyectoDTONuevos;
    }

    public void setAutoresProyectoDTONuevos(List<AutorProyectoDTO> autoresProyectoDTONuevos) {
        this.autoresProyectoDTONuevos = autoresProyectoDTONuevos;
    }

    public Item getEstadoActual() {
        return estadoActual;
    }

    public void setEstadoActual(Item estadoActual) {
        this.estadoActual = estadoActual;
    }

    public List<DocenteProyectoDTO> getDocentesProyectoDTO() {
        return docentesProyectoDTO;
    }

    public void setDocentesProyectoDTO(List<DocenteProyectoDTO> docentesProyectoDTO) {
        this.docentesProyectoDTO = docentesProyectoDTO;
    }

    public List<DocenteProyectoDTO> getFilterDocentesProyectoDTO() {
        return filterDocentesProyectoDTO;
    }

    public void setFilterDocentesProyectoDTO(List<DocenteProyectoDTO> filterDocentesProyectoDTO) {
        this.filterDocentesProyectoDTO = filterDocentesProyectoDTO;
    }

    public List<LineaInvestigacion> getLineasInvestigacionSeleccionadas() {
        return lineasInvestigacionSeleccionadas;
    }

    public void setLineasInvestigacionSeleccionadas(List<LineaInvestigacion> lineasInvestigacionSeleccionadas) {
        this.lineasInvestigacionSeleccionadas = lineasInvestigacionSeleccionadas;
    }

    public ProyectoOntDTO getProyectoOntDTO() {
        return proyectoOntDTO;
    }

    public void setProyectoOntDTO(ProyectoOntDTO proyectoOntDTO) {
        this.proyectoOntDTO = proyectoOntDTO;
    }

    public List<DocumentoProyectoDTO> getDocumentosProyectoDTO() {
        return documentosProyectoDTO;
    }

    public void setDocumentosProyectoDTO(List<DocumentoProyectoDTO> documentosProyectoDTO) {
        this.documentosProyectoDTO = documentosProyectoDTO;
    }

    public List<DocumentoProyectoDTO> getFilterDocumentosProyectoDTO() {
        return filterDocumentosProyectoDTO;
    }

    public void setFilterDocumentosProyectoDTO(List<DocumentoProyectoDTO> filterDocumentosProyectoDTO) {
        this.filterDocumentosProyectoDTO = filterDocumentosProyectoDTO;
    }

    public List<DocumentoProyectoDTO> getDocumentosProyectosDTOAgregados() {
        return documentosProyectosDTOAgregados;
    }

    public void setDocumentosProyectosDTOAgregados(List<DocumentoProyectoDTO> documentosProyectosDTOAgregados) {
        this.documentosProyectosDTOAgregados = documentosProyectosDTOAgregados;
    }

    public CoordinadorPeriodoDTO getCoordinadorPeriodoDTOCarreraSeleccionada() {
        return coordinadorPeriodoDTOCarreraSeleccionada;
    }

    public void setCoordinadorPeriodoDTOCarreraSeleccionada(CoordinadorPeriodoDTO coordinadorPeriodoDTOCarreraSeleccionada) {
        this.coordinadorPeriodoDTOCarreraSeleccionada = coordinadorPeriodoDTOCarreraSeleccionada;
    }

}
