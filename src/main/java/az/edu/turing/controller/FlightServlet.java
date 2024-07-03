package az.edu.turing.controller;

import az.edu.turing.dao.entity.FlightsEntity;
import az.edu.turing.exception.FlightNotFoundException;
import az.edu.turing.model.FlightsDto;
import az.edu.turing.service.FlightsService;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class FlightServlet extends HttpServlet {

    private final ObjectMapper mapper;
    private final FlightsService flightsService;

    public FlightServlet(ObjectMapper mapper, FlightsService flightsService) {

        this.mapper = mapper;
        this.flightsService = flightsService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        try {
            if (pathInfo == null || pathInfo.equals("/flights")) {
                List<FlightsDto> flights = (List<FlightsDto>) flightsService.getAllFlights();
                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");
                mapper.writeValue(resp.getWriter(), flights);
            } else if (pathInfo.startsWith("/destination/")) {
                String destination = pathInfo.substring("/destination/".length());
                List<FlightsEntity> flights = (List<FlightsEntity>) flightsService.getAllFlightsByDestination(destination);
                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");
                mapper.writeValue(resp.getWriter(), flights);
            } else if (pathInfo.startsWith("/location/")) {
                String location = pathInfo.substring("/location/".length());
                List<FlightsEntity> flights = (List<FlightsEntity>) flightsService.getAllFlightsByLocation(location);
                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");
                mapper.writeValue(resp.getWriter(), flights);
            } else if (pathInfo.startsWith("/next24hours")) {
                String location = pathInfo.substring("/next24hours/".length());
                LocalDateTime dateTime = LocalDateTime.now();
                List<FlightsDto> flights = (List<FlightsDto>) flightsService.flightsInNext24Hours(location, dateTime);
                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");
                mapper.writeValue(resp.getWriter(), flights);
            } else {
                String idString = pathInfo.substring(1);
                long id = Long.parseLong(idString);
                FlightsEntity flight = flightsService.getOneFlightByFlightId(id).orElseThrow(() -> new FlightNotFoundException("Flight not found"));
                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");
                mapper.writeValue(resp.getWriter(), flight);
            }
        } catch (FlightNotFoundException f) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, f.getMessage());
        } catch (NumberFormatException n) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid id format");
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            FlightsDto flightDto = mapper.readValue(req.getReader(), FlightsDto.class);
            flightsService.createFlights(flightDto);
            resp.setStatus(HttpServletResponse.SC_CREATED);
        } catch (IOException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Error reading request data: " + e.getMessage());
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unexpected error occurred: " + e.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing id");
            return;
        }

        String idStr = pathInfo.substring(1);
        try {
            long id = Long.parseLong(idStr);
            FlightsDto flightsDto = mapper.readValue(req.getReader(), FlightsDto.class);

            FlightsEntity flightEntity = new FlightsEntity(flightsDto.getDepartureDateTime(), flightsDto.getDestination(), flightsDto.getLocation(), flightsDto.getSeats(), id);

            flightsService.update(flightEntity);
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid id format");
        } catch (FlightNotFoundException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unexpected error occurred: " + e.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing Id");
            return;
        }
        String idString = pathInfo.substring(1);
        try {
            long id = Long.parseLong(idString);
            flightsService.delete(id);
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid id format");
        } catch (FlightNotFoundException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unexpected error occurred: " + e.getMessage());
        }
    }
}
