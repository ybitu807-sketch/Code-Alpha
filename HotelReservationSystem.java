import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
class Room {
    private int roomNumber;
    private String category; // Standard, Deluxe, Suite
    private double pricePerNight;
    private boolean isAvailable;

    public Room(int roomNumber, String category, double pricePerNight, boolean isAvailable) {
        this.roomNumber = roomNumber;
        this.category = category;
        this.pricePerNight = pricePerNight;
        this.isAvailable = isAvailable;
    }

    public int getRoomNumber() { return roomNumber; }
    public String getCategory() { return category; }
    public double getPricePerNight() { return pricePerNight; }
    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }

    @Override
    public String toString() {
        String status = isAvailable ? "Available" : "Booked";
        return String.format("Room %-5d | %-10s | Rs. %-8.2f/night | %s", 
                roomNumber, category, pricePerNight, status);
    }
}

// Represents a customer's reservation
class Reservation {
    private int bookingId;
    private String guestName;
    private int roomNumber;
    private String category;
    private int nights;
    private double totalAmount;
    private String paymentMethod;

    public Reservation(int bookingId, String guestName, int roomNumber, String category, int nights, double totalAmount, String paymentMethod) {
        this.bookingId = bookingId;
        this.guestName = guestName;
        this.roomNumber = roomNumber;
        this.category = category;
        this.nights = nights;
        this.totalAmount = totalAmount;
        this.paymentMethod = paymentMethod;
    }

    public int getBookingId() { return bookingId; }
    public int getRoomNumber() { return roomNumber; }
    public String getGuestName() { return guestName; }

    // Format for saving into the text file
    public String toFileString() {
        return bookingId + "," + guestName + "," + roomNumber + "," + category + "," + nights + "," + totalAmount + "," + paymentMethod;
    }

    @Override
    public String toString() {
        return "Booking ID : " + bookingId +
               "\nGuest Name : " + guestName +
               "\nRoom No    : " + roomNumber + " (" + category + ")" +
               "\nDuration   : " + nights + " night(s)" +
               "\nTotal Paid : Rs. " + String.format("%.2f", totalAmount) + " via " + paymentMethod;
    }
}

public class HotelReservationSystem {
    private static ArrayList<Room> rooms = new ArrayList<>();
    private static ArrayList<Reservation> reservations = new ArrayList<>();
    private static final String FILE_NAME = "bookings.txt";
    private static int nextBookingId = 1001;
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        initializeRooms();
        loadBookingsFromFile();

        System.out.println("==============================================");
        System.out.println("       WELCOME TO GRAND HORIZON HOTEL         ");
        System.out.println("==============================================");

