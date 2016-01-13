package ru.icl.parser.resource;

import java.util.List;
import ru.icl.parser.model.Tender;

//представляет найденные тендеры
public interface ResourceProcessor { 
    public List<Tender> process(StringBuilder httpResource);
}
