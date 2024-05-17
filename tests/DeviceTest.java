import com.alchemy.*;
import com.alchemy.IngredientConditions.*;
import com.alchemy.quantity.FluidUnit;
import com.alchemy.quantity.PowderUnit;
import com.alchemy.quantity.Quantity;
import com.alchemy.quantity.Unit;
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
    Quantity basicQuantity = new Quantity(1, FluidUnit.SPOON);

    Temperature basicTemp = new Temperature(0,20);

    @Before
    public void setUpFixture() {
        ingredient = new AlchemicIngredient(10L);
        ingredient2 = new AlchemicIngredient(10L);
        container1 = new IngredientContainer(ingredient,basicQuantity.getUnit());
        container2 = new IngredientContainer(ingredient2,basicQuantity.getUnit());
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

    @Test
    public void KettleTestName() throws Exception{
        Kettle kettle = new Kettle();
        lab.addDevice(kettle);
        //mixing of two ingredients with same name
        kettle.addIngredient(container1);
        kettle.addIngredient(container2);
        kettle.react();
        assertEquals(kettle.getContent().getFirst().getContent().getFullName(), ingredient.getFullName());
        //mixing multiple ingredients with different names
        Temperature basicTemp = new Temperature(0,20);
        IngredientState.State bassicState = IngredientState.State.Liquid;
        int basicAmount = 15;
        AlchemicIngredient testIngredient1 = new AlchemicIngredient("Name One",basicTemp,bassicState,basicAmount);
        AlchemicIngredient testIngredient2 = new AlchemicIngredient("Name Two",basicTemp,bassicState,basicAmount);
        AlchemicIngredient testIngredient3 = new AlchemicIngredient("Name Three",basicTemp,bassicState,basicAmount);
        kettle.addIngredient(new IngredientContainer(testIngredient1,FluidUnit.BARREL));
        kettle.addIngredient(new IngredientContainer(testIngredient2,FluidUnit.BARREL));
        kettle.addIngredient(new IngredientContainer(testIngredient3,FluidUnit.BARREL));
        kettle.react();
        AlchemicIngredient testIngredient4 = kettle.getContent().getFirst().getContent();
        Assert.assertEquals(testIngredient4.getFullName(),"Name One mixed with Name Two and Name Three");
        AlchemicIngredient testIngredient5 = new AlchemicIngredient("An Alphabetically First Name",basicTemp,bassicState,basicAmount);
        kettle.addIngredient(new IngredientContainer(testIngredient4,FluidUnit.BARREL));
        kettle.addIngredient(new IngredientContainer(testIngredient5,FluidUnit.BARREL));
        kettle.react();
        AlchemicIngredient testIngredient6 = kettle.getContent().getFirst().getContent();
        Assert.assertEquals(testIngredient6.getFullName(),"An Alphabetically First Name mixed with Name One , Name Two and Name Three");
    }
    @Test
    public void KettleTestStateAndStandardTemperature() throws Exception{
        Kettle kettle = new Kettle();
        lab.addDevice(kettle);
        //mixing one ingredient close to [0,20] and one far away
        AlchemicIngredient closeTo20 = new AlchemicIngredient("Name",new Temperature(0,19), IngredientState.State.Powder,5);
        AlchemicIngredient farFrom20 = new AlchemicIngredient("Othername",new Temperature(50,0), IngredientState.State.Liquid,5);
        kettle.addIngredient(new IngredientContainer(closeTo20,CHEST));
        kettle.addIngredient(new IngredientContainer(farFrom20, FluidUnit.BARREL));
        kettle.react();
        AlchemicIngredient newIngredient = kettle.getContent().getFirst().getContent();
        Assert.assertEquals(newIngredient.getState().isSolid(),closeTo20.getState().isSolid());
        Assert.assertEquals(newIngredient.getStandardType().getStandardTemperature(),closeTo20.getStandardType().getStandardTemperature());
        //mixing two ingredients equally far from [0,20] with different states and standardTemperatures
        AlchemicIngredient equalTo20 = new AlchemicIngredient("YetAnotherName",new Temperature(0,21), IngredientState.State.Powder,5);
        kettle.addIngredient(new IngredientContainer(closeTo20,CHEST));
        kettle.addIngredient(new IngredientContainer(equalTo20, FluidUnit.BARREL));
        kettle.react();
        AlchemicIngredient newIngredient2 = kettle.getContent().getFirst().getContent();
        Assert.assertEquals(newIngredient2.getState().isSolid(),equalTo20.getState().isSolid());
        Assert.assertEquals(newIngredient2.getStandardType().getStandardTemperature(),equalTo20.getStandardType().getStandardTemperature());
    }
    @Test
    public void kettleTestQuantity() throws Exception{
        Kettle kettle = new Kettle();
        lab.addDevice(kettle);
        //testing basic addition of ingredients of same state
        AlchemicIngredient testIngredient1 = new AlchemicIngredient(new Quantity(5, SPOON));
        AlchemicIngredient testIngredient2 = new AlchemicIngredient(new Quantity(3,FluidUnit.JUG));
        kettle.addIngredient(new IngredientContainer(testIngredient1, FluidUnit.BARREL));
        kettle.addIngredient(new IngredientContainer(testIngredient2, FluidUnit.BARREL));
        kettle.react();
        AlchemicIngredient newIngredient = kettle.getContent().getFirst().getContent();
        assertTrue(newIngredient.getQuantity().isEqualTo(new Quantity(110,SPOON)));
        //testing rounding down of units smaller than spoons of opposite state
        kettle.addIngredient(new IngredientContainer(newIngredient,FluidUnit.BARREL));
        AlchemicIngredient smallIngredient1 = new AlchemicIngredient("Small",basicTemp, new IngredientState(true),new Quantity(5,PINCH));
        AlchemicIngredient smallIngredient2 = new AlchemicIngredient("Smaller", basicTemp,new IngredientState(true),new Quantity(4,PINCH));
        kettle.addIngredient(new IngredientContainer(testIngredient1, FluidUnit.BARREL));
        kettle.addIngredient(new IngredientContainer(smallIngredient1, CHEST));
        kettle.addIngredient(new IngredientContainer(smallIngredient2,CHEST));
        kettle.react();
        assertTrue(newIngredient.getQuantity().isEqualTo(new Quantity(111,SPOON)));
    }
    @Test
    public void KettleTestTemperature() throws Exception{
        Kettle kettle = new Kettle();
        lab.addDevice(kettle);
        AlchemicIngredient testIngredient1 = new AlchemicIngredient("Name",new Temperature(50,0),new IngredientState(false),new Quantity(5,FluidUnit.SPOON));
        AlchemicIngredient testIngredient2 = new AlchemicIngredient("NewName",new Temperature(0,30),new IngredientState(false),new Quantity(3,FluidUnit.SPOON));
        kettle.addIngredient(new IngredientContainer(testIngredient1,FluidUnit.BARREL));
        kettle.addIngredient(new IngredientContainer(testIngredient2,FluidUnit.BARREL));
        kettle.react();
        int expectedValue = (5*(-50)/8)+((3*(30))/8);
        assertTrue(kettle.getContent().getFirst().getContent().getTemperature().getHotness() == expectedValue | kettle.getContent().getFirst().getContent().getTemperature().getHotness() == expectedValue);
    }
}
