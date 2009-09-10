package chameleon.test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.rejuse.io.DirectoryScanner;

import chameleon.core.language.Language;
import chameleon.input.ModelFactory;
import chameleon.input.ParseException;

public class BasicModelProvider implements ModelProvider {
	
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
	public BasicModelProvider(ModelFactory factory, String fileExtension) {
		setFactory(factory);
		setFileExtension(fileExtension);
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

	/**
	 * Create a new model. The base files are processed, the predefined elements
	 * are added, and the custom files are processed.
	 */
	public Language model() throws ParseException, IOException {
		// Create a clone, we don't want to accidentally add files to an existing model.
    ModelFactory factory = factory().clone();
    factory.initializeBase(baseFiles());
    factory.addToModel(customFiles());
    return factory.language();
	}
	
	/**
	 * Add the given directory to the list of directories that contain the custom model.
	 */
  public void include(String dirName) {
    _files.add(dirName);
  }
  
	/**
	 * Add the given directory to the list of directories that contain the base library for the language.
	 */
  public void includeBase(String dirName) {
    _files.add(dirName);
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
    for(String path: _baseFiles) {
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

  private ArrayList<String> _files = new ArrayList<String>();

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
}
