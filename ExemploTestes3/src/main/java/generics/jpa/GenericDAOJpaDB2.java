package generics.jpa;

import java.io.Serializable;

import dao.Persistente;

public abstract class GenericDAOJpaDB2 <T extends Persistente, E extends Serializable>
	extends GenericDAOJpa<T, E> {

	public GenericDAOJpaDB2(Class<T> persistenteClass) {
		super(persistenteClass, "Postgre2");	}


	
}