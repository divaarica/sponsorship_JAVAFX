package sn.dev.sponsorship_app.tools;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;

public class Outils {


    //methode qui permet de rediriger vers une autre page
    private void loadPage(ActionEvent event, String title, String url) throws IOException{
        ((Node) event.getSource()).getScene().getWindow().hide();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(url));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle(title);
        stage.show();
    }

    //vu que la methode loadPagee st priveealorson va passer pa cette methode pour y acceder
    public static void load(ActionEvent event, String title, String url) throws IOException{
        //on instancie un objet de la classe Outil et on appelle la methode loadPage
        new Outils().loadPage(event, title, url);
    }
}