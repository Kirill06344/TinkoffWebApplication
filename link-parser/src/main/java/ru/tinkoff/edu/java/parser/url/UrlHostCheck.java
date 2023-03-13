package ru.tinkoff.edu.java.parser.url;

import java.net.URL;

public interface UrlHostCheck {

    default boolean check(URL url, String host) {
        return url.getHost().equalsIgnoreCase(host) && url.getPort() == -1;
    }
}
