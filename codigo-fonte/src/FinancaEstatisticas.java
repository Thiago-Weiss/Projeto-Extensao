import java.util.List;
import java.util.stream.DoubleStream;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.Number;
import jxl.write.biff.RowsExceededException;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class FinancaEstatisticas {
    private String filtro;
    private List<Pedido> listaPedidos;
    private int qntPedidos;
    private double[] listTotalCustos = { 0 };
    private double totalCustos;
    private double[] listTotalPago = { 0 };
    private double totalPago;

    public FinancaEstatisticas(List<Pedido> pedidosList, String filtro) {
        this.filtro = filtro;
        this.listaPedidos = pedidosList;
        qntPedidos = pedidosList.size();

        listTotalCustos = pedidosList.stream().filter(pedido -> pedido.getCustos() != null && pedido.getCustos() != 0)
                .mapToDouble(Pedido::getCustos).toArray();
        totalCustos = DoubleStream.of(listTotalCustos).sum();

        listTotalPago = listaPedidos.stream()
                .filter(pedido -> pedido.getValorPago() != null && pedido.getValorPago() != 0)
                .mapToDouble(Pedido::getValorPago).toArray();
        totalPago = DoubleStream.of(listTotalPago).sum();

    }

    public List<Pedido> getListPedidos() {
        return listaPedidos;
    }

    public int getQtnPedidos() {
        return qntPedidos;
    }

    /////////// CUSTOS ///////////
    /////////// CUSTOS ///////////
    /////////// CUSTOS ///////////
    public double getCustosTotal() {
        return totalCustos;
    }

    public double getCustosMedio() {
        return totalCustos / listTotalCustos.length;
    }

    public int gCustosetQnt() {
        return listTotalCustos.length;
    }

    /////////// PAGO ///////////
    /////////// PAGO ///////////
    /////////// PAGO ///////////
    public double getPagoTotal() {
        return totalPago;
    }

    public double getPagoMedio() {
        return totalPago / listTotalPago.length;
    }

    public int gPagoetQnt() {
        return listTotalPago.length;
    }

    /////////// LUCRO ///////////
    /////////// LUCRO ///////////
    /////////// LUCRO ///////////
    public double getLucroTotal() {
        return totalPago - totalCustos;
    }

    public double getLucroMedio() {
        return (totalPago - totalCustos) / qntPedidos;
    }

    public int gLucroetInt() {
        return qntPedidos;
    }

    /////////// EXCEL ///////////
    /////////// EXCEL ///////////
    /////////// EXCEL ///////////
    public void gerarRelatorio() {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            String nome = "Estatisticas " + LocalDate.now().format(formatter) + ".xls";
            WritableWorkbook workbook = Workbook.createWorkbook(new File(nome));
            WritableSheet resumo = workbook.createSheet("Resumo", 0);
            WritableSheet pedidosTabela = workbook.createSheet("Dados dos pedidos", 1);
// purgatorio
            escreverResumo(resumo);
            escreverPedidos(pedidosTabela);

            workbook.write();
            workbook.close();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erro ao gerar o excel");
            System.out.println("Contate o suporte e detalhe o que foi feito na hora do erro");
        } catch (WriteException e) {
            e.printStackTrace();
            System.out.println("Erro ao gerar o excel");
            System.out.println("Contate o suporte e detalhe o que foi feito na hora do erro");
        }
    }

    private void escreverResumo(WritableSheet sheet) throws RowsExceededException, WriteException {

        // formatar o titulo e escrever
        sheet.mergeCells(0, 0, 5, 0);
        sheet.setRowView(0, 20 * 20);
        WritableFont font = new WritableFont(WritableFont.ARIAL, 14, WritableFont.BOLD);
        WritableCellFormat cellFormat = new WritableCellFormat(font);
        cellFormat.setAlignment(Alignment.CENTRE);
        cellFormat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
        Label label = new Label(0, 0, filtro, cellFormat);
        sheet.addCell(label);

        // formatar as celulas
        sheet.setColumnView(0, 20);
        sheet.setColumnView(1, 15);
        sheet.setRowView(2, 20 * 20);
        sheet.setRowView(5, 20 * 20);
        sheet.setRowView(8, 20 * 20);


        sheet.addCell(new Label(0, 1, "Quantidade de pedidos"));
        sheet.addCell(new Number(1, 1, qntPedidos));

        // valores recebidos
        sheet.mergeCells(0, 2, 1, 2);
        sheet.addCell(new Label(0, 2, "Valores recebidos", cellFormat));

        sheet.addCell(new Label(0, 3, "Total recebido:"));
        sheet.addCell(new Label(1, 3, String.format("R$: %.2f", getPagoTotal())));

        sheet.addCell(new Label(0, 4, "Media por pedido:"));
        sheet.addCell(new Label(1, 4, String.format("R$: %.2f", getPagoMedio())));

        // custos
        sheet.mergeCells(0, 5, 1, 5);
        sheet.addCell(new Label(0, 5, "Custos", cellFormat));

        sheet.addCell(new Label(0, 6, "Custos total:"));
        sheet.addCell(new Label(1, 6, String.format("R$: %.2f", getCustosTotal())));

        sheet.addCell(new Label(0, 7, "Media por pedido:"));
        sheet.addCell(new Label(1, 7, String.format("R$: %.2f", getCustosMedio())));

        // lucro
        sheet.mergeCells(0, 8, 1, 8);
        sheet.addCell(new Label(0, 8, "Lucro", cellFormat));

        sheet.addCell(new Label(0, 9, "Lucro total:"));
        sheet.addCell(new Label(1, 9, String.format("R$: %.2f", getLucroTotal())));

        sheet.addCell(new Label(0, 10, "Media por pedido:"));
        sheet.addCell(new Label(1, 10, String.format("R$: %.2f", getLucroMedio())));

    }

    private void escreverPedidos(WritableSheet sheet) throws RowsExceededException, WriteException {

        sheet.mergeCells(0, 0, 10, 0);
        sheet.setRowView(0, 40 * 20);

        WritableFont font = new WritableFont(WritableFont.ARIAL, 14, WritableFont.BOLD);
        WritableCellFormat cellFormat = new WritableCellFormat(font);
        cellFormat.setAlignment(Alignment.CENTRE);
        cellFormat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);

        Label label = new Label(0, 0, "Dados dos Pedidos", cellFormat);
        sheet.addCell(label);

        sheet.setColumnView(0, 15);
        sheet.setColumnView(1, 15);
        sheet.setColumnView(2, 15);