        boolean running = true;
        while (running) {
            System.out.println("\n1. Search Available Rooms");
            System.out.println("2. Make a Reservation");
            System.out.println("3. Cancel a Reservation");
            System.out.println("4. View Booking Details");
            System.out.println("5. Exit");
            System.out.print("Select an option: ");

            int choice;
            try {
                choice = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number!");
                continue;
            }

            switch (choice) {
                case 1:
                    searchAvailableRooms();
                    break;
                case 2:
                    bookRoom();
                    break;
                case 3:
                    cancelReservation();
                    break;
                case 4:
                    viewBookings();
                    break;
                case 5:
                    saveBookingsToFile();
                    System.out.println("Thank you for using our Hotel Reservation System!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Please choose between 1 and 5.");
            }
        }
        sc.close();
    }

    // Set up default rooms in the hotel
    private static void initializeRooms() {
        rooms.add(new Room(101, "Standard", 1500.0, true));
        rooms.add(new Room(102, "Standard", 1500.0, true));
        rooms.add(new Room(103, "Standard", 1500.0, true));
        rooms.add(new Room(201, "Deluxe", 2800.0, true));
        rooms.add(new Room(202, "Deluxe", 2800.0, true));
        rooms.add(new Room(301, "Suite", 5000.0, true));
        rooms.add(new Room(302, "Suite", 5000.0, true));
    }

    private static void searchAvailableRooms() {
        System.out.println("\nFilter by Category:");
        System.out.println("1. All Available Rooms");
        System.out.println("2. Standard (Rs. 1500/night)");
        System.out.println("3. Deluxe   (Rs. 2800/night)");
        System.out.println("4. Suite    (Rs. 5000/night)");
        System.out.print("Enter choice: ");

        String filter = "All";
        try {
            int c = Integer.parseInt(sc.nextLine());
            if (c == 2) filter = "Standard";
            else if (c == 3) filter = "Deluxe";
            else if (c == 4) filter = "Suite";
        } catch (NumberFormatException e) {
            System.out.println("Showing all available rooms by default.");
        }

        System.out.println("\n--- Available Rooms ---");
        boolean found = false;
        for (Room r : rooms) {
            if (r.isAvailable() && (filter.equals("All") || r.getCategory().equalsIgnoreCase(filter))) {
                System.out.println(r);
                found = true;
            }
        }
        if (!found) {
            System.out.println("Sorry, no available rooms match your criteria right now.");
        }
    }

    private static void bookRoom() {
        System.out.print("\nEnter Guest Full Name: ");
        String name = sc.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("Guest name cannot be empty.");
            return;
        }

        System.out.print("Enter Room Number you wish to book: ");
        int roomNum;
        try {
            roomNum = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid room number!");
            return;
        }

        Room selectedRoom = null;
        for (Room r : rooms) {
            if (r.getRoomNumber() == roomNum) {
                selectedRoom = r;
                break;
            }
        }

        if (selectedRoom == null) {
            System.out.println("Room " + roomNum + " does not exist.");
            return;
        }

        if (!selectedRoom.isAvailable()) {
            System.out.println("Sorry, Room " + roomNum + " is already booked.");
            return;
        }

        System.out.print("Enter number of nights: ");
        int nights;
        try {
            nights = Integer.parseInt(sc.nextLine());
            if (nights <= 0) {
                System.out.println("Number of nights must be at least 1.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input for nights.");
            return;
        }

        double totalCost = selectedRoom.getPricePerNight() * nights;
        System.out.printf("\nTotal Bill Amount: Rs. %.2f for %d night(s)\n", totalCost, nights);

        // Payment simulation
        System.out.println("Select Payment Method:");
        System.out.println("1. Credit / Debit Card");
        System.out.println("2. UPI");
        System.out.println("3. Net Banking");
        System.out.print("Enter payment option: ");

        String paymentMethod = "Card";
        String payChoice = sc.nextLine();
        if (payChoice.equals("2")) {
            paymentMethod = "UPI";
            System.out.print("Enter UPI ID (e.g., user@upi): ");
            sc.nextLine();
        } else if (payChoice.equals("3")) {
            paymentMethod = "Net Banking";
        } else {
            System.out.print("Enter last 4 digits of Card: ");
            sc.nextLine();
        }

        System.out.println("Processing payment...");
        System.out.println("Payment of Rs. " + String.format("%.2f", totalCost) + " successful!");

        // Confirm reservation
        selectedRoom.setAvailable(false);
        Reservation newRes = new Reservation(nextBookingId++, name, roomNum, selectedRoom.getCategory(), nights, totalCost, paymentMethod);
        reservations.add(newRes);
        saveBookingsToFile();

        System.out.println("\n--- Booking Confirmed! ---");
        System.out.println(newRes);
    }

    private static void cancelReservation() {
        if (reservations.isEmpty()) {
            System.out.println("\nNo active reservations found to cancel.");
            return;
        }

        System.out.print("\nEnter Booking ID to cancel: ");
        int id;
        try {
            id = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid Booking ID!");
            return;
        }

        Reservation target = null;
        for (Reservation res : reservations) {
            if (res.getBookingId() == id) {
                target = res;
                break;
            }
        }

        if (target == null) {
            System.out.println("Booking ID " + id + " not found.");
            return;
        }

        // Free up the room
        for (Room r : rooms) {
            if (r.getRoomNumber() == target.getRoomNumber()) {
                r.setAvailable(true);
                break;
            }
        }

        reservations.remove(target);
        saveBookingsToFile();
        System.out.println("Reservation (ID: " + id + ") for " + target.getGuestName() + " has been cancelled.");
        System.out.println("Refund initiated to original payment method.");
    }

    private static void viewBookings() {
        if (reservations.isEmpty()) {
            System.out.println("\nNo bookings made yet.");
            return;
        }

        System.out.println("\n================ ALL RESERVATIONS ================");
        for (Reservation res : reservations) {
            System.out.println(res);
            System.out.println("--------------------------------------------------");
        }
    }

    // Save current bookings to bookings.txt
    private static void saveBookingsToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Reservation res : reservations) {
                writer.println(res.toFileString());
            }
        } catch (IOException e) {
            System.out.println("Warning: Could not save bookings to file.");
        }
    }

    // Load saved bookings on startup
    private static void loadBookingsFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int maxId = 1000;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 7) {
                    int id = Integer.parseInt(parts[0]);
                    String name = parts[1];
                    int roomNo = Integer.parseInt(parts[2]);
                    String category = parts[3];
                    int nights = Integer.parseInt(parts[4]);
                    double total = Double.parseDouble(parts[5]);
                    String payMethod = parts[6];

                    reservations.add(new Reservation(id, name, roomNo, category, nights, total, payMethod));
                    if (id > maxId) maxId = id;

                    // Mark the corresponding room as booked
                    for (Room r : rooms) {
                        if (r.getRoomNumber() == roomNo) {
                            r.setAvailable(false);
                            break;
                        }
                    }
                }
            }
            nextBookingId = maxId + 1;
        } catch (Exception e) {
            System.out.println("Note: Starting with a fresh booking record.");
        }
    }
}