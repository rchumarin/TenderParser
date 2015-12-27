package ru.icl.parser.service;

import java.util.List;

//представляет найденные тендеры
public interface ResourceProcessor { 
    public List process(StringBuilder httpResource);
}
