package org.aikodi.chameleon.analysis.dependency;

import static org.aikodi.rejuse.predicate.UniversalPredicate.isTrue;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.aikodi.chameleon.analysis.AnalysisOptions;
import org.aikodi.chameleon.analysis.OptionGroup;
import org.aikodi.chameleon.analysis.PredicateOptionGroup;
import org.aikodi.chameleon.analysis.predicate.IsSource;
import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.namespace.Namespace;
import org.aikodi.chameleon.plugin.LanguagePluginImpl;
import org.aikodi.chameleon.ui.widget.checkbox.CheckboxPredicateSelector;
import org.aikodi.chameleon.workspace.DocumentScanner;
import org.aikodi.chameleon.workspace.Project;
import org.aikodi.chameleon.workspace.View;
import org.aikodi.rejuse.action.Nothing;
import org.aikodi.rejuse.data.tree.GuardedTreeStructure;
import org.aikodi.rejuse.data.tree.walker.GuardedTreeWalker;
import org.aikodi.rejuse.exception.Handler;
import org.aikodi.rejuse.function.Function;

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
          isTrue(), 
          isTrue(), 
          Declaration.class, identity(), _target.predicate(), 
          isTrue(),
          new DependencyAnalysis.NOOP());
      Set<Namespace> namespaces = new HashSet<>();
      for(View view: _project.views()) {
        for(DocumentScanner scanner: view.sourceScanners()) {
          namespaces.addAll(scanner.namespaces());
        }
      }
      for(Namespace namespace: namespaces) {
        Handler<LookupException, Nothing> resume = Handler.<LookupException>resume();
        GuardedTreeWalker<Element, LookupException, Nothing> guardedTreeWalker = new GuardedTreeWalker<>(dependencyAnalysis, resume);
        GuardedTreeStructure<Element, LookupException, Nothing> guardedTree = new GuardedTreeStructure<>(namespace.logical(), resume);
        guardedTreeWalker.traverse(guardedTree);
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
