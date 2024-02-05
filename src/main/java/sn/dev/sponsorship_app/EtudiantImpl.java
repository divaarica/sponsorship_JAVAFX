package sn.dev.sponsorship_app;

import java.sql.ResultSet;

public class EtudiantImpl implements IUser{
    private DBConnection db = new DBConnection();

    private ResultSet rs;
    private int ok;
    @Override
    public User seConnecter(String email, String password) {
        String sql="SELECT * FROM users WHERE email = ? AND password = ?";
        User user = null;
        try{
            //Ouverture de la connection et preparation de la requete
            db.initPrep(sql);

            //Passage de valeurs a la requete
            db.getPreState().setString(1,email);
            db.getPreState().setString(2,password);

            //Execution de la requete
            rs = db.executeSelect();
            if(rs.next()){
                user = new User();
                //user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));

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
