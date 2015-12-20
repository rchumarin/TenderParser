package ru.icl.parser.factory;

import java.util.List;

//представляет найденные тендеры
public interface ResourceProcessor { 
    public List process(StringBuilder httpResource);
}
