import java.io.Serializable

/**
 * One module that owns strings, as `GenerateKeyStringsTask` was told to generate them.
 *
 * The four values here are exactly the four that task is configured with, which is the point: if
 * they disagree, the generated registry names an object that does not exist and the build fails
 * rather than a screen quietly showing string names.
 */
data class StringOwnerModule(
    val owner: String,
    val packageName: String,
    val objectName: String,
    val idsObjectName: String
) : Serializable {

    /** The generated `name -> English text` map. Lives in commonMain, so every platform has it. */
    val valuesObjectName: String get() = objectName + "Values"

    companion object {

        private const val serialVersionUID: Long = 1L
    }
}

/**
 * Every module that owns strings, in one place.
 *
 * ## Why this list exists
 *
 * Each platform has to tell the resolver which module owns which names, and that used to be a hand
 * written list per platform - `MainApp.registerStringOwners()`, its copy in `BaseTestApp`,
 * `IosStringOwners` and `DesktopStringOwners`. Four lists of the same thing, and nothing checked
 * them against each other or against the build.
 *
 * They drifted exactly as you would expect. The desktop one had five of sixteen modules, so the
 * plugin list rendered `objectives_shortname` instead of plugin names - visible only by running it.
 * The Android pair were split across two files for historical reasons, which is its own trap: adding
 * a module meant knowing which of the two to edit.
 *
 * So the list lives here, beside the task that generates the objects it names, and each shell gets a
 * generated registry rather than a hand written one. Adding a module with strings means adding one
 * line here.
 *
 * **Keep it in step with the `GenerateKeyStringsTask` registrations** in each module's build file.
 * A module generated but missing here shows its names on screen; a module here but not generated
 * fails the build, which is the better direction of the two.
 */
object StringOwnerModules {

    val ALL: List<StringOwnerModule> = listOf(
        StringOwnerModule("interfaces", "app.gmaaps.core.interfaces", "InterfacesStrings", "InterfacesStringIds"),
        StringOwnerModule("keys", "app.gmaaps.core.keys", "KeysStrings", "KeysStringIds"),
        StringOwnerModule("coreUi", "app.gmaaps.core.ui", "CoreUiStrings", "CoreUiStringIds"),
        StringOwnerModule("implementation", "app.gmaaps.implementation", "ImplementationStrings", "ImplementationStringIds"),
        StringOwnerModule("ui", "app.gmaaps.ui", "UiStrings", "UiStringIds"),
        StringOwnerModule("aps", "app.gmaaps.plugins.aps", "ApsStrings", "ApsStringIds"),
        StringOwnerModule("automation", "app.gmaaps.plugins.automation", "AutomationStrings", "AutomationStringIds"),
        StringOwnerModule("calibration", "app.gmaaps.plugins.calibration", "CalibrationStrings", "CalibrationStringIds"),
        StringOwnerModule("configuration", "app.gmaaps.plugins.configuration", "ConfigurationStrings", "ConfigurationStringIds"),
        StringOwnerModule("constraints", "app.gmaaps.plugins.constraints", "ConstraintsStrings", "ConstraintsStringIds"),
        StringOwnerModule("main", "app.gmaaps.plugins.main", "MainStrings", "MainStringIds"),
        StringOwnerModule("sensitivity", "app.gmaaps.plugins.sensitivity", "SensitivityStrings", "SensitivityStringIds"),
        StringOwnerModule("smoothing", "app.gmaaps.plugins.smoothing", "SmoothingStrings", "SmoothingStringIds"),
        StringOwnerModule("source", "app.gmaaps.plugins.source", "SourceStrings", "SourceStringIds"),
        StringOwnerModule("sync", "app.gmaaps.plugins.sync", "SyncStrings", "SyncStringIds"),
        StringOwnerModule("virtual", "app.gmaaps.pump.virtual", "VirtualStrings", "VirtualStringIds")
    )
}

