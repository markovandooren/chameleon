package org.aikodi.chameleon.oo.type;

import org.aikodi.chameleon.core.lookup.LookupException;

public interface BoxableTypeReference extends TypeReference {

    BoxableTypeReference box() throws LookupException;
}
