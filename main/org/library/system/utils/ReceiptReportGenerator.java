package org.library.system.utils;

import org.library.system.model.Book;
import org.library.system.model.Loan;
import org.library.system.model.User;

/**
 * Clase utilitaria para generar el comprobante de prestamo en PDF
 * usando JasperReports.
 *
 * INSTRUCCIONES PARA EL EQUIPO:
 * 1. Agregar la dependencia de JasperReports en el pom.xml o en las librerias del proyecto.
 *    - jasperreports-x.x.x.jar
 *    - commons-digester-x.x.jar
 *    - commons-collections4-x.x.jar
 *    - commons-beanutils-x.x.jar
 *    - commons-logging-x.x.jar
 *    - ecj-x.x.jar (opcional)
 *    - itext-x.x.x.jar (para PDF)
 *
 * 2. Disenar el reporte en Jaspersoft Studio y guardar el archivo .jrxml y .jasper
 *    en la carpeta: src/main/resources/org/library/system/reports/
 *
 * 3. Completar el metodo generateReceipt() con la logica de llenado del reporte.
 */
public class ReceiptReportGenerator {

    private ReceiptReportGenerator() {}

    /**
     * Genera el comprobante de prestamo en PDF.
     *
     * @param loan    Prestamo generado
     * @param student Estudiante que recibe el libro
     * @param book    Libro prestado
     * @param receiptNumber Numero de comprobante (puede ser loan_id)
     * @return ruta del archivo PDF generado, o null si falla
     */
    public static String generateReceipt(Loan loan, User student, Book book, int receiptNumber) {
        // TODO: Implementar con JasperReports
        //
        // Ejemplo de implementacion:
        //
        // try {
        //     // 1. Cargar el archivo .jasper compilado
        //     InputStream reportStream = ReceiptReportGenerator.class.getResourceAsStream(
        //             "/org/library/system/reports/receipt.jasper"
        //     );
        //
        //     // 2. Preparar los parametros del reporte
        //     Map<String, Object> params = new HashMap<>();
        //     params.put("studentName", student.getFirst_name() + " " + student.getLast_name());
        //     params.put("studentCode", student.getUser_code());
        //     params.put("studentEmail", student.getEmail());
        //     params.put("bookTitle", book.getTitle());
        //     params.put("bookIsbn", book.getIsbn());
        //     params.put("bookAuthor", book.getAuthor());
        //     params.put("loanDate", loan.getLoan_date());
        //     params.put("dueDate", loan.getDue_date());
        //     params.put("receiptNumber", receiptNumber);
        //
        //     // 3. Compilar y llenar el reporte
        //     JasperReport jasperReport = (JasperReport) JRLoader.loadObject(reportStream);
        //     JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params);
        //
        //     // 4. Exportar a PDF
        //     String outputPath = System.getProperty("user.home") + "/Desktop/"
        //             + "comprobante_" + receiptNumber + ".pdf";
        //     JasperExportManager.exportReportToPdfFile(jasperPrint, outputPath);
        //
        //     return outputPath;
        //
        // } catch (JRException e) {
        //     System.err.println("Error al generar el reporte: " + e.getMessage());
        //     return null;
        // }
        return null;
    }
}