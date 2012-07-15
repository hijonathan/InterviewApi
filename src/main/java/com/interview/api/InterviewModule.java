package com.interview.api;

import javax.sql.DataSource;

import com.interview.api.resources.QuestionsResource;
import com.interview.api.utils.DbUtils;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

public class InterviewModule extends JerseyServletModule {

	@Override
	protected void configureServlets() {
		bind(QuestionsResource.class);
		bind(DataSource.class).toInstance(DbUtils.createDataSource());
		serve("/*").with(GuiceContainer.class);
	}
}
