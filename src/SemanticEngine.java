/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package proyecto;

import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.util.Collections;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedHashMap;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.spans.Spans;
import org.apache.lucene.search.spans.SpanTermQuery;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.util.Version;
import edu.stanford.smi.protege.exception.OntologyLoadException;
  
   /**
    * Esta clase representa el nÃºcleo principal de la aplicaciÃ³n, y es la encargada del proceso de indexaciÃ³n y
    * bÃºsqueda dando como resultado dos ficheros de texto que contienen el contexto de los tÃ©rminos temporales,
    * uno de ellos con el contexto de tamaÃ±o tres contextThreeFile.txt, y otro con el contexto de tamaÃ±o cinco
    * contextFiveFile.txt..
    * @version 0.1, 26/06/2011.
    * @version 1.0, 14/10/2011.
    * @authors Zaid Dawood Issa.
    */

public class SemanticEngine {

     private Analyzer analyzer; // representa el analizador cuya objetivo es extraer los sÃ­mbolos fuera del texto.
     private Document doc;// representa el documento que es un conjunto de campos, y se puede imaginar como
                          //  un documento virtual.
     private Field id; //  representa el campo de nombre id y cuyo valor es el identificador del documento.
     private Field text;// representa el campo de nombre content y cuyo valor es el contenido del fichero de texto.
     private IndexReader reader;// representa el Ã­ndice de lectura, que representa el proceso de lectura sobre
                                // el Ã­ndice guardado.
     private IndexWriter writer;// representa el Ã­ndice de escritura, que representa el proceso de escritura sobre
                                // el Ã­ndice guardado.
     private IndexSearcher searcher;// representa el nÃºcleo del proceso de bÃºsqueda.
     // private int window;
     private Iterator iterator;// un tipo enumerado que se utiliza para la lectura de los tÃ©rminos temporales.
     private LinkedHashMap entries;// es una implementaciÃ³n de la interfaz Map, como tabla hash y una lista enlazada.
                                   // se usa para almacenar los tÃ©rminos temporales.
     private RAMDirectory ramDir;// representa la localizaciÃ³n del fichero de Ã­ndice.
     private SpanTermQuery termQuery;// representa la sentencia en donde se va a realizar la consulta.
     private Spans   spans;// representa el atributo que se usa para realizar la bÃºsqueda orientada a las spans que
                           // contienen los tÃ©rminos temporales.
     private Term term; // representa la unidad mÃ¡s bÃ¡sica del proceso de bÃºsqueda.
     private WindowVectorMapper windowVectorMapper; // representa la clase que determina el contexto de la palabra.
     private static String  comparisonDir; // representa el fichero de texto donde se va a guardar el resultado de comparaciÃ³n
                                           // entre el fichero semilla y el fichero de expresiones temporales.
     private static String  seedDir; // representa la ruta del fichero de semilla.
     private static String  docDir; //  representa la ruta del fichero de las expresiones temporales.
     private static String  seedElement; // representa los elementos de la semilla.
     private static String  documentElement; // representa los elementos del fichero de expresiones temporales.
     private static String [] documentElements; // representa las expresiones temporales en forma de un array de cadenas
                                                // de caracteres.
     private static String [] seedElements;  // representa los elementos de la semilla en forma de un array de cadenas 
                                             // de caracteres.
     private static ReadFromLargeFiles readFromLargeFiles; // representa la clase que proporciona acceso de
                                                           // lectura a ficheros de gran tamaÃ±o.
     private static SemanticEngine semanticEngine; // representa la clase principal de nuestra aplicaciÃ³n.
     private static OntologyExtraction  ontologyExtraction; // representa un objeto de la clase OntologyExtraction.
     private static OntologyComparison  ontologyComparison; // representa un objeto de la clase OntologyComparison.
     private static PrintWriter printWriterThree; // representa un flujo de salida del fichero contextThreeFile.txt.
     private static PrintWriter printWriterFive; // representa un flujo de salida del fichero contextFiveFile.txt.
     private static FileWriter fileWriterThree; // representa un flujo de salida del fichero contextThreeFile.txt.
     private static FileWriter fileWriterFive; // representa un flujo de salida del fichero contextFiveFile.txt.
     private static String inputSeed; // representa el fichero de entrada en donde esta guardara la ontologÃ­a temporal.

