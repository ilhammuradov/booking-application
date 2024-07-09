package az.edu.turing;

import az.edu.turing.controller.BookingServlet;
import az.edu.turing.controller.FlightServlet;
import az.edu.turing.dao.BookingRepository;
import az.edu.turing.dao.FlightsRepository;
import az.edu.turing.dao.impl.BookingPostgresRepository;
import az.edu.turing.dao.impl.FlightsPostgreRepository;
import az.edu.turing.service.BookingService;
import az.edu.turing.service.FlightsService;
import az.edu.turing.service.impl.BookingServicePostgresImpl;
import az.edu.turing.service.impl.FlightsServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class ServerApp {

    public static void main(String[] args) throws Exception {
        FlightsRepository flightsRepository = new FlightsPostgreRepository();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        FlightsService flightsService = new FlightsServiceImpl(flightsRepository);
        FlightServlet flightServlet = new FlightServlet(objectMapper, flightsService);
        BookingRepository bookingRepository = new BookingPostgresRepository();
        BookingService bookingService = new BookingServicePostgresImpl(bookingRepository, flightsRepository);
        BookingServlet bookingServlet = new BookingServlet(bookingService, objectMapper);

        Server server = new Server(9000);

        ServletContextHandler context = new ServletContextHandler();
        context.addServlet(new ServletHolder(flightServlet), "/flights/*");
        context.addServlet(new ServletHolder(bookingServlet), "/bookings/*");
        server.setHandler(context);

        server.start();
        server.join();
    }
}
