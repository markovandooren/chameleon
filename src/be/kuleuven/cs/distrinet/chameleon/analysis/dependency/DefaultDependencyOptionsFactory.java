package be.kuleuven.cs.distrinet.chameleon.analysis.dependency;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import be.kuleuven.cs.distrinet.chameleon.analysis.AbstractAnalysisOptions;
import be.kuleuven.cs.distrinet.chameleon.analysis.AnalysisOptions;
import be.kuleuven.cs.distrinet.chameleon.analysis.OptionGroup;
import be.kuleuven.cs.distrinet.chameleon.analysis.PredicateOptionGroup;
import be.kuleuven.cs.distrinet.chameleon.analysis.predicate.IsSource;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;
import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.namespace.Namespace;
import be.kuleuven.cs.distrinet.chameleon.plugin.LanguagePluginImpl;
import be.kuleuven.cs.distrinet.chameleon.ui.widget.checkbox.CheckboxPredicateSelector;
import be.kuleuven.cs.distrinet.chameleon.workspace.DocumentLoader;
import be.kuleuven.cs.distrinet.chameleon.workspace.Project;
import be.kuleuven.cs.distrinet.chameleon.workspace.View;
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
				for(DocumentLoader loader: view.sourceLoaders()) {
					namespaces.addAll(loader.namespaces());
				}
			}
			for(Namespace namespace: namespaces) {
				namespace.apply(dependencyAnalysis);
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
