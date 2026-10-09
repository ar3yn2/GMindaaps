package app.gmaaps.plugins.automation.elements

import app.gmaaps.plugins.automation.triggers.TriggerTestBase
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class InputInsulinTest : TriggerTestBase() {

    @Test fun setValueTest() {
        val i = InputInsulin()
        i.value = 5.0
        assertThat(i.value).isWithin(0.01).of(5.0)
    }
}

