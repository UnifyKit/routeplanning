package routeplanning;
/**
 * RoutePlanning.
 * 
 */
public class RoutePlanning {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        RoadNetwork rn = new RoadNetwork();
        
        //ReduceFileSize rfs = new ReduceFileSize("D:/workspace/routeplanning/src/routeplanning/resources/test2.osm", "D:/workspace/routeplanning/src/routeplanning/resources/test2_reduced.osm");
        //rfs.process();
        //rn.readFromOsmFile2("D:/workspace/routeplanning/src/routeplanning/resources/saarland_reduced.osm");
        //rn.readFromOsmFile();
        rn.readFromOsmFile2("D:/workspace/routeplanning/src/routeplanning/resources/saarland_reduced.osm");
        //System.out.println(rn.asString());
        //readOSMFile rf = new readOSMFile("D:/workspace/routeplanning/src/routeplanning/resources/saarland.osm");
        //rf.processFile();
        //System.out.println("adjacentArcs size: "+rn.adjacentArcs.size());
        //System.out.println("ROAD NETWORK: " + rn.asString());
    }
}
