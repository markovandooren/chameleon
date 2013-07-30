package be.kuleuven.cs.distrinet.chameleon.workspace;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;

import org.antlr.runtime.RecognitionException;

import be.kuleuven.cs.distrinet.chameleon.input.ParseException;
import be.kuleuven.cs.distrinet.chameleon.util.concurrent.CallableFactory;
import be.kuleuven.cs.distrinet.chameleon.util.concurrent.FixedThreadCallableExecutor;
import be.kuleuven.cs.distrinet.chameleon.util.concurrent.QueuePollingCallableFactory;
import be.kuleuven.cs.distrinet.rejuse.action.Action;
import be.kuleuven.cs.distrinet.rejuse.predicate.SafePredicate;

/**
 * A class for recursively loading files from a directory.
 * 
 * A directory loader stores a path instead of a file object such
 * that we can write a new configuration file whose paths are identical
 * to the configuration file that was originally read.
 * 
 * @author Marko van Dooren
 */
public class DirectoryLoader extends DocumentLoaderImpl implements FileLoader {
	

	/**
	 * Create a new directory loader for the given root directory, input source factor,
	 * file filter, and base loader setting.
	 * 
	 * @param root The path of the root directory from which elements must be loaded.
	 * @param filter A filter that selects files in the zip file based on their paths.
	 * @param isBaseLoader Indicates whether the loader is responsible for loading a base library.
	 */
 /*@
   @ public behavior
   @
   @ pre root != null;
   @
   @ post path() == root;
   @ post filter() == filter;
   @ post isBaseLoader() == isBaseLoader;
   @*/
	public DirectoryLoader(String root, SafePredicate<? super String> filter, FileInputSourceFactory factory) {
		this(root, filter, false, factory);
	}

	/**
	 * Create a new directory loader for the given root directory, input source factor,
	 * file filter, and base loader setting.
	 * 
	 * @param root The path of the root directory from which elements must be loaded.
	 * @param filter A filter that selects files in the zip file based on their paths.
	 * @param isBaseLoader Indicates whether the loader is responsible for loading a base library.
	 */
 /*@
   @ public behavior
   @
   @ pre root != null;
   @
   @ post path() == root;
   @ post filter() == filter;
   @ post isBaseLoader() == isBaseLoader;
   @*/
	public DirectoryLoader(String root, SafePredicate<? super String> filter, boolean isBaseLoader, FileInputSourceFactory factory) {
		super(isBaseLoader);
		setPath(root);
		setInputSourceFactory(factory);
		setFilter(filter);
	}
	
	protected void setFilter(SafePredicate<? super String> filter) {
		if(filter == null) {
			throw new IllegalArgumentException("The file name filter of a directory loader cannot be null");
		}
		_filter = filter;
	}
	
	public SafePredicate<? super String> filter() {
		return _filter;
	}
	
	private SafePredicate<? super String> _filter;
	
	/**
	 * This method is called when the directory loader is connected to a view.
	 */
	protected void notifyViewAdded(View view) throws ProjectException {
		setRoot(view.project().absoluteFile(path()));
		includeCustom();
	}

	private void setInputSourceFactory(FileInputSourceFactory factory) {
		if(factory == null) {
			throw new IllegalArgumentException("The given file input source factory is null.");
		}
		_inputSourceFactory = factory;
	}
	
	private File _root;
	
	private String _path;
	
	/**
	 * Return the path from which this directory loader loads files.
	 */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @*/
	public String path() {
		return _path;
	}
	
	private void setPath(String path) {
		if(path == null) {
			throw new IllegalArgumentException();
		}
		_path = path;
	}
	
	public File root() {
		return _root;
	}
	
	private void setRoot(File root) {
		if(root == null) {
			throw new IllegalArgumentException();
		}
		_root = root;
	}
	
	
	public FileInputSourceFactory inputSourceFactory() {
		return _inputSourceFactory;
	}

	private FileInputSourceFactory _inputSourceFactory;
	
	
	private void includeCustom() throws ProjectException {
		inputSourceFactory().initialize(view().namespace());
		doIncludeCustom(root());
	}

	private boolean responsibleFor(File file) {
		boolean result = false;
		if(file != null) {
			result = filter().eval(file.getName());
		}
		return result;
	}

	/**
	 * Add the given directory to the list of directories that contain the custom model.
	 * @throws ParseException 
	 * @throws IOException 
	 */
  private void doIncludeCustom(File root) throws ProjectException {
  	File[] files = root.listFiles(new FilenameFilter(){
			@Override
			public boolean accept(File file, String extension) {
				return filter().eval(extension);
			}
		});
  	File[] subdirs = root.listFiles(new FileFilter(){
			@Override
			public boolean accept(File pathname) {
				return pathname.isDirectory();
			}
		});
  	if(files != null) {
  		for(File file: files) {
  			try {
  				addToModel(file);
  			} catch (InputException e) {
  				throw new ProjectException(e);
  			}
  		}
  	}
  	if(subdirs != null) {
  		for(File subDir: subdirs) {
  			// push dir
  			inputSourceFactory().pushDirectory(subDir.getName());
  			// recurse
  			doIncludeCustom(subDir);
  			// pop dir
  			inputSourceFactory().popDirectory();
  		}
  	}
//  	try {
//  		addToModel(new DirectoryScanner().scan(dirName, fileExtension(), customRecursive()));
//		} catch (IOException e) {
//			throw new ProjectException(e);
//		} catch (ParseException e) {
//			throw new ProjectException(e);
//		}
  }
  
  /**
   * Return whether the custom files will be scanned recursively.
   */
  public boolean customRecursive() {
  	return _recursive;
  }
  
  private boolean _recursive = true;

	private void addToModel(Collection<File> files) throws IOException, ParseException {
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

		Action<File,Exception> unsafeAction = new Action<File,Exception>(File.class) {
			private boolean _debug = false;
			public void doPerform(File file) throws InputException {
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
	private void addToModel(File file) throws InputException {
    addInputSource(inputSourceFactory().create(file,this));
	}

	@Override
	public synchronized void tryToAdd(File file) throws InputException {
		try {
			if(responsibleFor(file) && ! file.isHidden()) {
				File relative = file.getParentFile();
				List<String> names = new ArrayList<String>();
				while(relative != null && (! relative.equals(_root))) {
					String relativeName = relative.getName();
					if(new File(relativeName).isHidden()) {
						return;
					}
					names.add(relativeName);
					relative = relative.getParentFile();
				}
				if(relative != null) {
					int size = names.size();
					for(int i = size - 1; i >= 0; i--) {
						_inputSourceFactory.pushDirectory(names.get(i));
					}
					_inputSourceFactory.create(file,this);		
				}
			}
		}
		finally {
			_inputSourceFactory.initialize(view().namespace());
		}
	}
	
	@Override
	public synchronized void tryToRemove(File file) throws InputException {
		InputSource toRemove = null;
		for(InputSource source: inputSources()) {
			IFileInputSource fsource = ((IFileInputSource)source);
			if(fsource.file().equals(file)) {
				toRemove = source; 
				break;
			}
		}
		if(toRemove != null) {
			removeInputSource(toRemove);
		}
	}
	
	
//	public static void main(String[] args) throws URISyntaxException {
//		URL objectLocation = Object.class.getResource("/java/lang/Object.class");
//		String fileName = objectLocation.getFile();
//		File file = new File(fileName.substring(5,fileName.indexOf('!')));
////		file = new File("/Users/marko");
//		System.out.println(file);
//	}

}
