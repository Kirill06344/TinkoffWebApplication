package parsers;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import ru.tinkoff.edu.java.parser.url.parsers.UrlGitHubParser;
import ru.tinkoff.edu.java.parser.url.parsers.UrlParser;
import ru.tinkoff.edu.java.parser.url.parsers.UrlStackOverflowParser;
import ru.tinkoff.edu.java.parser.url.results.GitHubResult;
import ru.tinkoff.edu.java.parser.url.results.StackOverflowResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class StackOverflowParserTest {

    private final UrlParser stackOverflowParser = new UrlStackOverflowParser();

    @ParameterizedTest
    @MethodSource("invalidLinksProvider")
    void parser_shouldNotParseInvalidLinks(String link) {
        //given
        URL url = null;
        try {
            url = new URL(link);
        } catch (MalformedURLException ex) {
            fail("Incorrect link to create URL");
        }

        //when
        var response = stackOverflowParser.parse(url);

        //then
        assertEquals(Optional.empty(), response);
    }

    @ParameterizedTest
    @MethodSource("validLinksProvider")
    void parser_shouldParseValidLinks(String link, String id) {
        //given
        URL url = null;
        try {
            url = new URL(link);
        } catch (MalformedURLException ex) {
            fail("Incorrect link to create URL");
        }

        //when
        var response = stackOverflowParser.parse(url);
        assertTrue(response.isPresent());
        assertTrue(response.get() instanceof StackOverflowResult);
        var stackOverflowResponse = (StackOverflowResult) (response.get());

        //then
        assertEquals(stackOverflowResponse.id(), id);
    }


    private static Stream<String> invalidLinksProvider() {
        return Stream.of(
                "https://stackoverflow.com/",
                "https://github.com/",
                "https://stackoverflow.com/questions",
                "https://edu.tinkoff.ru/my-activities/courses/stream/3f5c6fe7-a014-49cd-93d3-da8e91b5b931/exam/7662/1"
        );
    }

    public static Stream<Arguments> validLinksProvider() {
        return Stream.of(
                arguments("https://stackoverflow.com/questions/50131839", "50131839"),
                arguments("https://stackoverflow.com/questions/56212981", "56212981")
        );
    }


}

