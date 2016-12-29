package org.amdocs.tsuzammen.plugin.statestore.cassandra.dao;


import org.amdocs.tsuzammen.commons.datatypes.Id;
import org.amdocs.tsuzammen.commons.datatypes.SessionContext;
import org.amdocs.tsuzammen.commons.datatypes.item.Info;
import org.amdocs.tsuzammen.commons.datatypes.item.Item;

import java.util.List;
import java.util.Optional;

public interface ItemDao {
  void create(SessionContext context, Id itemId, Info itemInfo);

  void save(SessionContext context, Id itemId, Info itemInfo);

  void delete(SessionContext context, Id itemId);

  Optional<Item> get(SessionContext context, Id itemId);

  List<Item> list(SessionContext context);
}
