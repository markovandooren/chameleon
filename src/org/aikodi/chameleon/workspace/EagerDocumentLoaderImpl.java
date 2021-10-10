package org.aikodi.chameleon.workspace;

import org.aikodi.chameleon.core.namespace.Namespace;

import java.util.List;

public abstract class EagerDocumentLoaderImpl extends DocumentLoaderImpl {

  @Override
  public List<String> targetDeclarationNames(Namespace ns) throws InputException {
    load();
    return refreshTargetDeclarationNames(ns);
  }

}
