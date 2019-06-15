import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Map;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.encoding.WinAnsiEncoding;

public class PDFcreator {
    private String filename;
    private LocalDate today = LocalDate.now();
    private String outputPath;

    public String getFilename() {
        return filename;
    }

    public String getOutputPath() {
        return outputPath;
    }


    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }


    public PDFcreator() {

        this.filename = "Project_Report_" + today.toString() + ".pdf";
        Path currentRelativePath = Paths.get("");
        this.outputPath = currentRelativePath.toAbsolutePath().toString();

    }

    public PDFcreator(String outputPath) {

        this.outputPath = outputPath;
        this.filename = "Project_Report_" + today.toString() + ".pdf";
    }


    public void createPDF(Map<String, Double> rep) throws IOException {


            PDDocument document = new PDDocument();

            PDPage summaryPage = new PDPage();
            document.addPage(summaryPage);


            try (PDPageContentStream contentStream = new PDPageContentStream(document, summaryPage)) {
                contentStream.beginText();
                contentStream.setFont( PDType1Font.TIMES_ROMAN, 12 );
                contentStream.newLineAtOffset(100,700);
                contentStream.setLeading(14.5f);
                contentStream.showText("Employee / Total hours worked");
                contentStream.newLine();

                Integer count = 1;
                for(Map.Entry<String, Double> entry: rep.entrySet()) {
                    String line = removeNonWinChars(count.toString()
                                                            + ": "
                                                            + entry.getKey()
                                                            + " : "
                                                            + entry.getValue());

                    contentStream.newLine();
                    contentStream.showText(line);
                    contentStream.newLine();
                    count++;
                }

                contentStream.endText();
            }

               document.save(outputPath + "\\" + filename);
               document.close();


               //test

//              File pdfFile = new File(outputPath + "\\" + filename);
//
//              Desktop.getDesktop().open(pdfFile);

        }



    private String removeNonWinChars(String text) {

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            if (WinAnsiEncoding.INSTANCE.contains(text.charAt(i))) {
                result.append(text.charAt(i));
            }
        }

        return result.toString();

    }

}
