package conf.persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import conf.*;
import java.io.*;
import java.nio.file.*;
import java.time.Year;
import java.util.*;

public class JsonPersistenceManager {
    private static final String DATA_FILE = "conference_data.json";
    private static final String SPEAKERS_FILE = "speakers_data.json";
    private static final String ATTENDEES_FILE = "attendees_data.json";
    private static final String SESSIONS_FILE = "sessions_data.json";
    private static final String STAFF_FILE = "staff_data.json";
    private static final String VENDORS_FILE = "vendors_data.json";
    private final Gson gson;
    private Conference conference;

    public JsonPersistenceManager() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        // Register type adapter for Year class
        gsonBuilder.registerTypeAdapter(Year.class, new TypeAdapter<Year>() {
            @Override
            public void write(JsonWriter out, Year value) throws IOException {
                out.value(value.getValue());
            }

            @Override
            public Year read(JsonReader in) throws IOException {
                return Year.of(in.nextInt());
            }
        });
        this.gson = gsonBuilder.setPrettyPrinting().create();
    }

    public void saveConference(Conference conference) {
        try {
            String json = gson.toJson(conference);
            Files.write(Paths.get(DATA_FILE), json.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to save conference data", e);
        }
    }

    public Conference loadConference() {
        try {
            if (!Files.exists(Paths.get(DATA_FILE))) {
                return null;
            }
            String json = new String(Files.readAllBytes(Paths.get(DATA_FILE)));
            return gson.fromJson(json, Conference.class);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load conference data", e);
        }
    }

    public void saveSpeakers(List<Speaker> speakers) {
        try {
            String json = gson.toJson(speakers);
            Files.write(Paths.get(SPEAKERS_FILE), json.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to save speakers data", e);
        }
    }

    public List<Speaker> loadSpeakers() {
        try {
            if (!Files.exists(Paths.get(SPEAKERS_FILE))) {
                return new ArrayList<>();
            }
            String json = new String(Files.readAllBytes(Paths.get(SPEAKERS_FILE)));
            Speaker[] speakersArray = gson.fromJson(json, Speaker[].class);
            return new ArrayList<>(Arrays.asList(speakersArray));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load speakers data", e);
        }
    }

    public void saveAttendees(List<Attendee> attendees) {
        try {
            String json = gson.toJson(attendees);
            Files.write(Paths.get(ATTENDEES_FILE), json.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to save attendees data", e);
        }
    }

    public List<Attendee> loadAttendees() {
        try {
            if (!Files.exists(Paths.get(ATTENDEES_FILE))) {
                return new ArrayList<>();
            }
            String json = new String(Files.readAllBytes(Paths.get(ATTENDEES_FILE)));
            Attendee[] attendeesArray = gson.fromJson(json, Attendee[].class);
            return new ArrayList<>(Arrays.asList(attendeesArray));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load attendees data", e);
        }
    }

    public void saveSessions(List<Session> sessions) {
        try {
            String json = gson.toJson(sessions);
            Files.write(Paths.get(SESSIONS_FILE), json.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to save sessions data", e);
        }
    }

    public List<Session> loadSessions() {
        try {
            if (!Files.exists(Paths.get(SESSIONS_FILE))) {
                return new ArrayList<>();
            }
            String json = new String(Files.readAllBytes(Paths.get(SESSIONS_FILE)));
            Session[] sessionsArray = gson.fromJson(json, Session[].class);
            return new ArrayList<>(Arrays.asList(sessionsArray));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load sessions data", e);
        }
    }

    public void saveStaff(List<Staff> staff) {
        try {
            String json = gson.toJson(staff);
            Files.write(Paths.get(STAFF_FILE), json.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to save staff data", e);
        }
    }

    public List<Staff> loadStaff() {
        try {
            if (!Files.exists(Paths.get(STAFF_FILE))) {
                return new ArrayList<>();
            }
            String json = new String(Files.readAllBytes(Paths.get(STAFF_FILE)));
            Staff[] staffArray = gson.fromJson(json, Staff[].class);
            return new ArrayList<>(Arrays.asList(staffArray));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load staff data", e);
        }
    }

    public void saveVendors(List<VendorSponsor> vendors) {
        try {
            String json = gson.toJson(vendors);
            Files.write(Paths.get(VENDORS_FILE), json.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to save vendors data", e);
        }
    }

    public List<VendorSponsor> loadVendors() {
        try {
            if (!Files.exists(Paths.get(VENDORS_FILE))) {
                return new ArrayList<>();
            }
            String json = new String(Files.readAllBytes(Paths.get(VENDORS_FILE)));
            VendorSponsor[] vendorsArray = gson.fromJson(json, VendorSponsor[].class);
            return new ArrayList<>(Arrays.asList(vendorsArray));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load vendors data", e);
        }
    }
}