    /**
     * Representa la operaciÃ³n encargada del proceso de bÃºsqueda, es decir se empieza creando el Ã­ndice de bÃºsqueda
     * utilizando la clase de IndexWriter para pasar luego a establecer tanto el documento como los campos dentro de
     * ese documento.
     * @param  documentElements representa las expresiones temporales en forma de un array de cadenas.                                               de caracteres.
     * @param  seedElements     representa los elementos de la semilla en forma de un array de cadenas de caracteres.
     */

private void termSearchAux(String [] documentElements, String [] seedElements) throws IOException,ParseException
     {
         fileWriterThree = new FileWriter("D:/UPM/Tesis/07.Prototipos/03_Zaid/Ficheros/contextThreeFile.txt");
         printWriterThree= new PrintWriter(fileWriterThree);
         fileWriterFive = new FileWriter("D:/UPM/Tesis/07.Prototipos/03_Zaid/Ficheros/contextFiveFile.txt");
         printWriterFive= new PrintWriter(fileWriterFive);
         ramDir = new RAMDirectory();
         analyzer = new WhitespaceAnalyzer();
         // Indice de escritura.
         writer = new IndexWriter(ramDir, new StandardAnalyzer(Version.LUCENE_30,Collections.emptySet()), true,
                IndexWriter.MaxFieldLength.UNLIMITED);
         // La unidad de documento.
         doc = new Document();
         // La unidad de campo.
         id = new Field("id", "doc_" + 0, Field.Store.YES, Field.Index.NOT_ANALYZED_NO_NORMS);
         doc.add(id);
         // La unidad de texto.
         text = new Field("content", documentElements[0], Field.Store.NO, Field.Index.ANALYZED,
                  Field.TermVector.WITH_POSITIONS_OFFSETS);
         doc.add(text);
         writer.addDocument(doc);
         writer.close();
         // El indice de busqueda que se va a realizar sobre el indice de escritura.
         searcher = new IndexSearcher(ramDir);
         for (int i=0;i <seedElements.length;i++)
         {
             String stringSearchSub=seedElements[i];
             String stringSearch=stringSearchSub.substring(0,seedElements[i].length()-1).toLowerCase();
             this.termSearch(1,printWriterFive,printWriterThree,stringSearch,searcher);
         }
         //System.out.println("-------control-------------");
         for (int i=0;i <seedElements.length;i++)
         {
             String stringSearchSub=seedElements[i];
             String stringSearch=stringSearchSub.substring(0,seedElements[i].length()-1).toLowerCase();
             this.termSearch(2,printWriterFive,printWriterThree,stringSearch,searcher);
         }
  }

  

