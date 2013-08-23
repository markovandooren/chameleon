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
import be.kuleuven.cs.distrinet.rejuse.predicate.SafePredicate;

public class ZipLoader extends AbstractZipLoader {

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
   @ pre zipPath != null;
   @
   @ post path() == zipPath;
   @ post filter() == filter;
   @ post isBaseLoader() == isBaseLoader;
   @*/
	public ZipLoader(ZipFile zipFile, Predicate<? super String,Nothing> filter, boolean isBaseLoader) {
		super(zipFile, filter, isBaseLoader);
	}

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
   @ pre zipPath != null;
   @
   @ post path() == zipPath;
   @ post filter() == filter;
   @ post isBaseLoader() == false;
   @*/
	public ZipLoader(ZipFile zipFile, Predicate<? super String,Nothing> filter) {
		this(zipFile, filter, false);
	}

	@Override
	protected void processMap(ZipFile zipFile, List<Pair<Pair<String, String>, ZipEntry>> names) throws InputException {
		for(Pair<Pair<String, String>, ZipEntry> pair: names) {
			ZipEntry entry = pair.second();
			String qn = pair.first().second();
			if(qn.contains(".")) {
				throw new InputException("The ZipLoader class cannot yet deal with separate files for nested declarations.");
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
