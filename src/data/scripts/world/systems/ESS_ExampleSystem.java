package data.scripts.world.systems;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.*;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.impl.campaign.ids.*;
import com.fs.starfarer.api.impl.campaign.procgen.NebulaEditor;
import com.fs.starfarer.api.impl.campaign.procgen.StarAge;
import com.fs.starfarer.api.impl.campaign.procgen.StarSystemGenerator;
import com.fs.starfarer.api.impl.campaign.terrain.HyperspaceTerrainPlugin;
import com.fs.starfarer.api.util.Misc;
import org.lwjgl.util.vector.Vector2f;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class ESS_ExampleSystem implements SectorGeneratorPlugin { //A SectorGeneratorPlugin is a class from the game, that identifies this as a script that will have a 'generate' method
    @Override
    public void generate(SectorAPI sector) { //the parameter sector is passed. This is the instance of the campaign map that this script will add a star system to
        //initialise system
        StarSystemAPI system = sector.createStarSystem("Qaidis"); //create a new variable called system. this is assigned an instance of the new star system added to the Sector at the same time
        system.getLocation().set(15000, 1000); //sets location of system in hyperspace. map size is in the order of 100000x100000, and 0, 0 is the center of the map, this will set the location to the east and slightly south of the center
        system.setBackgroundTextureFilename("graphics/backgrounds/background1.jpg"); //sets the background image for when in the system. this is a filepath to an image in the core game files

        //set up star
        PlanetAPI star = system.initStar( //stars and planets are technically the same category of object, so stars use PlanetAPI
                "eqpa_qaidis", //set star id, this should be unique
                "star_orange", //set star type, the type IDs come from starsector-core/data/campaign/procgen/star_gen_data.csv
                900, //set radius, 900 is a typical radius size
                15000, //sets the location of the star's one-way jump point in hyperspace, since it is the center of the star system, we want it to be in the center of the star system jump points in hyperspace
                1000,
                900 //radius of corona terrain around star
        );

        //generate up to three entities in the centre of the system and returns the orbit radius of the furthest entity
        float innerOrbitDistance = StarSystemGenerator.addOrbitingEntities(
                system, //star system variable, used to add entities
                star, //focus object for entities to orbit
                StarAge.AVERAGE, //used by generator to decide which kind of planets to add
                0, //minimum number of entities
                0, //maximum number of entities
                4000, //the radius between the first generated entity and the focus object, in this case the star
                1, //used to assign roman numerals to the generated entities if not given special names
                true //generator will give unique names like "Ordog" instead of "Example Star System III"
        );

        //add first planet
        PlanetAPI epqaplanet = system.addPlanet( //assigns instance of newly created planet to variable planetOne
                "epqa_epqa_planet", //unique id string
                star, //orbit focus for planet
                "Epqa", //display name of planet
                "tundra", //planet type id, comes from starsector-core/data/campaign/procgen/planet_gen_data.csv
                30f, //starting angle in orbit
                120f, //planet size
                7000, //100 radius gap from the outer randomly generated entity created above
                365 //number of in-game days for it to orbit once
        );

        PlanetAPI forgeplanet = system.addPlanet( //assigns instance of newly created planet to variable planetOne
                "epqa_forge_planet", //unique id string
                star, //orbit focus for planet
                "Forge", //display name of planet
                "lava", //planet type id, comes from starsector-core/data/campaign/procgen/planet_gen_data.csv
                100f, //starting angle in orbit
                110f, //planet size
                4500, //100 radius gap from the outer randomly generated entity created above
                180 //number of in-game days for it to orbit once

        );

        PlanetAPI nebulaeplanet = system.addPlanet( //assigns instance of newly created planet to variable planetOne
                "epqa_nebulae_planet", //unique id string
                star, //orbit focus for planet
                "Nebulae", //display name of planet
                "frozen", //planet type id, comes from starsector-core/data/campaign/procgen/planet_gen_data.csv
                120f, //starting angle in orbit
                130f, //planet size
                8500, //100 radius gap from the outer randomly generated entity created above
                565 //number of in-game days for it to orbit once

        );

        PlanetAPI militaplanet = system.addPlanet( //assigns instance of newly created planet to variable planetOne
                "epqa_Milita_planet", //unique id string
                star, //orbit focus for planet
                "Milita", //display name of planet
                "barren", //planet type id, comes from starsector-core/data/campaign/procgen/planet_gen_data.csv
                10f, //starting angle in orbit
                100f, //planet size
                3000, //100 radius gap from the outer randomly generated entity created above
                180 //number of in-game days for it to orbit once

        );

        PlanetAPI holerockplanet = system.addPlanet( //assigns instance of newly created planet to variable planetOne
                "epqa_holerock_planet", //unique id string
                star, //orbit focus for planet
                "Holerock", //display name of planet
                "barren-bombarded", //planet type id, comes from starsector-core/data/campaign/procgen/planet_gen_data.csv
                10f, //starting angle in orbit
                110f, //planet size
                2000, //100 radius gap from the outer randomly generated entity created above
                120 //number of in-game days for it to orbit once

        );

        //use helper method from other script to easily configure the market. feel free to copy it into your own project
        MarketAPI EpqaMarket = ESS_AddMarketplace.addMarketplace( //A Market is separate to a Planet, and contains data about population, industries and conditions. This is a method from the other script in this mod, that will assign all marketplace conditions to the planet in one go, making it simple and easy
                "EpqaBrotherhood", //Factions.INDEPENDENT references the id String of the Independent faction, so it is the same as writing "independent", but neater. This determines the Faction associated with this market
                epqaplanet, //the PlanetAPI variable that this market will be assigned to
                null, //some mods and vanilla will have additional floating space stations or other entities, that when accessed, will open this marketplace. We don't have any associated entities for this method to add, so we leave null
                "Epqa", //Display name of market
                7, //population size
                new ArrayList<>(Arrays.asList( //List of conditions for this method to iterate through and add to the market
                        Conditions.POPULATION_7,
                        Conditions.FARMLAND_RICH,
                        Conditions.ORE_MODERATE,
                        Conditions.HABITABLE,
                        Conditions.COLD
                )),
                new ArrayList<>(Arrays.asList( //list of submarkets for this method to iterate through and add to the market. if a military base industry was added to this market, it would be consistent to add a military submarket too
                        Submarkets.SUBMARKET_OPEN, //add a default open market
                        Submarkets.SUBMARKET_STORAGE, //add a player storage market
                        Submarkets.SUBMARKET_BLACK //add a black market
                )),
                new ArrayList<>(Arrays.asList( //list of industries for this method to iterate through and add to the market
                        Industries.POPULATION, //population industry is required for weirdness to not happen
                        Industries.MEGAPORT, //same with spaceport
                        Industries.WAYSTATION,
                        Industries.FARMING,
                        Industries.COMMERCE,
                        Industries.HEAVYBATTERIES,
                        Industries.PATROLHQ,
                        Industries.BATTLESTATION_HIGH
                )),
                true, //if true, the planet will have visual junk orbiting and will play an ambient chatter audio track when the player is nearby
                false //used by the method to make a market hidden like a pirate base, not recommended for generating markets in a core world
        );

        //use helper method from other script to easily configure the market. feel free to copy it into your own project
        MarketAPI ForgeMarket = ESS_AddMarketplace.addMarketplace( //A Market is separate to a Planet, and contains data about population, industries and conditions. This is a method from the other script in this mod, that will assign all marketplace conditions to the planet in one go, making it simple and easy
                "EpqaBrotherhood", //Factions.INDEPENDENT references the id String of the Independent faction, so it is the same as writing "independent", but neater. This determines the Faction associated with this market
                forgeplanet, //the PlanetAPI variable that this market will be assigned to
                null, //some mods and vanilla will have additional floating space stations or other entities, that when accessed, will open this marketplace. We don't have any associated entities for this method to add, so we leave null
                "Forge", //Display name of market
                5, //population size
                new ArrayList<>(Arrays.asList( //List of conditions for this method to iterate through and add to the market
                        Conditions.POPULATION_5,
                        Conditions.RARE_ORE_ABUNDANT,
                        Conditions.ORE_ABUNDANT,
                        Conditions.LOW_GRAVITY,
                        Conditions.HOT
                )),
                new ArrayList<>(Arrays.asList( //list of submarkets for this method to iterate through and add to the market. if a military base industry was added to this market, it would be consistent to add a military submarket too
                        Submarkets.SUBMARKET_OPEN, //add a default open market
                        Submarkets.SUBMARKET_STORAGE, //add a player storage market
                        Submarkets.SUBMARKET_BLACK //add a black market
                )),
                new ArrayList<>(Arrays.asList( //list of industries for this method to iterate through and add to the market
                        Industries.POPULATION, //population industry is required for weirdness to not happen
                        Industries.SPACEPORT, //same with spaceport
                        Industries.WAYSTATION,
                        Industries.MINING,
                        Industries.REFINING,
                        Industries.ORBITALWORKS,
                        Industries.HEAVYBATTERIES,
                        Industries.ORBITALSTATION
                )),
                true, //if true, the planet will have visual junk orbiting and will play an ambient chatter audio track when the player is nearby
                false //used by the method to make a market hidden like a pirate base, not recommended for generating markets in a core world
        );

        var industry = ForgeMarket.getIndustry(Industries.ORBITALWORKS); //you'll need to add the industries
        industry.setSpecialItem(new SpecialItemData("corrupted_nanoforge", "")); //then this adds the items

        //use helper method from other script to easily configure the market. feel free to copy it into your own project
        MarketAPI NebulaeMarket = ESS_AddMarketplace.addMarketplace( //A Market is separate to a Planet, and contains data about population, industries and conditions. This is a method from the other script in this mod, that will assign all marketplace conditions to the planet in one go, making it simple and easy
                "EpqaBrotherhood", //Factions.INDEPENDENT references the id String of the Independent faction, so it is the same as writing "independent", but neater. This determines the Faction associated with this market
                nebulaeplanet, //the PlanetAPI variable that this market will be assigned to
                null, //some mods and vanilla will have additional floating space stations or other entities, that when accessed, will open this marketplace. We don't have any associated entities for this method to add, so we leave null
                "Nebulae", //Display name of market
                4, //population size
                new ArrayList<>(Arrays.asList( //List of conditions for this method to iterate through and add to the market
                        Conditions.POPULATION_4,
                        Conditions.VOLATILES_PLENTIFUL,
                        Conditions.ORE_MODERATE,
                        Conditions.VERY_COLD
                )),
                new ArrayList<>(Arrays.asList( //list of submarkets for this method to iterate through and add to the market. if a military base industry was added to this market, it would be consistent to add a military submarket too
                        Submarkets.SUBMARKET_OPEN, //add a default open market
                        Submarkets.SUBMARKET_STORAGE, //add a player storage market
                        Submarkets.SUBMARKET_BLACK //add a black market
                )),
                new ArrayList<>(Arrays.asList( //list of industries for this method to iterate through and add to the market
                        Industries.POPULATION, //population industry is required for weirdness to not happen
                        Industries.SPACEPORT, //same with spaceport
                        Industries.MINING,
                        Industries.FUELPROD,
                        Industries.GROUNDDEFENSES
                )),
                true, //if true, the planet will have visual junk orbiting and will play an ambient chatter audio track when the player is nearby
                false //used by the method to make a market hidden like a pirate base, not recommended for generating markets in a core world
        );

        //use helper method from other script to easily configure the market. feel free to copy it into your own project
        MarketAPI MilitaMarket = ESS_AddMarketplace.addMarketplace( //A Market is separate to a Planet, and contains data about population, industries and conditions. This is a method from the other script in this mod, that will assign all marketplace conditions to the planet in one go, making it simple and easy
                "EpqaBrotherhood", //Factions.INDEPENDENT references the id String of the Independent faction, so it is the same as writing "independent", but neater. This determines the Faction associated with this market
                militaplanet, //the PlanetAPI variable that this market will be assigned to
                null, //some mods and vanilla will have additional floating space stations or other entities, that when accessed, will open this marketplace. We don't have any associated entities for this method to add, so we leave null
                "Milita", //Display name of market
                3, //population size
                new ArrayList<>(Arrays.asList( //List of conditions for this method to iterate through and add to the market
                        Conditions.POPULATION_3,
                        Conditions.RARE_ORE_MODERATE,
                        Conditions.ORE_MODERATE,
                        Conditions.LOW_GRAVITY,
                        Conditions.NO_ATMOSPHERE
                )),
                new ArrayList<>(Arrays.asList( //list of submarkets for this method to iterate through and add to the market. if a military base industry was added to this market, it would be consistent to add a military submarket too
                        Submarkets.SUBMARKET_OPEN, //add a default open market
                        Submarkets.SUBMARKET_STORAGE, //add a player storage market
                        Submarkets.GENERIC_MILITARY,
                        Submarkets.SUBMARKET_BLACK //add a black market
                )),
                new ArrayList<>(Arrays.asList( //list of industries for this method to iterate through and add to the market
                        Industries.POPULATION, //population industry is required for weirdness to not happen
                        Industries.MEGAPORT, //same with spaceport
                        Industries.GROUNDDEFENSES,
                        Industries.HIGHCOMMAND,
                        Industries.BATTLESTATION
                )),
                true, //if true, the planet will have visual junk orbiting and will play an ambient chatter audio track when the player is nearby
                false //used by the method to make a market hidden like a pirate base, not recommended for generating markets in a core world
        );

        holerockplanet.getMarket().addCondition(Conditions.METEOR_IMPACTS);
        holerockplanet.getMarket().addCondition(Conditions.ORE_MODERATE);
        holerockplanet.getMarket().addCondition(Conditions.RARE_ORE_ABUNDANT);
        holerockplanet.getMarket().addCondition(Conditions.VERY_HOT);
        holerockplanet.getMarket().addCondition(Conditions.NO_ATMOSPHERE);

        //add an asteroid belt. asteroids are separate entities inside these, it will randomly distribute a defined number of them around the ring
        system.addAsteroidBelt(
                star, //orbit focus
                80, //number of asteroid entities
                innerOrbitDistance + 500, //orbit radius is 500 gap for outer randomly generated entity above
                255, //width of band
                190, //minimum and maximum visual orbit speeds of asteroids
                220,
                Terrain.ASTEROID_BELT, //ID of the terrain type that appears in the section above the abilities bar
                "Qaidis Asteroid Belt" //display name
        );

        //add a ring texture. it will go under the asteroid entities generated above
        system.addRingBand(star,
                "misc", //used to access band texture, this is the name of a category in settings.json
                "rings_asteroids0", //specific texture id in category misc in settings.json
                256f, //texture width, can be used for scaling shenanigans
                2,
                Color.white, //colour tint
                256f, //band width in game
                innerOrbitDistance + 500, //same as above
                200f,
                null,
                null
        );

        //add makeshift comm relay entity to system
        SectorEntityToken makeshiftRelay = system.addCustomEntity(
                "qaidis_makeshift_relay",
                "Qaidis System Relay",
                Entities.COMM_RELAY_MAKESHIFT,
                "EpqaBrotherhood"
        );
        //assign an orbit
        makeshiftRelay.setCircularOrbit(star, 270f, 4000f, 3500f); //assign an orbit

        //add domain sensor array
        SectorEntityToken sensorArray = system.addCustomEntity(
                "qaidis_makeshift_sensor",
                "Qaidis Sensor Array",
                Entities.SENSOR_ARRAY_MAKESHIFT,
                "EpqaBrotherhood"
        );
        //assign an orbit, point down ensures it rotates to point towards center while orbiting
        sensorArray.setCircularOrbitPointingDown(star, 90f, 5000f, 2500f);

        //domain nav buoy
        SectorEntityToken navBuoy = system.addCustomEntity(
                "qaidis_nav_buoy",
                "Qaidis Navigation Beacon",
                Entities.NAV_BUOY_MAKESHIFT,
                "EpqaBrotherhood"
        );
        //assign orbit
        navBuoy.setCircularOrbit(star, 180f, 3000f, 1500f);

        //autogenerate jump points that will appear in hyperspace and in system
        system.autogenerateHyperspaceJumpPoints(true, true);

        //the following is hyperspace cleanup code that will remove hyperstorm clouds around this system's location in hyperspace
        //don't need to worry about this, it's more or less copied from vanilla

        //set up hyperspace editor plugin
        HyperspaceTerrainPlugin hyperspaceTerrainPlugin = (HyperspaceTerrainPlugin) Misc.getHyperspaceTerrain().getPlugin(); //get instance of hyperspace terrain
        NebulaEditor nebulaEditor = new NebulaEditor(hyperspaceTerrainPlugin); //object used to make changes to hyperspace nebula

        //set up radiuses in hyperspace of system
        float minHyperspaceRadius = hyperspaceTerrainPlugin.getTileSize() * 2f; //minimum radius is two 'tiles'
        float maxHyperspaceRadius = system.getMaxRadiusInHyperspace();

        //hyperstorm-b-gone (around system in hyperspace)
        nebulaEditor.clearArc(system.getLocation().x, system.getLocation().y, 0, minHyperspaceRadius + maxHyperspaceRadius, 0f, 360f, 0.25f);
    }
}
