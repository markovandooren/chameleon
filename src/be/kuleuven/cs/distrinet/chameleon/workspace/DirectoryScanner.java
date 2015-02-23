package be.kuleuven.cs.distrinet.chameleon.workspace;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import org.antlr.runtime.RecognitionException;

import antlr.TokenStreamException;
import be.kuleuven.cs.distrinet.chameleon.core.document.Document;
import be.kuleuven.cs.distrinet.chameleon.input.ParseException;
import be.kuleuven.cs.distrinet.rejuse.action.Nothing;
import be.kuleuven.cs.distrinet.rejuse.predicate.Predicate;

/**
 * A class for recursively scanning files from a directory.
 * 
 * A directory scanner stores a path instead of a file object such
 * that we can write a new configuration file whose paths are identical
 * to the configuration file that was originally read.
 * 
 * @author Marko van Dooren
 */
public class DirectoryScanner extends DocumentScannerImpl implements FileScanner {
	

	/**
	 * Create a new directory scanner for the given root directory, document loader factory,
	 * file filter, and base scanner setting.
	 * 
	 * @param root The path of the root directory from which elements must be loaded.
	 * @param filter A filter that selects files in the zip file based on their paths.
	 * @param factory The factory for the document loaders.
	 */
 /*@
   @ public behavior
   @
   @ pre root != null;
   @
   @ post path() == root;
   @ post filter() == filter;
   @ post isBaseScanner() == false;
   @ post documentLoaderFactor() == factory;
   @*/
	public DirectoryScanner(String root, Predicate<? super String,Nothing> filter, FileDocumentLoaderFactory factory) {
		this(root, filter, false, factory);
	}

	/**
	 * Create a new directory scanner for the given root directory, document loader factory,
	 * file filter, and base scanner setting.
	 * 
	 * @param root The path of the root directory from which elements must be loaded.
	 * @param filter A filter that selects files in the zip file based on their paths.
	 * @param isBaseScanner Indicates whether the scanner is responsible for scanning a base library.
    * @param factory The factory for the document loaders.
	 */
 /*@
   @ public behavior
   @
   @ pre root != null;
   @
   @ post path() == root;
   @ post filter() == filter;
   @ post isBaseScanner() == isBaseScanner;
   @ post documentLoaderFactor() == factory;
   @*/
	public DirectoryScanner(String root, Predicate<? super String,Nothing> filter, boolean isBaseScanner, FileDocumentLoaderFactory factory) {
		super(isBaseScanner);
		setPath(root);
		setDocumentLoaderFactory(factory);
		setFilter(filter);
	}
	
	protected void setFilter(Predicate<? super String,Nothing> filter) {
		if(filter == null) {
			throw new IllegalArgumentException("The file name filter of a directory scanner cannot be null");
		}
		_filter = filter;
	}
	
	/**
	 * @return The file name filter.
	 */
	public Predicate<? super String,Nothing> filter() {
		return _filter;
	}
	
	private Predicate<? super String,Nothing> _filter;
	
	/**
	 * This method is called when the directory scanner is connected to a view.
	 */
	@Override
	public void notifyContainerConnected(DocumentScannerContainer container) throws ProjectException {
		View view = view();
		if(view != null) {
			setRoot(view.project().absoluteFile(path()));
			includeCustom();
		}
	}

	private void setDocumentLoaderFactory(FileDocumentLoaderFactory factory) {
		if(factory == null) {
			throw new IllegalArgumentException("The given file document loader factory is null.");
		}
		_documentLoaderFactory = factory;
	}
	
	private File _root;
	
	private String _path;
	
	/**
	 * Return the path from which this directory scanner loads files.
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
	
	/**
	 * @return The top-level directory from which sources are loaded.
	 */
	public File root() {
		return _root;
	}
	
	private void setRoot(File root) {
		if(root == null) {
			throw new IllegalArgumentException();
		}
		_root = root;
	}
	
	/**
	 * @return The document loader factory.
	 */
	public FileDocumentLoaderFactory documentLoaderFactory() {
		return _documentLoaderFactory;
	}

	private FileDocumentLoaderFactory _documentLoaderFactory;
	
	
	private void includeCustom() throws ProjectException {
		documentLoaderFactory().initialize(view().namespace());
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
      File[] files = root.listFiles(new FilenameFilter() {
         @Override
         public boolean accept(File file, String extension) {
            return filter().eval(extension);
         }
      });
      File[] subdirs = root.listFiles(new FileFilter() {
         @Override
         public boolean accept(File pathname) {
            return pathname.isDirectory();
         }
      });
      if (files != null) {
         for (File file : files) {
            try {
               addToModel(file);
            } catch (InputException e) {
               throw new ProjectException(e);
            }
         }
      }
      if (subdirs != null) {
         for (File subDir : subdirs) {
            // push dir
            documentLoaderFactory().pushDirectory(subDir.getName());
            // recurse
            doIncludeCustom(subDir);
            // pop dir
            documentLoaderFactory().popDirectory();
         }
      }
   }
  
