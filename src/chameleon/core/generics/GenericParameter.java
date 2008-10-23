package chameleon.core.generics;

import chameleon.core.MetamodelException;
import chameleon.core.type.TypeReference;

/**
 * Created for experimentation
 * User: koenvdk
 * Date: 18-aug-2006
 * Time: 13:45:11
 *
 * Stelt de T in een <... T ...> voor, dus de declaratie van een generische parameter, zowel voor klassesn als methoden.
 * //todo is dit een subklasse van iets?
 */
public class GenericParameter {                           //todo extends elem impl en is een type-elem

    private TypeReference superTypeConstraint;   //todo constraint objecten maken    en MI
    private TypeReference subTypeConstraint;

    public TypeReference getSubTypeConstraint() {
        return subTypeConstraint;
    }

    public void setSubTypeConstraint(TypeReference subType) {
        subTypeConstraint = subType;
    }

    public TypeReference getSuperTypeConstraint() {
        return superTypeConstraint;
    }

    public void setSuperTypeConstraint(TypeReference superType) {
        superTypeConstraint = superType;
    }

    public boolean canHaveAsConcreteType(TypeReference type) throws MetamodelException {
        return type.getType().subTypeOf(getSuperTypeConstraint().getType()) && getSubTypeConstraint().getType().subTypeOf(type.getType());
    }

}
