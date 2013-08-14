package be.kuleuven.cs.distrinet.chameleon.eclipse.util;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;

import be.kuleuven.cs.distrinet.rejuse.contract.Contracts;

public class Files {

	public static void copy(IFile first, IFile second) throws CoreException {
		Contracts.notNull(first, "The source file cannot be null.");
		Contracts.notNull(second, "The destination file cannot be null.");
		Contracts.check(first.exists(), "The source file does not exist.");
		if(second.exists()) {
			second.setContents(first.getContents(), true, false, MONITOR);
		} else {
			second.create(first.getContents(), true, MONITOR);
		}
	}
	
	private final static NullProgressMonitor MONITOR = new NullProgressMonitor();
}
