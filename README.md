# Homework 1
Пользователи присылают ссылки в каком-то естественном формате, например:

    https://github.com/sanyarnd/tinkoff-java-course-2022/                
    https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c
    https://stackoverflow.com/search?q=unsupported%20link                

Наш бот поддерживает несколько типов ссылок, а именно GitHub и StackOverflow.

В будущем мы захотим добавить новые сайты, поэтому было бы неплохо, чтобы добавление нового парсера было максимально простым. Для этого может пригодиться шаблон Цепочка Обязанностей.

В случае с GitHub нас интересует имя пользователя и репозитория, а в случае со StackOverflow -- id вопроса.

В модуле link-parser напишите механизм разбора URL, который:

    для ссылок с GitHub возвращает пару пользователь/репозиторий
    для ссылок со StackOverflow возвращает id
    для остальных ссылок возвращает null

Можно пользоваться любыми библиотеками.

Желательно использовать современные возможности Java 17, например, sealed interface и record.
