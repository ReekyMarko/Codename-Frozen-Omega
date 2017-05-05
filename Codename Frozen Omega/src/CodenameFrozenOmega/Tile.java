package CodenameFrozenOmega;

import java.util.ArrayList;
import java.util.Random;

public class Tile {
	int herbivores, carnivores, diseases, plants, location;
	float temperature;
	
	public int getLocation() {
		return location;
	}

	public void setLocation(int location) {
		this.location = location;
	}

	public float getTemperature() {
		return temperature;
	}

	public void setTemperature(float temperature) {
		this.temperature = temperature;
	}

	public Tile(int location, float temperature) {
		super();
		this.location = location;
		this.temperature = temperature;
	}

	public int listAnimals(int id, ObjectsControl objectscontrol){
		if(id == 1){
			carnivores = objectscontrol.listCarnivoresInTile(location);
			return carnivores;
		}
		else if(id == 2){
			herbivores = objectscontrol.listHerbivoresInTile(location);
			return herbivores;
		}
		else if(id == 3){
			plants = objectscontrol.listPlantsInTile(location);
			return plants;
		}
		else if(id == 4){
			diseases = objectscontrol.listDiseasesInTile(location);		
			return carnivores;
		}
		return 0;
	}
	private static int randtemperature(int min, int max) {
		Random rn= new Random();
		int randomValue = min + (max - min) * rn.nextInt();
		return randomValue;
	    }
		public float settemperature(){
			float temperature =randtemperature(10,30);
			temperature=  (float) (temperature+Math.random());
			return Math.round(temperature);
			}
}