  /**
   * Return whether the custom files will be scanned recursively.
   */
  public boolean customRecursive() {
  	return _recursive;
  }
  
  private boolean _recursive = true;

//	private void addToModel(Collection<File> files) throws IOException, ParseException {
//		final int size = files.size();
//		class Counter {
//			private int count;
//			
//			synchronized void increase() {
//				this.count++;
//			}
//			synchronized int get() {
//				return count;
//			}
//		}
//		final Counter counter = new Counter();
//		final BlockingQueue<File> fileQueue = new ArrayBlockingQueue<File>(files.size(), true, files);
//
//		Action<File,Exception> unsafeAction = new Action<File,Exception>(File.class) {
//			private boolean _debug = false;
//			@Override
//         public void doPerform(File file) throws InputException {
//				counter.increase();
//				if(_debug) {System.out.println(counter.get()+" of "+size+" :"+file.getAbsolutePath());};
//				addToModel(file);
//			} 
//		};
//	  CallableFactory factory = new QueuePollingCallableFactory<File,Exception>(unsafeAction,fileQueue);
//	  try {
//	  	new FixedThreadCallableExecutor<Exception>(factory).run();
//	  } catch (IOException e) {
//	  	throw e;
//	  } catch (ParseException e) {
//	  	throw e;
//	  } catch (ExecutionException e) {
//	  	Throwable cause = e.getCause();
//			cause.printStackTrace();
//	  	if(cause instanceof IOException) {
//	  		throw (IOException)cause;
//	  	} else if(cause instanceof ParseException) {
//	  		throw (ParseException)cause;
//	  	} 
//	  } catch (Exception e) {
//	  	e.printStackTrace();
//	  }
//	}
	
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
    add(documentLoaderFactory().create(file,this));
	}

	@Override
	public synchronized IFileDocumentLoader tryToAdd(File file) throws InputException {
		IFileDocumentLoader result = null;
		try {
			if(responsibleFor(file) && ! file.isHidden()) {
				File relative = file.getParentFile();
				List<String> names = new ArrayList<String>();
				while(relative != null && (! relative.equals(_root))) {
					String relativeName = relative.getName();
					if(new File(relativeName).isHidden()) {
						return result;
					}
					names.add(relativeName);
					relative = relative.getParentFile();
				}
				if(relative != null) {
//					_documentLoaderFactory.resetToRoot();
					int size = names.size();
					for(int i = size - 1; i >= 0; i--) {
						_documentLoaderFactory.pushDirectory(names.get(i));
					}
					result = _documentLoaderFactory.create(file,this);		
				}
			}
		}
		finally {
			_documentLoaderFactory.initialize(view().namespace());
		}
		return result;
	}
	
	@Override
	public synchronized void tryToRemove(File file) throws InputException {
		DocumentLoader toRemove = null;
		for(DocumentLoader source: documentLoaders()) {
			IFileDocumentLoader fsource = ((IFileDocumentLoader)source);
			if(fsource.file().equals(file)) {
				toRemove = source; 
				break;
			}
		}
		if(toRemove != null) {
			remove(toRemove);
		}
	}
	
	@Override
	public boolean scansSameAs(DocumentScanner obj) {
		if(obj == this) {
			return true;
		}
		if(obj instanceof DirectoryScanner) {
			DirectoryScanner scanner = (DirectoryScanner) obj;
			return path().equals(scanner.path()) && filter().equals(scanner.filter());
		}
		return false;
	}
	
	@Override
	public String toString() {
		return "Directory scanner: "+path()+" with filter: "+filter().toString();
	}
	
	@Override
   public String label() {
		return path();
	}
	
	@Override
	public boolean canAddDocumentLoader(DocumentLoader source) {
		return source instanceof IFileDocumentLoader;
	}
	
	@Override
	public Document documentOf(File absoluteFile) throws InputException {
		for(DocumentLoader source: documentLoaders()) {
			//Safe cast since we control the addition of document loaders.
			IFileDocumentLoader s = (IFileDocumentLoader) source;
			if(s.file().equals(absoluteFile)) {
				return s.load();
			}
		}
		return null;
	}
}
