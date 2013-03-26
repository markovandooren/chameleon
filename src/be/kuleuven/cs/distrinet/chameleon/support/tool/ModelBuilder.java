/**
 * 
 */
package be.kuleuven.cs.distrinet.chameleon.support.tool;

import java.io.File;
import java.io.IOException;

import be.kuleuven.cs.distrinet.chameleon.core.namespace.Namespace;
import be.kuleuven.cs.distrinet.chameleon.input.ParseException;
import be.kuleuven.cs.distrinet.chameleon.test.provider.BasicNamespaceProvider;
import be.kuleuven.cs.distrinet.chameleon.test.provider.ElementProvider;
import be.kuleuven.cs.distrinet.chameleon.workspace.BootstrapProjectConfig;
import be.kuleuven.cs.distrinet.chameleon.workspace.ConfigException;
import be.kuleuven.cs.distrinet.chameleon.workspace.Project;
import be.kuleuven.cs.distrinet.chameleon.workspace.ProjectException;
import be.kuleuven.cs.distrinet.chameleon.workspace.Workspace;

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
	 * @param arguments The command line arguments provided in the format described in the class header.
	 * @param extension The file extension of the files that must be read in the directories provided as arguments.
	 * @throws ProjectException 
	 * @throws ConfigException 
	 */
 /*@
   @ public behavior
   @
   @ pre modelFactory != null;
   @ pre arguments != null;
   @ pre extension != null;
   @*/
	public ModelBuilder(String[] arguments, Workspace workspace) throws ConfigException {
		File projectXML = new File(arguments[0]);
		BootstrapProjectConfig config = new BootstrapProjectConfig(projectXML.getParentFile(), workspace);
		_project = config.project(projectXML,null);
		processArguments(arguments);
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
	private void processArguments(String[] arguments) {

// FIXME Support this		
//   * args[n] = fqn of package to read, let this start with "#" to NOT read the package recursively.
//		*...1 or more packageFqns possible...

		
    // Configure base library directory
    _namespaceProvider = new BasicNamespaceProvider();
    
    for(int i = 1; i < arguments.length;i++) {
     	String arg = arguments[i];
			if(arg.startsWith("@")) {
				_namespaceProvider.addNamespace(arg.substring(1));
      }
    }
	}
	
	public ElementProvider<Namespace> namespaceProvider() {
		return _namespaceProvider;
	}
	
	public Project project() {
		return _project;
	}
	
	private Project _project;
	
	private BasicNamespaceProvider _namespaceProvider;
		
	

}
