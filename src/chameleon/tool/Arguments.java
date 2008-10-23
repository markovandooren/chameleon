package chameleon.tool;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.rejuse.java.collections.Visitor;

import chameleon.core.compilationunit.CompilationUnit;
import chameleon.core.method.Method;
import chameleon.core.namespace.Namespace;
import chameleon.core.namespacepart.NamespacePart;
import chameleon.core.type.Type;

/**
 * @author Tim Laeremans
 */
public class Arguments {
/**
 * @param defaultPackage
 * @param set
 * @param types
 */
	public Arguments(String outputDirName, Namespace defaultNamespace, Set set, Set types, List arguments) {
  _outputDirName = outputDirName;
  _defaultNamespace = defaultNamespace;
  _fileSet = set;
  _types = new HashSet(types);
  _arguments = new ArrayList(arguments);
}

	
public Namespace getDefaultNamespace() {
  return _defaultNamespace;
}

	private Namespace _defaultNamespace;


public String getOutputDirName() {
  return _outputDirName;
}

private String _outputDirName;

public Set getFileSet() {
  return _fileSet;
}

	private Set _fileSet;


public Set getTypes() {
  return new HashSet(_types);
}

private Set _types;

//public Set getPublicTypes() {
//  Set result = getTypes();
//  new PrimitiveTotalPredicate() {
//    public boolean eval(Object o) {
//      return (((Type)o).getAccessModifier().getClass().equals(new Public().getClass())||
//      		((Type)o).getAccessModifier().getClass().equals(new Default().getClass()));          
//    }
//  }.filter(result);
//  return result;
//}



public Set getCompilationUnits() {
  final HashSet result = new HashSet();
  new Visitor() {
    public void visit(Object element) {
    	Type t = (Type)element;
    	NamespacePart np = t.getNearestNamespacePart() ;
    	//it is possible that the user wants to include klasses
    	//that were in the default namespace
    	//we must make shure that the default types like int, long, ...
    	//that are also in the default namespace, are not printed
    	//(because they are not linked to a compilation unit).
    	if(np.getParent() != null && np.getNearestNamespacePart() != null)
    		result.add(np.getNearestNamespacePart());
    }
  }.applyTo(getTypes());
  return result;
}

public Set getMethods() {
  final Set result = new HashSet();
  new Visitor() {
    public void visit(Object element) {
    	result.addAll(((CompilationUnit)element).getDescendants(Method.class));
    }
  }.applyTo(getCompilationUnits());
  return result;
}

public List getArguments() {
  return new ArrayList(_arguments);
}

private List _arguments;
}
