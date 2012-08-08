package chameleon.test.provider;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;

import org.antlr.runtime.RecognitionException;
import org.rejuse.io.DirectoryScanner;

import chameleon.core.document.Document;
import chameleon.core.language.Language;
import chameleon.core.namespace.RootNamespace;
import chameleon.input.ModelFactory;
import chameleon.input.ParseException;
import chameleon.util.concurrent.CallableFactory;
import chameleon.util.concurrent.FixedThreadCallableExecutor;
import chameleon.util.concurrent.QueuePollingCallableFactory;
import chameleon.util.concurrent.UnsafeAction;
import chameleon.workspace.Project;
import chameleon.workspace.ProjectBuilder;

public class DirectoryProjectBuilder implements ProjectBuilder {
	
	/**
	 * Create a new model provider with the given factory.
	 */
 /*@
   @ public behavior
   @
   @ pre factory != null;
   @
   @ post factory() == factory;
   @*/
	public DirectoryProjectBuilder(ModelFactory factory, String fileExtension) {
		setFactory(factory);
		setFileExtension(fileExtension);
	}
	
	private Project _project;
	
	public Project project() {
		return _project;
	}
	
	private ModelFactory _factory;
	
	/**
	 * Return the model factory used by this provider to create models.
	 * @return
	 */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @*/
	public ModelFactory factory() {
		return _factory;
	}
	
	/**
	 * Set the model factory.
	 */
 /*@
   @ public behavior
   @
   @ pre factory != null;
   @
   @ post factory() == factory;
   @*/
	protected void setFactory(ModelFactory factory) {
		_factory = factory;
	}
	
	private String _fileExtension;

	/**
	 * Return the extension of the files that will be read to create the model.
	 */
	public String fileExtension() {
		return _fileExtension;
	}

	/**
	 * Set the file extension for this model provider.
	 */
	public void setFileExtension(String fileExtension) {
		_fileExtension = fileExtension;
	}
	
	

//	/**
//	 * Create a new model. The base files are processed, the predefined elements
//	 * are added, and the custom files are processed.
//	 */
//	public Project create() throws ProjectException {
//		Project result = createProject(name(), language());
//		// Create a clone, we don't want to accidentally add files to an existing model.
//    ModelFactory factory = factory().language().clone().plugin(ModelFactory.class);
//    factory.initializeBase(baseFiles());
//    factory.addToModel(customFiles());
//    return factory.language();
//	}
	
	protected Project createProject(String name, Language language) {
		return new Project(name, new RootNamespace());
	}
	
	/**
	 * Add the given directory to the list of directories that contain the custom model.
	 */
  public void includeCustom(String dirName) {
    _customFiles.add(dirName);
  }
  
	/**
	 * Add the given directory to the list of directories that contain the base library for the language.
	 */
  public void includeBase(String dirName) {
  	_baseFiles.add(dirName);
  }

  /**
   * Return the collection of base files.
   */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @ post ! \result.contains(null);
   @*/
  public Collection<File> customFiles() {
    List<File> files = new ArrayList<File>();
    for(String path: _customFiles) {
      files.addAll(new DirectoryScanner().scan(path, fileExtension(), customRecursive()));
    }
    return files;
  }

  /**
   * Return the collection of base files.
   */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @ post ! \result.contains(null);
   @*/
  public Collection<File> baseFiles() {
    List<File> files = new ArrayList<File>();
    for(String path: _baseFiles) {
      files.addAll(new DirectoryScanner().scan(path, fileExtension(), baseRecursive()));
    }
    return files;
  }

  private ArrayList<String> _customFiles = new ArrayList<String>();

  private ArrayList<String> _baseFiles = new ArrayList<String>();

  /**
   * Return whether the custom files will be scanned recursively.
   */
  public boolean customRecursive() {
  	return _recursive;
  }
  
  /**
   * Return whether the custom files will be scanned recursively.
   */
  public boolean baseRecursive() {
  	return _baseRecursive;
  }
  
  /**
	 * Return the symbol for separating directories from each other
	 */
 /*@
   @ public behavior
   @
   @ post \result == File.separator;
   @*/
	public String separator() {
	  return File.separator;
	}

  private boolean _recursive = true;

  private boolean _baseRecursive = true;
  
	/**
	 * Initialize the base infrastructure from the given collection of files.
	 * A model will be created from the given files, and any predefined elements
	 * will be added. 
	 */
	public void initializeBase(Collection<File> base) throws IOException, ParseException {
		addToModel(base);
		modelFactory().initializePredefinedElements(project().namespace());
	}
	
	

	public void addToModel(Collection<File> files) throws IOException, ParseException {
		final int size = files.size();
		class Counter {
			private int count;
			
			synchronized void increase() {
				this.count++;
			}
			synchronized int get() {
				return count;
			}
		}
		final Counter counter = new Counter();
		final BlockingQueue<File> fileQueue = new ArrayBlockingQueue<File>(files.size(), true, files);

	  UnsafeAction<File,Exception> unsafeAction = new UnsafeAction<File,Exception>() {
	  	private boolean _debug = false;
		public void actuallyPerform(File file) throws IOException, ParseException {
					counter.increase();
					if(_debug) {System.out.println(counter.get()+" of "+size+" :"+file.getAbsolutePath());};
  			  addToModel(file);
		} 
	  };
	  CallableFactory factory = new QueuePollingCallableFactory<File,Exception>(unsafeAction,fileQueue);
	  try {
	  	new FixedThreadCallableExecutor<Exception>(factory).run();
	  } catch (IOException e) {
	  	throw e;
	  } catch (ParseException e) {
	  	throw e;
	  } catch (ExecutionException e) {
	  	Throwable cause = e.getCause();
			cause.printStackTrace();
	  	if(cause instanceof IOException) {
	  		throw (IOException)cause;
	  	} else if(cause instanceof ParseException) {
	  		throw (ParseException)cause;
	  	} 
	  } catch (Exception e) {
	  	e.printStackTrace();
	  }
	}
	
	/**
	 * Return the top of the metamodel when parsing the given file.
	 *
	 * @param files
	 *            A set containing all .java files that should be parsed.
	 * @pre The given set may not be null | files != null
	 * @pre The given set may only contain effective files | for all o in files: |
	 *      o instanceof File && | ! ((File)o).isDirectory()
	 * @return The result will not be null | result != null
	 * @return All given files will be parsed and inserted into the metamodel
	 *
	 * @throws TokenStreamException
	 *             Something went wrong
	 * @throws RecognitionException
	 *             Something went wrong
	 * @throws MalformedURLException
	 *             Something went wrong
	 * @throws FileNotFoundException
	 *             Something went wrong
	 * @throws RecognitionException 
	 */
	public void addToModel(File file) throws IOException, ParseException {
    // The constructor throws an FileNotFoundException if for some
    // reason the file can't be read.
    InputStream fileInputStream = new FileInputStream(file);

		modelFactory().parse(fileInputStream, new Document());
	}
	
	private ModelFactory modelFactory() {
		
	}

}
