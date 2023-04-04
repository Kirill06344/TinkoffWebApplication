import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import parsers.GithubParserTest;
import parsers.StackOverflowParserTest;
import ru.tinkoff.edu.java.parser.handler.UrlHandler;
import ru.tinkoff.edu.java.parser.url.results.GitHubResult;
import ru.tinkoff.edu.java.parser.url.results.StackOverflowResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UrlHandlerTest {

    private final UrlHandler handler = new UrlHandler();


    @ParameterizedTest
    @MethodSource("notUrlsProvider")
    void handler_shouldNotParseNotUrls(String link) {
        //

        //when
        var response = handler.getParseResult(link);

        //then
        assertEquals(Optional.empty(), response);
    }

    @ParameterizedTest
    @MethodSource("invalidLinksProvider")
    void handler_shouldNotParseInvalidLinks(String link) {
        //when
        var response = handler.getParseResult(link);

        //then
        assertEquals(Optional.empty(), response);
    }

    @ParameterizedTest
    @MethodSource("gitHubValidLinks")
    void handler_shouldParseGithubValidLinks(String link, String owner, String repo) {
        //when
        var response = handler.getParseResult(link);
        assertTrue(response.isPresent());
        assertTrue(response.get() instanceof GitHubResult);

        var gitHubResponse = (GitHubResult)(response.get());

        //then
        assertEquals(gitHubResponse.name(), owner);
        assertEquals(gitHubResponse.repository(), repo);
    }

    @ParameterizedTest
    @MethodSource("stackOverflowValidLinks")
    void handler_shouldParseGithubValidLinks(String link, String id) {
        //when
        var response = handler.getParseResult(link);
        assertTrue(response.isPresent());
        assertTrue(response.get() instanceof StackOverflowResult);

        var stackOverflowResponse = (StackOverflowResult) (response.get());

        //then
        assertEquals(stackOverflowResponse.id(), id);
    }

    private static Stream<String> notUrlsProvider() {
        return Stream.of("asdas", "http:/", "github.com", "stackoverflow/questions");
    }

    private static Stream<String> invalidLinksProvider() {
        return Stream.of(
                "https://stackoverflow.com/",
                "https://github.com/",
                "https://stackoverflow.com/questions",
                "https://edu.tinkoff.ru/my-activities/courses/stream/3f5c6fe7-a014-49cd-93d3-da8e91b5b931/exam/7662/1",
                "https://github.com/Kirill06344/TinkoffWebApplication/pull/3"
        );
    }

    private static Stream<Arguments> gitHubValidLinks() {
        return GithubParserTest.validLinksProvider();
    }

    private static Stream<Arguments> stackOverflowValidLinks() {
        return StackOverflowParserTest.validLinksProvider();
    }
}
