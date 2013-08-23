package be.kuleuven.cs.distrinet.chameleon.ui.widget;

import be.kuleuven.cs.distrinet.chameleon.workspace.DocumentLoader;

import com.google.common.collect.ImmutableList;

public class DocumentLoaderNode {
	public DocumentLoaderNode(String _name, ImmutableList<DocumentLoader> _documentLoaders) {
		this._name = _name;
		this._documentLoaders = _documentLoaders;
	}
	public String name() {
		return _name;
	}
	private String _name;
	public ImmutableList<DocumentLoader> documentLoaders() {
		return _documentLoaders;
	}
	private ImmutableList<DocumentLoader> _documentLoaders;
}