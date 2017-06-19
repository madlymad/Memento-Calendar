package com.alexstyl.specialdates.contact;

import android.content.ContentResolver;
import android.content.Context;

import com.alexstyl.specialdates.events.database.DatabaseContract.AnnualEventsContract;
import com.alexstyl.specialdates.events.database.EventSQLiteOpenHelper;
import com.alexstyl.specialdates.events.database.SourceType;
import com.alexstyl.specialdates.events.peopleevents.DeviceContactsQuery;
import com.squareup.haha.guava.collect.ImmutableList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContactsProvider {

    private static ContactsProvider INSTANCE;

    private static final int CACHE_SIZE = 2 * 1024;
    private final Map<Integer, ContactsProviderSource> sources;

    public static ContactsProvider get(Context context) {
        if (INSTANCE == null) {
            Map<Integer, ContactsProviderSource> sources = new HashMap<>();
            sources.put(AnnualEventsContract.SOURCE_DEVICE, buildAndroidSource(context));
            sources.put(AnnualEventsContract.SOURCE_FACEBOOK, new FacebookContactsSource(new EventSQLiteOpenHelper(context)));
            INSTANCE = new ContactsProvider(sources);

        }
        return INSTANCE;
    }

    private static AndroidContactsProviderSource buildAndroidSource(Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        DeviceContactFactory factory = new DeviceContactFactory(contentResolver);
        ContactCache<Contact> contactCache = new ContactCache<>(CACHE_SIZE);
        DeviceContactsQuery deviceContactsQuery = new DeviceContactsQuery(contentResolver);
        return new AndroidContactsProviderSource(contactCache, factory, deviceContactsQuery);
    }

    public ContactsProvider(Map<Integer, ContactsProviderSource> sources) {
        this.sources = sources;
    }

    public Contact getContact(long contactID, @SourceType int source) throws ContactNotFoundException {
        if (sources.containsKey(source)) {
            ContactsProviderSource contactsProviderSource = sources.get(source);
            return contactsProviderSource.getOrCreateContact(contactID);
        }
        throw new IllegalArgumentException("Unknown source type: " + source);

    }

    public List<Contact> getAllContacts() {
        List<Contact> contacts = new ArrayList<>();
        for (ContactsProviderSource providerSource : sources.values()) {
            contacts.addAll(providerSource.getAllContacts());
        }
        return ImmutableList.copyOf(contacts);
    }

}
