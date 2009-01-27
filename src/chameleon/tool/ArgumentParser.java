package chameleon.tool;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import chameleon.core.compilationunit.CompilationUnit;
import chameleon.core.element.Element;
import chameleon.core.namespace.Namespace;
import chameleon.input.MetaModelFactory;
import chameleon.input.ParseException;
import chameleon.linkage.ILinkage;
import chameleon.linkage.ILinkageFactory;
import chameleon.linkage.IParseErrorHandler;

/**
 * @author Tim Laeremans
 */
public class ArgumentParser {
  
 /*@
   @ public behavior
   @
   @ pre factory != null;
   @
   @ post getFactory() == factory;
   @*/
  public ArgumentParser(MetaModelFactory factory, boolean output) {
    _factory = factory;
    _output = output;
  }
  
 /*@
   @ public behavior
   @
   @ pre factory != null;
   @
   @ post getFactory() == factory;
   @ post getOutput() == true;
   @*/
  public ArgumentParser(MetaModelFactory factory) {
   this(factory, true);
 }
 
  private boolean _output;
  
  /**
   * Check wether or not the first argument is the output directory.
   */
  public boolean getOutput() {
    return _output;
  }
  
 /*@
   @ public behavior
   @
   @ post \result != null;
   @*/
  public MetaModelFactory getFactory() {
    return _factory;
  }

	private MetaModelFactory _factory;

	  /**
	   * outputDir?
	   * inputDir+
	   * @packageName : recursive
	   * #packageName : direct
	   * %packageName : 
	   */
  public Arguments parse(String[] args, String extension) throws ParseException, MalformedURLException, FileNotFoundException, IOException, Exception {
    int low = (getOutput() ? 1 : 0);
   // ArrayList al = new ArrayList();
    Set files = new HashSet();
   // Set files = new FileSet();
    for(int i = low; i < args.length;i++) {
    	if(! args[i].startsWith("@") && ! args[i].startsWith("#")&& ! args[i].startsWith("%")) {
        //files.include(new PatternPredicate(new File(args[i]), new FileNamePattern("**/*.csharp")));
    		files.addAll(getFactory().loadFiles(args[i],extension,true));
      }
    }
    Namespace mm = _factory.getMetaModel(new OutputParserFactory(), files);
    Set types = new HashSet();
    
    for(int i = low; i < args.length;i++) {
      if(args[i].startsWith("@")) {
        types.addAll(((Namespace)mm.findNamespaceOrType(args[i].substring(1))).getAllTypes());
      }
    }
    for(int i = low; i < args.length;i++) {
      if(args[i].startsWith("#")) {
        types.addAll(((Namespace)mm.findNamespaceOrType(args[i].substring(1))).getTypes());
      }
    }
//    new PrimitiveTotalPredicate() {
//      public boolean eval(Object o) {
//        return (! (o instanceof ArrayType));
//      }
//    }.filter(types);
    List arguments = new ArrayList();
    for(int i = low; i < args.length;i++) {
      if(args[i].startsWith("%")) {
        arguments.add(args[i].substring(1));
      }
    }
    if(getOutput()) {
    	return new Arguments(args[0], mm, files, types, arguments);
    }else {
    	return new Arguments(null, mm, files, types, arguments);
    }
  }
  
  static class OutputParserFactory implements ILinkageFactory{

	public ILinkage createLinkage(File file) {
		return new ILinkage(){

			public IParseErrorHandler getParseErrorHandler() {
				return null;
			}

			public String getSource() {
				return null;
			}

			public void decoratePosition(int offset, int length, String dectype, Element el) {
			}

			public int getLineOffset(int i) {
				return 0;
			}

			public void addCompilationUnit(CompilationUnit cu) {
				
			}};
	}}
}
