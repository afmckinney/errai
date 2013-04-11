/*
 * Copyright 2013 JBoss, by Red Hat, Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jboss.errai.otec.mutation;

import java.util.Collection;
import java.util.List;

/**
 * @author Mike Brock
 */
public class Transformer {
  private final OTEntity entity;
  private final OTPeer peer;
  private final OTOperation operation;

  private Transformer(final OTEntity entity, final OTPeer peer, final OTOperation operation) {
    this.entity = entity;
    this.peer = peer;
    this.operation = operation;
  }

  public static Transformer createTransformer(final OTEntity entity, final OTPeer peer, final OTOperation operation) {
    return new Transformer(entity, peer, operation);
  }

  public void transform() {
    final TransactionLog transactionLog = entity.getTransactionLog();
    final List<Mutation> mutations = operation.getMutations();
    final Collection<OTOperation> loggedOps = transactionLog.getLogFromId(operation.getRevision());

    // if no operation was carried out we can just apply the new operations
    if (loggedOps.isEmpty()) {
      for (final Mutation mutation : mutations) {
        mutation.apply(entity.getState());
      }
      transactionLog.appendLog(operation);
      entity.setRevision(operation.getRevision());
    }
    else {
      // transform operations and re-apply
    }
  }
}
