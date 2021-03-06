import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.CategorySeries;
import org.knowm.xchart.style.Styler;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ChartCreator {
    private String outputPath;
    private String byMonthReportName = "monthly_report.jpg";
    private String byDayReportName = "days_by_hour_report.jpg";


    ChartCreator() {

        Path currentRelativePath = Paths.get("");
        this.outputPath = currentRelativePath.toAbsolutePath().toString();

    }

    ChartCreator(String outputPath) {

        this.outputPath = outputPath;

    }


    String getOutputPath() {
        return outputPath;
    }

    String getByMonthReportName() {
        return byMonthReportName;
    }

    String getByDayReportName() {
        return byDayReportName;
    }

    public void setOutputPath(String outputPath) {

        this.outputPath = outputPath;

    }


    void saveTopTenDays(Map<LocalDate, Double> topDays) throws IOException {

        CategoryChart chart = new CategoryChartBuilder().width(1800).height(600).title("days").build();

        chart.getStyler().setChartTitleVisible(true);
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNW);
        chart.getStyler().setDefaultSeriesRenderStyle(CategorySeries.CategorySeriesRenderStyle.Stick);

        List<String> xData = new ArrayList<>();
        List<Double> yData = new ArrayList<>();
        for (Map.Entry<LocalDate, Double> entry : topDays.entrySet()) {
            xData.add(entry.getKey().toString());
            yData.add(entry.getValue());
        }

        chart.addSeries("Worked hours by day", xData, yData);
        BitmapEncoder.saveJPGWithQuality(chart, outputPath + "\\" + byDayReportName, 0.95f);

    }


    void saveByMonth(Map<String, Double> topDays) throws IOException {

        CategoryChart chart = new CategoryChartBuilder().width(1800).height(600).title("Monthly report")
                .xAxisTitle("Month").yAxisTitle("Hours").build();

        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNW);
        chart.getStyler().setHasAnnotations(true);

        List<String> xData = new ArrayList<>();
        List<Double> yData = new ArrayList<>();
        for (Map.Entry<String, Double> entry : topDays.entrySet()) {
            xData.add(entry.getKey());
            yData.add(entry.getValue());
        }
        chart.addSeries("worked hours by month", xData, yData);
        BitmapEncoder.saveJPGWithQuality(chart, outputPath + "\\" + byMonthReportName, 0.95f);

    }

}