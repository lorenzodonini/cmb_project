package tum_model;

import core.Coord;
import core.Settings;
import movement.Path;
import movement.TumCharacter;

import java.util.Random;

public class MainHallState implements IState {
    private Coord upperLeftCorner;
    private Coord lowerRightCorner;
    private Random randomGenerator;

    private static final String SETTINGS_UPPER_LEFT = "hallUpperLeft";
    private static final String SETTINGS_SIZE = "hallSize";
    private static final String SCENARIO_NAMESPACE = "Scenario";

    //DEFAULT CTOR
    public MainHallState(final Settings settings) {
        settings.setNameSpace(SCENARIO_NAMESPACE);
        double [] upperLeft = settings.getCsvDoubles(SETTINGS_UPPER_LEFT);
        double [] size = settings.getCsvDoubles(SETTINGS_SIZE);
        settings.restoreNameSpace();

        //Defining a size for the main hall
        upperLeftCorner = new Coord(upperLeft[0], upperLeft[1]);
        lowerRightCorner = new Coord(upperLeftCorner.getX() + size[0], upperLeftCorner.getY() + size[1]);
        randomGenerator = new Random();
    }

    @Override
    public void enterState(TumCharacter character) {
        //Do nothing here
    }

    public Path getPathForCharacter(TumCharacter character) {
        final Path p = new Path(character.getDefaultSpeed());
        Coord coord;

        p.addWaypoint(character.getLastLocation());

        do {
            coord = new Coord(randomGenerator.nextDouble() * lowerRightCorner.getX(), randomGenerator.nextDouble() * lowerRightCorner.getY());
        } while (!FmiBuilding.getInstance().isInMainHall(coord));
        p.addWaypoint(coord);
        return p;
    }

    public double getPauseTimeForCharacter(TumCharacter character) {
        double minutes = 0;
        double time = character.getTimeUntilNextLecture();
        if (time <= 0) {
            time = 0;
        }
        else {
            time = Math.max(time - 5*60, 0);
        }
        switch (character.getCurrentAction()) {
            case EAT:
                minutes = 15;
                break;
            case INDIVIDUAL_STUDY:
                minutes = 30;
                break;
            case GROUP_STUDY:
                minutes = 20;
                break;
            case SOCIAL:
                minutes = 5;
                break;
            default:
                minutes = 0;
                break;
        }
        return Math.min(minutes * 60, time);
    }

    @Override
    public void exitState(TumCharacter character) {
        //Do nothing here
    }
}

