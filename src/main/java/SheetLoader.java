import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SheetLoader {

    private List<Path> sheetList = new ArrayList<>();
    private Pattern pattern = Pattern.compile(".*" + ".xls");

//    public SheetLoader(String path) {
//        this.path = path;
//
//    }


    public List<Path> getSheetList() {
        return sheetList;
    }


    public void addSheetToList() throws Exception{
        String path = "C:\\Users\\hans\\Desktop\\reporter-dane";
        //String path = "C:\\Users\\hans\\Desktop\\reporter-dane\\reporter-dane\\2012\\01";
//        Files.walk(Paths.get(path)).forEach(System.out::println);


//        System.out.println("----------------------------");
//        sheetList = Files.walk(Paths.get(path)).filter(Files::isRegularFile).filter(SheetPredicate).collect(Collectors.toList());
        sheetList = Files.walk(Paths.get(path))
                         .filter(Files::isRegularFile)
                         .filter(file -> {
                            String testString = file.toString();
                            Matcher matcher = pattern.matcher(testString);
                            return matcher.find();
                         })
                         .collect(Collectors.toList());
    }

}
