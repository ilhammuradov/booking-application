package az.edu.turing.util;

import az.edu.turing.controller.BookingController;
import az.edu.turing.controller.FlightsController;
import az.edu.turing.dao.BookingRepository;
import az.edu.turing.dao.FlightsRepository;
import az.edu.turing.dao.entity.BookingEntity;
import az.edu.turing.dao.entity.FlightsEntity;
import az.edu.turing.dao.impl.BookingPostgreRepository;
import az.edu.turing.dao.impl.FlightsPostgreRepository;
import az.edu.turing.exception.InvalidMenuActionException;
import az.edu.turing.model.BookingDto;
import az.edu.turing.model.FlightsDto;
import az.edu.turing.service.BookingService;
import az.edu.turing.service.FlightsService;
import az.edu.turing.service.impl.BookingServicePostgresImpl;
import az.edu.turing.service.impl.FlightsServiceImpl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ConsoleUtil {
    FlightsRepository flightsRepository = new FlightsPostgreRepository();
    //new FlightsFileDao(new ObjectMapper().registerModule(new JavaTimeModule()));
    FlightsService flightsService = new FlightsServiceImpl(flightsRepository);
    FlightsController flightsController = new FlightsController(flightsService);

    BookingRepository bookingRepository = new BookingPostgreRepository();
    // new BookingFileDao(new ObjectMapper().registerModule(new JavaTimeModule()));
    BookingService bookingService = new BookingServicePostgresImpl(bookingRepository, flightsRepository);
    // new BookingServiceImpl(bookingDao,flightsDao);
    BookingController bookingController = new BookingController(bookingService);


    public void displayMainMenu() {
        System.out.println("""
                ---Main Menu---
                1. Online-board
                2. Show flight info
                3. Search and book a flight
                4. Cancel booking
                5. My flights
                6. Exit
                Enter your choice:\s""");


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
            } catch (InvalidMenuActionException | NoSuchElementException e) {
                System.out.println("test");
            }
        } while (choice != 6);

        scanner.close();
    }

    public void displayOnlineBoard() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter location: ");
        String location = scanner.nextLine().trim();

        if (location.isEmpty()) {
            System.out.println("Location cannot be empty. Please enter a valid location.");
            return;
        }

        LocalDateTime dateTime = LocalDateTime.now();
        Collection<FlightsDto> flights = flightsController.getOnlineBoard(location, dateTime);

        if (flights != null && !flights.isEmpty()) {
            for (FlightsDto flight : flights) {
                System.out.println(flight.getFlightId() + " - " + flight.getDestination() + " - " + flight.getDepartureDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
            }
        } else {
            System.out.println("No flights found for the specified location and time.");
        }
    }


    public void showFlightInfo() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter flight ID: ");
        try {
            long id = scanner.nextLong();
            Optional<FlightsEntity> flight = flightsController.getOneFlightByFlightId(id);

            System.out.println("===== Flight Info =====");
            if (flight.isPresent()) {
                FlightsEntity f = flight.get();
                System.out.println(f.getDepartureDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) + " - " + f.getDestination() + " - " + f.getSeats());
            } else {
                System.out.println("Flight not found!");
            }
        } catch (InputMismatchException e) {
            System.out.println("Enter a valid flight ID.");
        }
    }


    public void searchAndBookFlight() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter destination: ");
        String destination = scanner.nextLine();

        Collection<FlightsEntity> foundFlights = flightsController.getAllFlightsByDestination(destination);

        if (foundFlights.isEmpty()) {
            System.out.println("No flights found for the specified destination.");
        } else {
            System.out.println("Found Flights:");
            for (FlightsEntity flight : foundFlights) {
                System.out.println(flight.getFlightId() + ". " + flight);
            }

            System.out.print("Enter the ID of the flight you want to book (0 to return to the main menu): ");
            long flightId = scanner.nextLong();
            scanner.nextLine();

            if (flightId == 0) {
                return;
            }

            FlightsEntity selectedFlight = flightsController.getOneFlightByFlightId(flightId).orElse(null);
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