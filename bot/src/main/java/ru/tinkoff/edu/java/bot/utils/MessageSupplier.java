package ru.tinkoff.edu.java.bot.utils;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

@Slf4j
@Component
public class MessageSupplier {

    private final MustacheFactory mf = new DefaultMustacheFactory();

    public String convertTemplate(String path, Map<String, Object> scopes) {
        Mustache m = mf.compile(path);
        StringWriter writer = new StringWriter();
        try {
            m.execute(writer, scopes).flush();
        } catch (IOException e) {
            return "Something went wrong";
        }
        return writer.toString();
    }
}
