package ru.icl.parser.factory;

public class Factory {
    
    private static Factory instance = new Factory();
    private HttpResource httpResource = null;
    private ResourceProcessor resourceProcessor = null;

    private Factory() { }
    
    public static Factory getInstance() {
        return Factory.instance;
    }
    
    public HttpResource getHttpResource() {
        if(httpResource == null) httpResource = new HttpResourceImpl();
        return httpResource;                  
    }
    
    public ResourceProcessor getResourceProcessor() {
        if(resourceProcessor == null) resourceProcessor = new ResourceProcessorImpl();
        return resourceProcessor;                  
    }
   
}

//ANOTHER EXAMPLE
/*
public interface SomeInterface{
    public void doSomething();
}

public class SomeInterfaceFactory{
    private static SomeInterface impl = null;
    private SomeInterfaceFactory(){}
    public static SomeInterface getSomeInterface(){
        if (impl == null){
            impl = new SomeInterfaceImpl();
        }
        return impl;
    }

    private class SomeInterfaceImpl implements SomeInterface{
        public void doSomething(){
            // ... implementation
        }
    }
}
*/