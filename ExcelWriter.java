import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import java.io.File;
import java.io.IOException;

public class ExcelWriter {

    public static void main(String[] args) throws WriteException {
        try {
            // Criar um novo arquivo Excel
            WritableWorkbook workbook = Workbook.createWorkbook(new File("arquivo_excel.xls"));

            // Criar uma nova planilha
            WritableSheet sheet = workbook.createSheet("Planilha1", 0);

            // Adicionar dados à planilha
            addData(sheet);

            // Escrever e fechar o arquivo Excel
            workbook.write();
            workbook.close();
            
            System.out.println("Arquivo Excel criado com sucesso.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void addData(WritableSheet sheet) {
        try {
            // Adicionar cabeçalhos
            sheet.addCell(new Label(0, 0, "Nome"));
            sheet.addCell(new Label(1, 0, "Dia"));
            sheet.addCell(new Label(2, 0, "Quantidade"));

            // Adicionar dados
            sheet.addCell(new Label(0, 1, "Item1"));
            sheet.addCell(new Label(1, 1, "2023-01-01"));
            sheet.addCell(new jxl.write.Number(2, 1, 10));

            sheet.addCell(new Label(0, 2, "Item2"));
            sheet.addCell(new Label(1, 2, "2023-01-02"));
            sheet.addCell(new jxl.write.Number(2, 2, 20));

            sheet.addCell(new Label(0, 3, "Item3"));
            sheet.addCell(new Label(1, 3, "2023-01-03"));
            sheet.addCell(new jxl.write.Number(2, 3, 30));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