   /**
     * Es la operaciÃ³n encargada de realizar el proceso de bÃºsqueda, se empieza creando el Ã­ndice de bÃºsqueda para 
     * pasar luego a establecer el tÃ©rmino que se busca, y la construcciÃ³n de la sentencia encargada de realizar 
     * la consulta.
     * @param window parametro de tipo entero para determinar si el contexto es tres o cinco.
     * @param five   representa el flujo de salida del contexto de tres palabras.
     * @param three  representa el flujo de salida del contexto de tres palabras.
     * @param seedElement representa el termino extraido del fichero de la semilla, que se utiliza como termino de busqueda.
     * @param s representa el indice de busqueda.
     */

private void termSearch(int window,PrintWriter five, PrintWriter three,String seedElement,IndexSearcher s) throws IOException,ParseException
    {
        term=new Term("content",seedElement);
        //System.out.println("term"+term.field());
        termQuery = new SpanTermQuery(term);
        //System.out.println("SpanTermQuery"+termQuery);
        // El indice de lectura que se va a realizar sobre el texto indexado.
        reader = s.getIndexReader();
        //System.out.println("-------reader--------------");
        // La consulta del termino buscado sobre el indice de lectura.
        spans = termQuery.getSpans(reader);
        //System.out.println("-------spans-------------------");
        windowVectorMapper = new WindowVectorMapper();
        switch(window)
        {
            case 1:
                while (spans.next() == true)
                {
                    three.println("---------ContextoTress--------------");
                    windowVectorMapper.start = spans.start() - window;
                    windowVectorMapper.end = spans.end() + window;
                    reader.getTermFreqVector(spans.doc(), "content", windowVectorMapper); // from here jump to the map.
                    three.println("la palabra buscada es"+seedElement);
                    three.println("-----------------------------");
                    three.println("Los terminos son");
                    entries=windowVectorMapper.keyOrder(windowVectorMapper.entries);
                    iterator =entries.values().iterator();
                    while(iterator.hasNext())
                    {
                        three.println(iterator.next());
                    }
                    three.println("-----------------------------");
                    windowVectorMapper.entries.clear();
                }//while1
            case 2:
                while (spans.next() == true)
                {
                    five.println("-----------ContextoCincoo-----------------");
                    windowVectorMapper.start = spans.start() - window;
                    windowVectorMapper.end = spans.end() + window;
                    reader.getTermFreqVector(spans.doc(), "content", windowVectorMapper); // from here jump to the map.
                    five.println("la palabra buscada es"+seedElement);
                    five.println("-----------------------------");
                    five.println("Los terminos son");
                    entries=windowVectorMapper.keyOrder(windowVectorMapper.entries);
                    iterator =entries.values().iterator();
                    while(iterator.hasNext())
                    {
                        five.println(iterator.next());
                    }
                    five.println("-----------------------------");
                    windowVectorMapper.entries.clear();
                }//while2
        }


}

  
 /**
     * Es la operaciÃ³n encargada de realizar el proceso de bÃºsqueda, se empieza creando el Ã­ndice de bÃºsqueda para 
     * pasar luego a establecer el tÃ©rmino que se busca, y la construcciÃ³n de la sentencia encargada de realizar 
     * la consulta.
  */
public static void main(String[] args) throws IOException,ParseException,OntologyLoadException
 {
     NumberFormat numberFormat=NumberFormat.getInstance();
     Calendar rightNow = Calendar.getInstance();
     long timeNow=rightNow.getTimeInMillis();
     readFromLargeFiles = new ReadFromLargeFiles();
     semanticEngine=new SemanticEngine();
     ontologyExtraction=new OntologyExtraction();
     ontologyComparison=new OntologyComparison();
     docDir  = "D:/UPM/Tesis/07.Prototipos/03_Zaid/Ficheros/time6.txt";
     File file= new File(docDir);
     int  size=(int)(file.length()/1024);
     inputSeed = "D:/UPM/Tesis/07.Prototipos/03_Zaid/Ficheros/time.owl";
     seedDir=ontologyExtraction.extraction(inputSeed);
     comparisonDir=ontologyComparison.comparison(seedDir,docDir);
     seedElement=readFromLargeFiles.ReadFromLargeFiles(seedDir);
     documentElement=readFromLargeFiles.ReadFromLargeFiles(docDir);
     documentElements= documentElement.split("line.separator");
     seedElements= seedElement.split("\n");     
     semanticEngine.termSearchAux(documentElements,seedElements);
     Calendar rightAfter = Calendar.getInstance();
     long timeAfter=rightAfter.getTimeInMillis();
     long mili=timeAfter-timeNow;    
     if (size < 1000)
     {
         int seconds=(int)(mili/1000);
         System.out.println("total time: "+seconds+ "  seconds");
     }
     else
     {
         int seconds=(int)(mili%60000);
         int minutes=(int)(mili/60000);
         System.out.println("total time: "+minutes+" minutes"
                 + seconds/1000 + " seconds");
     }


}
}
