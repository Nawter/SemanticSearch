/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package proyecto;
import edu.stanford.smi.protegex.owl.ProtegeOWL;
import edu.stanford.smi.protegex.owl.model.*;
import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;
import edu.stanford.smi.protege.exception.OntologyLoadException;
import java.util.*;
import java.io.*;
import java.lang.Exception.*;

   /**
    * Esta clase representa la operaciÃ³n de extracciÃ³n de la ontologÃ­a temporal desde
    * un fichero OWL time.owl hasta un fichero de texto seed.txt.
    * @version 0.1, 26/06/2011.
    * @version 1.0, 14/10/2011.
    * @authors Zaid Dawood Issa.
    */


public class OntologyExtraction {
    private FileWriter   seed; // representa el primer flujo de salida del fichero de la semilla.
    private PrintWriter  outputSeed; // representa el segundo flujo de salida del fichero de la semilla.
    private String       fileSeed; // representa el fichero en donde se almacenan los tÃ©rminos temporales extraÃ­dos del fichero que
                                   // contiene la ontologÃ­a temporal.
    private JenaOWLModel jenaModel; //   representa el modelo de Jena.
    private Collection   jenaClasses; // representa las clases del modelo de Jena.
    private Collection   jenaDataTypeProperties; // representa las propiedades de los tipos de datos de Jena.
    private Collection   jenaObjectProperties; //   representa las propiedades de los objetos de Jena.


    /** 
     * Permite la generaciÃ³n del fichero de texto de la semilla a partir de un fichero que contiene
     * la ontologÃ­a temporal escrita en OWL.
     * @param  inputSeed representa el fichero de entrada en donde esta guardara la ontologÃ­a temporal.
     * @return devuelve  representa el fichero en donde se almacenan los tÃ©rminos temporales extraÃ­dos del
     * fichero que contiene la ontologÃ­a temporal.
     */
    public String extraction(String inputSeed)throws  IOException,OntologyLoadException {

      try{
                    fileSeed= "D:/UPM/Tesis/07.Prototipos/03_Zaid/Ficheros/seed.txt";
                    seed= new FileWriter(fileSeed);
                    outputSeed= new PrintWriter(seed);
                    jenaModel = ProtegeOWL.createJenaOWLModelFromURI(inputSeed);
                    jenaClasses = jenaModel.getUserDefinedOWLNamedClasses();
                    jenaDataTypeProperties = jenaModel.getUserDefinedOWLDatatypeProperties();
                    jenaObjectProperties = jenaModel.getUserDefinedOWLObjectProperties();
                    for (Iterator it = jenaClasses.iterator(); it.hasNext();) {
                        OWLNamedClass cls = (OWLNamedClass) it.next();
                        Collection instances = cls.getInstances(false);
                        outputSeed.println(cls.getBrowserText());
                        for (Iterator jt = instances.iterator(); jt.hasNext();) {
                            OWLIndividual individual = (OWLIndividual) jt.next();
                            outputSeed.println(individual.getBrowserText());
                       }
                 }
                    for (Iterator it = jenaDataTypeProperties.iterator(); it.hasNext();) {
                        OWLDatatypeProperty dTP = (OWLDatatypeProperty) it.next();
                        outputSeed.println(dTP.getBrowserText());
                     }
                     for (Iterator it = jenaObjectProperties.iterator(); it.hasNext();) {
                        OWLObjectProperty oP = (OWLObjectProperty) it.next();
                        outputSeed.println(oP.getBrowserText());
                     }
                      outputSeed.close();
         } catch (IOException oe){
		oe.printStackTrace();
	         }
           catch (Exception o){
                o.printStackTrace();
                 }
    return fileSeed;
    }

}





