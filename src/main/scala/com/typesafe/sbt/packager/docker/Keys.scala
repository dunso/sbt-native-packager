package com.typesafe.sbt
package packager
package docker

import sbt._

/**
  * Docker settings
  */
trait DockerKeys {
  val dockerGenerateConfig = TaskKey[File]("docker-generate-config", "Generates configuration file for Docker.")
  val dockerPackageMappings =
    TaskKey[Seq[(File, String)]]("docker-package-mappings", "Generates location mappings for Docker build.")

  //- The image to use as a base for running the application.
  //- It should include binaries on the path for chown, mkdir, have a discoverable java binary,
  //- and include the user configured by daemonUser (daemon, by default).
  val dockerBaseImage =
    SettingKey[String]("dockerBaseImage", "Base image for Dockerfile.")

  //- A list of TCP ports to expose from the Docker image.
  val dockerExposedPorts = SettingKey[Seq[Int]]("dockerExposedPorts", "TCP Ports exposed by Docker image")

  //- A list of UDP ports to expose from the Docker image.
  val dockerExposedUdpPorts = SettingKey[Seq[Int]]("dockerExposedUdpPorts", "UDP Ports exposed by Docker image")

  //- A list of data volumes to make available in the Docker image.
  val dockerExposedVolumes = SettingKey[Seq[String]]("dockerExposedVolumes", "Volumes exposed by Docker image")

  //- The repository to which the image is pushed when the docker:publish task is run.
  //- This should be of the form [repository.host[:repository.port]] (assumes use of the index.docker.io repository)
  //- or [repository.host[:repository.port]][/username] (discouraged, but available for backwards compatibilty.).
  val dockerRepository = SettingKey[Option[String]]("dockerRepository", "Repository for published Docker image")

  //- The username or organization to which the image is pushed when the docker:publish task is run.
  //- This should be of the form [username] or [organization].
  val dockerUsername = SettingKey[Option[String]]("dockerUsername", "Username for published Docker image")

  //- The alias to be used for tagging the resulting image of the Docker build.
  //- The type of the setting key is DockerAlias. Defaults to [dockerRepository/][dockerUsername/][packageName]:[version].
  val dockerAlias =
    SettingKey[DockerAlias]("dockerAlias", "Docker alias for the built image")

  //- The list of aliases to be used for tagging the resulting image of the Docker build.
  //- The type of the setting key is Seq[DockerAlias].
  //- Alias values are in format of [dockerRepository/][dockerUsername/][packageName]:[tag]
  //- where tags are list of including your project version and latest tag(if dockerUpdateLatest is enabled).
  //- To append additional aliases to this list, you can add them by extending dockerAlias.
  //- dockerAliases ++= Seq(dockerAlias.value.withTag(Option("stable")),
  //- dockerAlias.value.withRegistryHost(Option("registry.internal.yourdomain.com")))
  val dockerAliases =
    SettingKey[Seq[DockerAlias]]("dockerAliases", "Docker aliases for the built image")

  //- The flag to automatic update the latest tag when the docker:publish task is run.
  //- Default value is FALSE. In order to use this setting, the minimum docker console version required is 1.10.
  //- See https://github.com/sbt/sbt-native-packager/issues/871 for a detailed explanation.
  val dockerUpdateLatest =
    SettingKey[Boolean]("dockerUpdateLatest", "Set to update latest tag")

  //- Overrides the default entrypoint for docker-specific service discovery tasks before running the application.
  //- Defaults to the bash executable script, available at bin/<script name> in the current WORKDIR of /opt/docker.
  val dockerEntrypoint = SettingKey[Seq[String]]("dockerEntrypoint", "Entrypoint arguments passed in exec form")
  val dockerCmd = SettingKey[Seq[String]](
    "dockerCmd",
    "Docker CMD. Used together with dockerEntrypoint. Arguments passed in exec form"
  )

  //- Overrides the default Docker exec command. Defaults to Seq("docker")
  val dockerExecCommand = SettingKey[Seq[String]]("dockerExecCommand", "The shell command used to exec Docker")

  //- The docker server version. Used to leverage new docker features while maintaining backwards compatibility.
  val dockerVersion = TaskKey[Option[DockerVersion]]("dockerVersion", "The docker server version")

  //- Overrides the default Docker build options. Defaults to Seq("--force-rm", "-t", "[dockerAlias]").
  //- This default is expanded if dockerUpdateLatest is set to true.
  val dockerBuildOptions = SettingKey[Seq[String]]("dockerBuildOptions", "Options used for the Docker build")

  //- Overrides the default Docker build command. The reason for this is that many systems restrict docker execution to root,
  //- and while the accepted guidance is to alias the docker command alias docker='/usr/bin/docker',
  //- neither Java nor Scala support passing aliases to sub-processes, and most build systems run builds using a non-login,
  //- non-interactive shell, which also have limited support for aliases,
  //- which means that the only viable option is to use sudo docker directly.
  //- Defaults to Seq("[dockerExecCommand]", "build", "[dockerBuildOptions]", ".").
  val dockerBuildCommand = SettingKey[Seq[String]]("dockerBuildCommand", "Command for building the Docker image")

  //- A map of labels that will be applied to the Docker image.
  val dockerLabels = SettingKey[Map[String, String]]("dockerLabels", "Labels applied to the Docker image")

  //- A map of environment variables that will be applied to the Docker image.
  val dockerEnvVars =
    SettingKey[Map[String, String]]("dockerEnvVars", "Environment Variables applied to the Docker image")

  //- Overrides the default Docker rmi command. This may be used if force flags or other options need to be passed to the command docker rmi.
  //- Defaults to Seq("[dockerExecCommand]", "rmi") and will be directly appended with the image name and tag.
  val dockerRmiCommand =
    SettingKey[Seq[String]]("dockerRmiCommand", "Command for removing the Docker image from the local registry")

  val dockerCommands = TaskKey[Seq[CmdLike]]("dockerCommands", "List of docker commands that form the Dockerfile")
}
