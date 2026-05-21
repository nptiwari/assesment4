import model.Driver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.DriverService;
import util.AlgorithmUtils;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;



public class SystemTest {
    public SystemTest() {
        super();
    }

    private ArrayList<Driver> drivers;

    @BeforeEach
    public void setUp() {
        drivers = new ArrayList<>();
        drivers.add(new Driver("D003", "Charlie", "c@mail.com", "043", "LIC3", 4.2, 0, 0, "Car", "Toyota", "Camry", "PL3", 2020, "AVAILABLE"));
        drivers.add(new Driver("D001", "Alice", "a@mail.com", "041", "LIC1", 4.9, 0, 0, "Car", "Ford", "Focus", "PL1", 2022, "AVAILABLE"));
        drivers.add(new Driver("D002", "Bob", "b@mail.com", "042", "LIC2", 4.6, 0, 0, "SUV", "Kia", "Sorento", "PL2", 2021, "AVAILABLE"));
    }

    @Test
    public void testQuickSortById() {
        AlgorithmUtils.sortDriversById(drivers, 0, drivers.size() - 1);
        assertEquals("D001", drivers.get(0).getId());
        assertEquals("D002", drivers.get(1).getId());
        assertEquals("D003", drivers.get(2).getId());
    }

    @Test
    public void testBinarySearch() {
        // Must sort before binary search
        AlgorithmUtils.sortDriversById(drivers, 0, drivers.size() - 1);
        
        Driver found = AlgorithmUtils.binarySearchDriverById(drivers, "D002");
        assertNotNull(found);
        assertEquals("Bob", found.getName());

        Driver notFound = AlgorithmUtils.binarySearchDriverById(drivers, "D999");
        assertNull(notFound);
    }

    @Test
    public void testBinarySearchNotFound() {
        // 1. Sort the list first (requirement for binary search)
        AlgorithmUtils.sortDriversById(drivers, 0, drivers.size() - 1);
        
        // 2. Search for an ID that we know does NOT exist
        Driver result = AlgorithmUtils.binarySearchDriverById(drivers, "D999");
        
        // 3. Assert that the result is null 
        assertNull(result, "Driver D999 should not exist and should return null");
    }
}