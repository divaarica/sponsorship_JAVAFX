package sn.dev.sponsorship_app.controllers;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import sn.dev.sponsorship_app.DBConnection;
import sn.dev.sponsorship_app.Parrainage;
import sn.dev.sponsorship_app.User;
import sn.dev.sponsorship_app.tools.Notification;
import sn.dev.sponsorship_app.tools.Outils;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class CandidatController implements Initializable {

    DBConnection db = new DBConnection();

    private int id;

    private ResultSet rs;


    @FXML
    private TableView<User> electeurTbl;

    @FXML
    private TableColumn<User, Integer> idCol;

    @FXML
    private Button logoutBtn;


    @FXML
    private TableColumn<User, String> nomCol;

    @FXML
    private TableColumn<User, String> prenomCol;

    @FXML
    private TableView<Parrainage> dateTbl;


    @FXML
    private TableColumn<Parrainage,LocalDateTime> dateCol;

    @FXML
    private TableColumn<Integer, Integer> totalCol;

    @FXML
    private TableView<Integer> tblTotal;


    @FXML
    void logout(ActionEvent event) {
        try{
            Notification.NotifSuccess("Succes", "Deconnexion Reussie !");
            Outils.load(event,"Gestion des utilisateurs", "/Fxml/login.fxml");
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadTable();
        int nbParrainage = nbParrainage();  // Obtenez le nombre de parrainages
        //totalCol.setCellValueFactory((Callback<TableColumn.CellDataFeatures<Integer, Integer>, ObservableValue<Integer>>) new SimpleIntegerProperty(nbParrainage).asObject());
    }



//    public boolean check_etat(User U){
//        if (U.getActived() == 0) {
//            String sql = "DELETE FROM parrainer WHERE candidat = ?";
//            try {
//                db.initPrep(sql);
//                db.getPreState().setInt(1, SessionManager.getInstance().getLoggedInUser().getId());
//                db.executeMAJ();
//            } catch (SQLException e) {
//                throw new RuntimeException();
//            }
//            Notification.NotifError("Erreur !", "Impossible de participer: votre compte a été désactivé!");
//            return false;
//
//        }
//        return true;
//    }

    private ObservableList<User> getElecteurs() {
        ObservableList<User> users = FXCollections.observableArrayList();
        ObservableList<Parrainage> par = FXCollections.observableArrayList();
        String sql = "SELECT * FROM users u JOIN parrainer p ON u.id=p.electeur WHERE p.candidat = ? AND u.profil=2";
        try{
            db.initPrep(sql);
            db.getPreState().setInt(1, SessionManager.getInstance().getLoggedInUser().getId());
            rs = db.executeSelect();
            while(rs.next()){
                User u = new User();
                Parrainage p = new Parrainage();
                u.setId(rs.getInt("id"));
                u.setNom(rs.getString("nom"));
                u.setPrenom(rs.getString("prenom"));
                users.add(u);
            }
            db.closeConnection();
        }catch (SQLException e){
            throw  new RuntimeException();
        }

        return users;
    }


    private ObservableList<Parrainage> getDate() {
        ObservableList<Parrainage> par = FXCollections.observableArrayList();
        String sql = "SELECT * FROM users u JOIN parrainer p ON u.id=p.electeur WHERE p.candidat = ? AND u.profil=2";
        try{
            db.initPrep(sql);
            db.getPreState().setInt(1, SessionManager.getInstance().getLoggedInUser().getId());
            rs = db.executeSelect();
            while(rs.next()){
                Parrainage p = new Parrainage();
                p.setDateParrainage(rs.getTimestamp("dateParrainage").toLocalDateTime());
                par.add(p);
            }
            db.closeConnection();
        }catch (SQLException e){
            throw  new RuntimeException();
        }

        return par;
    }

//

    private int nbParrainage() {
        int nb = 0;
        String sql = "SELECT COUNT(*) AS nb FROM users u JOIN parrainer p ON u.id=p.electeur WHERE p.candidat = ? AND u.profil=2";
        try {
            db.initPrep(sql);
            db.getPreState().setInt(1, SessionManager.getInstance().getLoggedInUser().getId());
            rs = db.executeSelect();
            while (rs.next()) {
                nb = rs.getInt("nb");
                System.out.println(nb);
            }
            db.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return nb;
    }

    public void loadTable(){
        ObservableList<User> liste = getElecteurs();
        ObservableList<Parrainage> liste2 = getDate();
        electeurTbl.setItems(liste);
        dateTbl.setItems(liste2);

        idCol.setCellValueFactory(new PropertyValueFactory<User,Integer>("id"));
        nomCol.setCellValueFactory(new PropertyValueFactory<User,String >("nom"));
        prenomCol.setCellValueFactory(new PropertyValueFactory<User,String >("prenom"));
        dateCol.setCellValueFactory(new PropertyValueFactory<Parrainage,LocalDateTime>("dateParrainage"));

    }




}
