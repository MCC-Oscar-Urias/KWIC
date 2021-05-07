package com.company;


import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;
import java.io.*;
import java.util.*;

public class Main extends PDFTextStripper{

    private ArrayList<HashSet<String>> lista;   //lista de palabras
    private HashSet<String> parsedText;         //iterador
    private HashSet<String> palabras;           //palabras
    private ArrayList<String> listaPalabrasC;   //lista de palabras clave
    private ArrayList<String> listaPalabrasCR;   //lista de palabras clave reales
    private ArrayList<String> indicePalabras;   //indice de palabras
    private int numeroPaginas;                  //numer de pagina
    private ArrayList<String> palabraSalida;    //palabras de salida al output medium
    public Main() throws IOException {

    }

    //Metodo que ingresa un pdf con palabras y un txt con palabras Clave
    //este metodo lee las palabras del pdf y las guarda por hoja, ademas lee las palabras del txt
    //y las guarda

    public void input(String pdf, String clave){
        lista=new ArrayList<>();
        PDDocument pdDocument = null;
        try {
            pdDocument = PDDocument.load(new File(pdf));
            numeroPaginas=pdDocument.getNumberOfPages();
            for(int i=0; i<numeroPaginas;i++){
                parsedText = new HashSet<>();
                setStartPage(i+1);
                setEndPage(i+1);
                Writer dummyWriter = new OutputStreamWriter(new ByteArrayOutputStream());
                writeText(pdDocument, dummyWriter);
                iterador();
                lista.add(palabras);
            }

            listaPalabrasC = new ArrayList<>();
            listaPalabrasCR = new ArrayList<>();

            Scanner Lector = new Scanner(new File(clave));
            while (Lector.hasNext()){
                listaPalabrasC.add(Lector.next().toLowerCase());
            }
            Scanner m = new Scanner(new File(clave));
            while (m.hasNext()){
                listaPalabrasCR.add(m.next());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //se sobreescribe el metodo WriteString para que funcione bien (tenia un error en la libreria)
    @Override
    protected void writeString(String text, List<TextPosition> textPositions) throws IOException {
        parsedText.add(text);
    }


    //metodo iterador que agarra un String y las separa por palabras
    private void iterador(){
        String[] cadena;
        String palabra="";
        Iterator value =parsedText.iterator();
        cadena = new String[parsedText.size()];
        while (value.hasNext()) {
            palabra=palabra + (String) value.next();
        }
        cadena=palabra.split(" ");
        palabras = new HashSet<>();
        for(int i=0; i< cadena.length; i++){
            palabras.add(cadena[i].toLowerCase());
        }
    }

    //metodo para buscar palabras
    //recorre todas las palabras y va guardando las que sean palabra clave
    public void searchWord(){
        indicePalabras = new ArrayList<>();
        palabraSalida = new ArrayList<>();
        for(int i=0;i<numeroPaginas;i++){
            for(int j=0; j<listaPalabrasC.size();j++){
                if(lista.get(i).contains(listaPalabrasC.get(j))){
                    if(indicePalabras.contains(listaPalabrasCR.get(j))){
                        indicePalabras.remove(listaPalabrasCR.get(j));
                    }
                    indicePalabras.add(listaPalabrasCR.get((j)));
                }
            }
        }

    }

    //Metodo que se encarga de escribir las palabras clave
    public void output(FileWriter escritor) throws IOException {
        for(int i=0;i<palabraSalida.size();i++) {
            escritor.write(palabraSalida.get(i));
            escritor.write('\n');
        }
        escritor.flush();
    }

    //Metodo que alfabetiza el indice de palabras que son clave
    public void alfabetizador(){
        for(int j=0; j<indicePalabras.size();j++){
            String palSalida ="";
            for(int i=0;i<numeroPaginas;i++){
                if(lista.get(i).contains(indicePalabras.get(j).toLowerCase())){
                    palSalida+=" "+(i+1);
                }
            }
            palabraSalida.add(indicePalabras.get(j)+palSalida);
        }
        Collections.sort(palabraSalida,java.text.Collator.getInstance());
    }


    public static void main(String[] args) throws IOException {
        Main main =new Main();
        main.input("C:\\Users\\felix\\Downloads\\ave fenix.pdf","C:\\Users\\felix\\Downloads\\WordKeys.txt");
        main.searchWord();
        main.alfabetizador();
        main.output(new FileWriter(new File("outputMedium.txt")));
    }
}