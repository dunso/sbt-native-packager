package com.typesafe.sbt.packager.archetypes.scripts

import sbt._

/**
  * Keys related to the [[BashStartScriptPlugin]]
  *
  * @see [[BashStartScriptPlugin]]
  */
trait BashStartScriptKeys {

  //- Creates or discovers the bash script used by this project.
  val makeBashScripts = TaskKey[Seq[(File, String)]]("makeBashScripts", "Creates start scripts for this project.")

  //- The location of the bash script template.
  val bashScriptTemplateLocation =
    TaskKey[File]("bashScriptTemplateLocation", "The location of the bash script template.")

  val bashScriptReplacements = TaskKey[Seq[(String, String)]](
    "bashScriptReplacements",
    """|Replacements of template parameters used in the bash script.
       |  Default supported templates:
       |  app_mainclass - the main class that should be executed
       |""".stripMargin
  )

  val bashScriptDefines =
    TaskKey[Seq[String]]("bashScriptDefines", "A list of definitions that should be written to the bash file template.")

  //- A list of extra definitions that should be written to the bash file template.
  val bashScriptExtraDefines = TaskKey[Seq[String]](
    "bashScriptExtraDefines",
    "A list of extra definitions that should be written to the bash file template."
  )

  //- The location of the bash script on the target system. Default ${app_home}/../conf/application.ini
  val bashScriptConfigLocation = TaskKey[Option[String]](
    "bashScriptConfigLocation",
    "The location where the bash script will load default argument configuration from."
  )
}
