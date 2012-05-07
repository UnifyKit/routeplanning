package routeplanning;
/**
 * RoutePlanning.
 * 
 */
public class MainClass {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //ReduceFileSize rfs = new ReduceFileSize("D:/workspace/routeplanning/src/routeplanning/resources/test2.osm", "D:/workspace/routeplanning/src/routeplanning/resources/test2_reduced.osm");
        //rfs.process();
    	RoadNetwork rn = new RoadNetwork();
        //rn.readFromOsmFile();
        rn.readFromOsmFile2("D:/workspace/routeplanning/src/routeplanning/resources/saarland_reduced.osm");
        //System.out.println("ROAD NETWORK: " + rn.asString());
    }
}
