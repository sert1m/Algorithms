package test;

import kdtrees.KdTree;
import kdtrees.PointStorage;

public class KdTreeTest extends PointStorageTest{

    @Override
    protected PointStorage getPointStorageToTest() {
        return new KdTree();
    }
    
}
