/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package proyecto;


import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.lucene.index.TermVectorMapper;
import org.apache.lucene.index.TermVectorOffsetInfo;


   /**
    * Esta clase representa la operaciÃ³n que determina el tamaÃ±o de la ventana alrededor de la palabra es decir
    * cuantas palabras alrededor de la palabra van a representar el contexto en que estÃ¡ ubicada la palabra.
    * @version 0.1, 26/06/2011.
    * @version 1.0, 14/10/2011.
    * @authors Zaid Dawood Issa.
    */

public class WindowVectorMapper extends TermVectorMapper{
public int start; // representa el inicio de la ventana.
public int end;   // representa el fin de la ventana.
public LinkedHashMap <Integer,String> entries = 
        new LinkedHashMap <Integer,String> (); // representa la estructura de datos donde se va a guardar el contexto
                                               // de la palabra sin ordenar. Ya que la ordenaciÃ³n es tarea de la
                                               // siguiente clase SemanticEngine.

    /*  Es una Sobrecarga del mÃ©todo map de TermVectorMapper.
     *  Este mÃ©todo representa la operaciÃ³n que se aplica sobre la estructura de datos entries para almacenar
     *  el contexto de la palabra. Es decir guarda la informacion del term vector dentro de la estructura de datos.
     *  @param term  el termino que se aÃ±ade al vector.
     *  @param frequency la frecuencia del termino en el documento.
     *  @param offsets es nulo si no se especifica el offset, en otro caso se utiliza el offset dentro del campo
     *  del termino.
     *  @param positions es nulo si no se especifica el offset, en otro caso se utiliza el positions dentro del campo
     *  del termino.
     */
public void map(String term, int frequency, TermVectorOffsetInfo[] offsets, int[] positions) {

    for (int i = 0; i < positions.length; i++) {
       if (positions[i] >= start && positions[i] < end) {
          entries.put(positions[i],term);
       }
    }
  }

    /**
     *  Es una Sobrecarga del metodo map de setExpectations.En esta clase no se utiliza el mÃ©todo.
     *  Avisa al mapper de lo que pueda esperar en lo que respecta al campo, el numero de terminos, de desplazamiento
     *  y posiciones de almacenamiento. Este mÃ©todo serÃ¡ llamado una vez antes de recuperar el vector del campo.
     *  @param  field      representa el campo del vector.
     *  @param  numTerms   representa el numero de terminos que se necesitan para ser mapeados.
     *  @param  storeOffsets   verdadero si el mapper espera la informaciÃ³n de offset.
     *  @param  storePositions verdadero si el mapper espera la informacion de las posiciones.
     */
public void setExpectations(String field, int numTerms, boolean storeOffsets, boolean storePositions) {
    
  }

    /**
     * Este mÃ©todo que se aplica sobre la estructura de datos entries dando como resultado una estructura de datos
     * ordenada.
     * @param  map representa la estructura de datos sin ordenador.
     * @return devuelve LinkedHashMap<K, V> una estructura de datos ordenada.
     */
public <K,V> LinkedHashMap<K, V> keyOrder(final Map<K, V> map) {

    // Desarollar un comparador para comparar entre los valores de una Map.
    Comparator <Map.Entry<K,V>> comparador = new Comparator<Map.Entry<K,V>>()
    {
      // Sobrecarga del metodo compare dentro de Comparator
       public int compare(Map.Entry <K,V> k1,Map.Entry <K,V> k2)
      {
          return ((Comparable)k1.getKey()).compareTo(k2.getKey());
      }
    };
    // Hacer una conversion desde Map a una lista.
    List <Map.Entry <K,V>> mapEntries = new LinkedList <Map.Entry <K,V>> (map.entrySet());
    // Ordenadr la nueva lista
    Collections.sort(mapEntries,comparador);
    // Construir un nuevo LinkedHasMap que contiene la nueva Lista
    LinkedHashMap<K,V> result = new LinkedHashMap <K,V>();
    for(Map.Entry<K,V> entry:mapEntries)
    {
        result.put(entry.getKey(),entry.getValue());
    }
    return result;
}
}
