package edu.unl.sigett;

import com.hp.hpl.jena.sparql.vocabulary.FOAF;
import edu.unl.sigett.lud.vocabulay.Vocabulario;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
       try {
            Vocabulario lUDSigett = new Vocabulario();
            lUDSigett.getModel().setNsPrefix("foaf", FOAF.NS);
            lUDSigett.getModel().setNsPrefix("sigett", lUDSigett.getNS());
            lUDSigett.crearProyectoOnt();
            lUDSigett.crearAutorOnt();
            lUDSigett.crearAutorProyectoOnt();
            lUDSigett.crearDocenteOnt();
            lUDSigett.crearDirectorProyectoOnt();
            lUDSigett.crearAreaOnt();
            lUDSigett.crearNivelAcademicoOnt();
            lUDSigett.crearCarreraOnt();
            lUDSigett.crearLineaInvestigacionOnt();
            lUDSigett.crearLineaInvestigacionProyectoOnt();
            lUDSigett.crearPeriodoAcademicoOnt();
            lUDSigett.crearOfertaAcademicaOnt();
            lUDSigett.crearProyectoCarreraOfertaOnt();

            lUDSigett.crearPropiedadHasAutor();
            lUDSigett.crearPropiedadHasDocente();
            lUDSigett.crearPropiedadHasProyecto();
            lUDSigett.crearPropiedadId();
            lUDSigett.crearPropiedadHasCarrera();
            lUDSigett.crearPropiedadSigla();
            lUDSigett.crearPropiedadEsParteDeArea();
            lUDSigett.crearPropiedadEsParteDeNivelAcademico();
            lUDSigett.crearPropiedadEsParteDePeriodoAcademico();
            lUDSigett.crearPropiedadFechaFinAcademico();
            lUDSigett.crearPropiedadFechaInicioAcademico();
            lUDSigett.crearPropiedadFechaPublicacion();
            lUDSigett.crearPropiedadHasLineaInvestigacion();
            lUDSigett.crearPropiedadHasOfertaAcademica();
            File file = new File("D://sigett.owl");
            if (!file.exists()) {
                file.createNewFile();
            }
            PrintWriter out = new PrintWriter(file);
            out.println("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");
            lUDSigett.getModel().write(out, "RDF/XML");
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
