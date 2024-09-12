package generics.jpa;

import java.io.Serializable;

import dao.Persistente;

public abstract class GenericDAOJpaDB1 <T extends Persistente, E extends Serializable>
	extends GenericDAOJpa<T, E> {

	public GenericDAOJpaDB1(Class<T> persistenteClass) {
		super(persistenteClass, "Postgre1");	}


	
}