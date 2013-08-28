package be.kuleuven.cs.distrinet.chameleon.analysis.dependency;

import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.analysis.AbstractAnalysisOptions;
import be.kuleuven.cs.distrinet.chameleon.analysis.Analysis;
import be.kuleuven.cs.distrinet.chameleon.analysis.AnalysisOptions;
import be.kuleuven.cs.distrinet.chameleon.analysis.OptionGroup;
import be.kuleuven.cs.distrinet.chameleon.analysis.PredicateOptionGroup;
import be.kuleuven.cs.distrinet.chameleon.analysis.predicate.IsSource;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;
import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.plugin.LanguagePluginImpl;
import be.kuleuven.cs.distrinet.chameleon.ui.widget.checkbox.CheckboxSelector;
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
	
	public static class DefaultDependencyOptions extends AbstractAnalysisOptions<Element, DependencyResult> {

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

		public CheckboxSelector<Element> onlySource() {
			return new CheckboxSelector<>(new IsSource(), "Only source declarations");
		}

		@Override
		public Analysis createAnalysis() {
			return new DependencyAnalysis<>(
					Declaration.class,
					new True(), 
					new True(), 
					Declaration.class, identity(), _target.predicate(), 
					new True(),
					new DependencyAnalysis.NOOP());
		}
		
	}

	@Override
	public LanguagePluginImpl clone() {
		return new DefaultDependencyOptionsFactory();
	}

}
