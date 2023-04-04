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
import ru.tinkoff.edu.java.parser.url.results.GitHubResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class GithubParserTest {

    private final UrlParser gitHubParser = new UrlGitHubParser();

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
        var response = gitHubParser.parse(url);

        //then
        assertEquals(Optional.empty(), response);
    }

    @ParameterizedTest
    @MethodSource("validLinksProvider")
    void parser_shouldParseValidLinks(String link, String owner, String repo) {
        //given
        URL url = null;
        try {
            url = new URL(link);
        } catch (MalformedURLException ex) {
            fail("Incorrect link to create URL");
        }

        //when
        var response = gitHubParser.parse(url);
        assertTrue(response.isPresent());
        assertTrue(response.get() instanceof GitHubResult);
        var gitHubResponse = (GitHubResult)(response.get());

        //then
        assertEquals(gitHubResponse.name(), owner);
        assertEquals(gitHubResponse.repository(), repo);
    }


    private static Stream<String> invalidLinksProvider() {
        return Stream.of(
                "https://github.com/Kirill06344/TinkoffWebApplication/pull/3",
                "https://github.com/",
                "https://github.com/Kirill06344/java-labs/blob/master/README.md",
                "https://edu.tinkoff.ru/my-activities/courses/stream/3f5c6fe7-a014-49cd-93d3-da8e91b5b931/exam/7662/1"
                );
    }

    public static Stream<Arguments> validLinksProvider() {
        return Stream.of(
                arguments("https://github.com/Kirill06344/TinkoffWebApplication",
                        "Kirill06344", "TinkoffWebApplication"),
                arguments("https://github.com/Kirill06344/Calendar", "Kirill06344", "Calendar"),
                arguments("http://github.com/Kirill06344/android", "Kirill06344", "android")
        );
    }


}
