package ru.icl.parser.resource;

//получание страницы
public interface HttpResource {
    public StringBuilder getHttpResource(final String address);
}
