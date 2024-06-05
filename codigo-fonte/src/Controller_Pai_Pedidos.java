import java.net.URL;
import java.time.LocalDate;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextInputControl;

public class Controller_Pai_Pedidos extends ControllerUtils {

    protected void voltarMenu(ActionEvent event) {
        URL janela = getClass().getResource("interfaces/Menu.fxml");
        String janelaName = "Menu";
        switchWindow(event, janela, janelaName);
    }

    //////////////////////// GETTERS ////////////////////////
    //////////////////////// GETTERS ////////////////////////
    //////////////////////// GETTERS ////////////////////////

    protected Double getCampoInDouble(TextField campo) {
        String valor = campo.getText().substring(4).replace(",", ".");
        if (!(valor.isBlank() || valor.equals("."))) {
            return Double.parseDouble(valor);
        }
        return null;
    }

    protected String getCampoText(TextInputControl campo) {
        String texto = campo.getText();
        if (texto == null) {
            return null;
        }
        texto = texto.trim();
        if (texto.isBlank()) {
            return null;
        }
        return texto;
    }

    //////////////////////// FORMATACAO ////////////////////////
    //////////////////////// FORMATACAO ////////////////////////
    //////////////////////// FORMATACAO ////////////////////////

    protected void setUpCamposDoubles(TextField campoTotal, TextField campoPago, Label textFaltaPagar,
            TextField campoCustos) {

        campoPago.textProperty().addListener((observable, oldValue, newValue) -> {
            attTextValor(campoTotal, campoPago, textFaltaPagar);
        });
        campoTotal.textProperty().addListener((observable, oldValue, newValue) -> {
            attTextValor(campoTotal, campoPago, textFaltaPagar);
        });

        attTextValor(campoTotal, campoPago, textFaltaPagar);

        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            if (Pattern.matches("R\\$: [0-9]{0,4}([.,][0-9]{0,2})?", newText)) {
                return change;
            }
            return null;
        };// purgatorio

        campoTotal.setTextFormatter(new TextFormatter<>(filter));
        campoPago.setTextFormatter(new TextFormatter<>(filter));
        campoCustos.setTextFormatter(new TextFormatter<>(filter));
    }

    protected boolean checagemCampos(String texto, TextInputControl campo) {
        if (texto == null) {
            alertaCampoErrado(campo);
            return true;
        }
        return false;
    }

    protected boolean checagemCampos(LocalDate text, DatePicker campo) {
        if (text == null) {
            alertaCampoErrado(campo);
            return true;
        }
        return false;
    }

    private static void attTextValor(TextField campoTotal, TextField campoPago, Label textFaltaPagar) {
        String textoCampoTotal = campoTotal.getText().substring(4).replace(",", ".");
        String textoCampoPago = campoPago.getText().substring(4).replace(",", ".");
        if (textoCampoPago.isBlank() || textoCampoPago.equals(".")) {
            textoCampoPago = "0";
        }
        if (textoCampoTotal.isBlank() || textoCampoTotal.equals(".")) {
            textoCampoTotal = "0";
        }

        double diferenca = Double.parseDouble(textoCampoTotal) - Double.parseDouble(textoCampoPago);

        if (diferenca > 0) {
            textFaltaPagar.setText(String.format("Falta pagar: R$ %.2f", diferenca));
        } else {
            textFaltaPagar.setText("Pedido pago");
        }

    }

}
