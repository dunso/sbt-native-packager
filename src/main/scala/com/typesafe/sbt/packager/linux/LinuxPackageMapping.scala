package com.typesafe.sbt
package packager
package linux

import sbt._
import LinuxPlugin.Users

/**
  *
  * @param user The user owning all the mappings. Default = “root”
  * @param group The group owning all the mappings. Default = “root”
  * @param permissions Access permissions for all the mappings. Default = “755”
  * @param config Are the mappings config files. Default = “false”
  * @param docs Are the mappings docs. Default = false
  */
case class LinuxFileMetaData(user: String = Users.Root,
                             group: String = Users.Root,
                             permissions: String = "755",
                             config: String = "false",
                             docs: Boolean = false) {

  def withUser(u: String) = copy(user = u)
  def withGroup(g: String) = copy(group = g)
  def withPerms(p: String) = copy(permissions = p)
  def withConfig(value: String = "true") = copy(config = value)
  def asDocs() = copy(docs = true)
}

/**
  *
  * @param mappings A list of mappings aggregated by this LinuxPackageMapping
  * @param fileData Permissions for all the defined mappings. Default = “root:root 755”
  * @param zipped  Are the mappings zipped? Default = false
  */
case class LinuxPackageMapping(mappings: Traversable[(File, String)],
                               fileData: LinuxFileMetaData = LinuxFileMetaData(),
                               zipped: Boolean = false) {

  /**
    * This denotes which user should be the owner of the given files in the resulting package.
    *
    * @param user
    * @return
    */
  def withUser(user: String) = copy(fileData = fileData withUser user)

  /**
    * This denotes which group should be the owner of the given files in the resulting package.
    *
    * @param group
    * @return
    */
  def withGroup(group: String) = copy(fileData = fileData withGroup group)

  /**
    * This function adjusts the installation permissions of the associated files.
    * The flags passed should be of the form of a mask, e.g. 0755.
    *
    * @param perms
    * @return
    */
  def withPerms(perms: String) = copy(fileData = fileData withPerms perms)

  /**
    * This denotes whether or not a %config attribute is attached to the given files in the generated rpm SPEC.
    * Any value other than "true" will be placed inside the %config() definition.
    * For example withConfig("noreplace") results in %config(noreplace) attribute in the rpm spec.
    * @param c
    * @return
    */
  def withConfig(c: String = "true") = copy(fileData = fileData withConfig c)
  def withContents() =
    copy(mappings = Mapper.mapDirectoryAndContents(mappings.toSeq: _*))

  /**
    * This denotes that the mapped files are documentation files.
    * Note: I believe these are only used for ``RPM``s.
    *
    * @return
    */
  def asDocs() = copy(fileData = fileData asDocs ())

  /** Modifies the current package mapping to have gzipped data. */
  /**
    * This ensures that the files are written in compressed format to the destination.
    * This is a convenience for distributions that want files zipped.
    *
    * @return
    */
  def gzipped = copy(zipped = true)
}

// TODO - Maybe this can support globbing symlinks?
// Maybe it should share an ancestor with LinuxPackageMapping so we can configure symlinks the same time as normal files?
/**
  * encapsulate symlinks on your destination system.
  * @param link The actual link that points to destination
  * @param destination The link destination
  */
case class LinuxSymlink(link: String, destination: String)
object LinuxSymlink {

  def makeRelative(from: String, to: String): String = {
    val partsFrom: Seq[String] = from split "/" filterNot (_.isEmpty)
    val partsTo: Seq[String] = to split "/" filterNot (_.isEmpty)

    val prefixAndOne = (1 to partsFrom.length)
      .map(partsFrom.take)
      .dropWhile(seq => partsTo.startsWith(seq))
      .headOption getOrElse sys.error("Cannot symlink to yourself!")
    val prefix = prefixAndOne dropRight 1
    if (prefix.length > 0) {
      val escapeCount = (partsTo.length - 1) - prefix.length
      val escapes = (0 until escapeCount) map (i => "..")
      val remainder = partsFrom drop prefix.length
      (escapes ++ remainder).mkString("/")
    } else from
  }
  // TODO - Does this belong here?
  def makeSymLinks(symlinks: Seq[LinuxSymlink], pkgDir: File, relativeLinks: Boolean = true): Unit =
    for (link <- symlinks) {
      // TODO - drop preceeding '/'
      def dropFirstSlash(n: String): String =
        if (n startsWith "/") n drop 1
        else n
      def addFirstSlash(n: String): String =
        if (n startsWith "/") n
        else "/" + n
      val to = pkgDir / dropFirstSlash(link.link)
      val linkDir = to.getParentFile
      if (!linkDir.isDirectory) IO.createDirectory(linkDir)
      val name = IO.relativize(linkDir, to).getOrElse {
        sys.error("Could not relativize names (" + to + ") (" + linkDir + ")!!! *(logic error)*")
      }
      val linkFinal =
        if (relativeLinks) makeRelative(link.destination, link.link)
        else addFirstSlash(link.destination)
      // from ln man page
      // -f --force remove existing destination files
      if (!to.exists)
        sys.process.Process(Seq("ln", "-sf", linkFinal, name), linkDir).! match {
          case 0 => ()
          case n =>
            sys.error("Failed to symlink " + link.destination + " to " + to)
        }
    }
}