// purgatorio
        sheet.addCell(new Label(0, 1, "Nome"));
        sheet.addCell(new Label(1, 1, "Telefone"));
        sheet.addCell(new Label(2, 1, "CPF/CNPJ"));
        sheet.addCell(new Label(3, 1, "Endereço da entrega"));
        sheet.addCell(new Label(4, 1, "Data do Pedido"));
        sheet.addCell(new Label(5, 1, "Data da Entrega"));
        sheet.addCell(new Label(6, 1, "Total"));
        sheet.addCell(new Label(7, 1, "Pago"));
        sheet.addCell(new Label(8, 1, "Lucro"));
        sheet.addCell(new Label(9, 1, "Custos"));
        sheet.addCell(new Label(10, 1, "Descrição"));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        int i = 2;
        for (Pedido p : listaPedidos) {
            sheet.addCell(new Label(0, i, p.getNomeCliente()));
            sheet.addCell(new Label(1, i, p.getTelefoneCliente()));
            sheet.addCell(new Label(2, i, p.getCpfCliente()));
            sheet.addCell(new Label(3, i, p.getEnderecoEntrega()));
            sheet.addCell(new Label(4, i, p.getDataCriacao().format(formatter)));
            sheet.addCell(new Label(5, i, p.getDataEntrega().format(formatter)));
            Double precoPedido = (p.getPrecoPedido() != null) ? p.getPrecoPedido() : 0.0;
            Double valorPago = (p.getValorPago() != null) ? p.getValorPago() : 0.0;
            Double custos = (p.getCustos() != null) ? p.getCustos() : 0.0;
            double lucro = valorPago - custos;
            sheet.addCell(new Number(6, i, precoPedido));
            sheet.addCell(new Number(7, i, valorPago));
            sheet.addCell(new Number(8, i, lucro));
            sheet.addCell(new Number(9, i, custos));
            sheet.addCell(new Label(10, i, p.getDescricaoPedido()));
            i++;
        }
    }

}
