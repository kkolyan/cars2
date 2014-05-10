package net.kkolyan.pivot.net.kkolyan.cars2.autoru;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author nplekhanov
 */
public class ImportTool {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");
        try {
            OffersImportTask importer = context.getBean(OffersImportTask.class);
            importer.doImport();
        } finally {
            context.destroy();
        }
    }
}
