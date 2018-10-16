package com.typesafe.sbt
package packager
package rpm

import linux._
import sbt._

/** RPM Specific keys. */
trait RpmKeys {
  // METADATA keys.
  val rpmVendor =
    SettingKey[String]("rpm-vendor", "Name of the vendor for this RPM.")
  val rpmOs = SettingKey[String]("rpm-os", "Name of the os for this RPM.")

  //- The release number is the package’s version.
  //- When the sofware is first packaged at a particular version, the release should be "1".
  //- If the software is repackaged at the same version, the release number should be incremented,
  //- and dropped back to "1" when the software version is new.
  //- Its value defines the third com ponent of the rpm file name (packageName-version-rpmRelease.packageArchitecture.rpm),
  //- as well as the Release: tag in the spec file. Its default value is "1".
  val rpmRelease = SettingKey[String]("rpm-release", "Special release number for this rpm (vs. the software).")

  //- The path passed set as the base for the revocable package
  val rpmPrefix = SettingKey[Option[String]]("rpm-prefix", "File system prefix for relocatable package.")
  val rpmMetadata = SettingKey[RpmMetadata]("rpm-metadata", "Metadata associated with the generated RPM.")

  // Changelog
  //- External file to be imported and used to generate the changelog of the RPM.
  val rpmChangelogFile = SettingKey[Option[String]]("rpm-changelog-file", "RPM changelog file to be imported")

  // DESCRIPTION KEYS
  // TODO - Summary and license are required.
  val rpmLicense = SettingKey[Option[String]]("rpm-license", "License of the code within the RPM.")
  val rpmDistribution = SettingKey[Option[String]]("rpm-distribution")
  val rpmUrl =
    SettingKey[Option[String]]("rpm-url", "Url to include in the RPM.")
  val rpmGroup =
    SettingKey[Option[String]]("rpm-group", "Group to associate with the RPM.")
  val rpmPackager =
    SettingKey[Option[String]]("rpm-packger", "Person who packaged this rpm.")
  val rpmIcon = SettingKey[Option[String]]("rpm-icon", "name of the icon to use with this RPM.")
  val rpmDescription =
    SettingKey[RpmDescription]("rpm-description", "Description of this rpm.")

  // DEPENDENCIES
  //- Enable or disable the automatic processing of provided packages.
  //- Takes the form "yes" or "no", defaults to "yes". Defines the AutoProv: tag in the spec file.
  val rpmAutoprov =
    SettingKey[String]("rpm-autoprov", "enable/disable automatic processing of 'provides' (\"yes\"/\"no\").")

  //- Enable or disable the automatic processing of required packages.
  //- Takes the form "yes" or "no", defaults to "yes". Defines the AutoReq: tag in the spec file.
  val rpmAutoreq =
    SettingKey[String]("rpm-autoreq", "enable/disable automatic processing of requirements (\"yes\"/\"no\").")

  //- The RPM package names that this RPM provides.
  val rpmProvides =
    SettingKey[Seq[String]]("rpm-provides", "Packages this RPM provides.")

  //- The RPM packages that are required to be installed for this RPM to work.
  val rpmRequirements =
    SettingKey[Seq[String]]("rpm-requirements", "Packages this RPM requires.")

  //- The RPM packages this RPM needs before installation
  val rpmPrerequisites = SettingKey[Seq[String]]("rpm-prerequisites", "Packages this RPM need *before* installation.")

  //- The packages this RPM allows you to remove
  val rpmObsoletes = SettingKey[Seq[String]]("rpm-obsoletes", "Packages this RPM makes obsolete.")
  val rpmConflicts = SettingKey[Seq[String]]("rpm-conflicts", "Packages this RPM conflicts with.")
  val rpmDependencies =
    SettingKey[RpmDependencies]("rpm-dependencies", "Configuration of dependency info for this RPM.")

  //- Run rpmbuild via Linux setarch command. Use this for cross-platform builds.
  val rpmSetarch = SettingKey[Option[String]]("rpm-setarch", "run rpmbuild in the context of an architecture.")

  // MAINTAINER SCRIPTS
  @deprecated("Use maintainerScripts in RPM and RpmConstants.Pretrans instead.", "1.1.x")
  val rpmPretrans =
    SettingKey[Option[String]]("rpm-pretrans", "%pretrans scriptlet")
  @deprecated("Use maintainerScripts in RPM and RpmConstants.Pre instead.", "1.1.x")
  val rpmPre = SettingKey[Option[String]]("rpm-pre", "%pre scriptlet")
  @deprecated("Use maintainerScripts in RPM and RpmConstants.Verifyscript instead.", "1.1.x")
  val rpmVerifyscript =
    SettingKey[Option[String]]("rpm-verifyscript", "%verifyscript scriptlet")
  @deprecated("Use maintainerScripts in RPM and RpmConstants.Post instead.", "1.1.x")
  val rpmPost = SettingKey[Option[String]]("rpm-post", "%post scriptlet")
  @deprecated("Use maintainerScripts in RPM and RpmConstants.Posttrans instead.", "1.1.x")
  val rpmPosttrans =
    SettingKey[Option[String]]("rpm-posttrans", "%posttrans scriptlet")
  @deprecated("Use maintainerScripts in RPM and RpmConstants.Preun instead.", "1.1.x")
  val rpmPreun = SettingKey[Option[String]]("rpm-preun", "%preun scriptlet")
  @deprecated("Use maintainerScripts in RPM and RpmConstants.Postun instead.", "1.1.x")
  val rpmPostun = SettingKey[Option[String]]("rpm-postun", "%postun scriptlet")

  // SPEC
  val rpmSpecConfig = TaskKey[RpmSpec]("rpm-spec-config", "All the configuration for an RPM .spec file.")

  // SCRIPTS
  val rpmScripts = TaskKey[RpmScripts]("rpm-scripts", "Configuration of pre- and post-integration scripts.")
  val rpmScriptsDirectory = SettingKey[File](
    "rpm-scriptlets-directory",
    "Directory where all debian control scripts reside. Default is 'src/rpm/scriptlets'"
  )

  val rpmBrpJavaRepackJars = SettingKey[Boolean](
    "brp-java-repack-jars",
    """Overrides the __os_post_install scriptlet
      http://swaeku.github.io/blog/2013/08/05/how-to-disable-brp-java-repack-jars-during-rpm-build/ for details"""
  )

  // Building
  val rpmLint = TaskKey[Unit]("rpm-lint", "Runs rpmlint program against the generated RPM, if available.")

  @deprecated("Use daemonStdoutLogFile instead", "1.1.x")
  val rpmDaemonLogFile =
    SettingKey[String]("rpm-daemon-log-file", "Name of the log file generated by application daemon")

}
