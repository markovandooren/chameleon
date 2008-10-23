package chameleon.core.generics;

import chameleon.core.MetamodelException;
import chameleon.core.namespace.NamespaceOrTypeReference;
import chameleon.core.type.Type;
import chameleon.core.type.TypeReference;

/**
 * Created for experimentation
 * User: koenvdk
 * Date: 18-aug-2006
 * Time: 11:42:56
 *
 * Stelt dingen zoals T voor, die zowel ongespecificeerd kunnen zijn, als reeds kunnen verwijzen naar een concreet type.
 * Dus stelt het gebruik van een generische parameter voor.
 */
public class GenericReference extends TypeReference {

    private TypeReference pointedType;    //todo pointedtype weg en enkel in een parameterizedtypereference bewaren?
                                        //todo of juist daar weghalen? (want komt ook nog in methodes)
    private GenericParameter genericParameter;

    public GenericReference(String qn, GenericParameter genericParameter) {
        super(qn);
        this.genericParameter = genericParameter;
        pointedType = genericParameter.getSuperTypeConstraint();//todo correct? Dit is dan eentje die nog niet concreet gemaakt is...
    }

    public GenericReference(NamespaceOrTypeReference target, String name, GenericParameter genericParameter) {
        super(target, name);
        this.genericParameter = genericParameter;
    }

    public TypeReference getPointedType() {
        return pointedType;
    }

    public void setPointedType(TypeReference pointedType) {
        this.pointedType = pointedType;
    }

    public GenericParameter getGenerical() {
        return genericParameter;
    }

    public void setGenerical(GenericParameter genericParameter) {
        this.genericParameter = genericParameter;
    }

    @Override public Type getType() throws MetamodelException {
        return getPointedType().getType();
    }

    @Override public GenericReference clone() {
        return new GenericReference(getTarget().clone(),getName(),getGenerical());
    }

}
