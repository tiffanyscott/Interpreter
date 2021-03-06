package plc.interpreter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Contains JUnit tests for {@link Regex}. Tests declarations for steps 1 & 2
 * are provided, you must add your own for step 3.
 *
 * To run tests, either click the run icon on the left margin or execute the
 * gradle test task, which can be done by clicking the Gradle tab in the right
 * sidebar and navigating to Tasks > verification > test Regex(double click to run).
 */
public class RegexTests {

    /**
     * This is a parameterized test for the {@link Regex#EMAIL} regex. The
     * {@link ParameterizedTest} annotation defines this method as a
     * parameterized test, and {@link MethodSource} tells JUnit to look for the
     * static method {@link #testEmailRegex()}.
     *
     * For personal preference, I include a test name as the first parameter
     * which describes what that test should be testing - this is visible in
     * IntelliJ when running the tests.
     */

    @ParameterizedTest
    @MethodSource
    public void testEmailRegex(String test, String input, boolean success) {
        test(input, Regex.EMAIL, success);
    }
    public static Stream<Arguments> testEmailRegex() {
        return Stream.of(

                //passing
                Arguments.of("Alphanumeric", "thelegend27@gmail.com", true),
                Arguments.of("UF Domain", "otherdomain@ufl.edu", true),
                Arguments.of("Has 1 @ Sign", "test@gmail.com", true),
                Arguments.of("Org domain", "test@company.org", true),
                Arguments.of("US Domain", "test@america.us", true),
                Arguments.of("Dashdomain", "test@NORTH-america.us", true),
                //failing
                Arguments.of("Missing Domain Dot", "missingdot@gmailcom", false),
                Arguments.of("Symbols", "symbols#$%@gmail.com", false),
                Arguments.of("Dot before @", "test.ing@hotmailcom", false),
                Arguments.of("Dot last 1", "testing.@hotmail..com", false),
                Arguments.of("Dot last 2", "testing@hotmail.com.", false),
                Arguments.of("Dot and @ together 1", "testing@.hotmail.com.", false),
                Arguments.of("Dot and @ together 2", "testing.@hotmail.com.", false),
                Arguments.of("Has 2 @ Signs", "test@ing@hotmail.com", false),
                Arguments.of("Has < 1 @ Sign", "testgmail.com", false)
        );
    }

    @ParameterizedTest
    @MethodSource
    public void testFileNamesRegex(String test, String input, boolean success) {
        //this one is different as we're also testing the file name capture
        Matcher matcher = test(input, Regex.FILE_NAMES, success);
        if (success) {
            Assertions.assertEquals(input.substring(0, input.indexOf(".")), matcher.group("name"));
        }
    }

    public static Stream<Arguments> testFileNamesRegex() {
        return Stream.of(
                //passing
                Arguments.of("Java File", "Regex.tar.java", true),
                Arguments.of("Java Class", "RegexTests.class", true),
                Arguments.of("Java Class", "RegexTests.tar.alt.class", true),
                Arguments.of("Java File", "r.class.java", true),
                Arguments.of("Java Class", "RegexTests.class.class", true),
                Arguments.of("dash", "Regex-Tests.class.class", true),
                //failing
                Arguments.of("Directory", "directory", false),
                Arguments.of("Python File", "scrippy.py", false),
                Arguments.of("Text File", "texting.txt", false),
                Arguments.of("Embedded class", "scrippy.class.not", false),
                Arguments.of("Embedded File", "scrippy.java.file", false),
                Arguments.of("dashes", "scrippy-java-file", false),
                Arguments.of("dashes", ".class", false)

        );
    }

    @ParameterizedTest
    @MethodSource
    public void testEvenStringsRegex(String test, String input, boolean success) {
        test(input, Regex.EVEN_STRINGS, success);
    }

    public static Stream<Arguments> testEvenStringsRegex() {
        return Stream.of(
                //passing
                Arguments.of("14 Characters", "thishas14chars", true),
                Arguments.of("10 Characters", "i<3pancakes!", true),
                Arguments.of("12 Characters", "i<3pancakes!!!", true),
                Arguments.of("20 Characters", "20202020202020202020", true),
                Arguments.of("18 Characters", "123456789asbfjdlpd", true),
                //failing
                Arguments.of("9 Characters", "9MANYchar", false),
                Arguments.of("0 Characters", "", false),
                Arguments.of("1 Character", "1", false),
                Arguments.of("26 Characters", "abcdefghijklmnopqrstuvwxyz", false),
                Arguments.of("15 characters", "idont<3pancakes", false)
        );
    }

