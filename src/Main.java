import manejadores.MioObjetos;
import modelos.AnimalDeCompanya;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException, ParserConfigurationException, TransformerException, SAXException {
        ArrayList<AnimalDeCompanya>animalDeCompanyas=new ArrayList<>();
        int opcion;
        do{
            opcion=menu();
            switch (opcion){
                case 1:
                    crearAnimal();
                    break;
                case 2:
                    escribirBinario(animalDeCompanyas);
                    break;
                case 3:
                    cargarFichero(animalDeCompanyas);
                    break;
                case 4 :
                    escribirXML(animalDeCompanyas);
                    break;
                case 5:
                    cargaFicheroXML(animalDeCompanyas);
                    break;
                case 6:
                  for  (AnimalDeCompanya a: animalDeCompanyas){
                      System.out.println(a.toString());
                }
                  break;
                case 7:
                    System.out.println("Cuida a tus amimales");
                    break;
            }
        }while (opcion!=7);


    }

    private static int menu() {

        Scanner scanner = new Scanner(System.in);
        int opcion;
        do {
            System.out.println("1.Crear Animal");
            System.out.println("2.Escribir lista en  fichero binario");
            System.out.println("3.Cargar de fichero Binario");
            System.out.println("4.Escribir en fichero XML");
            System.out.println("5.Leer de Fichero XML");
            System.out.println("6.Mostrar lista");
            System.out.println("7.Salir");

                opcion = scanner.nextInt();


        } while (opcion < 1 || opcion > 7);
        return opcion;
    }

    private static AnimalDeCompanya crearAnimal() {
        Scanner scanner = new Scanner(System.in);
        AnimalDeCompanya animalDeCompanya = new AnimalDeCompanya();
        System.out.println("Dime la especie");
        animalDeCompanya.setEspecie(scanner.nextLine());
        System.out.println("Dime la raza");
        animalDeCompanya.setRaza(scanner.nextLine());
        System.out.println("dime color");
        System.out.println("Dime la edad");
        return animalDeCompanya;
    }

    private static void cargarFichero(ArrayList<AnimalDeCompanya> animales) throws IOException {

        File ficheroBinario = new File("fichero.dat");
        FileInputStream fis = new FileInputStream(ficheroBinario);
        ObjectInputStream ois = new ObjectInputStream(fis);
        try {
            while (true) {

                AnimalDeCompanya animalDeCompanya = (AnimalDeCompanya) ois.readObject();
            }
        } catch (ClassNotFoundException e) {
        }

    }

    private static void escribirBinario(ArrayList<AnimalDeCompanya> animalDeCompanyas) throws IOException {
        File file = new File("fichero.dat");
        boolean existe = file.exists();

        ObjectOutputStream oos;

        oos = existe ? new MioObjetos(new FileOutputStream(file, true)) : new ObjectOutputStream(new FileOutputStream(file));
        for(AnimalDeCompanya a: animalDeCompanyas){
            oos.writeObject(a);
        }
        oos.close();
    }

    private static void cargaFicheroXML(ArrayList<AnimalDeCompanya>animalDeCompanyas) throws ParserConfigurationException, IOException, SAXException {
        File file= new File("fichero.xml");

        DocumentBuilderFactory documentBuilderFactory=DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder= documentBuilderFactory.newDocumentBuilder();
        Document document= documentBuilder.parse(file);

        document.getDocumentElement().normalize();

        NodeList elementos= document.getElementsByTagName("Animal");

        for (int i = 0; i < elementos.getLength(); i++) {
            AnimalDeCompanya animalDeCompanya= new AnimalDeCompanya();
            Node node=elementos.item(i);

            if(node.getNodeType() == Node.ELEMENT_NODE){
                Element element= (Element) node;

              animalDeCompanya.setEspecie( element.getElementsByTagName("especie").item(0).getTextContent());
                animalDeCompanya.setRaza( element.getElementsByTagName("raza").item(0).getTextContent());
                animalDeCompanya.setEdad( Integer.parseInt(element.getElementsByTagName("edad").item(0).getTextContent()));
                animalDeCompanya.setColor( element.getElementsByTagName("color").item(0).getTextContent());
            }
           animalDeCompanyas.add(animalDeCompanya);

        }

}
     private static  void escribirXML(ArrayList<AnimalDeCompanya>animalDeCompanyas) throws ParserConfigurationException, IOException, SAXException, TransformerException {
    File file= new File("fichero.xml");

    DocumentBuilderFactory documentBuilderFactory=DocumentBuilderFactory.newInstance();
    DocumentBuilder documentBuilder= documentBuilderFactory.newDocumentBuilder();
    Document document=documentBuilder.newDocument();
    //Document document= documentBuilder.parse(file);

         Element raiz=document.createElement("animales");
         document.appendChild(raiz);

         for (AnimalDeCompanya a: animalDeCompanyas) {
             // creo atributo
             Element element=document.createElement("animal");
             Element especieE=document.createElement("especie");
             especieE.setTextContent(a.getEspecie());

             Element razaE= document.createElement("raza");
             razaE.setTextContent(a.getRaza());
             Element edadE= document.createElement("edad");
             edadE.setTextContent(String.valueOf(a.getEdad()));
             Element colorE =document.createElement("color");
             colorE.setTextContent(a.getColor());

             //inserto los atributos
             element.appendChild(especieE);
             element.appendChild(razaE);
             element.appendChild(edadE);
             element.appendChild(colorE);

             raiz.appendChild(element);
         }
         TransformerFactory transformerFactory=TransformerFactory.newInstance();
         Transformer optimus=transformerFactory.newTransformer();
         StreamResult archivo=new StreamResult(new File("animales.xml"));
         optimus.setOutputProperty(OutputKeys.INDENT,"YES");
         DOMSource ds = new DOMSource(document);
         optimus.transform(ds,archivo);


}
}

