package org.aikodi.chameleon.support.tool;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.aikodi.chameleon.core.namespace.Namespace;
import org.aikodi.chameleon.oo.type.Type;

/**
 * @author Marko van Dooren
 * @author Tim Laeremans
 */
public class Arguments {
/**
 * @param defaultPackage
 * @param files
 * @param types
 */
	public Arguments(String outputDirName, Namespace defaultNamespace, Set<File> files, Set<Type> types, List arguments) {
  _outputDirName = outputDirName;
  _defaultNamespace = defaultNamespace;
  _fileSet = files;
  _types = new ArrayList<Type>(types);
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

public Set<File> getFileSet() {
  return _fileSet;
}

	private Set<File> _fileSet;


public List<Type> getTypes() {
  return new ArrayList(_types);
}

private List<Type> _types;

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

//public List<CompilationUnit> getCompilationUnits() {
//  final List<CompilationUnit> result = new ArrayList<CompilationUnit>();
//  new Visitor() {
//    public void visit(Object element) {
//    	Type t = (Type)element;
//    	NamespacePart np = t.nearestAncestor(NamespacePart.class);
//    	//it is possible that the user wants to include klasses
//    	//that were in the default namespace
//    	//we must make sure that the default types like int, long, ...
//    	//that are also in the default namespace, are not printed
//    	//(because they are not linked to a compilation unit).
//    	if(np.parent() != null && np.getNearestNamespacePart() != null)
//    		result.add(np.getNearestNamespacePart());
//    }
//  }.applyTo(getTypes());
//  return result;
//}

//public Set<Method> getMethods() {
//  final Set result = new HashSet();
//  new Visitor() {
//    public void visit(Object element) {
//    	result.addAll(((CompilationUnit)element).descendants(Method.class));
//    }
//  }.applyTo(getCompilationUnits());
//  return result;
//}

public List getArguments() {
  return new ArrayList(_arguments);
}

private List _arguments;
}
