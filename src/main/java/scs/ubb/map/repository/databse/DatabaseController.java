package scs.ubb.map.repository.databse;

import com.byteslounge.cdi.annotation.Property;
import scs.ubb.map.domain.Entity;
import scs.ubb.map.repository.InMemoryRepository;
import scs.ubb.map.validators.Validator;

public class DatabaseController<ID, E extends Entity<ID>> extends InMemoryRepository<ID, E> {

    @Property(value = "database.url", resourceBundleBaseName = "database.proprieties")
    private String databaseURL;

    public DatabaseController(Validator<E> validator) {
        super(validator);
    }

    public void write() {
        System.out.println(databaseURL);
    }

    public static void main(String[] args) {
        DatabaseController controller = new DatabaseController(null);
        controller.write();
    }
}
