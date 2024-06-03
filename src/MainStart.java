import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class MainStart extends Application {

    @Override
    public void start(Stage primaryStage) {

        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("interfaces/Menu.fxml"));
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Menu");
            try {
                Image icon = new Image(getClass().getResourceAsStream("icone.png"));
                primaryStage.getIcons().add(icon);
            } catch (Exception e) {
                System.out.println("erro no icone/icon");
            }
            primaryStage.setResizable(false);
            primaryStage.setMaximized(false);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/confeitariaBD";
        String user = "postgres";
        String password = "2015";
        InterfaceBancoDados.getInstance(url, user, password);
        InterfaceBancoDados.startDataPedido();
        InterfaceBancoDados.startDataTask();
        launch(args);
    }
}


