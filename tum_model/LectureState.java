package tum_model;

import core.Coord;
import movement.Path;
import movement.TumCharacter;

import java.util.List;

/**
 * Created by rober on 22-Nov-15.
 */
public class LectureState implements IState
{
    public LectureState() {
    }

    @Override
    public void enterState(TumCharacter character) {
        TumUtilities.printStateAccessDetails(character,true);
        character.attendNextLecture();
    }

    @Override
    public Path getPathForCharacter(TumCharacter character) {
        Coord lectureLocation = character.getCurrentLecture().getLectureRoom().getPosition();
        Coord lastLocation = character.getLastLocation();
        List<Coord> entryPath = FmiBuilding.getInstance().getEntryPath(lectureLocation);

        final Path p = new Path(character.getDefaultSpeed());
        p.addWaypoint(lastLocation);
        for(Coord point : entryPath) {
            p.addWaypoint(point);
        }
        p.addWaypoint(lectureLocation);
        return p;
    }

    @Override
    public double getPauseTimeForCharacter(TumCharacter character) {
        Lecture lecture = character.getCurrentLecture();
        return lecture.getEndTime() - lecture.getStartTime();
    }

    @Override
    public void exitState(TumCharacter character) {
        TumUtilities.printStateAccessDetails(character,false);
    }
}

