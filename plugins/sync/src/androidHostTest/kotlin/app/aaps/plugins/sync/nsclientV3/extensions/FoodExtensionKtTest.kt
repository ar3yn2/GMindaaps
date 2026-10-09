package app.gmaaps.plugins.sync.nsclientV3.extensions

import app.gmaaps.core.data.model.FD
import app.gmaaps.core.data.model.IDs
import app.gmaaps.core.nssdk.localmodel.food.NSFood
import app.gmaaps.core.nssdk.mapper.convertToRemoteAndBack
import app.gmaaps.plugins.sync.extensions.contentEqualsTo
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

internal class FoodExtensionKtTest {

    @Test
    fun toFood() {
        val food = FD(
            isValid = true,
            name = "name",
            category = "category",
            subCategory = "subcategory",
            portion = 2.0,
            carbs = 20,
            fat = 21,
            protein = 22,
            energy = 23,
            unit = "g",
            gi = 25,
            ids = IDs(
                nightscoutId = "nightscoutId"
            )
        )

        val food2 = (food.toNSFood().convertToRemoteAndBack() as NSFood).toFood()
        assertThat(food.contentEqualsTo(food2)).isTrue()
        assertThat(food.ids.contentEqualsTo(food2.ids)).isTrue()
    }
}

