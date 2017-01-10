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
import org.amdocs.tsuzammen.datatypes.item.ItemVersion;
import org.amdocs.tsuzammen.plugin.statestore.cassandra.dao.ElementRepository;
import org.amdocs.tsuzammen.plugin.statestore.cassandra.dao.ElementRepositoryFactory;
import org.amdocs.tsuzammen.plugin.statestore.cassandra.dao.VersionDao;
import org.amdocs.tsuzammen.plugin.statestore.cassandra.dao.VersionDaoFactory;
import org.amdocs.tsuzammen.plugin.statestore.cassandra.dao.types.ElementEntity;
import org.amdocs.tsuzammen.plugin.statestore.cassandra.dao.types.ElementEntityContext;

import java.util.Collection;
import java.util.Optional;

class VersionStateStore {

  Collection<ItemVersion> listItemVersions(SessionContext context, Id itemId) {
    return getVersionDao(context).list(context, context.getUser().getUserName(), itemId);
  }


  boolean isItemVersionExist(SessionContext context, Id itemId, Id versionId) {
    return true;
  }


  ItemVersion getItemVersion(SessionContext context, Id itemId, Id versionId) {
    String privateSpace = context.getUser().getUserName();
    return getOptionalItemVersion(context, privateSpace, itemId, versionId)
        .orElseThrow(() -> new RuntimeException(
            String.format(StateStoreMessages.ITEM_VERSION_NOT_EXIST,
                itemId, versionId, privateSpace)));
  }


  void createItemVersion(SessionContext context, Id itemId, Id baseVersionId,
                         Id versionId, Info versionInfo) {
    String space = context.getUser().getUserName();
    getVersionDao(context)
        .create(context, space, itemId, versionId, baseVersionId, versionInfo);

    if (baseVersionId == null) {
      return;
    }

    copyElements(context, space, itemId, baseVersionId, versionId);
  }


  void publishItemVersion(SessionContext context, Id itemId, Id versionId) {
    String privateSpace = context.getUser().getUserName();

    copyVersionInfo(context, privateSpace, StateStoreConstants.PUBLIC_SPACE, itemId, versionId);
    copyElements(context, privateSpace, StateStoreConstants.PUBLIC_SPACE, itemId, versionId);
  }


  void syncItemVersion(SessionContext context, Id itemId, Id versionId) {

  }

  private void copyVersionInfo(SessionContext context, String sourceSpace, String targetSpace,
                               Id itemId, Id versionId) {
/*    Optional<ItemVersion> itemVersion =
        getOptionalItemVersion(context, sourceSpace, itemId, versionId);
    getVersionDao(context).update(context, targetSpace, itemId, versionId, versionInfo);*/
  }

  private void copyElements(SessionContext context, String sourceSpace, String targetSpace,
                            Id itemId, Id versionId) {
    ElementRepository elementRepository = getElementRepository(context);

    ElementEntityContext elementContext = new ElementEntityContext(sourceSpace, itemId, versionId);
    Collection<ElementEntity> elementEntities = elementRepository.list(context, elementContext);

    elementContext.setSpace(targetSpace);
    elementEntities
        .forEach(elementEntity -> elementRepository.create(context, elementContext, elementEntity));
  }

  private void copyElements(SessionContext context, String space, Id itemId, Id sourceVersionId,
                            Id targetVersionId) {
    ElementRepository elementRepository = getElementRepository(context);

    ElementEntityContext elementContext = new ElementEntityContext(space, itemId, sourceVersionId);
    Collection<ElementEntity> elementEntities = elementRepository.list(context, elementContext);

    elementContext.setVersionId(targetVersionId);
    elementEntities
        .forEach(elementEntity -> elementRepository.create(context, elementContext, elementEntity));
  }

  Optional<ItemVersion> getOptionalItemVersion(SessionContext context, String space,
                                               Id itemId, Id versionId) {
    return getVersionDao(context).get(context, space, itemId, versionId);
  }

  VersionDao getVersionDao(SessionContext context) {
    return VersionDaoFactory.getInstance().createInterface(context);
  }

  private ElementRepository getElementRepository(SessionContext context) {
    return ElementRepositoryFactory.getInstance().createInterface(context);
  }
}