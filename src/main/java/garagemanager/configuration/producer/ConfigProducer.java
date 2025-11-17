//package garagemanager.configuration.producer;
//
//import garagemanager.configuration.qualifier.PhotosDir;
//import jakarta.enterprise.context.ApplicationScoped;
//import jakarta.enterprise.inject.Produces;
//import jakarta.inject.Inject;
//import jakarta.servlet.ServletContext;
//
//@ApplicationScoped
//public class ConfigProducer {
//
//    @Inject
//    private ServletContext servletContext;
//
//    @Produces
//    @PhotosDir
//    public String producePhotosPath() {
//        return servletContext.getInitParameter("photosDir");
//    }
//}
