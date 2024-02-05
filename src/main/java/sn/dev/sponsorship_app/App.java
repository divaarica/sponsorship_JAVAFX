package sn.dev.sponsorship_app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        //creation d'une page
        //FXMLLoader.load(getClass().getResource("")) permet de charger un fichier xml
        Parent parent = FXMLLoader.load(getClass().getResource("/Fxml/login.fxml"));
        Scene scene = new Scene(parent);
        stage.setTitle("Connexion");
        stage.setScene(scene);
        stage.show();


    }

    //Methode main qui est la methode d'entree
    public static void main(String[] args){
//        DBConnection db = new DBConnection();
//        db.initPrep("SELECT * FROM user");
        launch();


    }
}
