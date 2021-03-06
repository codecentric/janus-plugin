/*
 * Copyright (C) 2012 codecentric AG
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.codecentric.janus.plugin.vcs;

import de.codecentric.janus.VersionControlSystem;
import de.codecentric.janus.plugin.Validatable;
import hudson.Extension;
import hudson.model.Describable;
import hudson.model.Descriptor;
import hudson.model.Hudson;
import hudson.util.FormValidation;
import org.apache.commons.validator.routines.UrlValidator;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class VCSConfiguration implements Describable<VCSConfiguration>, Validatable {
    private static final UrlValidator URL_VALIDATOR = new UrlValidator(
            UrlValidator.ALLOW_LOCAL_URLS | UrlValidator.ALLOW_ALL_SCHEMES
    );

    private String name,
            checkoutUrl,
            checkoutBuildJob,
            commitBuildJob,
            generationBuildJob;
    private VersionControlSystem vcs;

    public VCSConfiguration() {
    }

    @DataBoundConstructor
    public VCSConfiguration(String name,
                            String checkoutUrl,
                            String checkoutBuildJob,
                            String commitBuildJob,
                            String generationBuildJob,
                            VersionControlSystem vcs) {
        this.name = name;
        this.checkoutUrl = checkoutUrl;
        this.checkoutBuildJob = checkoutBuildJob;
        this.commitBuildJob = commitBuildJob;
        this.generationBuildJob = generationBuildJob;
        this.vcs = vcs;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCheckoutUrl() {
        return checkoutUrl;
    }

    public void setCheckoutUrl(String checkoutUrl) {
        this.checkoutUrl = checkoutUrl;
    }

    public String getCheckoutBuildJob() {
        return checkoutBuildJob;
    }

    public void setCheckoutBuildJob(String checkoutBuildJob) {
        this.checkoutBuildJob = checkoutBuildJob;
    }

    public String getCommitBuildJob() {
        return commitBuildJob;
    }

    public void setCommitBuildJob(String commitBuildJob) {
        this.commitBuildJob = commitBuildJob;
    }

    public String getGenerationBuildJob() {
        return generationBuildJob;
    }

    public void setGenerationBuildJob(String generationBuildJob) {
        this.generationBuildJob = generationBuildJob;
    }

    public VersionControlSystem getVcs() {
        return vcs;
    }

    public void setVcs(VersionControlSystem vcs) {
        this.vcs = vcs;
    }

    public boolean isValid() {
        FormValidation.Kind ok = FormValidation.Kind.OK;

        if (doCheckName(name).kind != ok) {
            return false;
        } else if (vcs == null) {
            return false;
        } else if (doCheckCheckoutUrl(checkoutUrl).kind != ok) {
            return false;
        } else if (doCheckBuildJob(generationBuildJob).kind != ok) {
            return false;
        } else if (doCheckBuildJob(checkoutBuildJob).kind != ok) {
            return false;
        } else if (doCheckBuildJob(commitBuildJob).kind != ok) {
            return false;
        }

        return true;
    }

    public static FormValidation doCheckName(@QueryParameter String value) {
        if (value == null || value.trim().isEmpty()) {
            return FormValidation.error("Please provide a name.");
        }

        return FormValidation.ok();
    }

    public static FormValidation doCheckCheckoutUrl(@QueryParameter String value) {
        if (URL_VALIDATOR.isValid(value)) {
            return FormValidation.ok();
        }

        return FormValidation.error("Please provide a valid URL.");
    }

    public static FormValidation doCheckVcs(@QueryParameter String value) {
        if (value == null || value.trim().isEmpty()) {
            return FormValidation.error("Please select one of the " +
                    "Version Control Systems.");
        }

        try {
            VersionControlSystem.valueOf(value);
        } catch (IllegalArgumentException ex) {
            return FormValidation.error("This Version Control " +
                    "System is not supported.");
        }

        return FormValidation.ok();
    }

    public static FormValidation doCheckBuildJob(@QueryParameter String value) {
        if (value == null || value.trim().isEmpty()) {
            return FormValidation.error("Please select one of the " +
                    "build jobs.");
        }

        if (Hudson.getInstance().getJobNames().contains(value)) {
            return FormValidation.ok();
        } else {
            return FormValidation.error("This build job doesn't exist.");
        }
    }

    public ConfigurationView getDescriptor() {
        return (ConfigurationView) Hudson
                .getInstance()
                .getDescriptor(VCSConfiguration.class);
    }

    public static VCSConfiguration[] get() {
        VCSConfiguration[] configs;
        configs = new ConfigurationView().getConfigurations();

        if (configs == null) {
            configs = new VCSConfiguration[]{};
        }

        return configs;
    }

    @Override
    public String toString() {
        return "VCSConfiguration{" +
                "name='" + name + '\'' +
                '}';
    }
}
