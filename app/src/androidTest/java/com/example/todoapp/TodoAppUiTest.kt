package com.example.todoapp

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TodoAppUiTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun before() {
        composeTestRule.activity.db.clearAllTables()
    }

    @Test
    fun addButtonOpensDialog() {
        composeTestRule.onNodeWithContentDescription("Add TODO").performClick()
        composeTestRule.onNodeWithText("Title").assertIsDisplayed()
    }

    @Test
    fun canAddNewTodo() {
        composeTestRule.onNodeWithContentDescription("Add TODO").performClick()
        composeTestRule.onNodeWithText("Title").performTextInput("Buy groceries")
        composeTestRule.onNodeWithText("Description").performTextInput("Milk and eggs")
        composeTestRule.onNodeWithText("Save").performClick()
        composeTestRule.onNodeWithText("Buy groceries").assertIsDisplayed()
    }

    @Test
    fun cancelButtonClosesDialog() {
        composeTestRule.onNodeWithContentDescription("Add TODO").performClick()
        composeTestRule.onNodeWithText("Cancel").performClick()
        composeTestRule.onNodeWithText("Title").assertDoesNotExist()
    }

    @Test
    fun canEditTodo() {
        composeTestRule.onNodeWithContentDescription("Add TODO").performClick()
        composeTestRule.onNodeWithText("Title").performTextInput("Buy groceries")
        composeTestRule.onNodeWithText("Description").performTextInput("Milk and eggs")
        composeTestRule.onNodeWithText("Save").performClick()
        composeTestRule.onNodeWithText("Buy groceries").assertIsDisplayed()

        composeTestRule.onNodeWithContentDescription("Edit TODO").performClick()
        composeTestRule.onNodeWithText("Title").performTextClearance()
        composeTestRule.onNodeWithText("Title").performTextInput("Buy new PC")
        composeTestRule.onNodeWithText("Description").performTextClearance()
        composeTestRule.onNodeWithText("Description").performTextInput("PC")
        composeTestRule.onNodeWithText("Save Changes").performClick()
        composeTestRule.onNodeWithText("Buy new PC").assertIsDisplayed()
        composeTestRule.onNodeWithText("PC").assertIsDisplayed()
        composeTestRule.onNodeWithText("Buy groceries").assertIsNotDisplayed()
    }
}
