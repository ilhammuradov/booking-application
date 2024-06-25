package az.edu.turing.util;

import az.edu.turing.controller.BookingController;
import az.edu.turing.controller.FlightsController;
import az.edu.turing.dao.BookingDao;
import az.edu.turing.dao.FlightsDao;
import az.edu.turing.dao.impl.BookingFileDao;
import az.edu.turing.dao.impl.FlightsFileDao;
import az.edu.turing.dao.impl.FlightsPostgreDao;
import az.edu.turing.entity.BookingEntity;
import az.edu.turing.exception.InvalidMenuActionException;
import az.edu.turing.model.BookingDto;
import az.edu.turing.model.FlightsDto;
import az.edu.turing.service.BookingService;
import az.edu.turing.service.FlightsService;
import az.edu.turing.service.impl.BookingServiceImpl;
import az.edu.turing.service.impl.FlightsServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ConsoleUtil {
    FlightsDao flightsDao = //new FlightsPostgreDao();
            new FlightsFileDao(new ObjectMapper().registerModule(new JavaTimeModule()));
    FlightsService flightsService = new FlightsServiceImpl(flightsDao);
    FlightsController flightsController = new FlightsController(flightsService);

    BookingDao bookingDao = new BookingFileDao(new ObjectMapper().registerModule(new JavaTimeModule()));
    BookingService bookingService = new BookingServiceImpl(bookingDao,flightsDao);
    BookingController bookingController = new BookingController(bookingService);


    public void displayMainMenu() {
        System.out.println("---Main Menu---\n" +
                "1. Online-board\n" +
                "2. Show flight info\n" +
                "3. Search and book a flight\n" +
                "4. Cancel booking\n" +
                "5. My flights\n" +
                "6. Exit\n" +
                "Enter your choice: ");

    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            displayMainMenu();
            choice = scanner.nextInt();
            scanner.nextLine();

            try {
                switch (choice) {
                    case 1:
                        displayOnlineBoard();
                        break;
                    case 2:
                        showFlightInfo();
                        break;
                    case 3:
                        searchAndBookFlight();
                        break;
                    case 4:
                        cancelBooking();
                        break;
                    case 5:
                        findMyFlights();
                        break;
                    case 6:
                        System.out.println("Exiting...");
                        break;
                    default:
                        throw new InvalidMenuActionException("Invalid choice! Please try again.");
                }
            } catch (InvalidMenuActionException e) {
                System.out.println(e.getMessage());
            }
        } while (choice != 6);

        scanner.close();
    }

    public void displayOnlineBoard() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter location: ");
        String location = scanner.nextLine();
        LocalDateTime dateTime = LocalDateTime.now();
        List<FlightsDto> flights = flightsController.getOnlineBoard(location, dateTime);
        if (flights != null) {
            for (FlightsDto flight : flights) {
                System.out.println(flight.getFlightId() + " - " + flight.getDestination() + " - " + flight.getDepartureDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
            }
        } else {
            System.out.println("Flight not found!");
        }
    }

    public void showFlightInfo() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter flight ID: ");
        try {
            long id = scanner.nextLong();
            List<FlightsDto> flights = flightsController.getFlightInfoByFlightId(id);
            if (flights != null) {
                for (FlightsDto f : flights) {
                    System.out.println(f.getDepartureDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) + " - " + f.getDestination() + " - " + f.getSeats());
                }
            } else {
                System.out.println("Flight not found!");
            }
        } catch (InputMismatchException e) {
            System.out.println("Enter valid flight Id");
        }

        System.out.println("===== Flight Info =====");

    }

    public void searchAndBookFlight() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter destination: ");
        String destination = scanner.nextLine();

        List<FlightsDto> foundFlights = flightsController.getAllFlightsByDestination(destination);

        if (foundFlights.isEmpty()) {
            System.out.println("No flights found for the specified destination.");
        } else {
            System.out.println("Found Flights:");
            for (FlightsDto flight : foundFlights) {
                System.out.println(flight.getFlightId() + ". " + flight);
            }

            System.out.print("Enter the ID of the flight you want to book (0 to return to the main menu): ");
            long flightId = scanner.nextLong();
            scanner.nextLine();

            if (flightId == 0) {
                return;
            }

            FlightsDto selectedFlight = flightsController.getOneFlightByFlightId(flightId).orElse(null);
            if (selectedFlight == null) {
                System.out.println("Invalid flight ID. Returning to the main menu.");
                return;
            }

            System.out.println("Enter names and surnames of all passengers:");
            System.out.print("Passengers: ");
            String passengerName = scanner.nextLine();
            List<String> passengerNames = new ArrayList<>(Arrays.asList(passengerName.split(",")));
            passengerNames.replaceAll(String::trim);

            BookingDto bookingDto = new BookingDto();
            bookingDto.setFlightId(selectedFlight.getFlightId());
            bookingDto.setPassengerNames(passengerNames);

            bookingController.searchAndBookFlight(bookingDto, passengerNames.size());
            System.out.println("Flight booked successfully!");
        }
    }

    public void cancelBooking() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter ticket ID: ");
        long id;
        id = scanner.nextLong();
        bookingController.cancelBooking(id);

    }

    public void findMyFlights() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter full name:");

        try {
            String fullName = scanner.nextLine();
            Collection<BookingEntity> myFlights = bookingController.myFlights(fullName);
            System.out.println(myFlights);
            if (myFlights.isEmpty()) {
                System.out.println("No flight found under your name.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input type. Please enter a valid full name");
        }
    }
}