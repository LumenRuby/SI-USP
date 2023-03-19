package TextChat;
import java.util.ArrayList;
import java.io.*;


public class FichaPersonagem{
    //Instance vars
    private String nome;
    private ArrayList<String> itens = new ArrayList<>();
    private int forca;
    private int intel;
    private int dex;
    private int carisma;
    private int con;
    int id;
    
    public void setNome(String nome){
        this.nome = nome;
    }
    
    public void setForca(int forca){
        this.forca = forca;
    }
    public void setIntel(int intel){
        this.intel = intel;
    }
    public void setDex(int dex){
        this.dex = dex;
    }
    public void setCarisma(int carisma){
        this.carisma = carisma;
    }
    public void setConst(int constituicao){
        this.con = constituicao;
    }

       
    //constructor
    FichaPersonagem(String args[]){
        this.nome = args[1];
 	    this.forca = Integer.parseInt(args[2]);
	    this.intel = Integer.parseInt(args[3]);
	    this.dex = Integer.parseInt(args[4]);
	    this.carisma = Integer.parseInt(args[5]);
	    this.con = Integer.parseInt(args[6]);
    }
	
    void exportaTxt() throws FileNotFoundException{
        
        PrintStream printStream = new PrintStream(this.nome + ".txt");
        System.setOut(printStream);
        System.out.println("Nome: " +this.nome);
        System.out.println("Forca: " +this.forca);
        System.out.println("Inteligencia: " +this.intel);
        System.out.println("Destreza: " +this.dex);
        System.out.println("Carisma: " +this.carisma);
        System.out.println("Constituicao: " +this.con);
        System.out.println("Inventario: " +this.itens);
        System.out.println("id: " +this.id);
        PrintStream consoleStream = new PrintStream(
                                    new FileOutputStream
                                    (FileDescriptor.out));
        System.setOut(consoleStream);
                
    }
	
    public void addItems(String[] item){
        for(int i = 0; i < item.length; i++){
            itens.add(item[i]);
        }
        try {
            exportaTxt();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void removeItem(String item){
        itens.remove(item);
    }

    public static FichaPersonagem loadChar(String nome) throws IOException{
        String[] parameters = new String[7];

        FileReader file = new FileReader(nome + ".txt");
        BufferedReader br = new BufferedReader(file);
        br.readLine();
            
        parameters[0] = nome;
        parameters[1] = nome;
        int count = 2;
        String[] aux;
        while (count < 7) {
            aux = br.readLine().split(" ");
            if (aux != null) {
                parameters[count] = aux[1];
                count++;
            }
        }

        aux = br.readLine().split(", ");
        aux[0] = aux[0].replace("Inventario: ", "");
        aux[0] = aux[0].replace("[", "");
        aux[aux.length - 1] = aux[aux.length - 1].replace("]", "");
                
        br.close();
        FichaPersonagem personagem = new FichaPersonagem(parameters);
        personagem.addItems(aux);
        personagem.exportaTxt();
    
        return personagem;
    }  
    
}
