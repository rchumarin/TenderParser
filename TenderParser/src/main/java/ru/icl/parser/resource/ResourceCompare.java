package ru.icl.parser.resource;

import java.util.List;
import ru.icl.parser.model.Tender;

public interface ResourceCompare {
    public List getResourceWithoutDublicate(List<Tender> resourceProcessor, List<Tender> resourceDataBase);
}
