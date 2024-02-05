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
import sn.dev.sponsorship_app.DBConnection;
import sn.dev.sponsorship_app.Etudiant;
import sn.dev.sponsorship_app.tools.Notification;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class UtilisateurController implements Initializable {

    private DBConnection db = new DBConnection() ;

    @FXML
    private Button effacerBtn;

    @FXML
    private Button enregistrerBtn;

    @FXML
    private TableColumn<Etudiant, Integer> idCol;

    @FXML
    private TableColumn<Etudiant, String> matiereCol;

    @FXML
    private TextField matiereTxt;

    @FXML
    private Button modifierBtn;

    @FXML
    private TableColumn<Etudiant, String> nomCol;

    @FXML
    private TextField nomTxt;

    @FXML
    private TableColumn<Etudiant, String> prenomCol;

    @FXML
    private TextField prenomTxt;

    @FXML
    private Button supprimerBtn;

    //id concerneer pour la modification
    private int id;

    @FXML
    private TableView<Etudiant> utilisateursTbl;

    //implementer cette interface pour permettre que losqu'on lance la page , le tableau se charge
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadTable();

    }

    @FXML
    void vider(ActionEvent event) {
        clearFields();

    }



    @FXML
    void update(ActionEvent event) {

        String sql = "UPDATE etudiant SET nom=?, prenom=?, matiere=? WHERE id=?";
        try{
            db.initPrep(sql);
            db.getPreState().setString(1, nomTxt.getText());
            db.getPreState().setString(2, prenomTxt.getText());
            db.getPreState().setString(3, matiereTxt.getText());
            db.getPreState().setInt(4, id);
            db.executeMAJ();
            db.closeConnection();
            //reinitialisation du tableau
            loadTable();
            clearFields();
            Notification.NotifSuccess("Succes", "Etudiant modifier avec succes");
            enregistrerBtn.setDisable(false);

        }catch (SQLException e){
            throw new RuntimeException();
        }


    }


    @FXML
    void getData(MouseEvent event) {
        Etudiant e = utilisateursTbl.getSelectionModel().getSelectedItem();
        id = e.getId();
        prenomTxt.setText(e.getPrenom());
        nomTxt.setText(e.getNom());
        matiereTxt.setText(e.getNom());
        //grise le bouton enregistre
        enregistrerBtn.setDisable(true);

    }

    //methode qui permet de recuperer les elements en base de donnes
    public ObservableList<Etudiant> getEtudiants(){
        ObservableList<Etudiant> etudiants = FXCollections.observableArrayList();
        String sql = "SELECT * FROM etudiant ORDER BY nom ASC";
        try{
            db.initPrep(sql);
            ResultSet rs = db.executeSelect();
            while(rs.next()){
                Etudiant e = new Etudiant();
//                e.setId(rs.getInt(1));
                e.setId(rs.getInt("id"));
                e.setNom(rs.getString("nom"));
                e.setPrenom(rs.getString("prenom"));
                e.setMatiere(rs.getString("matiere"));
                etudiants.add(e);
            }
            db.closeConnection();
        }catch (SQLException e){
           throw  new RuntimeException();
        }

        return etudiants;
    }

    //pour charger le tableau
    public void loadTable(){
        ObservableList<Etudiant> liste = getEtudiants();
        utilisateursTbl.setItems(liste);

        //il faut specifier le type d'information de chaque colonne
        // set... permet d'affecter une valeur  la cellule
        //new PropertyValueFactory<Etudiant,Integer>("id") permet de recuperer l'informtion et de le donner a la cellule
        idCol.setCellValueFactory(new PropertyValueFactory<Etudiant,Integer>("id"));
        nomCol.setCellValueFactory(new PropertyValueFactory<Etudiant,String >("nom"));
        prenomCol.setCellValueFactory(new PropertyValueFactory<Etudiant,String >("prenom"));
        matiereCol.setCellValueFactory(new PropertyValueFactory<Etudiant,String >("matiere"));
    }

    @FXML
    void save(ActionEvent event) {
        String sql = "INSERT INTO etudiant VALUES (NULL,?,?,?)";
        try{
            db.initPrep(sql);
            db.getPreState().setString(1, nomTxt.getText());
            db.getPreState().setString(2, prenomTxt.getText());
            db.getPreState().setString(3, matiereTxt.getText());
            db.executeMAJ();
            db.closeConnection();
            //reinitialisation du tableau
            loadTable();
            clearFields();
            Notification.NotifSuccess("Succes", "Etudiant enregistrer avec succes");

        }catch (SQLException e){
            throw new RuntimeException();
        }

    }

    public void clearFields(){
        nomTxt.setText("");
        prenomTxt.setText("");
        matiereTxt.setText("");
    }

    @FXML
    void delete(ActionEvent event) {

        String sql = "DELETE FROM etudiant WHERE id=?";
        try{
            db.initPrep(sql);
            db.getPreState().setInt(1, id);
            db.executeMAJ();
            db.closeConnection();
            //reinitialisation du tableau
            loadTable();
            clearFields();
            Notification.NotifSuccess("Succes", "Etudiant supprimer avec succes");
            enregistrerBtn.setDisable(false);

        }catch (SQLException e){
            throw new RuntimeException();
        }

    }






}
