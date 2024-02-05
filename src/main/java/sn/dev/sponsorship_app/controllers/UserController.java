package sn.dev.sponsorship_app.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import sn.dev.sponsorship_app.*;
import sn.dev.sponsorship_app.tools.Notification;
import sn.dev.sponsorship_app.tools.Outils;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class UserController implements Initializable {

    DBConnection db = new DBConnection();

    private int id;

    private ResultSet rs;

    @FXML
    private ComboBox<Integer> activedCbb;

    @FXML
    private TableColumn<User, Integer> activedCol;

    @FXML
    private Button addCandidatBtn;

    @FXML
    private Button addElecteurBtn;

    @FXML
    private Button ajouterBtn;

    @FXML
    private Button dashboardBtn;

    @FXML
    private Button effacerBtn;

    @FXML
    private Button electionBtn;

    @FXML
    private TableColumn<User, String> loginCol;

    @FXML
    private TableColumn<User, String> passwordCol;


    @FXML
    private Button modifierBtn;

    @FXML
    private TableColumn<User, String> nomCol;

    @FXML
    private TextField nomTxt;

    @FXML
    private TextField passwordTxt;

    @FXML
    private TableColumn<User, String> prenomCol;


    @FXML
    private TableColumn<User, Integer> idCol;


    @FXML
    private TextField prenomTxt;

    @FXML
    private ComboBox<String> profilCbb;

    @FXML
    private TableColumn<User, Integer> profilCol;

    @FXML
    private TableView<User> userTbl;

    @FXML
    private Button supprimerBtn;

    //Initialisation
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Ajouter les rôles au ComboBox
        profilCbb.setItems(FXCollections.observableArrayList("Electeur","Candidat"));

        // Activer ou desactiver profil
        activedCbb.setItems(FXCollections.observableArrayList(0,1));
        loadTable();

    }

    public Role getRoleById(int id){
        String sql = "SELECT * FROM role WHERE id=?";
        Role role = null;
        try {
            db.initPrep(sql);
            db.getPreState().setInt(1,id);
            rs = db.executeSelect();
            if(rs.next()){
                role = new Role();
                role.setName(rs.getString("name"));
                role.setId(rs.getInt("id"));
                role.setActived(rs.getInt("actived"));
            }
            db.closeConnection();
        }catch (Exception e){
            e.printStackTrace();
        }
        return role;
    }
    public Role getRoleByName(String name){
        String sql = "SELECT * FROM role WHERE name=?";
        Role role = null;
        try {
            db.initPrep(sql);
            db.getPreState().setString(1,name);
            rs = db.executeSelect();
            if(rs.next()){
                role = new Role();
                role.setName(rs.getString("name"));
                role.setId(rs.getInt("id"));
                role.setActived(rs.getInt("etat"));
            }
            db.closeConnection();
        }catch (Exception e){
            e.printStackTrace();
        }
        return role;
    }


    //recuperation info du tableau
    @FXML
    void getData(MouseEvent event) {
        User u = userTbl.getSelectionModel().getSelectedItem();
        id = u.getId();
        System.out.println(id);
        prenomTxt.setText(u.getPrenom());
        nomTxt.setText(u.getNom());
        passwordTxt.setText(u.getPassword());
        System.out.println("ok");

        // Utilisez une méthode pour récupérer le nom du rôle à partir de l'ID
        Role role = getRoleById(u.getProfil());

        // Parcourez les éléments du ComboBox pour trouver le rôle correspondant
        for (String s : profilCbb.getItems()) {
            System.out.println(role.getName());
            if (s.equalsIgnoreCase(role.getName())) {
                // Définissez le rôle de l'utilisateur comme la valeur par défaut
                profilCbb.setValue(s);
                break;
            }
        }
        for (int s : activedCbb.getItems()) {
            if (s == u.getActived()) {
                // Définissez le rôle de l'utilisateur comme la valeur par défaut
                activedCbb.setValue(s);
                break;
            }
        }
        ajouterBtn.setDisable(true);
    }


    @FXML
    void add(ActionEvent event) {

        String sql = "INSERT INTO users VALUES (NULL,?,?,?,?,?,?)";
        Role profil = getRoleByName(profilCbb.getValue());
        IUser userDao = new UserImpl();
        try{
            db.initPrep(sql);
            db.getPreState().setString(1, nomTxt.getText());
            db.getPreState().setString(2, prenomTxt.getText());
            db.getPreState().setString(3, userDao.generateLogin(nomTxt.getText(),prenomTxt.getText()));//mettrelogin
            db.getPreState().setString(4, passwordTxt.getText());
            db.getPreState().setInt(5, activedCbb.getValue());
            db.getPreState().setInt(6, profil.getId());
            db.executeMAJ();
            db.closeConnection();
            //reinitialisation du tableau
            loadTable();
            clearFields();
            Notification.NotifSuccess("Succes", "Etudiant enregistrer avec succes");

        }catch (SQLException e){
            e.printStackTrace();
        }

    }

    @FXML
    void clear(ActionEvent event) {
        clearFields();

    }


