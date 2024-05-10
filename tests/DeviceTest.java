import com.alchemy.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DeviceTest {
    Laboratory lab;
    Laboratory lab2;
    AlchemicIngredient ingredient;
    AlchemicIngredient ingredient2;
    IngredientContainer container1;
    IngredientContainer container2;
    @Before
    public void setUpFixture() {
        try {
            ingredient = new AlchemicIngredient("Test Ingredient", 10,true);
            ingredient2 = new AlchemicIngredient("Test Ingredient", 10,false);
            container1 = new IngredientContainer(ingredient,20);
            container2 = new IngredientContainer(ingredient2,30);
            lab = new Laboratory();
            lab2 = new Laboratory();
        } catch (Name.IllegalNameException e) {
            fail("Valid name should not throw an exception: " + e.getMessage());
        }
    }
    @Test
    public void LaboratoryDeviceTest() throws Laboratory.LaboratoryFullException {
        CoolingBox fridge = new CoolingBox();
        CoolingBox fridge2 = new CoolingBox();
        //test that only one device of each type can be added to the Laboratory
        lab.addDevice(fridge);
        Assert.assertThrows(Laboratory.LaboratoryFullException.class,()->{lab.addDevice(fridge2);});
        Assert.assertThrows(Device.NotInLaboratoryException.class, fridge2::react);

    }
    @Test
    public void CoolingBoxTest() throws Device.DeviceFullException, Device.NotInLaboratoryException, Exception {
        //adding Ingredients test
        CoolingBox fridge = new CoolingBox();
        CoolingBox fridge2 = new CoolingBox(40L, 0L);
        fridge.setTemperature(20L,0L);
        lab.addDevice(fridge);
        lab2.addDevice(fridge2);
        //test DeviceFullException
        fridge.addIngredient(container1);
        Assert.assertThrows(Device.DeviceFullException.class,()->{fridge.addIngredient(container2);});
        //test if container gets destroyed
        Assert.assertNull(container1.getContents());
        //test basic cooling function
        fridge.react();
        IngredientContainer cooledContainer = fridge.getContents();
        Assert.assertEquals(20L,(long)cooledContainer.getContents().getTemperature().getColdness());
        Assert.assertEquals(0L,(long)cooledContainer.getContents().getTemperature().getHotness());
        fridge2.addIngredient(cooledContainer);
        fridge2.react();
        IngredientContainer cooledContainer2 = fridge2.getContents();
        Assert.assertEquals(40L,(long)cooledContainer2.getContents().getTemperature().getColdness());
        Assert.assertEquals(0L,(long)cooledContainer2.getContents().getTemperature().getHotness());
        // test ingredient doesn't heat up when in coolingBox
        fridge.addIngredient(cooledContainer2);
        fridge.react();
        IngredientContainer cooledContainer3 = fridge.getContents();
        Assert.assertEquals(40L,(long)cooledContainer3.getContents().getTemperature().getColdness());
        Assert.assertEquals(0L,(long)cooledContainer3.getContents().getTemperature().getHotness());
    }
    @Test
    public void OvenTest() throws Device.DeviceFullException, Device.NotInLaboratoryException ,Exception{
        //adding Ingredients test
        Oven oven = new Oven();
        Oven oven2 = new Oven(0L, 60L);
        oven.setTemperature(0L,40L);
        lab.addDevice(oven);
        lab2.addDevice(oven2);
        //test DeviceFullException
        oven.addIngredient(container1);
        Assert.assertThrows(Device.DeviceFullException.class,()->{oven.addIngredient(container2);});
        //test if container gets destroyed
        Assert.assertNull(container1.getContents());
        //test basic heating function
        oven.react();
        IngredientContainer heatedContainer = oven.getContents();
        Assert.assertTrue(35<=heatedContainer.getContents().getTemperature().getHotness()&& heatedContainer.getContents().getTemperature().getHotness() >=40);
        Assert.assertEquals(0L,(long)heatedContainer.getContents().getTemperature().getColdness());
        oven2.addIngredient(heatedContainer);
        oven2.react();
        IngredientContainer heatedContainer2 = oven2.getContents();
        Assert.assertTrue(55<=heatedContainer2.getContents().getTemperature().getHotness()&& heatedContainer2.getContents().getTemperature().getHotness()<=65 );
        Assert.assertEquals(0L,(long)heatedContainer2.getContents().getTemperature().getColdness());
        // test ingredient doesn't cool down when in Oven
        oven.addIngredient(heatedContainer2);
        oven.react();
        IngredientContainer heatedContainer3 = oven.getContents();
        Assert.assertTrue(55<=heatedContainer3.getContents().getTemperature().getHotness()&& heatedContainer3.getContents().getTemperature().getHotness()<=65 );
        Assert.assertEquals(0L,(long)heatedContainer3.getContents().getTemperature().getColdness());
    }
    @Test
    public void TransmorgrifierTest() throws Exception{
       Transmorgrifier transmorgrifier = new Transmorgrifier();
       lab.addDevice(transmorgrifier);
       transmorgrifier.addIngredient(container1);
       transmorgrifier.react();
       IngredientContainer changedContainer = transmorgrifier.getContents();
       Assert.assertFalse(changedContainer.getContents().getState().isSolid());
       transmorgrifier.addIngredient(changedContainer);
       transmorgrifier.react();
        IngredientContainer changedContainer2 = transmorgrifier.getContents();
        Assert.assertTrue(changedContainer2.getContents().getState().isSolid());
    }
}
