package com.b.jbehavetest.spring;

import static org.jbehave.core.io.CodeLocations.codeLocationFromClass;

import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.jbehave.core.InjectableEmbedder;
import org.jbehave.core.annotations.Configure;
import org.jbehave.core.annotations.UsingEmbedder;
import org.jbehave.core.annotations.UsingSteps;
import org.jbehave.core.annotations.spring.UsingSpring;
import org.jbehave.core.embedder.Embedder;
import org.jbehave.core.io.StoryFinder;
import org.jbehave.core.junit.spring.SpringAnnotatedEmbedderRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;



@RunWith(SpringAnnotatedEmbedderRunner.class)
@Configure()
@UsingEmbedder(embedder = Embedder.class, generateViewAfterStories = true, ignoreFailureInStories = true, ignoreFailureInView = true) 
@ContextConfiguration({
	"classpath:com/b/jbehavetest/spring/hsqldb-datasource.xml"
})
//@UsingSpring(resources = {"classpath:com/b/jbehavetest/spring/YcIntegrationSteps.xml"})
@UsingSpring(resources = {
		"classpath:com/b/jbehavetest/spring/hsqldb-datasource.xml"
})
@UsingSteps(instances = { YcIntegrationSteps.class })
public class YcIntegrationStory extends InjectableEmbedder {
	@Autowired
	private DataSource dataSource;
	@Test
	public void run() {
		injectedEmbedder().runStoriesAsPaths(storyPaths());
	}

	protected List<String> storyPaths() {
		String searchInDirectory = codeLocationFromClass(this.getClass()).getFile();
		return new StoryFinder().findPaths(searchInDirectory, Arrays.asList("**/YcIntegrationStory.story"), null);
	}

}