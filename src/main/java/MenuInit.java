import org.apache.commons.cli.*;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;

class MenuInit {


         static void appStarter(String[] args) throws Exception {
             ArrayList<String> filesCreated = new ArrayList<>();

            Options options = new Options();


            options.addOption("p","path", true, "path to search recursively " +
                    "for .xls files");
            options.addOption("s","summary", false, "prints project summary on " +
                    "terminal [Used with -p]");
            options.addOption("o", true, "directory where reports are saved [Used with -p]");
            options.addOption("d", false, "display generated reports [Used with -p]");

            CommandLineParser parser = new DefaultParser();
            HelpFormatter formatter = new HelpFormatter();

            try {
                CommandLine cmd = parser.parse(options, args);

                if (cmd.hasOption("p") && cmd.hasOption("o") && !(cmd.hasOption("s"))) {

                    String path = cmd.getOptionValue("p");
                    String outputPath = cmd.getOptionValue("o");

                    SheetLoader sl = new SheetLoader();
                    sl.setPath(path);
                    sl.addSheetToList();
                    SheetReader sr = new SheetReader();
                    ChartCreator ck = new ChartCreator(outputPath);
                    ck.saveTopTenDays(sr.getTopDays(sr.workedByDayReport(sl.getSheetList())));
                    ck.saveByMonth(sr.getTopMonth(sr.byMonthReport(sl.getSheetList())));
                    PDFcreator pdFcreator = new PDFcreator(outputPath);
                    pdFcreator.createPDF(sr.getTopEmployees(sr.noLifeDudesReport(sl.getSheetList())));

                    filesCreated.add(ck.getOutputPath() + "\\" + ck.getByDayReportName());
                    filesCreated.add(ck.getOutputPath() + "\\" + ck.getByMonthReportName());
                    filesCreated.add(pdFcreator.getOutputPath() + "\\" + pdFcreator.getFilename());

                    System.out.println("\n\n\nCreated the following files: ");
                    for (String i : filesCreated) {
                        System.out.println(i);
                    }

                    if (cmd.hasOption("d")) {
                        for (String i : filesCreated) {

                            File pdfFile = new File(i);
                            Desktop.getDesktop().open(pdfFile);
                        }

                    }
                }

                else if (cmd.hasOption("p") && !(cmd.hasOption("s"))) {

                    String path = cmd.getOptionValue("p");
                    SheetLoader sl = new SheetLoader();
                    sl.setPath(path);
                    sl.addSheetToList();
                    SheetReader sr = new SheetReader();
                    ChartCreator ck = new ChartCreator();

                    ck.saveTopTenDays(sr.getTopDays(sr.workedByDayReport(sl.getSheetList())));
                    ck.saveByMonth(sr.getTopMonth(sr.byMonthReport(sl.getSheetList())));
                    PDFcreator pdFcreator = new PDFcreator();
                    pdFcreator.createPDF(sr.getTopEmployees(sr.noLifeDudesReport(sl.getSheetList())));

                    filesCreated.add(ck.getOutputPath() + "\\" + ck.getByDayReportName());
                    filesCreated.add(ck.getOutputPath() + "\\" + ck.getByMonthReportName());
                    filesCreated.add(pdFcreator.getOutputPath() + "\\" + pdFcreator.getFilename());

                    System.out.println("\n\n\nCreated the following files: ");
                    for (String i : filesCreated) {
                        System.out.println(i);
                    }

                    if (cmd.hasOption("d")) {
                        for (String i : filesCreated) {

                            File pdfFile = new File(i);
                            Desktop.getDesktop().open(pdfFile);
                        }

                    }
                }

                else if (cmd.hasOption("p") && cmd.hasOption("s")) {

                    System.out.println("\n\n=====================");
                    System.out.println("Debrief:");
                    SheetReader sr = new SheetReader();
                    SheetLoader sl = new SheetLoader();
                    String path = cmd.getOptionValue("p");
                    sl.setPath(path);
                    sl.addSheetToList();
                    sr.readProjectDebrief(sl.getSheetList());
                } else {

                    formatter.printHelp("Work Analyzer", options);

                }
            }
            catch ( ParseException exp ) {
                System.out.println( "Unexpected exception: " + exp.getMessage() );
                formatter.printHelp("Work Analyzer", options);
            }
        }

}
