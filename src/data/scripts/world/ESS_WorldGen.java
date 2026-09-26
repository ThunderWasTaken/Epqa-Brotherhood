package data.scripts.world;

import com.fs.starfarer.api.campaign.SectorAPI;
import com.fs.starfarer.api.campaign.SectorGeneratorPlugin;
import data.scripts.world.systems.ESS_ExampleSystem;

public class ESS_WorldGen implements SectorGeneratorPlugin {
    //this script will be used to do campaign generation for this mod. typically used to set up faction relationships, spawn multiple systems etc.
    @Override
    public void generate(SectorAPI sector) {
        new ESS_ExampleSystem().generate(sector);
    }
}
