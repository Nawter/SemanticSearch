/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package proyecto;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;



   /**
    * Esta clase representa la operaciÃ³n de lectura de datos desde ficheros de gran tamaÃ±o,
    * recibiendo un fichero de texto, y utilizando una clase de flujo de entrada junto con una clase de
    * StringBuffer que es una clase donde se puede manejar las cadenas de caracteres y no como la clase
    * String en donde los caracteres son inmutables.
    * @version 0.1, 26/06/2011.
    * @version 1.0, 14/10/2011.
    * @authors Zaid Dawood Issa.
    */

public class ReadFromLargeFiles {
private BufferedReader bufferedReader=null;
private FileReader fileReader=null;
private String line=null;
private String ls=null;
private StringBuffer stringBuffer=null;



    /**
     * Lectura de ficheros de datos desde ficheros de gran tamaÃ±o.
     * @param  inputSeed representa el fichero de entrada
     * @return devuelve  una StringBuffer que alberga el contendio separado por lineas.
     */
public String ReadFromLargeFiles(String string) throws FileNotFoundException,IOException {
     fileReader=new FileReader(string);
     // String es inmutable,StringBuffer es un buffer
     // donde se puede manejar los strings.
     stringBuffer= new StringBuffer();
     bufferedReader = new BufferedReader(fileReader);
     // separador de linea /n.
     ls = System.getProperty("line.separator");
     while( ( line = bufferedReader.readLine() ) != null ) {
        stringBuffer.append( line );
        stringBuffer.append( ls );
    }
        return stringBuffer.toString();
    }
}
