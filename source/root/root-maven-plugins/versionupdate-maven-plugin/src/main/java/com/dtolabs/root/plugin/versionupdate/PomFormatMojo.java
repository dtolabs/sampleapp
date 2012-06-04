package com.dtolabs.root.plugin.versionupdate;

import java.io.File;

import org.apache.maven.plugin.MojoExecutionException;
import org.jfrog.maven.annomojo.annotations.MojoGoal;

@MojoGoal("pomFormat")
public class PomFormatMojo extends VersionUpdateHelper {

    @Override
    protected void updateVersion(File pomFile) throws MojoExecutionException {
        String version = getProject().getVersion();
        updatePomVersion(pomFile, version);
    }
}
