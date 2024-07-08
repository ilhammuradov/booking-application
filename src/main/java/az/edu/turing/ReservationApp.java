package az.edu.turing;

import az.edu.turing.util.ConsoleUtil;

import java.util.NoSuchElementException;

public class ReservationApp {
    public static void main(String[] args) {
        try {
            ConsoleUtil consoleUtil = new ConsoleUtil();
            consoleUtil.start();
        }catch (NoSuchElementException e){
            System.out.println("There is no reservation");
        }

    }
}
