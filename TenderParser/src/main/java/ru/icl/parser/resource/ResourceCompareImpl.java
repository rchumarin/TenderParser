package ru.icl.parser.resource;

import java.util.Iterator;
import java.util.List;
import ru.icl.parser.model.Tender;

//получение списка тендеров без дубликатов, размещенных в БД 
public class ResourceCompareImpl implements ResourceCompare{

    public List<Tender> getResourceWithoutDublicate(List<Tender> resourceProcessor, List<Tender> resourceDataBase) {
        
        Iterator<Tender> iteratorResourceProcessor = resourceProcessor.iterator();
        while(iteratorResourceProcessor.hasNext()) {
            Tender tenderResourceProcessor = iteratorResourceProcessor.next();
            Iterator<Tender> iteratorResourceDataBase = resourceDataBase.iterator();
            while(iteratorResourceDataBase.hasNext()) {
                Tender tenderResourceDataBase = iteratorResourceDataBase.next();
                //сравнение двух коллекции по Id_tender
                if (tenderResourceProcessor.getIdTender().equals(tenderResourceDataBase.getIdTender())) {                
                    iteratorResourceProcessor.remove();
               }
            }    
        }
    return resourceProcessor;
    }
    
}
