package conf.persistence;

import conf.Conference;
import conf.Attendee;
import conf.PaymentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.time.Year;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class JsonPersistenceManagerTest {
    private JsonPersistenceManager persistenceManager;
    private static final String CONFERENCE_TEST_FILE = "conference_data.json";
    private static final String ATTENDEES_TEST_FILE = "attendees_data.json";
    private static final String SESSIONS_TEST_FILE = "sessions_data.json";
    private static final String STAFF_TEST_FILE = "staff_data.json";
    private static final String VENDORS_TEST_FILE = "vendors_data.json";

    @BeforeEach
    void setUp() {
        persistenceManager = new JsonPersistenceManager();
    }

    @AfterEach
    void tearDown() {
        // Clean up test files after each test
        String[] testFiles = {
            CONFERENCE_TEST_FILE,
            ATTENDEES_TEST_FILE,
            SESSIONS_TEST_FILE,
            STAFF_TEST_FILE,
            VENDORS_TEST_FILE
        };

        for (String testFile : testFiles) {
            File file = new File(testFile);
            if (file.exists()) {
                file.delete();
            }
        }
    }

    @Test
    void testSaveAndLoadConference() {
        // Create a test conference
        Conference conference = new Conference("Test Conference", "TestConf", Year.of(2024), "Test Venue");

        // Save the conference
        persistenceManager.saveConference(conference);

        // Load the conference
        Conference loadedConference = persistenceManager.loadConference();

        // Verify the loaded conference matches the original
        assertNotNull(loadedConference);
        assertEquals(conference.getName(), loadedConference.getName());
        assertEquals(conference.getNickName(), loadedConference.getNickName());
        assertEquals(conference.getYear(), loadedConference.getYear());
        assertEquals(conference.getVenue(), loadedConference.getVenue());
    }

    @Test
    void testLoadNonExistentFile() {
        // Ensure the file doesn't exist
        File file = new File(CONFERENCE_TEST_FILE);
        if (file.exists()) {
            file.delete();
        }

        // Try to load from non-existent file
        Conference conference = persistenceManager.loadConference();
        assertNull(conference);
    }

    @Test
    void testSaveNullConference() {
        // Save null conference (should create empty or delete file)
        persistenceManager.saveConference(null);

        // Try to load - should return null
        Conference conference = persistenceManager.loadConference();
        assertNull(conference);
    }
}
