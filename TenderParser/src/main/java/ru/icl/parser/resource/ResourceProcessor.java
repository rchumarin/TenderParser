package ru.icl.parser.resource;

import java.util.List;

//представляет найденные тендеры
public interface ResourceProcessor { 
    public List process(StringBuilder httpResource);
}
