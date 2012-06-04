package com.dtolabs.root.plugin.tools;

import java.io.*;
import java.io.IOException;
import java.util.*;
import java.util.Properties;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.jfrog.maven.annomojo.annotations.MojoAggregator;
import org.jfrog.maven.annomojo.annotations.MojoGoal;
import org.jfrog.maven.annomojo.annotations.MojoParameter;



@MojoGoal("writeVersion")
@MojoAggregator
public class WriteVersionMojo extends AbstractMojo {
	
	@MojoParameter(readonly = true, required = true, expression = "${project}")
	private MavenProject project;

	protected MavenProject getProject() {
		return project;
	}

	@MojoParameter(readonly = true, required = true, expression = "${reactorProjects}")
	protected List<MavenProject> reactorProjects;

	protected MavenProject getRootProject() {
		MavenProject p = reactorProjects.get(0);
		return p;
	}
	
	  @MojoParameter(required = true, expression = "${basedir}")
	    private File sourceDir;
	
	@Override
	public void execute() throws MojoExecutionException {

		MavenProject rootProject = getRootProject();
		String pomversion = rootProject.getVersion();
		Properties properties = new Properties();
		properties.put("projectVersion", pomversion);
		// Write properties file.
		try {
			properties.store(new FileOutputStream(new File(sourceDir,"version.properties")), null);
		} catch (IOException e) {
			throw new MojoExecutionException("error writing file",e);
		}
	}
	
}