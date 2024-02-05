package sn.dev.sponsorship_app.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import sn.dev.sponsorship_app.IUser;
import sn.dev.sponsorship_app.User;
import sn.dev.sponsorship_app.UserImpl;
import sn.dev.sponsorship_app.tools.Notification;
import sn.dev.sponsorship_app.tools.Outils;

public class loginController {
    private IUser userDao = new UserImpl();

    @FXML
    private Button logoutBtn;

    @FXML
    private TextField loginTxt;

    @FXML
    private Button loginBtn;

    @FXML
    private PasswordField passwordTxt;

    @FXML
    void login(ActionEvent event){
       String login = loginTxt.getText();
       String password = passwordTxt.getText();
       if(login.isEmpty() || password.isEmpty()){
           Notification.NotifError("Erreur", "Tous les champs sont obligatoires");
       }else{
           User u = userDao.seConnecter(login, password);
           if(u != null){
               try{
                   Notification.NotifSuccess("Succes", "Connexion Reussie !");
                   SessionManager.getInstance().setLoggedInUser(u);
                   System.out.println(u.getActived());
                   if(u.getProfil()==1){
                       Outils.load(event,"Bienvenu Administrateur", "/Fxml/admin.fxml");

                   } else if (u.getProfil()==2) {
                       if (u.getActived()==1){
                           Outils.load(event,"Bienvenue Electeur", "/Fxml/electeur.fxml");
                       }else {
                           Notification.NotifError("Erreur", "Votre compte a ete desactiver !!");
                       }


                   }else {
                       Outils.load(event,"Bienvenu Candidat", "/Fxml/candidat.fxml");
                   }
               }catch (Exception e){
                   e.printStackTrace();
               }
           }else {
               Notification.NotifError("Erreur", "Identifiants inccorects !!");
           }
       }
    }

}
