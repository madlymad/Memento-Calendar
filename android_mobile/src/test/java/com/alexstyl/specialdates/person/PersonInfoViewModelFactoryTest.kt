package com.alexstyl.specialdates.person

import com.alexstyl.resources.JavaStrings
import com.alexstyl.specialdates.Optional
import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.contact.DisplayName
import com.alexstyl.specialdates.date.ContactEvent
import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.date.Months.JANUARY
import com.alexstyl.specialdates.date.Months.JULY
import com.alexstyl.specialdates.events.peopleevents.StandardEventType
import org.fest.assertions.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.runners.MockitoJUnitRunner
import java.net.URI

@RunWith(MockitoJUnitRunner::class)
class PersonInfoViewModelFactoryTest {

    private val strings = JavaStrings()
    private val toViewModel = PersonDetailsViewModelFactory(strings, AgeCalculator(Date.on(30, JULY, 2017)));

    @Test
    fun whenPassingAContact_thenAlwaysReturnItsName() {
        var resultViewModel = toViewModel(aContactCalled("Anna Roberts"), null, true)
        assertThat(resultViewModel.displayName).isEqualTo("Anna Roberts")
    }

    @Test
    fun whenPassingNoContactEvent_thenAgeAndStarSignIsEmptyString() {
        var resultViewModel = toViewModel(aContactCalled("Anna Roberts"), null, true)
        assertThat(resultViewModel.ageAndStarSignlabel).isEqualTo("")
    }

    @Test
    fun whenPassingABirthdayWithoutYear_thenAgeAndStarSignContainsOnlyStarSign() {
        val dateOfBirth = Date.on(1, JANUARY)
        val contactEvent = ContactEvent(Optional.absent(), StandardEventType.BIRTHDAY, dateOfBirth, aContactCalled("Anna Roberts"))

        var resultViewModel = toViewModel(aContactCalled("Anna Roberts"), contactEvent, true)
        assertThat(resultViewModel.ageAndStarSignlabel).isEqualTo("Capricorn ♑")
    }

    @Test
    fun whenPassingABirthdayWithYear_thenAgeAndStarSignContainsBothAgeAndStarSign() {
        val dateOfBirth = Date.on(1, JANUARY, 1990)
        val contactEvent = ContactEvent(Optional.absent(), StandardEventType.BIRTHDAY, dateOfBirth, aContactCalled("Anna Roberts"))

        var resultViewModel = toViewModel(aContactCalled("Anna Roberts"), contactEvent, true)
        assertThat(resultViewModel.ageAndStarSignlabel).isEqualTo("27, Capricorn ♑")
    }


    fun aContactCalled(peter: String): Contact {
        return Contact(-1, DisplayName.from(peter), URI.create("https://www.alexstyl.com/image.jpg"), 1)
    }

}
