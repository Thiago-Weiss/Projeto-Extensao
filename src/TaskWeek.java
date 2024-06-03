import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class TaskWeek {
    private int idTabela;
    private String text;
    private String cor;
    private Label label;
    private VBox box;
    private String diaSemana;

    // constructor do usuario
    public TaskWeek(String texto, String cor, Label label, VBox box, String diaSemana) {
        this.text = texto;
        this.cor = cor;
        this.label = label;
        this.box = box;
        this.diaSemana = diaSemana;
    }

    // constructor do banco de dados
    public TaskWeek(int id, String texto, String cor, String diaSemana) {
        this.idTabela = id;
        this.text = texto;
        this.cor = cor;
        this.diaSemana = diaSemana;
    }

    public void setDiaSemana(String dia) {
        this.diaSemana = dia;
        InterfaceBancoDados.attTarefaDiasemana(idTabela, dia);
    }

    public void setIdTabela(int id) {
        this.idTabela = id;
    }

    public void setLabel(Label label) {
        this.label = label;
    }

    public void setBox(VBox box) {
        this.box = box;
    }

    public String getText() {
        return text;
    }

    public String getCor() {
        return cor;
    }

    public VBox getBox() {
        return box;
    }

    public Label getLabel() {
        return label;
    }

    public String getDia() {
        return diaSemana;
    }

    public int getIdTabela() {
        return idTabela;
    }

}
