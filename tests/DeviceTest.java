import com.alchemy.*;
import com.alchemy.IngredientConditions.CoolingBox;
import com.alchemy.IngredientConditions.Oven;
import com.alchemy.quantity.PowderUnit;
import com.alchemy.quantity.Unit;
import com.alchemy.IngredientConditions.Transmorgrifier;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static com.alchemy.quantity.PowderUnit.*;
import static org.junit.Assert.*;

public class DeviceTest {
    Laboratory lab;
    Laboratory lab2;
    AlchemicIngredient ingredient;
    AlchemicIngredient ingredient2;
    IngredientContainer container1;
    IngredientContainer container2;
    Unit basicUnit = PowderUnit.BOX;

    @Before
    public void setUpFixture() {
        ingredient = new AlchemicIngredient(10L);
        ingredient2 = new AlchemicIngredient(10L);
        container1 = new IngredientContainer(ingredient,basicUnit);
        container2 = new IngredientContainer(ingredient2,basicUnit);
        lab = new Laboratory();
        lab2 = new Laboratory();
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
        assertThrows(Device.DeviceFullException.class,()->{fridge.addIngredient(container2);});
        //test if container gets destroyed
        Assert.assertNull(container1.getContent());
        //test basic cooling function
        fridge.react();
        IngredientContainer cooledContainer = fridge.getContents();
        Assert.assertEquals(20L,(long)cooledContainer.getContent().getTemperature().getColdness());
        Assert.assertEquals(0L,(long)cooledContainer.getContent().getTemperature().getHotness());
        fridge2.addIngredient(cooledContainer);
        fridge2.react();
        IngredientContainer cooledContainer2 = fridge2.getContents();
        Assert.assertEquals(40L,(long)cooledContainer2.getContent().getTemperature().getColdness());
        Assert.assertEquals(0L,(long)cooledContainer2.getContent().getTemperature().getHotness());
        // test ingredient doesn't heat up when in coolingBox
        fridge.addIngredient(cooledContainer2);
        fridge.react();
        IngredientContainer cooledContainer3 = fridge.getContents();
        Assert.assertEquals(40L,(long)cooledContainer3.getContent().getTemperature().getColdness());
        Assert.assertEquals(0L,(long)cooledContainer3.getContent().getTemperature().getHotness());
    }

    @Test
    public void OvenTest() throws Device.DeviceFullException, Device.NotInLaboratoryException ,Exception{
        //Test is run 20 times to take into account the randomness
        //adding Ingredients test
        Oven oven = new Oven();
        Oven oven2 = new Oven(0L, 60L);
        oven.setTemperature(0L,40L);
        lab.addDevice(oven);
        lab2.addDevice(oven2);
        //test DeviceFullException
        oven.addIngredient(container1);
        assertThrows(Device.DeviceFullException.class,()->{oven.addIngredient(container2);});
        //test if container gets destroyed
        Assert.assertNull(container1.getContent());
        //test basic heating function
        oven.react();
        IngredientContainer heatedContainer = oven.getContents();
        Assert.assertTrue(35<=heatedContainer.getContent().getTemperature().getHotness()&& heatedContainer.getContent().getTemperature().getHotness() <=45);
        Assert.assertEquals(0L,(long)heatedContainer.getContent().getTemperature().getColdness());
        oven2.addIngredient(heatedContainer);
        oven2.react();
        IngredientContainer heatedContainer2 = oven2.getContents();
        Assert.assertTrue(55<=heatedContainer2.getContent().getTemperature().getHotness()&& heatedContainer2.getContent().getTemperature().getHotness()<=65 );
        Assert.assertEquals(0L,(long)heatedContainer2.getContent().getTemperature().getColdness());
        // test ingredient doesn't cool down when in Oven
        oven.addIngredient(heatedContainer2);
        oven.react();
        IngredientContainer heatedContainer3 = oven.getContents();
        Assert.assertTrue(55<=heatedContainer3.getContent().getTemperature().getHotness()&& heatedContainer3.getContent().getTemperature().getHotness()<=65 );
        Assert.assertEquals(0L,(long)heatedContainer3.getContent().getTemperature().getColdness());
    }

    @Test
    public void TransmorgrifierTest() throws Exception{
       Transmorgrifier transmorgrifier = new Transmorgrifier();
       lab.addDevice(transmorgrifier);
       Assert.assertFalse(container1.getContent().getState().getState().isSolid());
       //test basic function
       transmorgrifier.addIngredient(container1);
       transmorgrifier.react();
       IngredientContainer changedContainer = transmorgrifier.getContents();
       Assert.assertTrue(changedContainer.getContent().getState().getState().isSolid());
       transmorgrifier.addIngredient(changedContainer);
       transmorgrifier.react();
        IngredientContainer changedContainer2 = transmorgrifier.getContents();
        Assert.assertFalse(changedContainer2.getContent().getState().getState().isSolid());
    }
}
