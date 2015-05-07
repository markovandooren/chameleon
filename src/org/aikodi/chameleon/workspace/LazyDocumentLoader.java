package org.aikodi.chameleon.workspace;

import java.util.List;

import org.aikodi.chameleon.core.namespace.Namespace;

public interface LazyDocumentLoader extends DocumentLoader {
  
  public default List<String> targetDeclarationNames(Namespace ns) throws InputException {
    return refreshTargetDeclarationNames(ns);
  }


}
