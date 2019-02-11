package uk.ac.york.sepr4.ahod2.object.encounter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;

import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

public class EncounterManager {

    private final NavigableMap<Double, Encounter> map = new TreeMap<>();
    private double weights = 0;

    public EncounterManager(){
        Json json = new Json();

        Array<Encounter> encounters = json.fromJson(Array.class, Encounter.class, Gdx.files.internal("data/encounters.json"));
        encounters.forEach(encounter -> {
            weights+=encounter.getChance();
            map.put(weights, encounter);
        });

        Gdx.app.log("EncounterManager", "Loaded "+encounters.size +" encounters!");
    }

    public Encounter generateEncounter() {
        Random random = new Random();
        double val = random.nextDouble() * weights;
        return map.higherEntry(val).getValue();

    }
}
