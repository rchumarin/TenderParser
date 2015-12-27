package ru.icl.parser.service;

import java.util.List;
import ru.icl.parser.model.Tender;

public interface ResourceSelection {
    public List getResourceWithoutDublicate(List<Tender> resourceProcessor, List<Tender> resourceDataBase);
}
