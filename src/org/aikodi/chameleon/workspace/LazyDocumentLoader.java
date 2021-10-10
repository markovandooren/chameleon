package org.aikodi.chameleon.workspace;

import org.aikodi.chameleon.core.namespace.Namespace;

import java.util.List;

public interface LazyDocumentLoader extends DocumentLoader {
  
  public default List<String> targetDeclarationNames(Namespace ns) throws InputException {
    return refreshTargetDeclarationNames(ns);
  }


}
