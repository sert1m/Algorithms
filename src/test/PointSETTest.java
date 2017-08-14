package test;

import kdtrees.PointSET;
import kdtrees.PointStorage;

public class PointSETTest extends PointStorageTest {
    @Override
    protected PointStorage getPointStorageToTest() {
        return new PointSET();
    }
}
