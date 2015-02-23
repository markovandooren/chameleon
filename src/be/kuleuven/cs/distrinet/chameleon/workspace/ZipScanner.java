package be.kuleuven.cs.distrinet.chameleon.workspace;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import be.kuleuven.cs.distrinet.chameleon.core.namespace.InputSourceNamespace;
import be.kuleuven.cs.distrinet.chameleon.util.Pair;
import be.kuleuven.cs.distrinet.rejuse.action.Nothing;
import be.kuleuven.cs.distrinet.rejuse.predicate.Predicate;

public class ZipScanner extends AbstractZipScanner {

	/**
	 * Create a new zip scanner for the jar with the given path, file filter, 
	 * and base scanner setting.
	 * 
	 * @param path The path of the jar file from which elements must be scanned.
	 * @param filter A filter that selects files in the zip file based on their paths.
	 * @param isBaseScanner Indicates whether the scanner is responsible for scanning a base library.
	 */
 /*@
   @ public behavior
   @
   @ pre zipPath != null;
   @
   @ post path() == zipPath;
   @ post filter() == filter;
   @ post isBaseScanner() == isBaseScanner;
   @*/
	public ZipScanner(ZipFile zipFile, Predicate<? super String,Nothing> filter, boolean isBaseScanner) {
		super(zipFile, filter, isBaseScanner);
	}

	/**
	 * Create a new zip scanner for the jar with the given path, file filter.
	 * The scanner does not load a base library.
	 * 
	 * @param path The path of the jar file from which elements must be loaded.
	 * @param filter A filter that selects files in the zip file based on their paths.
	 */
 /*@
   @ public behavior
   @
   @ pre zipPath != null;
   @
   @ post path() == zipPath;
   @ post filter() == filter;
   @ post isBaseScanner() == false;
   @*/
	public ZipScanner(ZipFile zipFile, Predicate<? super String,Nothing> filter) {
		this(zipFile, filter, false);
	}

	@Override
	protected void processMap(ZipFile zipFile, List<Pair<Pair<String, String>, ZipEntry>> names) throws InputException {
		for(Pair<Pair<String, String>, ZipEntry> pair: names) {
			ZipEntry entry = pair.second();
			String qn = pair.first().second();
			if(qn.contains(".")) {
				throw new InputException("The ZipScanner class cannot yet deal with separate files for nested declarations.");
			} else {
				try {
					String packageFQN = namespaceFQN(entry.getName());
					InputStream inputStream = new BufferedInputStream(zipFile.getInputStream(entry));
					InputSourceNamespace ns = (InputSourceNamespace) view().namespace().getOrCreateNamespace(packageFQN);
					InputSource source = createInputSource(inputStream,qn,ns);
				} catch (IOException e) {
					throw new InputException(e);
				}
			}
		}
	}

	private InputSource createInputSource(InputStream stream, String declarationName, InputSourceNamespace ns) throws InputException {
		return new LazyReadOnceStreamInputSource(stream,declarationName,ns,this);
	}
	
}
