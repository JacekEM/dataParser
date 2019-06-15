import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import static java.util.stream.Collectors.toMap;


class SheetReader {

    private int dateColumn;
    private int workTimeColumn;

    private int getDateColumn() {
        return dateColumn;
    }

    private int getWorkTimeColumn() {
        return workTimeColumn;
    }

    private void setDateColumn(Sheet sheet) {
        DataFormatter formatter = new DataFormatter();
        Row row = sheet.getRow(0);
        for (Cell cell : row) {

            String text = formatter.formatCellValue(cell);
            if (text.contains("Data")) {
                this.dateColumn = cell.getColumnIndex();
            }
        }
    }

    private void setWorkTimeColumn(Sheet sheet) {
        DataFormatter formatter = new DataFormatter();
        Row row = sheet.getRow(0);
        for (Cell cell : row) {

            String text = formatter.formatCellValue(cell);
            if (text.contains("Czas")) {
                this.workTimeColumn = cell.getColumnIndex();
            }
        }
    }

    private String getEmployeeName(File file) {

        String employeeName = file.getName().replaceFirst("\\.\\w+$", "");
        return employeeName;
    }


    Map<LocalDate, Double> getTopDays(Map<LocalDate, Double> map) {

        return map.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).limit(10)
                .collect(toMap(e -> e.getKey(), e -> e.getValue(), (e1, e2) -> e2, LinkedHashMap::new));

    }

    Map<String, Double> getTopMonth(Map<String, Double> map) {

        return map.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(toMap(e -> e.getKey(), e -> e.getValue(), (e1, e2) -> e2, LinkedHashMap::new));

    }


    Map<String, Double> getTopEmployees(Map<String, Double> map) {

        return map.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(toMap(e -> e.getKey(), e -> e.getValue(), (e1, e2) -> e2, LinkedHashMap::new));

    }


    Map<String, Double> byMonthReport(List<Path> paths) throws IOException {

        Map<String, Double> monthlyHourSummary = new HashMap<>();

        for (Path path : paths) {

            FileInputStream inputStream = new FileInputStream(path.toString());
            Workbook workbook = new HSSFWorkbook(inputStream);
            Iterator<Sheet> si = workbook.iterator();

            while (si.hasNext()) {

                Sheet sheet = si.next();
                Date projectDate;
                LocalDate anotherFreakingDateInJava = LocalDate.now();
                setWorkTimeColumn(sheet);
                setDateColumn(sheet);

                for (Row row : sheet) {
                    if (row == null) {

                        continue;
                    }

                    for (Cell cell : row) {

                        if ((cell.getColumnIndex() == getDateColumn()) && (cell.getCellType().equals(CellType.NUMERIC))) {


                            projectDate = cell.getDateCellValue();
                            anotherFreakingDateInJava = projectDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                            monthlyHourSummary.putIfAbsent(anotherFreakingDateInJava.getMonth().toString(), 0.0);

                        }

                        if ((cell.getColumnIndex() == getWorkTimeColumn()) && (cell.getCellType().equals(CellType.NUMERIC))) {

                            monthlyHourSummary.put(anotherFreakingDateInJava.getMonth().toString(),
                                    monthlyHourSummary.get(anotherFreakingDateInJava.getMonth().toString())
                                            + cell.getNumericCellValue());
                        }

                    }

                }

            }
            workbook.close();
            inputStream.close();
        }

        return monthlyHourSummary;

    }


    Map<String, Double> noLifeDudesReport(List<Path> paths) throws IOException {

        Map<String, Double> workedPerEmployeeReport = new HashMap<>();

        for (Path path : paths) {

            File excelFile = new File(path.toString());
            FileInputStream inputStream = new FileInputStream(path.toString());
            String employeeName = getEmployeeName(excelFile);
            workedPerEmployeeReport.putIfAbsent(employeeName, 0.0);
            Workbook workbook = new HSSFWorkbook(inputStream);
            Iterator<Sheet> si = workbook.iterator();

            while (si.hasNext()) {

                Sheet sheet = si.next();
                setWorkTimeColumn(sheet);
                setDateColumn(sheet);

                for (Row row : sheet) {
                    if (row == null) {

                        continue;
                    }

                    for (Cell cell : row) {

                        if ((cell.getColumnIndex() == getWorkTimeColumn()) && (cell.getCellType().equals(CellType.NUMERIC))) {
                            workedPerEmployeeReport.put(employeeName, workedPerEmployeeReport.get(employeeName)
                                    + cell.getNumericCellValue());

                        }

                    }

                }
            }
            workbook.close();
            inputStream.close();
        }

        return workedPerEmployeeReport;

    }


    Map<LocalDate, Double> workedByDayReport(List<Path> paths) throws IOException {
        TreeMap<LocalDate, Double> workedByDayReport = new TreeMap<>(Comparator.reverseOrder());

        for (Path path : paths) {

            FileInputStream inputStream = new FileInputStream(path.toString());
            Workbook workbook = new HSSFWorkbook(inputStream);
            Iterator<Sheet> si = workbook.iterator();

            while (si.hasNext()) {

                Sheet sheet = si.next();
                LocalDate localDate = LocalDate.now();
                setWorkTimeColumn(sheet);
                setDateColumn(sheet);

                for (Row row : sheet) {
                    if (row == null) {

                        continue;
                    }

                    for (Cell cell : row) {

                        if ((cell.getColumnIndex() == getDateColumn()) && (cell.getCellType().equals(CellType.NUMERIC))) {

                            Date projectDate = cell.getDateCellValue();
                            localDate = projectDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                            workedByDayReport.putIfAbsent(localDate, 0.0);
                        }

                        if ((cell.getColumnIndex() == getWorkTimeColumn()) && (cell.getCellType().equals(CellType.NUMERIC))) {
                            workedByDayReport.put(localDate, workedByDayReport.get(localDate) + cell.getNumericCellValue());
                        }
                    }
                }
            }
            workbook.close();
            inputStream.close();
        }

        return workedByDayReport.descendingMap();

    }


    void readProjectDebrief(List<Path> paths) throws IOException {

        Map<String, Double> workHoursSummary = new HashMap<>();
        Map<String, Double> monthlyHourSummary = new HashMap<>();
        Map<String, Double> workedPerEmployee = new HashMap<>();

        for (Path path : paths) {

            File excelFile = new File(path.toString());
            FileInputStream inputStream = new FileInputStream(path.toString());
            String employeeName = getEmployeeName(excelFile);
            workedPerEmployee.putIfAbsent(employeeName, 0.0);
            Workbook workbook = new HSSFWorkbook(inputStream);
            String worksheetName;
            int sheetCounter = 0;
            Iterator<Sheet> si = workbook.iterator();
            DataFormatter formatter = new DataFormatter();

            while (si.hasNext()) {

                worksheetName = workbook.getSheetName(sheetCounter);
                sheetCounter = +1;
                Sheet sheet = si.next();
                double totalWorkedHours = 0.0;
                Date projectDate = new Date();
                LocalDate ldata = LocalDate.now();
                String data = " ";
                setWorkTimeColumn(sheet);
                setDateColumn(sheet);

                for (Row row : sheet) {
                    if (row == null) {

                        continue;
                    }

                    for (Cell cell : row) {

                        if ((cell.getColumnIndex() == getDateColumn()) && (cell.getCellType().equals(CellType.NUMERIC))) {

                            projectDate = cell.getDateCellValue();
                            data = formatter.formatCellValue(cell);
                            ldata = projectDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                            monthlyHourSummary.putIfAbsent(ldata.getMonth().toString(), 0.0);
                            workHoursSummary.putIfAbsent(data, 0.0);

                        }

                        if ((cell.getColumnIndex() == getWorkTimeColumn()) && (cell.getCellType().equals(CellType.NUMERIC))) {
                            workHoursSummary.put(data, workHoursSummary.get(data) + cell.getNumericCellValue());
                            monthlyHourSummary.put(ldata.getMonth().toString(), monthlyHourSummary.get(ldata.getMonth().toString())
                                    + cell.getNumericCellValue());
                            workedPerEmployee.put(employeeName, workedPerEmployee.get(employeeName) + cell.getNumericCellValue());
                            totalWorkedHours += cell.getNumericCellValue();
                        }

                    }

                }

                StringBuilder projectData = new StringBuilder("Employee name: " + employeeName + "\n"
                        + "Total worked hours: " + totalWorkedHours + "\n"
                        + "Project name: " + worksheetName + "\n"
                        + "Project date: " + ldata.getMonth() + " " + ldata.getYear()
                        + "\n"
                );


                System.out.println(projectData.toString());
            }
            workbook.close();
            inputStream.close();
        }
    }
}
