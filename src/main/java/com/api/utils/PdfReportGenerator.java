//package com.api.utils;
//
//import com.itextpdf.io.font.constants.StandardFonts;
//import com.itextpdf.kernel.colors.Color;
//import com.itextpdf.kernel.colors.DeviceRgb;
//import com.itextpdf.kernel.events.Event;
//import com.itextpdf.kernel.events.IEventHandler;
//import com.itextpdf.kernel.events.PdfDocumentEvent;
//import com.itextpdf.kernel.font.PdfFont;
//import com.itextpdf.kernel.font.PdfFontFactory;
//import com.itextpdf.kernel.pdf.PdfDocument;
//import com.itextpdf.kernel.pdf.PdfWriter;
//import com.itextpdf.kernel.geom.PageSize;
//import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
//import com.itextpdf.layout.*;
//import com.itextpdf.kernel.colors.Color;
//import com.itextpdf.layout.Style;
//import com.itextpdf.layout.borders.SolidBorder;
//import com.itextpdf.layout.element.*;
//import com.itextpdf.layout.properties.HorizontalAlignment;
//import com.itextpdf.layout.properties.TextAlignment;
//import com.itextpdf.layout.properties.UnitValue;
//import org.json.JSONObject;
//
//import java.io.File;
//import java.nio.charset.StandardCharsets;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//
//public class PdfReportGenerator {
//
//    private final Color BG_HEADER = new DeviceRgb(44, 62, 80);      // dark slate
//    private final Color GREEN = new DeviceRgb(39, 174, 96);
//    private final Color RED   = new DeviceRgb(231, 76, 60);
//    private final Color GREY  = new DeviceRgb(236, 240, 241);
//    private final Color TEXT  = new DeviceRgb(51, 51, 51);
//
//    private final Document doc;
//    private final PdfDocument pdf;
//    private int total = 0, passed = 0, failed = 0;
//
//    public PdfReportGenerator(String fileName) throws Exception {
//        File outDir = new File("reports");
//        if (!outDir.exists()) outDir.mkdirs();
//
//        PdfWriter writer = new PdfWriter(new File(outDir, fileName));
//        pdf = new PdfDocument(writer);
//        pdf.setDefaultPageSize(PageSize.A4);
//        pdf.addEventHandler(PdfDocumentEvent.END_PAGE, new Footer());
//        doc = new Document(pdf);
//        doc.setMargins(36, 36, 54, 36); // top,right,bottom,left
//
//        addHeader();
//        addMeta();
//        addSummaryTableHeader();
//    }
//
//    private void addHeader() throws Exception {
//        PdfFont bold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
//        Paragraph title = new Paragraph("API Test Execution Report")
//                .setFont(bold).setFontSize(16).setFontColor(new DeviceRgb(255, 255, 255))
//                .setTextAlignment(TextAlignment.CENTER).setMargin(0).setPaddingTop(8).setPaddingBottom(8);
//
//        Div bar = new Div()
//                .setBackgroundColor(BG_HEADER)
//                .setBorderRadius(new com.itextpdf.layout.properties.BorderRadius(6))
//                .setPadding(6).setMarginBottom(12);
//        bar.add(title);
//        doc.add(bar);
//    }
//
//    private void addMeta() throws Exception {
//        PdfFont reg = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
//        Paragraph meta = new Paragraph("Generated: " +
//                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
//                .setFont(reg).setFontSize(10).setFontColor(TEXT);
//        doc.add(meta);
//        doc.add(new Paragraph("\n"));
//    }
//
//    private Table summaryTable;
//
//    private void addSummaryTableHeader() {
//        summaryTable = new Table(UnitValue.createPercentArray(new float[]{40, 12, 12, 16, 20}))
//                .useAllAvailableWidth();
//
//        summaryTable.addHeaderCell(headerCell("Scenario"));
//        summaryTable.addHeaderCell(headerCell("Expected"));
//        summaryTable.addHeaderCell(headerCell("Actual"));
//        summaryTable.addHeaderCell(headerCell("Result"));
//        summaryTable.addHeaderCell(headerCell("Timestamp"));
//    }
//
//    private Cell headerCell(String text) {
//        return new Cell().add(new Paragraph(text).setFontSize(11).setFontColor(new DeviceRgb(255, 255, 255)))
//                .setBackgroundColor(BG_HEADER)
//                .setPadding(6);
//    }
//
//    private Cell cell(String text) {
//        return new Cell().add(new Paragraph(text).setFontSize(10).setFontColor(TEXT)).setPadding(6);
//    }
//
//    private Cell pill(String text, boolean pass) {
//        Color bg = pass ? GREEN : RED;
//        return new Cell().add(new Paragraph(text).setFontSize(10).setFontColor(new DeviceRgb(255, 255, 255)))
//                .setBackgroundColor(bg).setTextAlignment(TextAlignment.CENTER)
//                .setPadding(6);
//    }
//
//    /** Pretty-print JSON safely; falls back to raw if not valid JSON */
//    private String pretty(String jsonOrText) {
//        try {
//            if (jsonOrText == null || jsonOrText.isBlank()) return "";
//            return new JSONObject(jsonOrText).toString(2);
//        } catch (Exception e) {
//            return jsonOrText; // not valid JSON; return as-is
//        }
//    }
//
//    public void addTestResult(String scenario, String requestBody, String responseBody,
//                              int expectedStatus, int actualStatus, boolean pass) throws Exception {
//        total++;
//        if (pass) passed++; else failed++;
//
//        // Row in compact summary table
//        summaryTable.addCell(cell(scenario));
//        summaryTable.addCell(cell(String.valueOf(expectedStatus)));
//        summaryTable.addCell(cell(String.valueOf(actualStatus)));
//        summaryTable.addCell(pill(pass ? "PASS" : "FAIL", pass));
//        summaryTable.addCell(cell(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))));
//
//        // Detailed card per test
//        Div card = new Div()
//                .setBorder(new SolidBorder(new DeviceRgb(220, 223, 226), 1))
//                .setBorderRadius(new com.itextpdf.layout.properties.BorderRadius(8))
//                .setFontColor(new DeviceRgb(255, 255, 255))
//                .setPadding(12).setMarginTop(10).setMarginBottom(6);
//
//        // Title + status
//        Paragraph title = new Paragraph(scenario)
//                .setBold().setFontSize(12).setFontColor(TEXT).setMarginBottom(6);
//        card.add(title);
//
//        Table info = new Table(UnitValue.createPercentArray(new float[]{20, 30, 20, 30}))
//                .useAllAvailableWidth();
//        info.addCell(lightCell("Expected"));
//        info.addCell(lightCell(String.valueOf(expectedStatus)));
//        info.addCell(lightCell("Actual"));
//        info.addCell(lightCell(String.valueOf(actualStatus)));
//        info.addCell(lightCell("Result"));
//        Cell rs = lightCell(pass ? "PASS" : "FAIL");
//        rs.setBackgroundColor(pass ? new DeviceRgb(231, 245, 233) : new DeviceRgb(253, 235, 236));
//        rs.setFontColor(pass ? GREEN : RED).setBold();
//        info.addCell(rs);
//        info.addCell(lightCell("Time"));
//        info.addCell(lightCell(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
//        card.add(info.setMarginBottom(8));
//
//        // Request/Response blocks
//        card.add(box("Request", pretty(requestBody)));
//        card.add(box("Response", pretty(responseBody)));
//
//        doc.add(card);
//    }
//
//    private Cell lightCell(String text) {
//        return new Cell().add(new Paragraph(text).setFontSize(10).setFontColor(TEXT)).setPadding(6);
//    }
//
//    private Div box(String title, String content) {
//        Div wrap = new Div().setBackgroundColor(GREY)
//                .setBorderRadius(new com.itextpdf.layout.properties.BorderRadius(6))
//                .setPadding(8).setMarginTop(4);
//
//        Paragraph cap = new Paragraph(title).setBold().setFontSize(10).setFontColor(new DeviceRgb(52, 152, 219))
//                .setMarginBottom(4);
//        wrap.add(cap);
//
//        Paragraph pre = new Paragraph(content == null ? "" : content)
//                .setFontSize(9)
//                .setTextAlignment(TextAlignment.LEFT);
//        wrap.add(pre);
//        return wrap;
//    }
//
//    public void closeReport() {
//        // Add compact summary table first, then totals
//        doc.add(new Paragraph("\n"));
//        doc.add(new Paragraph("Summary").setBold().setFontSize(12).setFontColor(TEXT).setMarginBottom(6));
//        doc.add(summaryTable);
//
//        // Totals bar
//        Div totals = new Div().setMarginTop(12);
//        totals.add(stat("Total", total, BG_HEADER, new DeviceRgb(255, 255, 255)));
//        totals.add(stat("Passed", passed, GREEN, new DeviceRgb(255, 255, 255)));
//        totals.add(stat("Failed", failed, RED, new DeviceRgb(255, 255, 255)));
//        doc.add(totals);
//
//        doc.close();
//    }
//
//    private Div stat(String label, int value, Color bg, Color fg) {
//        Div d = new Div().setBackgroundColor(bg)
//                .setBorderRadius(new com.itextpdf.layout.properties.BorderRadius(6))
//                .setPaddingLeft(10).setPaddingRight(10).setPaddingTop(6).setPaddingBottom(6)
//                .setMarginRight(6).setMarginBottom(6)
//                .setWidth(UnitValue.createPercentValue(33))
//                .setHorizontalAlignment(HorizontalAlignment.LEFT);
//        Paragraph p = new Paragraph(label + ": " + value).setFontColor(fg).setBold().setFontSize(10);
//        d.add(p);
//        return d;
//    }
//
//    /** Simple footer with page numbers */
//    private class Footer implements IEventHandler {
//        @Override
//        public void handleEvent(Event event) {
//            PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
//            int pageNum = docEvent.getDocument().getPageNumber(docEvent.getPage());
//            String text = "Page " + pageNum;
//            PdfCanvas canvas = new PdfCanvas(docEvent.getPage());
//            new com.itextpdf.layout.Canvas(canvas, docEvent.getPage().getPageSize())
//                    .showTextAligned(text, docEvent.getPage().getPageSize().getWidth() - 40, 20,
//                            TextAlignment.RIGHT);
//            canvas.release();
//        }
//    }
//}
