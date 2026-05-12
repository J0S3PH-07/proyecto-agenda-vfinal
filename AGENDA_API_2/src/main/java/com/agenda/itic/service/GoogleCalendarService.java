package com.agenda.itic.service;

import com.agenda.itic.model.Activitat;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;

import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Date;

@Service
public class GoogleCalendarService {

    private final String CREDENTIALS_FILE_PATH = "/google-credentials.json";

    private static final String CALENDAR_ID = "b9f6776a6bfa51bf8224ce431d249255b9e4c19ef483f1380c4f22d86cb4f8ec@group.calendar.google.com";

    public Calendar obtenerClienteCalendar() throws Exception {
        InputStream in = GoogleCalendarService.class.getResourceAsStream(CREDENTIALS_FILE_PATH);

        if (in == null) {
            throw new RuntimeException("No se ha encontrado el archivo de credenciales: " + CREDENTIALS_FILE_PATH);
        }

        GoogleCredential credential = GoogleCredential.fromStream(in)
                .createScoped(Collections.singleton(CalendarScopes.CALENDAR));

        return new Calendar.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance(),
                credential)
                .setApplicationName("Mi API de Spring Boot")
                .build();
    }

    public String addEvent(Activitat activitat) {
        Event event = new Event().setSummary(activitat.getTitol())
                .setDescription(activitat.getDescripcio());

        ZoneId zoneId = ZoneId.of("Europe/Madrid");
        LocalDateTime inicioLocal = LocalDateTime.of(activitat.getData(), activitat.getHoraInici());
        LocalDateTime endLocal = LocalDateTime.of(activitat.getData(), activitat.getHoraFi());
        ZonedDateTime inicioZoned = inicioLocal.atZone(zoneId);
        ZonedDateTime endZoned = endLocal.atZone(zoneId);

        DateTime inicio = new DateTime(Date.from(inicioZoned.toInstant()));
        DateTime end = new DateTime(Date.from(endZoned.toInstant()));

        event.setStart(new EventDateTime().setDateTime(inicio).setTimeZone(zoneId.getId()));
        event.setEnd(new EventDateTime().setDateTime(end).setTimeZone(zoneId.getId()));
        System.out.println("Evento creado: " + event);

        try {
            Calendar calendar = obtenerClienteCalendar();
            Event createdEvent = calendar.events().insert(CALENDAR_ID, event).execute();
            return createdEvent.getId();
        } catch (Exception e) {
            throw new RuntimeException("Error al añadir evento", e);
        }
    }

    public void deleteEvent(String eventId) {
        try {
            Calendar calendar = obtenerClienteCalendar();
            calendar.events().delete(CALENDAR_ID, eventId).execute();
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar evento", e);
        }
    }
}
