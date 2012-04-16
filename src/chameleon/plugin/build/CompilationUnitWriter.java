package chameleon.plugin.build;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import chameleon.core.compilationunit.Document;
import chameleon.core.lookup.LookupException;
import chameleon.exception.ModelException;
import chameleon.plugin.PluginImpl;
import chameleon.plugin.output.Syntax;

public abstract class CompilationUnitWriter extends PluginImpl {

	String _extension;

	public String extension() {
		return _extension;
	}
	
	public CompilationUnitWriter(File outputDir, String extension) {
		_outputDir = outputDir;
		_extension = extension;
	}

	public String outputDirName() {
		return outputDir().getAbsolutePath();
	}
	
	public File outputDir() {
		return _outputDir;
	}

	private File _outputDir;
	
	public File write(Document cu) throws LookupException, ModelException, IOException {
		Syntax writer = cu.language().plugin(Syntax.class);
		if(writer != null) {
			String fileName = fileName(cu);
			if(fileName != null) {
				String packageFQN = packageFQN(cu);
				String relDirName = packageFQN.replace('.', File.separatorChar);
				File out = new File(outputDirName()+File.separatorChar + relDirName + File.separatorChar + fileName);
				File parent = out.getParentFile();
				parent.mkdirs();
				out.createNewFile();
				FileWriter fw = new FileWriter(out);
				fw.write(writer.toCode(cu));
				fw.close();
				System.out.println("Wrote: "+out.getAbsolutePath());
				return out;
			}
		} 
		return null;
	}
	
	public abstract String fileName(Document compilationUnit) throws LookupException, ModelException;

	public abstract String packageFQN(Document compilationUnit) throws LookupException, ModelException;
	
}
