
import java.nio.file.Path;


public class main {

    public static void main(String[] args) throws Exception{

        SheetLoader a = new SheetLoader();
        a.addSheetToList();

        for (Path p: a.getSheetList()) {

                    System.out.println(p.toString());

        }

        SheetReader sr = new SheetReader();

//        System.out.println("==========================");
//        MapPrinter mp = new MapPrinter();
//
//        System.out.println("==========================");
//        System.out.println("Months report:");
//        mp.readMap(sr.byMonthReport(a.getSheetList()));
//
//        System.out.println("\n\n=====================");
//        System.out.println("Employee report:");
//        mp.readMap(sr.noLifeDudesReport(a.getSheetList()));
//
//        System.out.println("\n\n=====================");
//        System.out.println("byDay report:");
//        mp.readMap(sr.workedByDayReport(a.getSheetList()));
//
//
//
//        System.out.println("==========================");
//        System.out.println("Months report:");
//        mp.readMap(sr.getTopMonth(sr.byMonthReport(a.getSheetList())));
//
//        System.out.println("\n\n=====================");
//        System.out.println("Employee report:");
//        mp.readMap(sr.getTopEmployees(sr.noLifeDudesReport(a.getSheetList())));
//
//        System.out.println("\n\n=====================");
//        System.out.println("byDay report:");
//        mp.readMap(sr.getTopDays(sr.workedByDayReport(a.getSheetList())));
//
//        System.out.println("\n\n=====================");
//        System.out.println("Debrief:");
//        sr.readProjectDebrief(a.getSheetList());




        ChartCreator.saveTopTenDays(sr.getTopDays(sr.workedByDayReport(a.getSheetList())));

        ChartCreator.saveByMonth(sr.byMonthReport(a.getSheetList()));

        PDFcreator pdFcreator = new PDFcreator();

        pdFcreator.createPDF(sr.getTopEmployees(sr.noLifeDudesReport(a.getSheetList())));
    }




}
