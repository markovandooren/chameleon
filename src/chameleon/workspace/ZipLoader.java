package chameleon.workspace;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.rejuse.predicate.SafePredicate;

import chameleon.core.namespace.InputSourceNamespace;
import chameleon.util.Pair;

public class ZipLoader extends AbstractZipLoader {

	public ZipLoader(String zipPath, SafePredicate<? super String> filter) {
		super(zipPath, filter);
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
					addInputSource(source);
				} catch (IOException e) {
					throw new InputException(e);
				}
			}
		}
	}

	private InputSource createInputSource(InputStream stream, String declarationName, InputSourceNamespace ns) throws InputException {
		return new LazyStreamInputSource(stream,declarationName,ns);
	}

}