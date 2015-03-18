package org.aikodi.chameleon.analysis.dependency;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.aikodi.chameleon.analysis.AnalysisOptions;
import org.aikodi.chameleon.analysis.OptionGroup;
import org.aikodi.chameleon.analysis.PredicateOptionGroup;
import org.aikodi.chameleon.analysis.predicate.IsSource;
import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.namespace.Namespace;
import org.aikodi.chameleon.plugin.LanguagePluginImpl;
import org.aikodi.chameleon.ui.widget.checkbox.CheckboxPredicateSelector;
import org.aikodi.chameleon.workspace.DocumentScanner;
import org.aikodi.chameleon.workspace.Project;
import org.aikodi.chameleon.workspace.View;

import be.kuleuven.cs.distrinet.rejuse.function.Function;
import be.kuleuven.cs.distrinet.rejuse.predicate.True;

import com.google.common.collect.ImmutableList;

public class DefaultDependencyOptionsFactory extends LanguagePluginImpl implements DependencyOptionsFactory {

	@Override
	public AnalysisOptions createConfiguration() {
		return new DefaultDependencyOptions();
	}

	public static Function identity() {
		Function identity = new Function() {

			@Override
			public Object apply(Object argument) throws Exception {
				return argument;
			}
		};
		return identity;
	}
	
	public static class DefaultDependencyOptions extends DependencyOptions<Element, DependencyResult> {

		@Override
		public List<? extends OptionGroup> optionGroups() {
			return ImmutableList.of(_target);
		}
		
		private TargetOptionGroup _target = new TargetOptionGroup();
		private class TargetOptionGroup extends PredicateOptionGroup {

			public TargetOptionGroup() {
				super("Target");
				addPredicateSelector(onlySource());
			}
			
		}

		public CheckboxPredicateSelector<Element> onlySource() {
			return new CheckboxPredicateSelector<>(new IsSource(), "Only source declarations");
		}

		@Override
		public DependencyResult analyze() {
			DependencyAnalysis<Declaration, Declaration> dependencyAnalysis = new DependencyAnalysis<>(
					Declaration.class,
					new True(), 
					new True(), 
					Declaration.class, identity(), _target.predicate(), 
					new True(),
					new DependencyAnalysis.NOOP());
			Set<Namespace> namespaces = new HashSet<>();
			for(View view: _project.views()) {
				for(DocumentScanner scanner: view.sourceScanners()) {
					namespaces.addAll(scanner.namespaces());
				}
			}
			for(Namespace namespace: namespaces) {
			  dependencyAnalysis.traverse(namespace.logical());
			}

			return dependencyAnalysis.result();
		}
		
		@Override
		public void setContext(Object context) {
			super.setContext(context);
			if(context instanceof Project) {
				_project = (Project) context;
			} else if(context instanceof Element) {
				_project = ((Element)context).project();
			}
		}
		
		private Project _project;
	}

	@Override
	public LanguagePluginImpl clone() {
		return new DefaultDependencyOptionsFactory();
	}

}
