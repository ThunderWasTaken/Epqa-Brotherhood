//"Yo Vexlia can I copy your homework" -Thunder
//"Sure, just change it a bit so it doesn't look the same" -Vexlia
package data.scripts;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.PersonImportance;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.characters.FullName.Gender;
import com.fs.starfarer.api.characters.ImportantPeopleAPI;
import com.fs.starfarer.api.characters.MutableCharacterStatsAPI;
import com.fs.starfarer.api.characters.PersonAPI;
import com.fs.starfarer.api.impl.campaign.ids.*;
import org.apache.log4j.Logger;

import java.util.List;

import static com.fs.starfarer.api.campaign.AICoreOfficerPlugin.*;
import static com.fs.starfarer.api.impl.campaign.AICoreOfficerPluginImpl.ALPHA_MULT;

public class epqa_people {

    // Epqa Brotherhood
    public static String EPQA_EPSILON = "epqa_epsilon";

    // Add more characters above me, don't forget!!!!

    public static Logger log = Global.getLogger(epqa_people.class);

    public static PersonAPI getPerson(String id) {
        return Global.getSector().getImportantPeople().getPerson(id);
    }

    public static void create() {
        createEpqa_people();

    }

    public static void createEpqa_people() {
        ImportantPeopleAPI ip = Global.getSector().getImportantPeople();
        MarketAPI market = null;

        // Epsilon Star, the man behind the plans.
        // Incredibly skilled and intelligent, there is more to him than one may expect.
        market = Global.getSector().getEconomy().getMarket("epqa_epqa_planet_market");
        if (market != null) {
            PersonAPI epqa_epsilon_person = Global.getFactory().createPerson();
            epqa_epsilon_person.setId(EPQA_EPSILON);
            epqa_epsilon_person.setFaction("EpqaBrotherhood");
            epqa_epsilon_person.setGender(Gender.MALE);
            epqa_epsilon_person.setRankId(Ranks.FACTION_LEADER);
            epqa_epsilon_person.setPostId(Ranks.FACTION_LEADER);
            epqa_epsilon_person.setImportance(PersonImportance.VERY_HIGH);
            epqa_epsilon_person.getName().setFirst("Epsilon");
            epqa_epsilon_person.getName().setLast("Star");
            epqa_epsilon_person.setPortraitSprite(Global.getSettings().getSpriteName("characters", "epqa_epsilon"));

            /* Skills */
            epqa_epsilon_person.getStats().setSkillLevel(Skills.INDUSTRIAL_PLANNING, 1);

            //(COMING SOON)person.getMemoryWithoutUpdate().set("$chatterChar", "sotf_sierra");

            if (!ip.containsPerson(epqa_epsilon_person)) {
                log.info("epqa_people: Epsilon Star did not exist. Has been generated retroactively");
                ip.addPerson(epqa_epsilon_person);
                market.setAdmin(epqa_epsilon_person);
                market.getCommDirectory().addPerson(epqa_epsilon_person, 0);
                market.addPerson(epqa_epsilon_person);
            } else
                log.info("epqa_people: Epsilon Star already exists. No action taken");
        }
    }
}