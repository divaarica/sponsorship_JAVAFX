package sn.dev.sponsorship_app.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import sn.dev.sponsorship_app.*;
import sn.dev.sponsorship_app.tools.Notification;
import sn.dev.sponsorship_app.tools.Outils;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class ElecteurController implements Initializable {

    DBConnection db = new DBConnection();

    private int id;

    private ResultSet rs;

    @FXML
    private TableView<User> candidatTbl;

    @FXML
    private Button logoutBtn;

    @FXML
    private TableColumn<User, String> nomCol;

    @FXML
    private TextField nomTxt;

    @FXML
    private Text nonTft;

    @FXML
    private TableColumn<User, Integer> numeroCol;

    @FXML
    private Button parrainerBtn;

    @FXML
    private TableColumn<User, String> prenomCol;

    @FXML
    private TextField prenomTxt;

    @FXML
    void creer_parrainage(ActionEvent event) {
        String sql = "INSERT INTO parrainer( dateParrainage, electeur, candidat) VALUES (?,?,?)";
        try {
            db.initPrep(sql);
            LocalDateTime currentDateTime = LocalDateTime.now();
            db.getPreState().setTimestamp(1, Timestamp.valueOf(currentDateTime));
            int loggedInUserId = SessionManager.getInstance().getLoggedInUser().getId();
            db.getPreState().setInt(2, loggedInUserId);
            db.getPreState().setInt(3, id);
            db.executeMAJ();
            clearFields();
            Notification.NotifSuccess("Succés !", "Parrainage effectué avec succès!");
        } catch (SQLException e) {
            clearFields();
            throw new RuntimeException();
        }
        parrainerBtn.setDisable(true);

    }

    @FXML
    void getData(MouseEvent event) {
        User u = candidatTbl.getSelectionModel().getSelectedItem();
        id = u.getId();
//        System.out.println(id);
        prenomTxt.setText(u.getPrenom());
        nomTxt.setText(u.getNom());

        if (! check_parrainage() && check_etat(SessionManager.getInstance().getLoggedInUser()) && check_nb_electeur(u.getId())) {
            parrainerBtn.setDisable(false);
        }

    }

    @FXML
    void logout(ActionEvent event) {
        try{
            Notification.NotifSuccess("Succes", "Deconnexion Reussie !");
            Outils.load(event,"Gestion des utilisateurs", "/Fxml/login.fxml");
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void clearFields(){
        nomTxt.setText("");
        prenomTxt.setText("");
    }

    private ObservableList<User> getCandidats() {
        ObservableList<User> users = FXCollections.observableArrayList();
        String sql = "SELECT * FROM users WHERE profil=3";
        try{
            db.initPrep(sql);
            ResultSet rs = db.executeSelect();
            while(rs.next()){
                User u = new User();
                u.setId(rs.getInt("id"));
                u.setNom(rs.getString("nom"));
                u.setPrenom(rs.getString("prenom"));
                u.setLogin(rs.getString("login"));
                u.setPassword(rs.getString("password"));
                u.setActived(rs.getInt("actived"));
                u.setProfil(rs.getInt("profil"));
                users.add(u);
            }
            db.closeConnection();
        }catch (SQLException e){
            throw  new RuntimeException();
        }

        return users;
    }

    public void loadTable(){
        ObservableList<User> liste = getCandidats();
        candidatTbl.setItems(liste);

        numeroCol.setCellValueFactory(new PropertyValueFactory<User,Integer>("id"));
        nomCol.setCellValueFactory(new PropertyValueFactory<User,String >("nom"));
        prenomCol.setCellValueFactory(new PropertyValueFactory<User,String >("prenom"));

    }


    public boolean check_parrainage(){
        String sql = "SELECT * FROM parrainer WHERE electeur = ?";
        try {
            db.initPrep(sql);
            db.getPreState().setInt(1, SessionManager.getInstance().getLoggedInUser().getId());
            rs = db.executeSelect();
            while(rs.next()){
                Notification.NotifError("Erreur !", "Impossible de parrainer: vous parrainer déjà un candidat!");
                return true;
            }
        }catch (SQLException e){
            throw new RuntimeException();
        }
        return false;
    }

    public boolean check_etat(User U){
        if (U.getActived() == 0) {
            String sql = "DELETE FROM parrainer WHERE electeur = ?";
            try {
                db.initPrep(sql);
                db.getPreState().setInt(1, SessionManager.getInstance().getLoggedInUser().getId());
                db.executeMAJ();
            } catch (SQLException e) {
                throw new RuntimeException();
            }
            Notification.NotifError("Erreur !", "Impossible de parrainer: votre compte a été désactivé!");
            return false;

        }
        return true;
    }

    public boolean check_nb_electeur(int idCand){

        String sql = "SELECT COUNT(*) FROM parrainer WHERE candidat = ?";
        try {
            db.initPrep(sql);
            db.getPreState().setInt(1, idCand);
            ResultSet rs = db.executeSelect();
            while (rs.next()){
                if (rs.getInt(1) == 7){
                    Notification.NotifError("Erreur !", "Impossible de parrainer: Ce candidat a déjà atteint son quotat maximale!");
                    return false;
                }
            }
            return true;
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nomTxt.setEditable(false);
        prenomTxt.setEditable(false);
        loadTable();
    }
}
