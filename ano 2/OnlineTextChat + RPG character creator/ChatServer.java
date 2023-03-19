package TextChat;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class ChatServer extends Thread {
    private static ArrayList<BufferedWriter>clientes;
    private static ServerSocket server;
    private String nome;
    private Socket con;
    private static ArrayList<FichaPersonagem> personagens;
    private InputStream in;
    private InputStreamReader inr;
    private BufferedReader bfr;


    public ChatServer(Socket con){
        this.con = con;
        try {
            in  = con.getInputStream();
            inr = new InputStreamReader(in);
            bfr = new BufferedReader(inr);
            con.setSoTimeout(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run(){

        System.setProperty("sun.security.ssl.allowUnsafeRenegotiation", "true");

        try{
      
        String msg;
        OutputStream ou =  this.con.getOutputStream();
        Writer ouw = new OutputStreamWriter(ou);
        BufferedWriter bfw = new BufferedWriter(ouw);
        clientes.add(bfw);
        nome = msg = bfr.readLine();
      
        while(!"Sair".equalsIgnoreCase(msg) && msg != null){
            msg = bfr.readLine();
            sendToAll(bfw, msg);
            System.out.println(msg);

            if (msg.startsWith("diceroll")){
                String [] parameters = msg.split(" ");
                if (parameters.length < 4) { break; }
                
                String [] finale = dice.roll(parameters);
                for(int i = 0; i < finale.length; i++){
                    msg = finale[i];
                    sendToAllAux(bfw, msg, "Resultado:");
                    System.out.println(msg);
                }
            }

            if(msg.startsWith("addChar")){
                String [] parameters = msg.split(" ");
                if (parameters.length != 7) { break; }
                FichaPersonagem personagem = new FichaPersonagem(parameters);
                personagens.add(personagem);
                personagem.id = personagens.indexOf(personagem);
                personagem.exportaTxt();
                Scanner input = new Scanner(new File(parameters[1] + ".txt"));

                while (input.hasNextLine()){
                    msg = input.nextLine();
                    sendToAllAux(bfw, msg, "");
                }

            }

            if(msg.startsWith("addItems")){
                String [] parameters = msg.split(" ");  
                FichaPersonagem personagem = personagens.get(Integer.parseInt(parameters[1]));
                
                String msg_aux = msg.replace("addItems", "");
                msg_aux = msg_aux.replace(parameters[1] + " ", "");
                String[] items = msg_aux.split(", ");
                if (items.length == 0) { break; }
                String[] items_aux = items[0].split(" ");

                if (items_aux.length >= 3){
                    StringBuffer sb = new StringBuffer();
                    sb.append(items_aux[2]);
                    for(int i = 3; i < items_aux.length; i++) {
                        sb.append(" "+ items_aux[i]);
                    }
                items[0] = items_aux[1] + " " + sb.toString();
                }
                else{ 
                    items[0] = items_aux[1]; 
                }
                personagem.addItems(items);
                personagem.exportaTxt();
            }

            if(msg.startsWith("removeItem")){
                String [] parameters = msg.split(" ");
                FichaPersonagem personagem = personagens.get(Integer.parseInt(parameters[1]));

                if(parameters.length >= 3){
                    StringBuffer sb = new StringBuffer();
                    for(int i = 3; i < parameters.length; i++) {
                        sb.append(" "+ parameters[i]);
                    }
                    parameters[2] = parameters[2] + sb.toString();
                }
                personagem.removeItem(parameters[2]);
                personagem.exportaTxt();
            }

            if(msg.startsWith("loadChar")){
                String[] parameters = msg.split(" ");
                if (parameters.length != 2) { break; }
                FichaPersonagem personagem = FichaPersonagem.loadChar(parameters[1]);
                personagens.add(personagem);
                personagem.id = personagens.indexOf(personagem);
                personagem.exportaTxt();
            }

            if(msg.equals("help")){
                msg = "Para fazer seu personagem, usa o comando addChar seguido do nome de seu personagem e 5 valores inteiros.";
                sendToAllAux(bfw, msg, "");
                msg = "Esses valores representam sua força, inteligência, destreza, carisma e constituição respectivamente.";
                sendToAllAux(bfw, msg,"");
                msg = "Exemplo: addChar Chaves 3 5 4 3 4";
                sendToAllAux(bfw, msg,"");
                sendToAllAux(bfw, "", "");

                msg = "Em seguida, temos o comando addItems, que adiciona um conjunto de itens ao inventário de um personagem. É necessário colocar o id do personagem após o comando.";
                sendToAllAux(bfw, msg, "");
                msg = "Exemplo: addItems 0 armadura vermelha, curativos, cajado raro";
                sendToAllAux(bfw, msg, "");
                sendToAllAux(bfw, "", "");
                msg = "Para remover um item individual, use removeItem. Após o comando, coloque o id do personagem e o nome do item que quer remover.";
                sendToAllAux(bfw, msg, "");
                msg = "Exemplo: removeItem 0 cajado raro";
                sendToAllAux(bfw, msg, "");
                sendToAllAux(bfw, "", "");

                msg = "Já criou um personagem em outra sessão do programa?  Use o comando loadChar com o nome de seu personagem logo depois, sua ficha será buscada no diretório da aplicação.";
                sendToAllAux(bfw, msg, "");
                msg = "Exemplo: loadChar Chaves";
                sendToAllAux(bfw, msg, "");
                sendToAllAux(bfw, "", "");

                msg = "Por fim, temos o comando diceroll para rolar os dados. O comando aceita 3 valores que representam: número de dados jogados, seu número de lados e um modificador positivo ou negativo.";
                sendToAllAux(bfw, msg, "");
                msg = "Exemplo: diceroll 2 20 -3";
                sendToAllAux(bfw, "", "");
            }
        }
      
        }catch (IOException e){
            e.printStackTrace();
        }catch (NumberFormatException e){
            e.printStackTrace();
        }
    
    }


    public void sendToAll(BufferedWriter bwSaida, String msg) throws  IOException{
        BufferedWriter bwS;

        for(BufferedWriter bw : clientes){
            bwS = (BufferedWriter)bw;
            if(!(bwSaida == bwS)){
                bw.write(nome + ": " + msg+"\r\n");
                bw.flush();
            }
        }
    }

    public void sendToAllAux(BufferedWriter bwSaida, String msg, String enun) throws  IOException{
        for(BufferedWriter bw : clientes){
            bw.write(enun + msg +"\r\n");
            bw.flush();
        }
        
    }

    public static void main(String []args) {

        try{
            //Cria os objetos necessário para instânciar o servidor
            JLabel lblMessage = new JLabel("Porta do Servidor:");
            JTextField txtPorta = new JTextField("12345");
            Object[] texts = {lblMessage, txtPorta };
            JOptionPane.showMessageDialog(null, texts);
            server = new ServerSocket(Integer.parseInt(txtPorta.getText()));
            server.setSoTimeout(0);
            clientes = new ArrayList<BufferedWriter>();
            personagens = new ArrayList<FichaPersonagem>();
            JOptionPane.showMessageDialog(null,"Servidor ativo na porta: "+
            txtPorta.getText());
      
            while(true){
                System.out.println("Aguardando conexão...");
                Socket con = server.accept();
                System.out.println("Cliente conectado...");
                Thread t = new ChatServer(con);
                t.start();
            }
      
        }catch (Exception e) {
            e.printStackTrace();
        }     
    } 
}