package app.gmaaps.database.transactions

import app.gmaaps.database.DelegatedAppDatabase
import app.gmaaps.database.daos.FoodDao
import app.gmaaps.database.entities.Food
import app.gmaaps.database.entities.embedments.InterfaceIDs
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class UpdateNsIdFoodTransactionTest {

    private lateinit var database: DelegatedAppDatabase
    private lateinit var foodDao: FoodDao

    @BeforeEach
    fun setup() {
        foodDao = mock()
        database = mock()
        whenever(database.foodDao).thenReturn(foodDao)
    }

    @Test
    fun `updates NS ID when different`() = runTest {
        val newNsId = "new-ns-123"
        val current = createFood(id = 1, nsId = "old-ns")
        val update = createFood(id = 1, nsId = newNsId)

        whenever(foodDao.findById(1)).thenReturn(current)

        val transaction = UpdateNsIdFoodTransaction(listOf(update))
        transaction.database = database
        val result = transaction.run()

        assertThat(current.interfaceIDs.nightscoutId).isEqualTo(newNsId)
        assertThat(result.updatedNsId).hasSize(1)

        verify(foodDao).updateExistingEntry(current)
    }

    @Test
    fun `does not update when NS ID is same`() = runTest {
        val sameNsId = "same-ns"
        val current = createFood(id = 1, nsId = sameNsId)
        val update = createFood(id = 1, nsId = sameNsId)

        whenever(foodDao.findById(1)).thenReturn(current)

        val transaction = UpdateNsIdFoodTransaction(listOf(update))
        transaction.database = database
        val result = transaction.run()

        assertThat(result.updatedNsId).isEmpty()

        verify(foodDao, never()).updateExistingEntry(any())
    }

    @Test
    fun `skips when food not found`() = runTest {
        val update = createFood(id = 999, nsId = "new-ns")

        whenever(foodDao.findById(999)).thenReturn(null)

        val transaction = UpdateNsIdFoodTransaction(listOf(update))
        transaction.database = database
        val result = transaction.run()

        assertThat(result.updatedNsId).isEmpty()

        verify(foodDao, never()).updateExistingEntry(any())
    }

    private fun createFood(
        id: Long,
        nsId: String?
    ): Food = Food(
        name = "Test Food",
        category = "Test",
        subCategory = "Test",
        portion = 100.0,
        carbs = 50,
        interfaceIDs_backing = InterfaceIDs(nightscoutId = nsId)
    ).also { it.id = id }
}

