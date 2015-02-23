package be.kuleuven.cs.distrinet.chameleon.workspace;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.util.Pair;
import be.kuleuven.cs.distrinet.chameleon.util.Util;
import be.kuleuven.cs.distrinet.rejuse.action.Nothing;
import be.kuleuven.cs.distrinet.rejuse.predicate.Predicate;

/**
 * An abstract class for document scanners that scan zip files.
 * 
 * @author Marko van Dooren
 */
public abstract class AbstractZipScanner extends DocumentScannerImpl {

	/**
	 * Create a new zip scanner for the zip with the given path, file filter, and base scanner setting.
	 * 
	 * @param path The path of the zip file from which elements must be scanned.
	 * @param filter A filter that selects files in the zip file based on their paths.
	 * @param isBaseScanner Indicates whether the scanner is responsible for scanning a base library.
	 */
 /*@
   @ public behavior
   @
   @ pre path != null;
   @
   @ post path() == path;
   @ post filter() == filter;
   @ post isBaseScanner() == isBaseScanner;
   @*/
	public AbstractZipScanner(ZipFile zipFile, Predicate<? super String,Nothing> filter, boolean isBaseScanner) {
		setPath(zipFile);
		setFilter(filter);
	}
	
	/**
	 * Create a new zip scanner for the zip with the given path and file filter. The scanner
	 * will not be responsible for loading a base library.
	 * 
	 * @param path The path of the zip file from which elements must be loaded.
	 * @param filter A filter that selects files in the zip file based on their paths.
	 */
 /*@
   @ public behavior
   @
   @ pre path != null;
   @
   @ post path() == path;
   @ post filter() == filter;
   @ post isBaseScanner() == false;
   @*/
	public AbstractZipScanner(ZipFile zipFile, Predicate<? super String,Nothing> filter) {
		this(zipFile,filter,false);
	}

	private void setPath(ZipFile file) {
		_zipFile = file;
	}
	
	private ZipFile _zipFile;
	
	/**
	 * Return the path of the zip file from which elements must be loaded.
	 */
 /*@
   @ public behavior
   @
   @ post result != null;
   @*/
	public ZipFile file() {
		return _zipFile;
	}
	
	private void setFilter(Predicate<? super String,Nothing> filter) {
		_filter = filter;
	}
	
	/**
	 * Return the filter that determines which files are loaded from the zip scanner based
	 * on the paths of the files.
	 * @return
	 */
	public Predicate<? super String,Nothing> filter() {
		return _filter;
	}
	
	private Predicate<? super String,Nothing> _filter;
	
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
	protected ZipFile zipFile() throws IOException {
		return _zipFile;
	}

	@Override
	public void notifyContainerConnected(DocumentScannerContainer container) throws ProjectException {
		try {
			if(view() != null) {
				createDocumentLoaders();
			}
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

	protected List<Pair<Pair<String, String>, ZipEntry>> createNameMap(ZipFile zip) {
  	Enumeration<? extends ZipEntry> entries = zip.entries();
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
	
	protected void createDocumentLoaders() throws IOException, LookupException, InputException {
		ZipFile zip = zipFile();
  	List<Pair<Pair<String, String>, ZipEntry>> names = createNameMap(zip);
  	// The entries must be sorted first such that if an inner class in processed, its outer
  	// class will have been processed first.
  	// TODO Explain why this is actually necessary. Not even sure it is needed anymore now that we have lazy
  	// loading. With eager loading it would try to add the inner class to the outer when the outer wasn't
  	// loaded yet.
  	sortNameMap(names);
  	processMap(zip, names);
	}

	protected abstract void processMap(ZipFile zip, List<Pair<Pair<String, String>, ZipEntry>> names) throws InputException;

	/**
	 * Return the fully qualified name of the namespace that corresponds to the directory name
	 * of the given entry name.
	 * @param entryName The name of the entry in the zip file.
	 * @return The name of the directory of the entry name with the '/' characters replaced by '.' characters. 
	 */
	protected String namespaceFQN(String entryName) {
		return Util.getAllButLastPart(Util.getAllButLastPart(entryName).replace('/', '.'));
	}

	@Override
	public boolean scansSameAs(DocumentScanner obj) {
		if(obj == this) {
			return true;
		}
		if(obj instanceof AbstractZipScanner) {
			AbstractZipScanner scanner = (AbstractZipScanner) obj;
			return scanner.filter().equals(filter()) && scanner.file().equals(file());
		}
		return false;
	}
	
	@Override
	public String label() {
		return file().getName(); 
	}


}
