package Ajedrez;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class MasterControl
{
    static String url ="D:\\Cosas Utiles\\Maestria\\Arquitectura de software\\Programas";
    static ArrayList <String> indexWords = new ArrayList<>();
    static String word;
    public static void main(String[] args) {

        input();
        searchWord(url);
        output();


    }
    private static void input(){

        Scanner lector = new Scanner(System.in);
        System.out.println("Ingresa la Palabra clave");
        word = lector.nextLine();


    }
    private static void searchWord(String dir){
        File carpeta = new File(dir);
        File[] rawlistado = carpeta.listFiles();

            for (File file : rawlistado) {
                if(file.isDirectory()){
                    searchWord(file.getPath());
                    }
                if(file.isFile()){
                    if(file.getName().contains(word)) {
                        indexWords.add(file.getPath());
                        
                    }
                }

            }

    }
    private static void output(){
        for (String archivo:indexWords) {
            System.out.println(archivo);
        }
    }


}
