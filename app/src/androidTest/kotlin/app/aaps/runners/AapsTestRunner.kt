package app.gmaaps.runners

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import app.gmaaps.di.BaseTestApp

/**
 * Instrumentation runner that swaps in [app.gmaaps.di.BaseTestApp] instead of `MainApp`.
 */
class AapsTestRunner : AndroidJUnitRunner() {

    override fun newApplication(cl: ClassLoader?, name: String?, context: Context?): Application =
        super.newApplication(cl, BaseTestApp::class.java.name, context)
}

