package app.gmaaps.plugins.automation.elements

import app.gmaaps.plugins.automation.triggers.TriggerTestBase
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class InputProfileNameTest : TriggerTestBase() {

    @Test fun setValue() {
        val inputProfileName = InputProfileName("Test")
        assertThat(inputProfileName.value).isEqualTo("Test")
        inputProfileName.value = "Test2"
        assertThat(inputProfileName.value).isEqualTo("Test2")
    }
}

