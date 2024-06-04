import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.format.Alignment;
import java.io.File;
import java.io.IOException;

public class ExcelWriter {

    public static void main(String[] args) throws WriteException {
        try {

            WritableWorkbook workbook = Workbook.createWorkbook(new File("arquivo_excel.xls"));
            WritableSheet sheet = workbook.createSheet("Planilha1", 0);

            addData(sheet);

            workbook.write();
            workbook.close();

            System.out.println("Arquivo Excel criado com sucesso.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void addData(WritableSheet sheet) {
        try {




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
