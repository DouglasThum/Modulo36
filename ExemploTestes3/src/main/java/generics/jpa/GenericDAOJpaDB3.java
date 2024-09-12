package generics.jpa;

import java.io.Serializable;

import dao.Persistente;

public abstract class GenericDAOJpaDB3 <T extends Persistente, E extends Serializable>
	extends GenericDAOJpa<T, E> {

	public GenericDAOJpaDB3(Class<T> persistenteClass) {
		super(persistenteClass, "Mysql1");	
	}
}