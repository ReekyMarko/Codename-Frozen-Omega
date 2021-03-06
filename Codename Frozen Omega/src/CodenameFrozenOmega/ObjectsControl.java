
package CodenameFrozenOmega;

import java.util.ArrayList;


import java.util.Iterator;

public class ObjectsControl{
	ArrayList<Carnivore> carnivore = new ArrayList<Carnivore>();
	ArrayList<Herbivore> herbivore = new ArrayList<Herbivore>();
	ArrayList<Plant> plant = new ArrayList<Plant>();
	ArrayList<Disease> disease = new ArrayList<Disease>();
	
	public ArrayList<Carnivore> getCarnivore() {
		return carnivore;
	}

	public void setCarnivore(ArrayList<Carnivore> carnivore) {
		this.carnivore = carnivore;
	}

	public ArrayList<Herbivore> getHerbivore() {
		return herbivore;
	}

	public void setHerbivore(ArrayList<Herbivore> herbivore) {
		this.herbivore = herbivore;
	}

	public ArrayList<Plant> getPlant() {
		return plant;
	}

	public void setPlant(ArrayList<Plant> plant) {
		this.plant = plant;
	}

	public ArrayList<Disease> getDisease() {
		return disease;
	}

	public void setDisease(ArrayList<Disease> disease) {
		this.disease = disease;
	}

	public void createObjects(String className,String species,int amount,int id,
								int preferredTemperature,int location, double lifespan,
								double age, int foodChainPosition, int habitability, boolean healthy) {
		int birthTick = 0;
		if(className == "Carnivore"){
			for(int i=1; i<=amount; i++){
				carnivore.add(new Carnivore(preferredTemperature, id, location, lifespan, age,
											species, foodChainPosition, habitability, birthTick, healthy));
			}
		}else if(className == "Herbivore"){
			for(int i=1; i<=amount; i++){
				herbivore.add(new Herbivore(preferredTemperature, id, location, lifespan, age,
											species, foodChainPosition, habitability, birthTick, healthy));
			}
		}else if(className == "Plant"){
			for(int i=1; i<=amount; i++){
				plant.add(new Plant(preferredTemperature, id, location, lifespan, habitability, age,
									healthy, birthTick, species));
				}
			}else if(className == "Disease"){
			for(int i=1; i<=amount; i++){
				disease.add(new Disease(preferredTemperature, id, location, lifespan, age,
										habitability, birthTick, healthy, species));
			}
		}
	}
	public int listHerbivoresInTile(int tile){
		int amount = 0;
		for (int i = 0; i<herbivore.size(); i++){
			if(herbivore.get(i).location == tile){
				amount = amount + 1;
			}
		}
		return amount;
	}
	public int listCarnivoresInTile(int tile){
		int amount = 0;
		for (int i = 0; i<carnivore.size(); i++){
			if(carnivore.get(i).location == tile){
				amount = amount + 1;
			}
		}
		return amount;
	}
	public int listDiseasesInTile(int tile){
		int amount = 0;
		for (int i = 0; i<disease.size(); i++){
			if(disease.get(i).location == tile){
				amount = amount + 1;
			}
		}
		return amount;
	}
	public int listPlantsInTile(int tile){
		int amount = 0;
		for (int i = 0; i<plant.size(); i++){
			if(plant.get(i).location == tile){
				amount = amount + 1;
			}
		}
		return amount;
	}
	public void timeToExpire(){
		for (int i = 0; i<herbivore.size(); i++){
			if(plant.get(i).age >= plant.get(i).lifespan){
				plant.set(i,null);
			}
		}
		for (int i = 0; i<carnivore.size(); i++){
			if(carnivore.get(i).age >= carnivore.get(i).lifespan){
				carnivore.set(i,null);
			}
			
		}
		for (int i = 0; i<plant.size(); i++){
			if(herbivore.get(i).age >= herbivore.get(i).lifespan){
				herbivore.set(i,null);
			}
		}
	}
	
