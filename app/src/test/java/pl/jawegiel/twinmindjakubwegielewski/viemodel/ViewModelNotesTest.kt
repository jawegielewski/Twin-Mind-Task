package pl.jawegiel.twinmindjakubwegielewski.viemodel

import org.junit.Test

class ViewModelNotesTest {

    @Test
    fun `isEditModeOn initial state check`() {        // Verify that `isEditModeOn` LiveData is initialized to `false` when the ViewModel is created.
    }

    @Test
    fun `isEditModeEnabled sets LiveData to true`() {        // Call `isEditModeEnabled(true)` and verify that the `isEditModeOn` LiveData value becomes `true`.
    }

    @Test
    fun `isEditModeEnabled sets LiveData to false`() {        // Call `isEditModeEnabled(false)` and verify that the `isEditModeOn` LiveData value becomes `false`.
    }

    @Test
    fun `isEditModeEnabled multiple true calls`() {        // Call `isEditModeEnabled(true)` multiple times and verify that the `isEditModeOn` LiveData value remains `true`.
    }

    @Test
    fun `isEditModeEnabled multiple false calls`() {        // Call `isEditModeEnabled(false)` multiple times and verify that the `isEditModeOn` LiveData value remains `false`.
    }

    @Test
    fun `isEditModeEnabled toggling state`() {        // Call `isEditModeEnabled(true)` then `isEditModeEnabled(false)` and verify the `isEditModeOn` LiveData reflects these changes correctly.
    }

    @Test
    fun `isEditModeOn observer notification on true`() {        // Add an observer to `isEditModeOn`. Call `isEditModeEnabled(true)` and verify the observer is notified with the value `true`.
    }

    @Test
    fun `isEditModeOn observer notification on false`() {        // Add an observer to `isEditModeOn`. Call `isEditModeEnabled(false)` (after it was true) and verify the observer is notified with the value `false`.
    }

    @Test
    fun `isEditModeOn observer not notified if value unchanged  true `() {        // Set to true. Add an observer. Call `isEditModeEnabled(true)` again. Verify the observer is not notified again if the LiveData's distinctUntilChanged behavior is active (though default LiveData will notify).
    }

    @Test
    fun `isEditModeOn observer not notified if value unchanged  false `() {        // Set to false. Add an observer. Call `isEditModeEnabled(false)` again. Verify the observer is not notified again if the LiveData's distinctUntilChanged behavior is active (though default LiveData will notify).
    }

    @Test
    fun `ViewModel lifecycle survives configuration change`() {        // Simulate a configuration change (e.g., screen rotation) and verify that the state of `isEditModeOn` is preserved.
    }

    @Test
    fun `LiveData observed from different threads`() {        // While `_isEditModeOn.value` should be set on the main thread, test observing `isEditModeOn` from a background thread (if applicable to testing framework) to ensure no threading issues, though LiveData generally handles this.
    }

    @Test
    fun `Ensure isEditModeOn is read only externally`() {        // Verify through compilation or reflection (if necessary for a deeper test) that external classes cannot directly set the value of `isEditModeOn` (as it's a `LiveData<Boolean>`), only `_isEditModeOn`.
    }
}