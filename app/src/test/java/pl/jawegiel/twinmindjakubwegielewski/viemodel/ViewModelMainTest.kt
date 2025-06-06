package pl.jawegiel.twinmindjakubwegielewski.viemodel

import org.junit.Test

class ViewModelMainTest {

    @Test
    fun `getUserPhoto LiveData returns correct Bitmap`() {        // Verify that the `userPhoto` LiveData emits the Bitmap retrieved from `repositorySharedPrefs`.
    }

    @Test
    fun `getUserPhoto LiveData emits null initially`() {        // Ensure that `userPhoto` LiveData is initially null or has no value before `getUserPhoto()` is called.
    }

    @Test
    fun `getUserPhoto updates LiveData when photo exists`() {        // Mock `repositorySharedPrefs.getBitmap` to return a valid Bitmap and verify that `_userPhoto.value` is updated accordingly after calling `getUserPhoto()`.
    }

    @Test
    fun `getUserPhoto does not update LiveData when photo is null`() {        // Mock `repositorySharedPrefs.getBitmap` to return null and verify that `_userPhoto.value` remains unchanged (or null if it was initially null) after calling `getUserPhoto()`.
    }

    @Test
    fun `getUserPhoto handles RepositorySharedPrefs exception`() {        // Mock `repositorySharedPrefs.getBitmap` to throw an exception and verify that the ViewModel handles it gracefully (e.g., LiveData remains unchanged, no crash).
    }

    @Test
    fun `getUserPhoto LiveData observer receives updates`() {        // Add an observer to `userPhoto` LiveData, call `getUserPhoto()`, and verify that the observer is triggered with the correct Bitmap.
    }

    @Test
    fun `getUserPhoto LiveData no update if repository returns same photo`() {        // Call `getUserPhoto()` twice with the repository returning the same Bitmap instance. Verify the LiveData observer is only triggered once (or as expected based on LiveData behavior for same value updates).
    }

    @Test
    fun `getUserPhoto LiveData update with different photo`() {        // Call `getUserPhoto()`, then change the Bitmap returned by the repository and call `getUserPhoto()` again. Verify the LiveData observer receives the new Bitmap.
    }

    @Test
    fun `getUserPhoto with empty string key in repository`() {        // Although the key is hardcoded, conceptually test the behavior if an empty string was somehow passed as a key to `repositorySharedPrefs.getBitmap`, assuming the repository handles it (this tests the interaction point).
    }

    @Test
    fun `getUserPhoto when ViewModel is cleared`() {        // Verify the state of `_userPhoto` and its observers after the ViewModel's `onCleared()` method is called (though not directly testable for `getUserPhoto` itself, it's contextually important for LiveData).
    }

    @Test
    fun `getUserPhoto called multiple times sequentially with existing photo`() {        // Call `getUserPhoto()` multiple times in a row when a photo exists in shared preferences. Verify `_userPhoto` is correctly updated and observers are notified appropriately (e.g., only on distinct changes).
    }

    @Test
    fun `getUserPhoto called multiple times sequentially with no photo`() {        // Call `getUserPhoto()` multiple times in a row when no photo exists in shared preferences. Verify `_userPhoto` remains null/unchanged and observers are not unnecessarily notified.
    }
}