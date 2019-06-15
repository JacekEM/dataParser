
import org.knowm.xchart.*;
import org.knowm.xchart.style.Styler;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ChartCreator {


        public static void saveTopTenDays(Map<LocalDate, Double> topDays) throws IOException {

            CategoryChart chart = new CategoryChartBuilder().width(1800).height(600).title("days").build();

            chart.getStyler().setChartTitleVisible(true);
            chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNW);
            chart.getStyler().setDefaultSeriesRenderStyle(CategorySeries.CategorySeriesRenderStyle.Stick);

            List<String> xData = new ArrayList<>();
            List<Double> yData = new ArrayList<>();
            for ( Map.Entry<LocalDate, Double> entry: topDays.entrySet() ) {
                xData.add(entry.getKey().toString());
                yData.add(entry.getValue());
            }

            chart.addSeries("Worked hours by day", xData, yData);
           // new SwingWrapper(chart).displayChart();
            BitmapEncoder.saveJPGWithQuality(chart, "C:\\Users\\hans\\Desktop\\reporter-dane\\days_by_hour_report.jpg", 0.95f);


        }


        public static void saveByMonth(Map<String, Double> topDays) throws IOException {

            CategoryChart chart = new CategoryChartBuilder().width(800).height(600).title("Monthly report")
                    .xAxisTitle("Month").yAxisTitle("Hours").build();

            chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNW);
            chart.getStyler().setHasAnnotations(true);

            List<String> xData = new ArrayList<>();
            List<Double> yData = new ArrayList<>();
            for ( Map.Entry<String, Double> entry: topDays.entrySet() ) {
                xData.add(entry.getKey());
                yData.add(entry.getValue());
            }
            chart.addSeries("worked hours by month",xData, yData);
            BitmapEncoder.saveJPGWithQuality(chart, "C:\\Users\\hans\\Desktop\\reporter-dane\\monthly_report.jpg", 0.95f);

        }

}