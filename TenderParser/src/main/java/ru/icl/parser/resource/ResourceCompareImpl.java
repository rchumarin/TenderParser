package ru.icl.parser.resource;

import java.util.Iterator;
import java.util.List;
import ru.icl.parser.model.Tender;

//получение списка тендеров без дубликатов, размещенных в БД 
public class ResourceCompareImpl implements ResourceCompare{

    @Override
    public List<Tender> getResourceWithoutDublicate(List<Tender> resourceProcessor, List<Tender> resourceDataBase) {
        
//            !!!! ДОРАБОТАТЬ ПРАВИЛЬНОЕ СРАВНЕНИЕ КОЛЛЕКЦИЙ
        
        Iterator iteratorResourceProcessor = resourceProcessor.iterator();
        while(iteratorResourceProcessor.hasNext()) {
            Tender tenderResourceProcessor = (Tender) iteratorResourceProcessor.next();
            Iterator iteratorResourceDataBase = resourceDataBase.iterator();
            while(iteratorResourceDataBase.hasNext()) {
                Tender tenderResourceDataBase = (Tender) iteratorResourceDataBase.next();
                if (tenderResourceProcessor.equals(tenderResourceDataBase)) {
                    iteratorResourceProcessor.remove();
               }
            }    
        }
    return resourceProcessor;
    }
    
}
