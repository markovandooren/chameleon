/**
 * 
 */
package chameleon.support.tool;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import chameleon.core.namespace.Namespace;
import chameleon.input.ModelFactory;
import chameleon.input.ParseException;
import chameleon.test.provider.BasicNamespaceProvider;
import chameleon.test.provider.DirectoryProjectBuilder;
import chameleon.test.provider.ElementProvider;
import chameleon.workspace.Project;

/**
 * A class for building models for a command line tool. The input arguments are used to create a model,
 * and an accompanying namespace provider that provides the namespaces provided as arguments.
 * 
 * @author Marko van Dooren
 */
public class ModelBuilder {

	/**
	 * Create a new model builder.
	 * 
	 * @param modelFactory The factory used to create the model based on the input files.
	 * @param arguments The command line arguments provided in the format described in the class header.
	 * @param extension The file extension of the files that must be read in the directories provided as arguments.
	 */
 /*@
   @ public behavior
   @
   @ pre modelFactory != null;
   @ pre arguments != null;
   @ pre extension != null;
   @*/
	public ModelBuilder(ModelFactory modelFactory, String[] arguments, String extension, boolean output, boolean base) throws ParseException, IOException {
		_projectBuilder = new DirectoryProjectBuilder(modelFactory, extension);
		_output = output;
		_base = base;
		_arguments = Arrays.asList(arguments);
		processArguments();
	}
	
  /**
   * args[0] = path for the directory to write output IF output() == true
   * args[1] = path to read input files
   * ...1 or more input paths possible...
   * args[i] = fqn of package to read, let this start with "@" to read the package recursively
   *...1 or more packageFqns possible...
   *
   * Example 
   * java Copy outputDir inputDir1 inputDir2 @myPackage.subPackage #java #java.security 
   * @throws IOException 
   * @throws ParseException 
   */
	private void processArguments() throws ParseException, IOException {

// FIXME Support this		
//   * args[n] = fqn of package to read, let this start with "#" to NOT read the package recursively.
//		*...1 or more packageFqns possible...

		
		int low = (output() ? 1 : 0);
    if(output()) {
    	_outputDir = new File(argument(1));
    }
    if(base()) {
    	low++;
    }
    // Configure base library directory
    if(base()) {
     	int baseIndex = low-1;
			String arg = argument(baseIndex+1);
			if(! arg.startsWith("@") && ! arg.startsWith("#")&& ! arg.startsWith("%")) {
     		_projectBuilder.includeBase(arg);
      }
    }
    _namespaceProvider = new BasicNamespaceProvider();
    
    for(int i = low; i < _arguments.size();i++) {
     	String arg = argument(i+1);
			if(! arg.startsWith("@")) {
				if(! arg.startsWith("#")&& ! arg.startsWith("%")) {
					_projectBuilder.includeCustom(arg);
				}
      } else {
				_namespaceProvider.addNamespace(arg.substring(1));
      }
    }
    _project = _projectBuilder.project();
	}
	
	public ElementProvider<Namespace> namespaceProvider() {
		return _namespaceProvider;
	}
	
	public Project project() {
		return _project;
	}
	
	private Project _project;
	
	private BasicNamespaceProvider _namespaceProvider;
	
	private DirectoryProjectBuilder _projectBuilder;
	
	/**
	 * Return whether an output directory is required.
	 */
	public boolean output() {
		return _output;
	}
	
	private boolean _output;

	/**
	 * Return the file extension of the source files used to build the model.
	 */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @*/
	public String extension() {
		return _extension;
	}
	
	private String _extension;
		
	public boolean base() {
		return _base;
	}
	
	private boolean _base;
	
	private File _outputDir;

	public File outputDir() {
		return _outputDir;
	}
	
	public List<String> arguments() {
		return new ArrayList<String>(_arguments);
	}
	
	public String argument(int indexBaseOne) {
		return _arguments.get(indexBaseOne - 1);
	}
	
	private List<String> _arguments;
	
}