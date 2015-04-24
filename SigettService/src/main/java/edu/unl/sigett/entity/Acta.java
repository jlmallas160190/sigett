/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.unl.sigett.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jorge-luis
 */
@Entity
@Table(name = "acta")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Acta.findAll", query = "SELECT a FROM Acta a"),
    @NamedQuery(name = "Acta.findById", query = "SELECT a FROM Acta a WHERE a.id = :id"),
    @NamedQuery(name = "Acta.findByNumero", query = "SELECT a FROM Acta a WHERE a.numero = :numero")})
public class Acta implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "numero")
    private String numero;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Column(name = "documento")
    private byte[] documento;
    @JoinColumn(name = "evaluacion_tribunal_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private EvaluacionTribunal evaluacionTribunalId;
    @JoinColumn(name = "catalogo_acta_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private CatalogoActa catalogoActaId;

    public Acta() {
    }

    public Acta(Long id) {
        this.id = id;
    }

    public Acta(Long id, String numero, byte[] documento) {
        this.id = id;
        this.numero = numero;
        this.documento = documento;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public byte[] getDocumento() {
        return documento;
    }

    public void setDocumento(byte[] documento) {
        this.documento = documento;
    }

    public EvaluacionTribunal getEvaluacionTribunalId() {
        return evaluacionTribunalId;
    }

    public void setEvaluacionTribunalId(EvaluacionTribunal evaluacionTribunalId) {
        this.evaluacionTribunalId = evaluacionTribunalId;
    }

    public CatalogoActa getCatalogoActaId() {
        return catalogoActaId;
    }

    public void setCatalogoActaId(CatalogoActa catalogoActaId) {
        this.catalogoActaId = catalogoActaId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Acta)) {
            return false;
        }
        Acta other = (Acta) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.unl.sigett.entity.Acta[ id=" + id + " ]";
    }
    
}
