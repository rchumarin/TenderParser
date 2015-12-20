package ru.icl.parser.factory;

//получание страницы
public interface HttpResource {
    public StringBuilder getHttpResource(final String address);
}
