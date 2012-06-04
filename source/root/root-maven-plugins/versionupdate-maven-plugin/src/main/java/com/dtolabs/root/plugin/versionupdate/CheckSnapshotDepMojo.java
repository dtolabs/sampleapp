package com.dtolabs.root.plugin.versionupdate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.ArtifactUtils;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.DependencyManagement;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.jfrog.maven.annomojo.annotations.MojoAggregator;
import org.jfrog.maven.annomojo.annotations.MojoGoal;
import org.jfrog.maven.annomojo.annotations.MojoParameter;

@MojoGoal("checkSnapshotDependency")
@MojoAggregator
public class CheckSnapshotDepMojo extends AbstractMojo {
	/**
	 * A map of projects to original versions before any transformation.
	 */

	private Map<String, String> originalVersions;

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

	@Override
	public void execute() throws MojoExecutionException {

		// Check if the project is a snapshot version
		for (Iterator<MavenProject> it = reactorProjects.iterator(); it.hasNext();) {
			MavenProject project = it.next();

			String projectId = ArtifactUtils.versionlessKey(project.getGroupId(), project.getArtifactId());

			if (!ArtifactUtils.isSnapshot(project.getVersion())) {
				throw new MojoExecutionException("The project " + projectId + " isn't a snapshot ("
				        + project.getVersion() + ").");
			}
		}

		// Check if the project is dependent on any snapshots
		List<Artifact> pluginSnapshotDependencies = new ArrayList<Artifact>();

		// Aggregate all snapshot dependencies messages
		StringBuilder message = new StringBuilder();

		// Check for dependencies in the plugin etc.

		Set<String> unreleasedDeps = new TreeSet<String>();

		for (Iterator<MavenProject> i = reactorProjects.iterator(); i.hasNext();) {
			MavenProject project = i.next();

			getLog().debug("Checking Project --> " + project.getName() + " for snapshot dependencies");

			List<Artifact> projectPluginSnapshotDependencies = checkProject(project, getOriginalVersions());
			if (!projectPluginSnapshotDependencies.isEmpty()) {

				for (Iterator<Artifact> j = projectPluginSnapshotDependencies.iterator(); j.hasNext();) {
					Artifact artifact = j.next();
					// message.append("Unreleased artifact " + artifact + " in project " + project.getName());
					unreleasedDeps.add(artifact.toString());
					// message.append("\n");

				}
				getLog().debug("------ Found unreleased Dependency ------");
				pluginSnapshotDependencies.addAll(projectPluginSnapshotDependencies);
			}

		}
		// Check dependency on non-reactor internal/external artifacts

		Set<String> projectDependency = getSnapshotDependency();

		for (Iterator<String> it = projectDependency.iterator(); it.hasNext();) {
			String[] list = (it.next()).split("\\*\\*\\*");
			// message.append("\n");
			// message.append("Snapshot Dependency: " + list[1] + " in project " + list[0]);
			unreleasedDeps.add(list[1]);
		}

		// If there are any snapshot dependencies. Kill the build

		if (!pluginSnapshotDependencies.isEmpty() || !projectDependency.isEmpty()) {
			getLog().error(message);
			getLog().error("*******************************");
			getLog()
			        .error(
			                "Project has SNAPSHOT dependencies. We should not release with the following dependencies as SNAPSHOT ");
			for (String dep : unreleasedDeps) {
				getLog().error(dep);
			}
			throw new MojoExecutionException("Found non released dependencies !! ");

		}

		getLog().info("Found no unreleased (SNAPSHOT) dependencies");

	}

	private List<Artifact> checkProject(MavenProject project, Map<String, String> originalVersions)
	        throws MojoExecutionException {
		Set<Artifact> snapshotDependencies = new HashSet<Artifact>();

		if (project.getParentArtifact() != null) {
			if (checkArtifact(project.getParentArtifact(), originalVersions)) {
				snapshotDependencies.add(project.getParentArtifact());
			}
		}

		for (Iterator<Artifact> i = project.getArtifacts().iterator(); i.hasNext();) {
			Artifact artifact = i.next();

			if (checkArtifact(artifact, originalVersions)) {
				snapshotDependencies.add(artifact);
			}
		}

		for (Iterator<Artifact> i = project.getPluginArtifacts().iterator(); i.hasNext();) {
			Artifact artifact = i.next();

			if (checkArtifact(artifact, originalVersions)) {
				snapshotDependencies.add(artifact);
			}
		}

		for (Iterator<Artifact> i = project.getReportArtifacts().iterator(); i.hasNext();) {
			Artifact artifact = i.next();

			if (checkArtifact(artifact, originalVersions)) {
				snapshotDependencies.add(artifact);
			}
		}

		for (Iterator<Artifact> i = project.getExtensionArtifacts().iterator(); i.hasNext();) {
			Artifact artifact = i.next();

			if (checkArtifact(artifact, originalVersions)) {
				snapshotDependencies.add(artifact);
			}
		}

		if (!snapshotDependencies.isEmpty()) {
			List<Artifact> snapshotsList = new ArrayList<Artifact>(snapshotDependencies);
			Collections.sort(snapshotsList);
			return snapshotsList;
		} else {
			// Return empty list since there are no snapshot dependencies
			return new ArrayList<Artifact>();
		}

	}

	private static boolean checkArtifact(Artifact artifact, Map<String, String> originalVersions) {
		String versionlessArtifactKey = ArtifactUtils.versionlessKey(artifact.getGroupId(), artifact.getArtifactId());

		// We are only looking at dependencies external to the project - ignore
		// anything found in the reactor as
		// it's version will be updated
		return !artifact.getVersion().equals(originalVersions.get(versionlessArtifactKey))
		        && ArtifactUtils.isSnapshot(artifact.getVersion());
	}

	/**
	 * Retrieve the original version map, before transformation, keyed by project's versionless identifier.
	 * 
	 * @return the map of project IDs to versions.
	 */
	protected Map<String, String> getOriginalVersions() {
		if (originalVersions == null) {
			originalVersions = new HashMap<String, String>();
			for (Iterator<MavenProject> i = reactorProjects.iterator(); i.hasNext();) {
				MavenProject project = i.next();
				originalVersions.put(ArtifactUtils.versionlessKey(project.getGroupId(), project.getArtifactId()),
				        project.getVersion());
			}
		}
		return originalVersions;
	}

	/**
	 * Return the dependency of this project on snapshot versions of other components
	 */
	protected Set<String> getSnapshotDependency() {
		Set<String> snapshotVersions = new TreeSet<String>();

		for (Iterator<MavenProject> i = reactorProjects.iterator(); i.hasNext();) {
			MavenProject project = i.next();
			DependencyManagement depMgmt;

			depMgmt = project.getDependencyManagement();

			List<Dependency> dependency;
			if (depMgmt != null) {
				dependency = project.getDependencyManagement().getDependencies();
			} else {
				dependency = Collections.emptyList();
			}

			for (Iterator<Dependency> o = dependency.iterator(); o.hasNext();) {
				Dependency deps = o.next();

				if (deps.getVersion() != null && deps.getVersion().endsWith("-SNAPSHOT")) {

					snapshotVersions.add(ArtifactUtils.versionlessKey(project.getGroupId(), project.getArtifactId())
					        + "***" + ArtifactUtils.versionlessKey(deps.getGroupId(), deps.getArtifactId()) + "-"
					        + deps.getVersion());
				}
			}
		}

		return snapshotVersions;

	}

}
