/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package  proyecto;
import java.util.*;
import java.io.*;
import java.lang.Exception.*;

   /**
    * Esta clase representa la operaciÃ³n de comparaciÃ³n del fichero de semilla seed.txt con el fichero
    * que contiene las expresiones temporales time.txt dando como resultado los tÃ©rminos temporales que
    * coinciden en ambos ficheros.
    * @version 0.1, 26/06/2011.
    * @version 1.0, 14/10/2011.
    * @authors Zaid Dawood Issa.
    */




public class OntologyComparison {

    private FileReader temporalExpressions; // representa el flujo de entrada del fichero de las expresiones temporales.
    private FileReader seed; // representa el flujo de entrada del fichero la semilla.
    private String fileComparison;// representa el fichero de salida como resultado de la comparaciÃ³n.
    private PrintWriter outputComparison; // representa el flujo de salida del fichero de comparaciÃ³n.
    private StreamTokenizer streamTemporalExpressions;// representa el flujo de entrada para el fichero
                                                      // de expresiones temporales orientado hacia los sÃ­mbolos,  es decir coge un fichero de texto y usa un Parser para trasformar este fichero en sÃ­mbolos, para luego realizar el tratamiento adecuado sobre estos sÃ­mbolos.
    private StreamTokenizer streamSeed; // representa el flujo de entrada para el fichero de la semilla
                                        // orientado hacia los sÃ­mbolos.
    private ArrayList listStreamTemporalExpressions; // representa una lista para el fichero de  expresiones temporales.
    private ArrayList listStreamSeed; // representa una lista para el fichero de semilla.
    private ArrayList listFinal; // representa una lista para el fichero de comparaciÃ³n.



     /**
     * Permite la generaciÃ³n de un fichero de texto como resultado de la comparaciÃ³n entre dos ficheros
     * uno de ellos es el fichero de texto de la semilla, y el otro fichero es el fichero de las
     * expresiones temporales.
     * @param  inputSeed    representa el fichero de entrada en donde esta guardara la ontologÃ­a temporal.
     * @param  temporalFile representa el fichero de entrada de las expresiones temporales.
     * @return devuelve  un fichero de salida como resultado de la comparaciÃ³n.
     */
    public String comparison(String inputSeed, String temporalFile) {
            try{
                      fileComparison ="D:/UPM/Tesis/07.Prototipos/03_Zaid/Ficheros/comparison.txt";
                      temporalExpressions = new FileReader(temporalFile);
                      seed = new FileReader(inputSeed);
                      outputComparison= new PrintWriter(fileComparison);
                      streamTemporalExpressions = new StreamTokenizer(temporalExpressions);
                      streamSeed = new StreamTokenizer(seed);
                      listStreamTemporalExpressions = new ArrayList();
                      listStreamSeed = new ArrayList();
                      listFinal = new ArrayList();
                      while(streamTemporalExpressions.nextToken()!= StreamTokenizer.TT_EOF){
                         if(streamTemporalExpressions.ttype == StreamTokenizer.TT_WORD){
                            if (streamTemporalExpressions.sval.endsWith("."))
                             {
                                String sin=streamTemporalExpressions.sval.replaceAll("\\.","");
                                listStreamTemporalExpressions.add(sin);
                             }
                             else  listStreamTemporalExpressions.add(streamTemporalExpressions.sval);
                         }
                      }

                      while(streamSeed.nextToken() != StreamTokenizer.TT_EOF){
                         if(streamSeed.ttype == StreamTokenizer.TT_WORD){
                             listStreamSeed.add(streamSeed.sval);
                         }
                      }

                       //  Este doble bucle aÃ±ade una carga de 10 segundos.
                       //  se ha cambiado la estrategia de diseÃ±o en vez de utilizar
                       //  la comparacion entre fichero y array se ha utilizado la comparacion entre
                       //  dos arrays uno con las palabras del fichero de texto y el otro con los terminos.
                       //  se ha cambiado uno de los bucles for por un bucle while poniendo una condicion
                       //  de terminacion para evitar las comprobaciones inncesarias. y se ha quitado
                       //  el ultimo bucle for el que hace las comprobaciones de las repticiones.
                       for (int i=0; i < listStreamSeed.size(); i++)
                       {    int j=0;
                            String tiempo = (String)listStreamTemporalExpressions.get((j)).toString().toLowerCase();
                            String semilla= (String)listStreamSeed.get((i)).toString().toLowerCase();
                            if (tiempo.equals(semilla))
                            {
                                     listFinal.add(tiempo);
                                     outputComparison.println(tiempo);
                            }
                            while(j < listStreamTemporalExpressions.size() && !(tiempo.equals(semilla)))
                          {
                            tiempo = (String)listStreamTemporalExpressions.get((j)).toString().toLowerCase();
                            semilla= (String)listStreamSeed.get((i)).toString().toLowerCase();
                            if (tiempo.equals(semilla))
                            {
                                listFinal.add(tiempo);
                                outputComparison.println(tiempo);
                            }
                            j++;
                          }
                       }
                     outputComparison.close();


         } catch (IOException oe){
		oe.printStackTrace();
	         }
           catch (Exception o){
                o.printStackTrace();
                 }
    return fileComparison;
    }

}

