//package sn.dev.sponsorship_app;
//
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.event.ActionEvent;
//import javafx.fxml.FXML;
//import javafx.fxml.Initializable;
//import javafx.scene.control.Button;
//import javafx.scene.control.TableColumn;
//import javafx.scene.control.TableView;
//import javafx.scene.control.TextField;
//import javafx.scene.control.cell.PropertyValueFactory;
//import javafx.scene.input.MouseEvent;
//import sn.dev.sponsorship_app.controllers.SessionManager;
//import sn.dev.sponsorship_app.tools.Notification;
//
//import java.net.URL;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Timestamp;
//import java.time.LocalDateTime;
//import java.util.ResourceBundle;
//
//public class ElecteurProfilController implements Initializable {
//
//    private int idUtil;
//    private DBConnection db = new DBConnection();
//
//    private ResultSet rs;
//    @FXML
//    private TableView<User> candidatTbl;
//
//    @FXML
//    private TableColumn<User, String> nomCol;
//
//    @FXML
//    private TextField nomTft;
//
//    @FXML
//    private TableColumn<User, Integer> numeroCol;
//
//    @FXML
//    private Button parrainerBtn;
//
//    @FXML
//    private TableColumn<User, String> prenomCol;
//
//    @FXML
//    private TextField prenomTft;
//
//    @FXML
//    void creer_parrainage(ActionEvent event) {
//
//
//        String sql = "INSERT INTO parrainer( dateParrainage, electeur, candidat) VALUES (?,?,?)";
//        try {
//            db.initPrep(sql);
//            LocalDateTime currentDateTime = LocalDateTime.now();
//            db.getPreState().setTimestamp(1, Timestamp.valueOf(currentDateTime));
//            int loggedInUserId = SessionManager.getInstance().getLoggedInUser().getId();
//            db.getPreState().setInt(2, loggedInUserId);
//            db.getPreState().setInt(3, idUtil);
//            db.executeMAJ();
//            vider_champ();
//            Notification.NotifSuccess("Succés !", "Parrainage effectué avec succès!");
//        } catch (SQLException e) {
//            vider_champ();
//            throw new RuntimeException();
//        }
//        parrainerBtn.setDisable(true);
//    }
//
//    @FXML
//    void getData(MouseEvent event) {
//        User util = candidatTbl.getSelectionModel().getSelectedItem();
//        idUtil = util.getId();
//        //etatUtil = util.getActived();
//        nomTft.setText(util.getNom());
//        prenomTft.setText(util.getPrenom());
//        if (! check_parrainage() && check_etat(SessionManager.getInstance().getLoggedInUser()) && check_nb_electeur(util.getId())) {
//            parrainerBtn.setDisable(false);
//        }
//    }
//
//    public void vider_champ(){
//        nomTft.setText("");
//        prenomTft.setText("");
//    }
//
//    public ObservableList<User> getUtilisateurs(){
//        ObservableList<User> users = FXCollections.observableArrayList();
//        String sql = "SELECT * FROM users WHERE profil = 3 ORDER BY nom ASC";
//        try {
//            db.initPrep(sql);
//            ResultSet rs = db.executeSelect();
//            while (rs.next()){
//                User Util = new User();
//                Util.setId(rs.getInt("id"));
//                Util.setNom(rs.getString("nom"));
//                Util.setPrenom(rs.getString("prenom"));
//                Util.setLogin(rs.getString("login"));
//                Util.setPassword(rs.getString("password"));
//                Util.setActived(rs.getInt("actived"));
//                Util.setProfil(trouveRole(rs.getInt("profil")));
//                users.add(Util);
//            }
//        }catch (SQLException e){
//            throw new RuntimeException();
//        }
//        return users;
//    }
//
//    public boolean check_parrainage(){
//        String sql = "SELECT * FROM parrainer WHERE electeur = ?";
//        try {
//            db.initPrep(sql);
//            db.getPreState().setInt(1, SessionManager.getInstance().getLoggedInUser().getId());
//            rs = db.executeSelect();
//            while(rs.next()){
//                Notification.NotifError("Erreur !", "Impossible de parrainer: vous parrainer déjà un candidat!");
//                return true;
//            }
//        }catch (SQLException e){
//            throw new RuntimeException();
//        }
//        return false;
//    }
//
//    public boolean check_etat(Utilisateur U){
//        if (U.getActived() == 0) {
//            String sql = "DELETE FROM parrainer WHERE electeur = ?";
//            try {
//                db.initPrepar(sql);
//                db.getPstm().setInt(1, SessionManager.getInstance().getLoggedInUser().getId());
//                db.executeMaj();
//            } catch (SQLException e) {
//                throw new RuntimeException();
//            }
//            Notification.NotifError("Erreur !", "Impossible de parrainer: votre compte a été désactivé!");
//            return false;
//
//        }
//        return true;
//    }
//
//    public boolean check_nb_electeur(int idCand){
//
//        String sql = "SELECT COUNT(*) FROM parrainer WHERE candidat = ?";
//        try {
//            db.initPrepar(sql);
//            db.getPstm().setInt(1, idCand);
//            ResultSet rs = db.executeSelect();
//            while (rs.next()){
//                if (rs.getInt(1) == 7){
//                    Notification.NotifError("Erreur !", "Impossible de parrainer: Ce cqndidat a déjà atteint son quotat maximale!");
//                    return false;
//                }
//            }
//            return true;
//        } catch (SQLException e) {
//            throw new RuntimeException();
//        }
//    }
//
//    public Role trouveRole(int id){
//        Role r = new Role();
//        String sql = "SELECT * FROM role WHERE id = ?";
//        try {
//            db.initPrepar(sql);
//            db.getPstm().setInt(1, id);
//            ResultSet rs = db.executeSelect();
//            while (rs.next()){
//                r.setId(rs.getInt("id"));
//                r.setName(rs.getString("name"));
//                r.setEtat(rs.getInt("etat"));
//            }
//        }catch (SQLException e){
//            throw new RuntimeException();
//        }
//        return r;
//    }
//
//    public void load_table(){
//
//        ObservableList<Utilisateur> liste = getUtilisateurs();
//        System.out.println(liste.get(0));
//        System.out.println(candidatTbl);
//        candidatTbl.setItems(liste);
//        numeroCol.setCellValueFactory(new PropertyValueFactory<Utilisateur, Integer>("id"));
//        nomCol.setCellValueFactory(new PropertyValueFactory<Utilisateur, String>("nom"));
//        prenomCol.setCellValueFactory(new PropertyValueFactory<Utilisateur, String>("prenom"));
//
//
//    }
//
//    @Override
//    public void initialize(URL url, ResourceBundle resourceBundle) {
//        System.out.println("initialize() called");
//        load_table();
//        parrainerBtn.setDisable(true);
//    }
//}
