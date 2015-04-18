package org.aikodi.chameleon.workspace;

import java.util.List;

import org.aikodi.chameleon.core.namespace.Namespace;

public abstract class EagerDocumentLoaderImpl extends DocumentLoaderImpl {

  @Override
  public List<String> targetDeclarationNames(Namespace ns) throws InputException {
    load();
    return refreshTargetDeclarationNames(ns);
  }

}
