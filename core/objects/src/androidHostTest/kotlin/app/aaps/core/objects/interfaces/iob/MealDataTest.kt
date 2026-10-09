package app.gmaaps.core.objects.interfaces.iob

import app.gmaaps.core.interfaces.aps.MealData
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class MealDataTest {

    @Test fun canCreateObject() {
        val md = MealData()
        assertThat(md.carbs).isWithin(0.01).of(0.0)
    }
}

