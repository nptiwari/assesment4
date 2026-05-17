package util;

import model.Driver;
import model.Rider;

import java.io.*;
import java.util.ArrayList;

/**
 * FileHandler - reads and writes Driver/Rider data to CSV.
 * All IO operations wrapped in try-catch for graceful error handling.
 */
public class FileHandler {

    public static void loadData(String filePath,
            ArrayList<Driver> drivers,
            ArrayList<Rider> riders) {

        File file = new File(filePath);
        if (!file.exists()) {
            Display.warn("Data file not found: " + filePath
                + ". Starting fresh.");
            return;
        }

        try (BufferedReader br =
                new BufferedReader(new FileReader(filePath))) {

            String line;
            int lineNo = 0, loaded = 0;

            while ((line = br.readLine()) != null) {
                lineNo++;
                line = line.trim();

                // Skip comments and empty lines
                if (line.isEmpty() || line.startsWith("#")) continue;

                String[] p = line.split(",", -1);

                try {
                    if (p[0].equalsIgnoreCase("DRIVER")
                            && p.length == 16) {
                        drivers.add(new Driver(
                            p[1],  // id
                            p[2],  // name
                            p[3],  // email
                            p[4],  // phone
                            p[5],  // licenseNo
                            Double.parseDouble(p[6]),   // rating
                            Double.parseDouble(p[7]),   // totalEarnings
                            Integer.parseInt(p[8]),     // tripsCompleted
                            p[9],  // vehicleType
                            p[10], // vehicleMake
                            p[11], // vehicleModel
                            p[12], // vehiclePlate
                            Integer.parseInt(p[13]),    // vehicleYear
                            p[14]  // status
                        ));
                        loaded++;

                    } else if (p[0].equalsIgnoreCase("RIDER")
                            && p.length == 9) {
                        riders.add(new Rider(
                            p[1],  // id
                            p[2],  // name
                            p[3],  // email
                            p[4],  // phone
                            Double.parseDouble(p[5]),   // riderRating
                            p[6],  // paymentMethod
                            p[7],  // homeAddress
                            Double.parseDouble(p[8])    // totalSpent
                        ));
                        loaded++;
                    }

                } catch (NumberFormatException e) {
                    Display.warn("Skipping malformed line "
                        + lineNo + ": " + e.getMessage());
                }
            }
            Display.success("Loaded " + loaded
                + " records from " + filePath);

        } catch (FileNotFoundException e) {
            Display.error("File not found: " + e.getMessage());
        } catch (IOException e) {
            Display.error("Error reading file: " + e.getMessage());
        }
    }

    public static void saveData(String filePath,
            ArrayList<Driver> drivers,
            ArrayList<Rider> riders) {

        // Ensure parent directory exists
        File f = new File(filePath);
        if (f.getParentFile() != null) {
            f.getParentFile().mkdirs();
        }

        try (BufferedWriter bw =
                new BufferedWriter(new FileWriter(filePath))) {

            bw.write("# Uber Management System - Data File");
            bw.newLine();
            bw.write("# DRIVER: id,name,email,phone,licenseNo,"
                + "rating,earnings,trips,vType,vMake,"
                + "vModel,plate,year,status,warnings");
            bw.newLine();
            bw.write("# RIDER: id,name,email,phone,"
                + "rating,payment,address,totalSpent");
            bw.newLine();

            for (Driver d : drivers) {
                bw.write(d.toCsv() + "," + d.getWarningCount());
                bw.newLine();
            }
            for (Rider r : riders) {
                bw.write(r.toCsv());
                bw.newLine();
            }

            Display.success("Data saved to " + filePath
                + " (" + (drivers.size() + riders.size())
                + " records)");

        } catch (IOException e) {
            Display.error("Error saving data: " + e.getMessage());
        }
    }
}