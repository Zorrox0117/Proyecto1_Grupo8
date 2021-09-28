package Sockets;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;


import BoardGeneration.ListGeneration;
import DoubleLinked.DoubleNode;
import DoubleLinked.DoublyLinkedList;
import GUI.VentanaJuego;
import GUI.VentanaPrincipal;
import GUI.VentanaReto;

/**
 * Esta clase maneja la generación del servidor
 */
public class Servidor implements Runnable {

    ServerSocket servidor = null;
    Socket socket = null;

    DataOutputStream output;
    DataInputStream input;

    //PORT de nuestro servidor
    int PORT = 5000;
    public static String Nombrejugador1 = VentanaPrincipal.Nombre;
    public static String Nombrejugador2;
    public static int varPos1;
    public static int varPos2;
    public static String ActOper;
    public static int res;
    private String respuesta;

    /**
     * Este método corre el servidor
     */
    public void run() {
        //Creamos el socket del servidor
        try {
            this.servidor = new ServerSocket(PORT);
            System.out.println("Servidor iniciado");

            //genero la lista para el tablero
            ListGeneration list = new ListGeneration();
            DoublyLinkedList board = list.random();
            DoubleNode node = new DoubleNode(null, "Reto", "+", 50, 50, 0, null);
            //Siempre estara escuchando peticiones
            while (true) {
                //Espero a que un cliente se conecte
                socket = servidor.accept();
                System.out.println("Cliente conectado");
                //Genero los canales de  entrada y salida
                input = new DataInputStream(socket.getInputStream());
                output = new DataOutputStream(socket.getOutputStream());

                //Leo el mensaje que me envia el cliente
                Nombrejugador2 = input.readUTF();
                System.out.println(Nombrejugador2);

                //Le envio un mensaje
                output.writeUTF(Nombrejugador1);

                // output.writeUTF(Json.generateString(Json.toJson(board), false));

                DoubleNode Node = board.head;


                while (true) {

                    // Recibimos y enviamos la posición
                    varPos2 = input.readInt();
                    output.writeInt(varPos1);

                    VentanaJuego.Posicion1.setText("Posicion: "+ varPos1);
                    VentanaJuego.Posicion2.setText("Posicion: "+ varPos2);

                    for (int i = 0; i > varPos1; i ++){
                        Node = Node.getNext();
                    }

                    if (Node.getField().equals("Reto")) {
                        int a = Node.getA();
                        int b = Node.getB();

                        switch (Node.getOperation()) {
                            case "+" -> respuesta = Integer.toString(a + b);
                            case "-" -> respuesta = Integer.toString(a - b);
                            case "*" -> respuesta = Integer.toString(a * b);
                            case "/" -> respuesta = Integer.toString(a / b);
                        }
                        VentanaReto.operacion.setText(a + Node.getOperation() + b);
                        if (Objects.equals(VentanaReto.respuesta, respuesta)){
                            break; // averiguar con isac la parte gráfica
                        }
                    } else if (Node.getField().equals("Trampa")) {
                        varPos1 -= Node.getMovement();
                    } else if (Node.getField().equals("Tunel")) {
                        varPos1 += Node.getMovement();
                    }
                    
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

