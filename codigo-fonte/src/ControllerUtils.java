import java.io.IOException;
import java.net.URL;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.scene.control.TextInputControl;
import javafx.stage.Stage;

public class ControllerUtils {

    protected void setLimitCaracters(TextInputControl campo, int length) {
        campo.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > length) {
                campo.setText(oldValue);
            }
        });
    }

    protected void switchWindow(ActionEvent event, URL janela, String janelaName) {
        try {
            Parent root = FXMLLoader.load(janela);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setTitle(janelaName);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void alertaCampoErrado(Control campo) {
        campo.setStyle("-fx-border-color: red ; -fx-border-width: 2px;");
        campo.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) { // Quando o campo ganha foco
                campo.setStyle("");
            }
        });
    }

}