//    void delete(ActionEvent event) {
//        String sql = "DELETE FROM users WHERE id=?";
//        try{
//            db.initPrep(sql);
//            db.getPreState().setInt(1, id);
//            db.executeMAJ();
//            db.closeConnection();
//            //reinitialisation du tableau
//            loadTable();
//            clearFields();
//            Notification.NotifSuccess("Succes", "Etudiant supprimer avec succes");
//            ajouterBtn.setDisable(false);
//
//        }catch (SQLException e){
//            throw new RuntimeException();
//        }
//
//    }
    @FXML
    void delete(ActionEvent event) {
        if(supprimerBtn.getText().equals("Desactiver")){
            String sql = "UPDATE users SET actived=0 WHERE id=?";
            try{
                db.initPrep(sql);
                db.getPreState().setInt(1, id);
                db.executeMAJ();
                db.closeConnection();
                //reinitialisation du tableau
                loadTable();
                clearFields();
                Notification.NotifSuccess("Succes", "Etudiant modifier avec succes");
                ajouterBtn.setDisable(false);
                supprimerBtn.setText("Activer");

            }catch (SQLException e){
                throw new RuntimeException();
            }

        }else{
            String sql = "UPDATE users SET actived=1 WHERE id=?";
            try{
                db.initPrep(sql);
                db.getPreState().setInt(1, id);

                db.executeMAJ();
                db.closeConnection();
                //reinitialisation du tableau
                loadTable();
                clearFields();
                Notification.NotifSuccess("Succes", "Etudiant modifier avec succes");
                ajouterBtn.setDisable(false);
                supprimerBtn.setText("Desactiver");

            }catch (SQLException e){
                throw new RuntimeException();
            }
        }



    }

    @FXML
    void update(ActionEvent event) {
        Role profil = getRoleByName(profilCbb.getValue());
        IUser userDao = new UserImpl();
        String sql = "UPDATE users SET nom=?, prenom=?, login=?, password=?, actived=?, profil=? WHERE id=?";
        try{
            db.initPrep(sql);
            db.getPreState().setString(1, nomTxt.getText());
            db.getPreState().setString(2, prenomTxt.getText());
            db.getPreState().setString(3, userDao.generateLogin(nomTxt.getText(),prenomTxt.getText()));//mettrelogin
            db.getPreState().setString(4, passwordTxt.getText());
            db.getPreState().setInt(5, activedCbb.getValue());
            db.getPreState().setInt(6, profil.getId());
            db.getPreState().setInt(7, id);
            db.executeMAJ();
            db.closeConnection();
            //reinitialisation du tableau
            loadTable();
            clearFields();
            Notification.NotifSuccess("Succes", "Etudiant modifier avec succes");
            ajouterBtn.setDisable(false);

        }catch (SQLException e){
            throw new RuntimeException();
        }

    }



//    public void addInputToCbb(){
//        profilCbb.getItems().add()
//    }

    //recupration des utilisateur en base
    private ObservableList<User> getUsers() {
        ObservableList<User> users = FXCollections.observableArrayList();
        String sql = "SELECT * FROM users ORDER BY nom ASC";
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
        ObservableList<User> liste = getUsers();
        userTbl.setItems(liste);

        //il faut specifier le type d'information de chaque colonne
        // set... permet d'affecter une valeur  la cellule
        //new PropertyValueFactory<Etudiant,Integer>("id") permet de recuperer l'informtion et de le donner a la cellule
        idCol.setCellValueFactory(new PropertyValueFactory<User,Integer>("id"));
        nomCol.setCellValueFactory(new PropertyValueFactory<User,String >("nom"));
        prenomCol.setCellValueFactory(new PropertyValueFactory<User,String >("prenom"));
        loginCol.setCellValueFactory(new PropertyValueFactory<User,String >("login"));
        passwordCol.setCellValueFactory(new PropertyValueFactory<User,String >("password"));
        profilCol.setCellValueFactory(new PropertyValueFactory<User,Integer>("profil"));
        activedCol.setCellValueFactory(new PropertyValueFactory<User,Integer>("actived"));
    }


    public void clearFields(){
        nomTxt.setText("");
        prenomTxt.setText("");
        passwordTxt.setText("");
        profilCbb.setValue(null);
        activedCbb.setValue(null);
    }


    @FXML
    void logout(ActionEvent event) {
        try{
            Notification.NotifSuccess("Succes", "Connexion Reussie !");
            Outils.load(event,"Gestion des utilisateurs", "/Fxml/login.fxml");
        }catch (Exception e){
            e.printStackTrace();
        }

    }





}
