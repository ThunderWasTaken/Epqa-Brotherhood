package data.scripts;

import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.Global;
import data.scripts.world.ESS_WorldGen;

public class ESSModPlugin extends BaseModPlugin { //very basic mod plugin used to generate the system when a new game is created
    @Override
    public void onNewGame() {
        new ESS_WorldGen().generate(Global.getSector());
    }
}
