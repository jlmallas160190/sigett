/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.session;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.sparql.vocabulary.FOAF;
import edu.unl.sigett.vocabulario.Vocabulario;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Map;
import javax.ejb.Stateless;

/**
 *
 * @author jorge-luis
 */
@Stateless
public class AutorProyectoService implements AutorProyectoServiceLocal {

    private Vocabulario vocabulario;
    private String path;

    @Override
    public void read(String path) {
        try {
            this.vocabulario = new Vocabulario();
            this.path = path;
            InputStream in = new FileInputStream(path);
            vocabulario.getModel().read(in, "RDF/XML");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void write(Map parametros) {
        try {
            Individual individuoProyecto = null;
            Individual individuoInvestigador = null;
            Individual individuoAutor = null;
            Individual individuoAutorProyecto = null;
            individuoProyecto = vocabulario.getModel().getIndividual(vocabulario.getNS() + "proyecto/" + parametros.get("proyectoId"));
            if (individuoProyecto != null) {
                individuoInvestigador = vocabulario.getModel().getIndividual(vocabulario.getNS() + "investigador" + parametros.get("autorId"));
                if (individuoInvestigador == null) {
                    individuoInvestigador = vocabulario.getModel().createIndividual(vocabulario.getNS() + "investigador/" + parametros.get("personaId"), vocabulario.editarClaseInvestigador());
                }
                individuoInvestigador.setPropertyValue(FOAF.firstName, vocabulario.getModel().createTypedLiteral(parametros.get("personaNombres")));
                individuoInvestigador.setPropertyValue(FOAF.surname, vocabulario.getModel().createTypedLiteral(parametros.get("personaApellidos")));
                individuoInvestigador.setPropertyValue(FOAF.mbox, vocabulario.getModel().createTypedLiteral(parametros.get("personaEmail")));
                individuoInvestigador.setPropertyValue(vocabulario.editarPropiedadNumeroIdentificacion(), vocabulario.getModel().createTypedLiteral(parametros.get("personaNumeroIdentificacion")));

                individuoAutor = vocabulario.getModel().getIndividual(vocabulario.getNS() + "autor/" + parametros.get("aspiranteId"));
                if (individuoAutor == null) {
                    individuoAutor = vocabulario.getModel().createIndividual(vocabulario.getNS() + "autor/" + parametros.get("aspiranteId"), vocabulario.editarClaseAutor());
                }
                individuoAutor.setPropertyValue(vocabulario.editarPropiedadId(), vocabulario.getModel().createTypedLiteral(parametros.get("aspiranteId")));
                individuoAutor.setPropertyValue(vocabulario.editarPropiedadEsInvestigador(), individuoInvestigador);

                individuoAutor = vocabulario.getModel().createIndividual(vocabulario.getNS() + "autor/" + parametros.get("aspiranteId"), vocabulario.editarClaseAutor());
                individuoAutorProyecto = vocabulario.getModel().getIndividual(vocabulario.getNS() + "autorProyecto/" + parametros.get("autorProyectoId"));
                if (individuoAutorProyecto == null) {
                    individuoAutorProyecto = vocabulario.getModel().createIndividual(vocabulario.getNS() + "autorProyecto/" + parametros.get("autorProyectoId"), vocabulario.editarClaseAutorProyecto());
                }
                individuoAutorProyecto.setPropertyValue(vocabulario.editarPropiedadId(), vocabulario.getModel().createTypedLiteral(parametros.get("autorProyectoId")));
                individuoAutorProyecto.setPropertyValue(vocabulario.editarPropiedadHasProyecto(), individuoProyecto);
                individuoAutorProyecto.setPropertyValue(vocabulario.editarPropiedadHasAutor(), individuoAutor);
                File file = new File(path);
                if (!file.exists()) {
                    file.createNewFile();
                }
                PrintWriter out = new PrintWriter(file);
                out.println("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");
                vocabulario.getModel().write(out, "RDF/XML");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
