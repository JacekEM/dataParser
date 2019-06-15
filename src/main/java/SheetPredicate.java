import java.nio.file.Path;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class SheetPredicate implements Predicate<Path>{

    final Pattern pattern = Pattern.compile(".*" + ".xls");

    @Override
    public boolean test(Path v) {

        String testString = v.toString();
        Pattern pattern = Pattern.compile(".*" + ".xls");
        Matcher matcher = pattern.matcher(testString);
        return matcher.find();
    }
}
