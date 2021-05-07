package com.company;
import java.io.IOException;
import java.io.Reader;
import java.io.*;
import java.util.Arrays;

public class MasterControl {

    private char[] caracteres = new char[1000];
    private int contadorCaracteres = 0;
    private int[] numeroRenglon = new int[1000];
    private int[][] indicePalabras;
    private int contadorPalabras = 0;
    private int[][] indiceAlfabetico;


    /** MasterControl (constructor) -> es el contructor de nuestra clase
     * Este se encarga de rellenar con -1 mi array de caracteres
     * y de numero de renglon
     */

    public MasterControl() {
        Arrays.fill(numeroRenglon, -1);
        Arrays.fill(caracteres, (char) -1);
    }

    /** INPUT->
     * lee el "inputMediunm" (archivo txt)
     * cuenta los numero de renglones y los guarda
     * cuenta los caracteres que hay y los guarda
     */

    public void input(Reader archivo) throws IOException {
        int entradaCaracter;
        int iteradorLinea = 0;
        boolean nuevaLinea = true;
        while ((entradaCaracter = archivo.read()) != -1) {
            if (nuevaLinea) {
                numeroRenglon[iteradorLinea++] = contadorCaracteres;
                nuevaLinea = false;
            }
            if (entradaCaracter == '\n') {
                nuevaLinea = true;
                entradaCaracter = ' ';
            }
            caracteres[contadorCaracteres++] = (char) entradaCaracter;
        }
    }

    /** CIRCULAR SHIFT->
     * cuenta las palabras de cada renglon y guarda su primera letra
     */

    public void circularShift() {
        indicePalabras = new int[numeroRenglon.length][30];
        int iteradorRenglon = 0;
        while (iteradorRenglon + 1 < numeroRenglon.length && numeroRenglon[iteradorRenglon] != -1) {
            int inicioLinea = numeroRenglon[iteradorRenglon];
            int finalLinea;
            if(numeroRenglon[iteradorRenglon + 1] != -1){
                finalLinea = numeroRenglon[iteradorRenglon + 1];
            }
            else{
                finalLinea = contadorCaracteres;
            }
            Arrays.fill(indicePalabras[iteradorRenglon], -1);
            int iteradorIndice = 0;
            boolean inicioPalabra = true;
            for (int iteradorCaracter = inicioLinea; iteradorCaracter < finalLinea; ++iteradorCaracter) {
                if (caracteres[iteradorCaracter] == ' ') {
                    inicioPalabra = true;
                    continue;
                }
                if (inicioPalabra) {
                    indicePalabras[iteradorRenglon][iteradorIndice++] = iteradorCaracter;
                    ++contadorPalabras;
                    inicioPalabra = false;
                }

            }
            ++iteradorRenglon;
        }
    }

    /** ALPHABETIZER->
     * compara la primera letra de cada palabra y las
     * ordena de mayor a menor
     */

    public void alphabetizer() {
        indiceAlfabetico = new int[contadorPalabras][3];
        int indice = 0;
        for (int linea = 0; linea < numeroRenglon.length && numeroRenglon[linea] != -1; ++linea) {
            for (int palabra = 0; palabra < indicePalabras[linea].length && indicePalabras[linea][palabra] != -1; ++palabra, ++indice) {
                indiceAlfabetico[indice][0] = linea;
                indiceAlfabetico[indice][1] = palabra;
                indiceAlfabetico[indice][2] = indicePalabras[linea][palabra];
            }
        }
        for (int indiceCiclo = 0; indiceCiclo < indiceAlfabetico.length - 1; ++indiceCiclo) {
            for (indice = 0; indice < indiceAlfabetico.length - indiceCiclo - 1; ++indice) {
                int indicePalabra = indicePalabras[indiceAlfabetico[indice][0]][indiceAlfabetico[indice][1]];
                int indiceSiguientePalabra = indicePalabras[indiceAlfabetico[indice + 1][0]][indiceAlfabetico[indice + 1][1]];
                char caracterPalabra = (char) -1;
                char caracterPalabraSiguiente = (char) -1;
                for (; caracterPalabra == caracterPalabraSiguiente; indicePalabra+=2, indiceSiguientePalabra+=2){
                    caracterPalabra = caracteres[indicePalabra];
                    caracterPalabraSiguiente = caracteres[indiceSiguientePalabra];
                }
                if (caracterPalabra > caracterPalabraSiguiente) {
                    int[] temporal = indiceAlfabetico[indice];
                    indiceAlfabetico[indice] = indiceAlfabetico[indice + 1];
                    indiceAlfabetico[indice + 1] = temporal;
                }
            }
        }
    }

    /** OUTPUT->
     * escribe el documento de salida "outputMedimun" (archivo txt)
     * escribe la primera palabra en orden alfabetico y despues escribe el resto de la oracion
     */

    public void output(Writer escritor) throws IOException {
        for (int indice = 0; indice < indiceAlfabetico.length; ++indice) {
            int inicioPalabra = indiceAlfabetico[indice][2];
            int inicioLinea = numeroRenglon[indiceAlfabetico[indice][0]];
            int finalLinea = numeroRenglon[indiceAlfabetico[indice][0] + 1] != -1 ? numeroRenglon[indiceAlfabetico[indice][0] + 1] : contadorCaracteres;
            for (int indiceCaracter = inicioPalabra; indiceCaracter < finalLinea; ++indiceCaracter) {
                escritor.write(caracteres[indiceCaracter]);
            }
            for (int indiceCaracter = inicioLinea; indiceCaracter < inicioPalabra; ++indiceCaracter) {
                escritor.write(caracteres[indiceCaracter]);
            }
            escritor.write('\n');
        }
        escritor.flush();
    }

    /** Master Control->
     * Es el programa principal
     * aqui se corren todos los subprogramas
     */

    public static void main(String[] args) {
        try {
            MasterControl kwic = new MasterControl();
            kwic.input(new FileReader(new File("inputMedium.txt")));
            kwic.circularShift();
            kwic.alphabetizer();
            kwic.output(new FileWriter(new File("outputMedium.txt")));
        } catch (IOException e) {
            System.out.println("no hay archivo");
        }
    }

}
