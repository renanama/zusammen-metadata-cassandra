/*
 * Copyright © 2016 Amdocs Software Systems Limited 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.amdocs.tsuzammen.plugin.statestore.cassandra;

import org.amdocs.tsuzammen.datatypes.Id;
import org.amdocs.tsuzammen.datatypes.SessionContext;
import org.amdocs.tsuzammen.datatypes.item.Info;
import org.amdocs.tsuzammen.datatypes.item.Item;
import org.amdocs.tsuzammen.plugin.statestore.cassandra.dao.ItemDao;
import org.amdocs.tsuzammen.plugin.statestore.cassandra.dao.ItemDaoFactory;

import java.util.Collection;
import java.util.Optional;

public class ItemStateStore {

  public Collection<Item> listItems(SessionContext context) {
    return getItemDao(context).list(context);
  }


  public boolean isItemExist(SessionContext context, Id itemId) {
    return true;
  }


  public Item getItem(SessionContext context, Id itemId) {
    return getOptionalItem(context, itemId).orElseThrow(() ->
        new RuntimeException(String.format(StateStoreMessages.ITEM_NOT_EXIST, itemId)));
  }


  public void createItem(SessionContext context, Id itemId, Info itemInfo) {
    getItemDao(context).create(context, itemId, itemInfo);
  }


  public void saveItem(SessionContext context, Id itemId, Info itemInfo) {
    getItemDao(context).save(context, itemId, itemInfo);
  }


  public void deleteItem(SessionContext context, Id itemId) {
    getItemDao(context).delete(context, itemId);
  }

  private Optional<Item> getOptionalItem(SessionContext context, Id itemId) {
    return getItemDao(context).get(context, itemId);
  }

  private ItemDao getItemDao(SessionContext context) {
    return ItemDaoFactory.getInstance().createInterface(context);
  }
}