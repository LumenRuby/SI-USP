package bandidados;
import java.sql.*;
import java.util.Scanner;

public class consulta{

    static private String nome_bd;
    static private String user;
    static private String password;

    public static Connection connect(String nome_bd, String user, String password) {
        Connection conn = null;
        String url = "jdbc:postgresql://localhost/" + nome_bd;
        try {
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Conectado ao servidor com sucesso.");
          
           
        } catch (SQLException e) {
            System.out.println("Erro na conexão.");
        }

        return conn;
    }

    public static void addGrupo(Connection conn, String nome){
        PreparedStatement stmt = null;
        String sql = "INSERT INTO Forca_armada (Nome) VALUES (?)";
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, nome);
            stmt.execute();
            stmt.close();

            System.out.println("Inserido com sucesso.");

           
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void addDivisao(Connection conn, int id, int barcos, int tanques, int soldados, int baixas){
        PreparedStatement stmt = null;
        String sql = "INSERT INTO Divisao (ForcaArmadaID, Barcos, Tanques, Soldados, Baixas) VALUES (?,?,?,?,?)";
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.setInt(2, barcos);
            stmt.setInt(3, tanques);
            stmt.setInt(4, soldados);
            stmt.setInt(5, baixas);
            stmt.execute();
            stmt.close();

            System.out.println("Inserido com sucesso.");

           
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void addConflito(Connection conn, String nome, int mortos, int feridos){
        PreparedStatement stmt = null;
        String sql = "INSERT INTO Conflito (Nome, Mortos, Feridos) VALUES (?,?,?)";
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, nome);
            stmt.setInt(2, mortos);
            stmt.setInt(3, feridos);
            stmt.execute();
            stmt.close();

            System.out.println("Inserido com sucesso.");

           
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void addLider(Connection conn, String nome, String desc, int id){
        PreparedStatement stmt = null;
        String sql = "INSERT INTO Lider_politico (Nome, Descricao, ForcaArmadaID) VALUES (?,?,?)";
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, nome);
            stmt.setString(2, desc);
            stmt.setInt(3, id);
            stmt.execute();
            stmt.close();

            System.out.println("Inserido com sucesso.");

           
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void addChefe(Connection conn, String nome, String faixa, int id_pol, int id_forca, int divisao){
        PreparedStatement stmt = null;
        String sql = "INSERT INTO Lider_militar (LiderPoliticoID, Nome, FaixaHierarquica, ForcaArmadaID, Divisao) VALUES (?,?,?,?,?)";
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id_pol);
            stmt.setString(2, nome);
            stmt.setString(3, faixa);
            stmt.setInt(4, id_forca);
            stmt.setInt(5, divisao);
            stmt.execute();
            stmt.close();

            System.out.println("Inserido com sucesso.");

           
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void getTop5Conflitos(Connection conn){
        String sql = "SELECT ConflitoID, Nome, Mortos FROM Conflito ORDER BY Mortos Desc LIMIT 5";
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){
                System.out.print("ID: " + rs.getInt("ConflitoID"));
                System.out.print(", Nome: " + rs.getString("Nome"));
                System.out.print(", Fatalidades: " + rs.getInt("Mortos") + "\n");
            }
            stmt.close();
           
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void getTop5Armas(Connection conn){
        String sql = "SELECT ForcaArmadaID, SUM(quantidade) AS total FROM lote_de_armas "
        + "WHERE ForcaArmadaID is not null GROUP BY ForcaArmadaID ORDER BY total Desc limit 5";
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){
                  
                System.out.print("ID: " + rs.getString("ForcaArmadaID"));
                System.out.print(", Armas: " + rs.getInt("total") + "\n");
            }
            stmt.close();
           
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    public static void getTop5Organizacao(Connection conn){
        String sql = "SELECT Nome, COUNT(*) AS total FROM Mediacao NATURAL JOIN Mediador group by Nome ORDER BY total Desc limit 5";
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){
                System.out.print("Nome: " + rs.getString("Nome"));
                System.out.print(", Mediações: " + rs.getInt("total") + "\n");
            }
            stmt.close();
           
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void getTop1Religiao(Connection conn){
        String sql = "select pais, count (conflitoid) AS total from pais_em_conflito natural join conflito_religioso group by pais"
        + " order by total desc limit 1";
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){
                System.out.print("Nome: " + rs.getString("Pais"));
                System.out.print(", Conflitos: " + rs.getInt("total") + "\n");
            }
            stmt.close();
           
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void getListaArmas(Connection conn){
        String sql = "select TraficanteID, ForcaArmadaID from Lote_de_armas where Nome = 'Barret M82' or Nome = 'M200 intervention'";
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){
                System.out.print("Traficante: " + rs.getInt("TraficanteID"));
                System.out.print(", Grupo Armado: "  + rs.getInt("ForcaArmadaID") + "\n");
            }
            stmt.close();
           
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void getHistograma(Connection conn){
        String sql1 = "SELECT count(*) AS total FROM conflito_religioso";
        String sql2 = "SELECT count(*) AS total FROM conflito_territorial";
        String sql3 = "SELECT count(*) AS total FROM conflito_racial";
        String sql4 = "SELECT count(*) AS total FROM conflito_economico";
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql1);

            while(rs.next()){
                System.out.print("Conflito religioso   | " + rs.getInt("total") + "\n");
            }

            rs = stmt.executeQuery(sql2);
            while(rs.next()){
                System.out.print("Conflito territorial | " + rs.getInt("total") + "\n");
            }

            rs = stmt.executeQuery(sql3);
            while(rs.next()){
                System.out.print("Conflito racial      | " + rs.getInt("total") + "\n");
            }

            rs = stmt.executeQuery(sql4);
            while(rs.next()){
                System.out.print("Conflito economico   | " + rs.getInt("total") + "\n");
            }

            stmt.close();
           
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }




   
    public static void main(String[] args) {
        boolean stop = false;
        Scanner in = new Scanner(System.in);
        String introducao = "Digite o nome do banco de dados em uma linha e usuário e senha na outra:";

        System.out.println(introducao);
        String nome_bd = in.nextLine();
        String user = in.next();
        String password = in.next();
        Connection conn = connect(nome_bd, user, password);

        while (stop == false){
            int op = in.nextInt();
            if (op == 0){
                stop = true;
            }
            if (op == 1){
                addDivisao(conn, in.nextInt(), in.nextInt(), in.nextInt(), in.nextInt(), in.nextInt());
            }
            if (op == 2){
                addConflito(conn, in.nextLine(), in.nextInt(), in.nextInt());
            }
            if (op == 3){
                addGrupo(conn, in.nextLine());
            }
            if(op == 4){
                addLider(conn, in.nextLine(), in.nextLine(), in.nextInt());
            }
            if(op == 5){
                addChefe(conn, in.nextLine(), in.nextLine(), in.nextInt(), in.nextInt(), in.nextInt());
            }
            if(op == 6){
                getHistograma(conn);
            }
            if(op == 7){
                getListaArmas(conn);
            }
            if (op == 8){
                getTop5Conflitos(conn);
            }
            if(op == 9){
                getTop5Organizacao(conn);
            }
            if(op == 10){
                getTop5Armas(conn);
            }
            if(op == 11){
                getTop1Religiao(conn);
            }
        }
    
    }
}
