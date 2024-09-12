package dao;

import generics.jpa.IGenericDAOJpa;

public interface IClienteDAOJpa<T extends Persistente> extends IGenericDAOJpa<T, Long> {

}
