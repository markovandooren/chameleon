package be.kuleuven.cs.distrinet.chameleon.workspace;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import be.kuleuven.cs.distrinet.rejuse.predicate.Predicate;
import be.kuleuven.cs.distrinet.rejuse.predicate.SafePredicate;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.util.Pair;
import be.kuleuven.cs.distrinet.chameleon.util.Util;

public abstract class AbstractZipLoader extends DocumentLoaderImpl {

	/**
	 * Create a new zip loader for the jar with the given path, file filter, and base loader setting.
	 * 
	 * @param path The path of the jar file from which elements must be loaded.
	 * @param filter A filter that selects files in the zip file based on their paths.
	 * @param isBaseLoader Indicates whether the loader is responsible for loading a base library.
	 */
 /*@
   @ public behavior
   @
   @ pre path != null;
   @
   @ post path() == path;
   @ post filter() == filter;
   @ post isBaseLoader() == isBaseLoader;
   @*/
	public AbstractZipLoader(String path, SafePredicate<? super String> filter, boolean isBaseLoader) {
		setPath(path);
		setFilter(filter);
	}
	
	/**
	 * Create a new jar loader for the jar with the given path and file filter. The loader
	 * will not be responsible for loading a base library.
	 * 
	 * @param path The path of the jar file from which elements must be loaded.
	 * @param filter A filter that selects files in the zip file based on their paths.
	 */
 /*@
   @ public behavior
   @
   @ pre path != null;
   @
   @ post path() == path;
   @ post filter() == filter;
   @ post isBaseLoader() == false;
   @*/
	public AbstractZipLoader(String path, SafePredicate<? super String> filter) {
		this(path,filter,false);
	}

	private void setPath(String path) {
		_path = path;
	}
	
	private String _path;
	
	/**
	 * Return the path of the jar file from which elements must be loaded.
	 */
 /*@
   @ public behavior
   @
   @ post result != null;
   @*/
	public String path() {
		return _path;
	}
	
	private void setFilter(SafePredicate<? super String> filter) {
		_filter = filter;
	}
	
	/**
	 * Return the filter that determines which files are loaded from the zip loader based
	 * on the paths of the files.
	 * @return
	 */
	public SafePredicate<? super String> filter() {
		return _filter;
	}
	
	private SafePredicate<? super String> _filter;
	
	/**
	 * Create a {@link ZipFile} object that represents the zip file
	 * from which elements must be loaded. If the path is not absolute
	 * it is interpreted as a path relative to the project root.
	 * 
	 * @throws IOException When the path is not a valid path for a zip file.
	 */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @ post \result.getAbsolutePath().equals(project().absolutePath(path()));
   @*/
	protected ZipFile createZipFile() throws IOException {
		return new ZipFile(project().absolutePath(_path));
	}

	@Override
	protected void notifyViewAdded(View view) throws ProjectException {
		try {
			createInputSources();
		} catch (Exception e) {
			throw new ProjectException(e);
		}
	}

	protected void sortNameMap(List<Pair<Pair<String, String>, ZipEntry>> names) {
		Collections.sort(names, new Comparator<Pair<Pair<String,String>,ZipEntry>>(){
			@Override
			public int compare(Pair<Pair<String,String>, ZipEntry> o1, Pair<Pair<String,String>, ZipEntry> o2) {
				int first = o1.first().first().length();
				int second = o2.first().first().length();
				return first - second;
			}
  	});
	}

	protected List<Pair<Pair<String, String>, ZipEntry>> createNameMap(ZipFile jar) {
  	Enumeration<? extends ZipEntry> entries = jar.entries();
		List<Pair<Pair<String,String>, ZipEntry>> names = new ArrayList<Pair<Pair<String,String>, ZipEntry>>();
  	while(entries.hasMoreElements()) {
  		ZipEntry entry = entries.nextElement();
  		String name = entry.getName();
  		if(_filter.eval(name)) {
  			String tmp = Util.getAllButLastPart(name).replace('/', '.').replace('$', '.');
  			if(! tmp.matches(".*\\.[0-9].*")) {
  				names.add(new Pair<Pair<String,String>, ZipEntry>(new Pair<String,String>(tmp,Util.getLastPart(Util.getAllButLastPart(name).replace('/', '.')).replace('$', '.')), entry));
  			}
  		}
  	}
		return names;
	}
	
	protected void createInputSources() throws IOException, LookupException, InputException {
		ZipFile jar = createZipFile();
  	List<Pair<Pair<String, String>, ZipEntry>> names = createNameMap(jar);
  	// The entries must be sorted first such that if an inner class in processed, its outer
  	// class will have been processed first.
  	// TODO Explain why this is actually necessary. Not even sure it is needed anymore now that we have lazy
  	// loading. With eager loading it would try to add the inner class to the outer when the outer wasn't
  	// loaded yet.
  	sortNameMap(names);
  	processMap(jar, names);
	}

	protected abstract void processMap(ZipFile jar, List<Pair<Pair<String, String>, ZipEntry>> names) throws InputException;

	/**
	 * Return the fully qualified name of the namespace that corresponds to the directory name
	 * of the given entry name.
	 * @param entryName The name of the entry in the zip file.
	 * @return The name of the directory of the entry name with the '/' characters replaced by '.' characters. 
	 */
	protected String namespaceFQN(String entryName) {
		return Util.getAllButLastPart(Util.getAllButLastPart(entryName).replace('/', '.'));
	}

}