	public void setHabitabilities(Calc calc,ObjectsControl control, ArrayList<Tile> tiles){
//		HERBIVORES	
		for(int i = 0; i<herbivore.size(); i++){
			calc.setHabitability(control,tiles.get(control.getHerbivore().get(i).location).getTemperature(),control.getHerbivore().get(i), "Herbivore");
		}
//		CARNIVORES
		for(int i = 0; i<carnivore.size(); i++){
			calc.setHabitability(control,tiles.get(control.getCarnivore().get(i).location).getTemperature(),control.getCarnivore().get(i), "Carnivore");
		}
//		PLANTS
		for(int i = 0; i<plant.size(); i++){
			calc.setHabitability(control,tiles.get(control.getPlant().get(i).location).getTemperature(),control.getPlant().get(i), "Plant");
		}
	}

	public void populationControl(ObjectsControl control, Calc calc){
		double prey, predators,newpredator,newprey,plants,newplants;
		//a ratio that predators eat prey b is reproduction rate per predators c is ratio that herbivores eat plants and d is reproduction rate of herbivores per plants
		double a=0.2, b=0.001, c=0.4, d=0.002;
		for(int i=0;i<4;i++){
		prey=control.listHerbivoresInTile(i);
		predators=control.listCarnivoresInTile(i);
		plants=control.listPlantsInTile(i);
		newprey = ((a * prey)-(b*prey*predators))+plants*0.05;
		newpredator = (prey*predators*d)-(c*predators);
		newplants = ((a * prey)-(b*plants));
		

		
		if (newplants < plants){
			newplants = newplants * -1;
			if(plant.get(i).location==i){
				removeFromArray(control, i, (int)newplants, "Plant");
				}
		}
		if (newplants > plants && control.plant.size()<1000){
			control.createObjects("Plant", "plant", (int)(newplants), 3,control.getHerbivore().get(i).preferredTemperature, i, (int)calc.setLife("Plant") , 0, 0, 0, true);
		}
		
		if (newprey < prey){
			newprey = newprey * -1;
			if(herbivore.get(i).location==i){
				removeFromArray(control, i, (int)newprey, "Herbivore");
				}
			}
		if (newprey > prey && control.herbivore.size()<1000){
			control.createObjects("Herbivore", "prey", (int)(newprey), 2,control.getHerbivore().get(i).preferredTemperature, i, (int)calc.setLife("Herbivore") , 0, 0, 0, true);
		}
		if(newpredator < predators){
			newpredator = newpredator * -1;
			if(carnivore.get(i).location==i){
				removeFromArray(control, i, (int)newprey, "Carnivore");
				}
			}
		if(newpredator > predators && control.carnivore.size()<1000){
			control.createObjects("Carnivore", "predator", (int)(newpredator), 1,control.getCarnivore().get(i).preferredTemperature, i, (int)calc.setLife("Carnivore") , 0, 0, 0, true);
			}
		
		if(newpredator == predators){
			control.createObjects("Carnivore", "predator", 3, 2,control.getCarnivore().get(i).preferredTemperature, i, (int)calc.setLife("Carnivore") , 0, 0, 0, true);
		}
		if(newprey == prey){
			control.createObjects("Herbivore", "prey", 3, 2,control.getHerbivore().get(i).preferredTemperature, i, (int)calc.setLife("Herbivore") , 0, 0, 0, true);
		}
		if(newplants == plants){
			control.createObjects("Plant", "plant", 3, 2,control.getHerbivore().get(i).preferredTemperature, i, (int)calc.setLife("Plant") , 0, 0, 0, true);
		}
		if(control.carnivore.size()>2500){
			
		}
		}
	}		
	
	
	public void removeDeadObjects(ObjectsControl control){
		for (int i = 0; i<carnivore.size(); i++){
			if(carnivore.get(i).age >= carnivore.get(i).lifespan){
				carnivore.remove(i);
			}
		}
		
		for (int i = 0; i<herbivore.size(); i++){
			if(herbivore.get(i).age >= herbivore.get(i).lifespan){
				herbivore.remove(i);
			}
		}
		
		for (int i = 0; i<plant.size(); i++){
			if(plant.get(i).age >= plant.get(i).lifespan){
				plant.remove(i);
			}
		}
		
	}
	public void age(ObjectsControl control,Calc calc){
//		HERBIVORES	
		for(int i = 0; i<herbivore.size(); i++){
		calc.setAge(control.getHerbivore().get(i).age, control.getHerbivore().get(i));
		}
//		CARNIVORES
		for(int i = 0; i<carnivore.size(); i++){
		
		}
//		PLANTS
		for(int i = 0; i<plant.size(); i++){
		
		}
	}
	public void move(ObjectsControl control, Calc calc, ArrayList<Tile> tiles) {
		int n;
		int k=2;
//		HERBIVORES	
		for(int i = 0; i<herbivore.size(); i++){
			n=control.getHerbivore().get(i).location;
			if (n%k<0){//left tile exists
				if (calc.getHabitability(n-1, control, control.getHerbivore().get(i), tiles, "Herbivore")>control.getHerbivore().get(i).habitability) {
					calc.setLocation(n-1, control.getHerbivore().get(i));
				}
			}
			if(n%k<k-1){//right tile exists
				if (calc.getHabitability(n+1, control, control.getHerbivore().get(i), tiles, "Herbivore")>control.getHerbivore().get(i).habitability) {
					calc.setLocation(n+1, control.getHerbivore().get(i));
				}
			}
			if (n>=k){//top tile exists
				if (calc.getHabitability(n-k, control, control.getHerbivore().get(i), tiles, "Herbivore")>control.getHerbivore().get(i).habitability) {
					calc.setLocation(n-k, control.getHerbivore().get(i));
				}
			}
			if (n<k*(k-1)){//bot tile exists
				if (calc.getHabitability(n+k, control, control.getHerbivore().get(i), tiles, "Herbivore")>control.getHerbivore().get(i).habitability) {
					calc.setLocation(n+k, control.getHerbivore().get(i));
				}
			}
		}
//		CARNIVORES
		for(int i = 0; i<carnivore.size(); i++){
			n=control.getCarnivore().get(i).location;
			if (n%k<0){//left tile exists
				if (calc.getHabitability(n-1, control, control.getCarnivore().get(i), tiles, "Carnivore")>control.getCarnivore().get(i).habitability) {
					calc.setLocation(n-1, control.getCarnivore().get(i));
				}
			}
			if(n%k<k-1){//right tile exists
				if (calc.getHabitability(n+1, control, control.getCarnivore().get(i), tiles, "Carnivore")>control.getCarnivore().get(i).habitability) {
					calc.setLocation(n+1, control.getCarnivore().get(i));
				}
			}
			if (n>=k){//top tile exists
				if (calc.getHabitability(n-k, control, control.getCarnivore().get(i), tiles, "Carnivore")>control.getCarnivore().get(i).habitability) {
					calc.setLocation(n-k, control.getCarnivore().get(i));
				}
			}
			if (n<k*(k-1)){//bot tile exists
				if (calc.getHabitability(n+k, control, control.getCarnivore().get(i), tiles, "Carnivore")>control.getCarnivore().get(i).habitability) {
					calc.setLocation(n+k, control.getCarnivore().get(i));
				}
			}
		}
		
		/*
		 * if (n%k<0), left tile exists
		 * if (n%k<k-1), right tile -
		 * if (n>=k) top
		 * if (n<k*(k-1)) bot
		*/
		
		
	}
    public void removeFromArray(ObjectsControl control, int location, int amount, String className){
        int x = 0;
        switch (className) {
        case "Herbivore":  
           Iterator<Herbivore> herb =herbivore.iterator();
           x=0;
           while(x<=amount && herb.hasNext()){
               
               if (herb.next().location==location){
                   x++;
                   herb.remove();
               }
               
        }

        case "Carnivore":  
             Iterator<Carnivore> carni = carnivore.iterator();
             x=0;     
             while(x<=amount && carni.hasNext()){
                	  if (carni.next().location==location){
                		  x++;
                		  carni.remove();
               }
        }
   
        case "Plant":  
            Iterator<Plant> plants = plant.iterator();
            x=0; 
            while(x<=amount && plants.hasNext()){
                    	if (plants.next().location==location){
                    		x++;
                    		plants.remove();
}}}}}