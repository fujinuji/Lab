package scs.ubb.map.repository.xml;

import scs.ubb.map.domain.Entity;
import scs.ubb.map.repository.InMemoryRepository;
import scs.ubb.map.validators.Validator;

public class InXMLRepository<ID, E extends Entity<ID>> extends InMemoryRepository<ID, E> {
    public InXMLRepository(Validator<E> validator) {
        super(validator);
    }


}
