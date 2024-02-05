package sn.dev.sponsorship_app;
import java.sql.*;

public class DBConnection {

    //pour la connexion
    private Connection conn;

    //pour les requetes preparees
    private PreparedStatement preState;

    //pour les resultats des requtes de type MAJ (INSERT, DELETE, UPDATE)
    private int ok;

    //pour les resultats des requetes de type SELECT
    private ResultSet result;


    //Methode pour se connecter a la base de donnes
    private void getConnection() {
        String database = "sponsorship_db";
        String host = "localhost";
        String port = "3306";
        String url = "jdbc:mysql://"+host+":"+port+"/"+database;
        // ou url = "jdbc:mysql://localhost:3306/gestion-users";
        String username = "root";
        String password = "";
        try {
            // Charger le pilote JDBC
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Établir la connexion
            conn = DriverManager.getConnection(url, username, password);
            System.out.println("ok");

        }catch (Exception e){
            e.printStackTrace(); //permet de determiner l'erreur
        }
    }

    //Methode pour initialiser la connexion a la base et preparer la requete
    public void initPrep(String sql){
        try {
            getConnection();//connexion a la base
            preState = conn.prepareStatement(sql);// preparation de la requete
        }catch (Exception e){
            System.out.println("Erreur de preparation de requete");
            //e.printStackTrace(); //permet de determiner l'erreur
        }
    }

    //pour faire la mise a jour des requetes ou traitment
    public int executeMAJ(){
        try {
            ok = preState.executeUpdate();
        }catch (Exception e){
            System.out.println("erreur lors du changement");
        }

        return ok;//retourne 1 ou 0 par rapport au nombre de ligne inserer ou non a la base

    }

    public ResultSet executeSelect(){
        result=null;
        try {
            result = preState.executeQuery();
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public void closeConnection(){
        try {
            if( conn != null){
                conn.close();
            }
        }catch (Exception e){
            //e.printStackTrace();
            System.out.println("erreur");
        }
    }

    public PreparedStatement getPreState() {
        return preState;
    }


}
