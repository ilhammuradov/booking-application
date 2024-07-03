package az.edu.turing.controller;

import az.edu.turing.exception.BookingNotFoundException;
import az.edu.turing.exception.FlightNotFoundException;
import az.edu.turing.exception.InvalidMenuActionException;
import az.edu.turing.model.BookingDto;
import az.edu.turing.service.BookingService;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class BookingServlet extends HttpServlet {

    private final BookingService bookingService;
    private final ObjectMapper objectMapper;

    public BookingServlet(BookingService bookingService, ObjectMapper objectMapper) {
        this.bookingService = bookingService;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        } else {
            String username = pathInfo.substring(1);
            try {
                var bookingsByPassenger = bookingService.getBookingsByPassenger(username);
                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");
                objectMapper.writeValue(resp.getWriter(), bookingsByPassenger);
            } catch (BookingNotFoundException b) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, b.getMessage());
            } catch (Exception e) {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String number = req.getParameter("numberofseats");
        try {
            int seats = Integer.parseInt(number);
            BookingDto bookingDto = objectMapper.readValue(req.getReader(), BookingDto.class);
            bookingService.bookFlight(bookingDto, seats);
            resp.setStatus(HttpServletResponse.SC_CREATED);
        } catch (FlightNotFoundException f) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, f.getMessage());
        } catch (InvalidMenuActionException i) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, i.getMessage());
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Missing ID");
            return;
        }
        String idStr = pathInfo.substring(1);
        try {
            long id = Long.parseLong(idStr);
            bookingService.cancelBooking(id);
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (BookingNotFoundException b) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, b.getMessage());
        } catch (NumberFormatException n) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, n.getMessage());
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
