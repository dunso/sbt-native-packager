package com.typesafe.sbt.packager.archetypes.systemloader

import sbt._

trait SystemloaderKeys {

  //- Loading system to be used for application start script (SystemV, Upstart, Systemd).
  //- This setting can be used to trigger systemloader specific behaviour in your build.
  val serverLoading = SettingKey[Option[ServerLoader.ServerLoader]](
    "server-loader",
    "Loading system to be used for application start script"
  )

  //- Determines if service will be automatically started after installation. The default value is true.
  val serviceAutostart = SettingKey[Boolean]("service-autostart", "Automatically start the service after installation")

  //- Sequence of runlevels on which application will start up
  val startRunlevels =
    SettingKey[Option[String]]("start-runlevels", "Sequence of runlevels on which application will start up")
  val stopRunlevels =
    SettingKey[Option[String]]("stop-runlevels", "Sequence of runlevels on which application will stop")

  //- Names of system services that should be provided at application start
  val requiredStartFacilities = SettingKey[Option[String]](
    "required-start-facilities",
    "Names of system services that should be provided at application start"
  )

  //- Names of system services that should be provided at application stop
  val requiredStopFacilities =
    SettingKey[Option[String]]("required-stop-facilities", "Names of system services that should be provided at")
  val termTimeout =
    SettingKey[Int]("term-timeout", "Timeout before sigterm on stop")

  //- Timeout before sigterm on stop
  val killTimeout = SettingKey[Int]("kill-timeout", "Timeout before sigkill on stop (after term)")

  //- Timeout between retries in seconds
  val retryTimeout =
    SettingKey[Int]("retry-timeout", "Timeout between retries in seconds")

  //- Number of retries to start service
  val retries =
    SettingKey[Int]("retries", "Number of retries to start service")

}
