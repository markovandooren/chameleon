package be.kuleuven.cs.distrinet.chameleon.analysis;

import java.util.List;

public interface AnalysisOptions {

	public List<OptionGroup> optionGroups();

	public Analysis createAnalysis();
}
