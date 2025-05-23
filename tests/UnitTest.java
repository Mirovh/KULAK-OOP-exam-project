import com.alchemy.quantity.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.alchemy.quantity.FluidUnit.*;
import static com.alchemy.quantity.PowderUnit.*;


public class UnitTest {
    Quantity smallTestFluid;
    Quantity smallTestPowder;
    Quantity mediumTestFluid;
    Quantity mediumTestPowder;
    Quantity largeTestFluid;
    Quantity largeTestPowder;

    @Before
    public void setUpFixture(){
        smallTestFluid = new Quantity(840, DROP);
        smallTestPowder = new Quantity(756, PINCH);
        mediumTestFluid = new Quantity(1, JUG);
        mediumTestPowder = new Quantity(1, SACK);
        largeTestFluid = new Quantity(1, BARREL);
        largeTestPowder = new Quantity(1, CHEST);
    }

    @Test
    public void testFluidCreation(){
        assertTrue(smallTestFluid.isEqualTo(mediumTestFluid));
        assertTrue(smallTestFluid.isEqualTo(DROP, 840L));
        assertTrue(smallTestFluid.isFluidUnit());
        assertFalse(mediumTestFluid.isPowderUnit());
    }

    @Test
    public void testPowderCreation(){
        assertTrue(smallTestPowder.isEqualTo(mediumTestPowder));
        assertTrue(smallTestPowder.isEqualTo(PINCH, 756L));
        assertTrue(smallTestPowder.isPowderUnit());
        assertFalse(mediumTestPowder.isFluidUnit());
    }

    @Test
    public void testComparison(){
        assertTrue(largeTestFluid.isGreaterThan(smallTestFluid));
        assertTrue(largeTestPowder.isGreaterThan(smallTestPowder));
        assertTrue(smallTestFluid.isGreaterThanOrEqualTo(mediumTestFluid));
        assertTrue(smallTestPowder.isGreaterThanOrEqualTo(mediumTestPowder));
        assertTrue(mediumTestPowder.isSmallerThan(largeTestPowder));
        assertTrue(mediumTestFluid.isSmallerThan(largeTestFluid));
        assertTrue(mediumTestFluid.isSmallerThanOrEqualTo(smallTestFluid));
        assertTrue(mediumTestPowder.isSmallerThanOrEqualTo(smallTestPowder));
    }

    @Test
    public void testConversion(){
        smallTestFluid.convertTo(JUG);
         assertEquals(JUG, smallTestFluid.getUnit());
         assertTrue(smallTestFluid.isFluidUnit());
         mediumTestPowder.convertToFluidUnit(DROP);
         assertEquals(DROP, mediumTestPowder.getUnit());
         assertTrue(mediumTestPowder.isFluidUnit()); // should be fluid since we converted to drop
        mediumTestPowder.convertToPowderUnit(PINCH);
         assertTrue(mediumTestPowder.isEqualTo(smallTestPowder));
         largeTestPowder.convertTo(PowderUnit.SPOON);
         assertFalse(largeTestPowder.isSmallerThanOrEqualTo(mediumTestPowder));
        assertThrows(Exception.class, () -> mediumTestFluid.convertTo(PowderUnit.CHEST));
        assertThrows(Exception.class, () -> mediumTestPowder.convertTo(FluidUnit.DROP));
    }
}
