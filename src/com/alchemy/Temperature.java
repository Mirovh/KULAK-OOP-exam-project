package com.alchemy;
import java.util.ArrayList;

public class Temperature {
    private Long hotness;
    private Long coldness;
    static Long temperatureLimit = 10000L;
    Temperature(Long hotness,Long coldness){
        this.hotness= hotness;
        this.coldness = coldness;
    }
    public ArrayList<Long> getTemperature(){
    ArrayList<Long> list = new ArrayList<>();
    list.add(coldness);
    list.add(hotness);
    return list;
    }
    public Long getHotness(){
        return hotness;
    }
    public Long getColdness(){
        return coldness;
    }
    public void heat(Long change){
        if(change <0){
            cool(change);
            return;
        }
        else{
            ArrayList<Long> newTemps = changeTemp(change,hotness,coldness);
            coldness = newTemps.getFirst();
            hotness = newTemps.getLast();
        }
    }

    public void cool(Long change){
        if(change<0){
            heat(change);
            return;
        }
        else{
            ArrayList<Long> newTemps = changeTemp(change,coldness,hotness);
            coldness = newTemps.getFirst();
            hotness = newTemps.getLast();
        }

    }
    private ArrayList<Long> changeTemp(Long change, Long higher,Long lower){
        if(lower >0){
            if(lower>change){
                change -= lower;
                lower = 0L;
                higher = change;
            }
            else{
                lower -= change;
            }
        }
        else{
            higher += change;
        }
        if(higher>temperatureLimit){
            higher = temperatureLimit;
        }
        ArrayList<Long> list = new ArrayList<>();
        list.add(higher);
        list.add(lower);
        return list;
    }
}
