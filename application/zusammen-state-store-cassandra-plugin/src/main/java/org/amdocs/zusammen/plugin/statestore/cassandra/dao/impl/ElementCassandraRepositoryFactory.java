package org.amdocs.zusammen.plugin.statestore.cassandra.dao.impl;

import org.amdocs.zusammen.datatypes.SessionContext;
import org.amdocs.zusammen.plugin.statestore.cassandra.dao.ElementRepository;
import org.amdocs.zusammen.plugin.statestore.cassandra.dao.ElementRepositoryFactory;

public class ElementCassandraRepositoryFactory extends ElementRepositoryFactory {

  private static final ElementRepository INSTANCE = new ElementCassandraRepository();

  @Override
  public ElementRepository createInterface(SessionContext context) {
    return INSTANCE;
  }
}