    @ParameterizedTest
    @MethodSource
    public void testIntegerListRegex(String test, String input, boolean success) {
        test(input, Regex.INTEGER_LIST, success);
    }

    public static Stream<Arguments> testIntegerListRegex() {
        return Stream.of(
                //passing
                Arguments.of("Empty List", "[]", true),
                Arguments.of("Single Element", "[1]", true),
                Arguments.of("Multiple Elements", "[1,2,3]", true),
                Arguments.of("Multiple Elements + Spaces", "[1, 2, 3]", true),
                Arguments.of("Mixed Spaced Elements", "[1,2, 3,3, 2,1]", true),
                //failing
                Arguments.of("Missing Brackets", "1,2,3", false),
                Arguments.of("Missing Commas", "[1 2 3]", false),
                Arguments.of("Just Commas", "[, , , ]", false),
                Arguments.of("Too Much Space", "[1,   2  , 3 ]", false),
                Arguments.of("space in the front", "[ 1,2,3]", false),
                Arguments.of("Space Before", "[ 1, 2, 3]", false),
                Arguments.of("Trailing Comma", "[1,2,3,]", false)
        );
    }
    @ParameterizedTest
    @MethodSource
    public void testIdentifierRegex(String test, String input, boolean success) {
        test(input, Regex.IDENTIFIER, success);
    }

    public static Stream<Arguments> testIdentifierRegex() {
        return Stream.of(
                //successful
                Arguments.of("lettersonly", "getName", true),
                Arguments.of("question&dash", "is-empty?", true),
                Arguments.of("symbolsonly", "<=>", true),
                Arguments.of("allthesymbols", "<>=", true),
                Arguments.of("underscorebegin", "_tester", true),
                Arguments.of("alphanumeric", "variable47", true),

                //unsuccessful

                Arguments.of("ampersand", "test&", false),
                Arguments.of("containscommas", "why,are,there,commas", false),
                Arguments.of("singleperiod", ".", false),
                Arguments.of("numberbegin", "42=life", false),
                Arguments.of("semi", "semi;colon", false)




        );
    }

    @ParameterizedTest
    @MethodSource
    public void testNumberRegex(String test, String input, boolean success) {
        test(input, Regex.NUMBER, success);
    }

    public static Stream<Arguments> testNumberRegex() {
        return Stream.of(
                //successful
                Arguments.of("single digit", "1", true),
                Arguments.of("negativenumber", "-234324", true),
                Arguments.of("positivenumber", "+35423", true),
                Arguments.of("decimal", "007.000", true),
                Arguments.of("lessthan1", "0.5", true),
                Arguments.of("bigNumber", "16432481254712548754971", true),
                //unsuccessful
                Arguments.of("trailingperiod", "5.", false),
                Arguments.of("leadingperiod", ".5", false),
                Arguments.of("doubleperiod", "....", false),
                Arguments.of("multiple periods", "123.242.2", false),
                Arguments.of("positiveafternumber", "1+3", false),
                Arguments.of("letters", "abc", false),
                Arguments.of("symbols", "!!!", false)

        );
    }
    @ParameterizedTest
    @MethodSource
    public void testStringRegex(String test, String input, boolean success) {
        test(input, Regex.STRING, success);
    }

    public static Stream<Arguments> testStringRegex() {
        return Stream.of(
                //successful
                Arguments.of("emptystringwithquotes", "\"\"", true),
                Arguments.of("plaintext", "\"HelloWorld\"", true),
                Arguments.of("newline", "\"Hello,\\nWorld!\"", true),
                Arguments.of("space", "\"Hello World\"", true),
                Arguments.of("tab", "\"Hello,\\tWorld!\"", true),
                Arguments.of("r", "\"Hello,\\rWorld!\"", true),
                //unsuccessful
                Arguments.of("unterminated", "\"unterminated", false),
                Arguments.of("slash", "\"invalid\\escape\"", false),
                Arguments.of("missingquotebegining", "unterminated\"", false),
                Arguments.of("noquotes", "", false),
                Arguments.of("singlequotes", "\'test\'", false),
                Arguments.of("stringnoquotes", "abc", false)

        );
    }


    /**
     * Asserts that the input matches the given pattern and returns the matcher
     * for additional assertions.
     */
    private static Matcher test(String input, Pattern pattern, boolean success) {
        Matcher matcher = pattern.matcher(input);
        Assertions.assertEquals(success, matcher.matches());
        return matcher;
    }

}
