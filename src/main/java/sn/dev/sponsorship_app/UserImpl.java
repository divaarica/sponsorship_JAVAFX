package sn.dev.sponsorship_app;

import java.sql.ResultSet;
import java.util.concurrent.ExecutionException;

public class UserImpl implements IUser{
    private DBConnection db = new DBConnection();

    private ResultSet rs;
    private int ok;
    @Override
    public User seConnecter(String login, String password) {
        String sql="SELECT * FROM users WHERE login = ? AND password = ?";
        User user = null;
        try{
            //Ouverture de la connection et preparation de la requete
            db.initPrep(sql);

            //Passage de valeurs a la requete
            db.getPreState().setString(1,login);
            db.getPreState().setString(2,password);

            //Execution de la requete
            rs = db.executeSelect();
            if(rs.next()){
                user = new User();
                user.setLogin(rs.getString("login"));
                user.setPassword(rs.getString("password"));
                user.setProfil(rs.getInt("profil"));
                user.setNom(rs.getString("nom"));
                user.setPrenom(rs.getString("prenom"));
                user.setId(rs.getInt("id"));
                user.setActived(rs.getInt("actived"));

            }
            //Fermeture de la connection
            db.closeConnection();

        }catch (Exception e){
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public String generateLogin(String nom, String prenom) {
        int r = (1 + (int)(Math.random()*1000));
        return nom.substring(0,2)+prenom.substring(0,2)+String.format("%3d",r).replace(' ','0');

    }
}
