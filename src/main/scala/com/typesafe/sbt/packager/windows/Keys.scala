package com.typesafe.sbt
package packager
package windows

import sbt._

/** windows settings */
trait WindowsKeys {

  //- The GUID to use to identify the windows package/product.
  val wixProductId =
    SettingKey[String]("wix-product-id", "The uuid of the windows package.")

  //- The GUID to use to identify the windows package/product upgrade identifier
  val wixProductUpgradeId =
    SettingKey[String]("wix-product-upgrade-id", "The uuid associated with upgrades for this package.")

  //- The information used to autoconstruct the <Product><Package/> portion of the wix xml. Note: unused if ``wixConfig`` is overridden
  val wixPackageInfo = SettingKey[WindowsProductInfo]("wix-package-info", "The configuration for this package.")

  //- An (optional) rtf file to display as the product license during installation. Defaults to src/windows/License.rtf
  val wixProductLicense = TaskKey[Option[File]]("wix-product-license", "The RTF file to display with licensing.")

  //- A set of windows features that users can install with this package. Note: unused if ``wixConfig`` is overridden
  val wixFeatures =
    TaskKey[Seq[WindowsFeature]]("wix-features", "Configuration of the windows installable features for this package.")

  //- inline XML to use for wix configuration. This is everything nested inside the <Product> element.
  val wixProductConfig =
    TaskKey[xml.Node]("wix-product-xml", "The WIX XML configuration for a product (nested in Wix/Product elements).")

  //- inline XML to use for wix configuration. This is used if the wixFile setting is not specified.
  val wixConfig =
    TaskKey[xml.Node]("wix-xml", "The WIX XML configuration for this package.")

  //- The file containing WIX xml that defines the build.
  //- Generates the Wix xml file from wixConfig and wixProductConfig setings, unless overriden.
  val wixFile = TaskKey[File]("wix-file", "The WIX XML file to package with.")

  //- the list of options to pass to the candle.exe command.
  val candleOptions = SettingKey[Seq[String]]("candle-options", "Options to pass to the candle.exe program.")

  //- the list of options to pass to the light.exe command.
  //- Most likely setting is: Seq("-ext", "WixUIExtension", "-cultures:en-us") for UI.
  val lightOptions = SettingKey[Seq[String]]("light-options", "Options to pass to the light.exe program.")

  //- the major version of the Wix tool-set (e.g. when using Wix 4.0.1, major version is 4). Default is 3.
  val wixMajorVersion =
    SettingKey[Int]("wix-major-version", "Major version of the Wix suit.")

}
