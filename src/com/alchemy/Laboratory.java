package com.alchemy;
import java.util.ArrayList;
public class Laboratory {
    private ArrayList<Device> devices;

    public Laboratory(){
        devices = new ArrayList<>();
    }

    public ArrayList<Device> getDevices() {
        return devices;
    }
    public void addDevice(Device device) throws LaboratoryFullException {
        if(canAddDevice(device)) {
            devices.add(device);
            device.setLaboratory(this);
        }
        else{
            throw new LaboratoryFullException("Laboratory already has " + device.getClass().getSimpleName());
        }
    }
    public Boolean canAddDevice(Device checkedDevice){
        boolean valid = true;
        if(devices == null){return true;}
        for(Device device: devices){
            if (device.getClass() == checkedDevice.getClass()) {
                valid = false;
                break;
            }
        }
        return valid;
    }
    public static class LaboratoryFullException extends Exception {
        public LaboratoryFullException(String e){super(e);}
    }
}
