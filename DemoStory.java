package com.b.jbehavetest;

import static org.jbehave.core.reporters.Format.TXT;

import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.configuration.MostUsefulConfiguration;
import org.jbehave.core.io.LoadFromClasspath;
import org.jbehave.core.io.StoryLoader;
import org.jbehave.core.junit.JUnitStory;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.jbehave.core.steps.InjectableStepsFactory;
import org.jbehave.core.steps.InstanceStepsFactory;

public class DemoStory extends JUnitStory {

	@Override
	public Configuration configuration() {
		Configuration configuration = new MostUsefulConfiguration();
		StoryLoader storyLoader;
		storyLoader = new LoadFromClasspath(this.getClass());
		configuration.useStoryLoader(storyLoader);
		StoryReporterBuilder storyReporterBuilder;
		storyReporterBuilder = new StoryReporterBuilder();
		storyReporterBuilder.withDefaultFormats();
		storyReporterBuilder.withFormats(TXT);
		configuration.useStoryReporterBuilder(storyReporterBuilder);
		return configuration;
	}

	@Override
	public InjectableStepsFactory stepsFactory() {
		return new InstanceStepsFactory(configuration(), new DemoSteps());
	}
}
