package org.aikodi.chameleon.oo.type;

import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.oo.language.LanguageWithBoxing;

public interface BoxableTypeReference extends TypeReference {

    default BoxableTypeReference box() throws LookupException {
        LanguageWithBoxing language = language(LanguageWithBoxing.class);
        return language.box(this, view().namespace());
    }
}
