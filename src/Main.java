import java.util.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        // 1. Lokasyonları (Düğümleri) Oluştur
        Location aksaray = new Location("Aksaray");
        Location konya = new Location("Konya");
        Location antalya = new Location("Antalya");
        Location ankara = new Location("Ankara");

        // 2. Yolları ve Mesafeleri (Kenarları) Ekle
aksaray.setTrafficDelays(konya,1000);
        // Aksaray - Konya arası bağlantı (Çift yönlü)
        aksaray.addDestination(konya, 150);
        konya.addDestination(aksaray, 150);

        // Konya - Antalya arası bağlantı (Çift yönlü)
        konya.addDestination(antalya, 320);
        antalya.addDestination(konya, 320);

        // Aksaray - Ankara arası bağlantı (Çift yönlü)
        aksaray.addDestination(ankara, 225);
        ankara.addDestination(aksaray, 225);

        // Ankara - Antalya arası doğrudan uçuş/özel yol varsayımı (Çift yönlü)
        ankara.addDestination(antalya, 480);
        antalya.addDestination(ankara, 480);

        // 3. Haritayı (Graph) Oluştur ve Düğümleri Ekle
        RouteMap harita = new RouteMap();
        harita.addLocation(aksaray);
        harita.addLocation(konya);
        harita.addLocation(antalya);
        harita.addLocation(ankara);

        // 4. Konsola Yazdırarak Test Et
        harita.printMap();
        harita.calculateShortestPath(aksaray);
        harita.printPath(antalya);
    }
}
class Location implements Comparable<Location>{
    private Map<Location,Integer> trafficDelays;
    @Override
    public int compareTo(Location other){
        return Integer.compare(this.shortestDistance, other.shortestDistance);
    }
    private String name;
    private Map<Location,Integer> adjacentLocations;
    private int shortestDistance=Integer.MAX_VALUE;
    private Location previousLocation=null;
    public Location(String name){
        this.name=name;
        adjacentLocations= new HashMap<>();
        trafficDelays=new HashMap<>();
    }
    public void addDestination(Location destination,int distance){
        adjacentLocations.put(destination,distance);
    }
    public String getName(){
        return name;
    }
    public Map<Location,Integer> getAdjacentLocations(){
        return adjacentLocations;
    }
    public int getShortestDistance(){
        return shortestDistance;
    }
    public void setShortestDistance(int a){
        shortestDistance=a;
    }
    public Location getPreviousLocation(){
        return previousLocation;
    }
    public void setPreviousLocation(Location b){
        previousLocation=b;
    }
    public void setTrafficDelays(Location destination, int delayPenalty){
        trafficDelays.put(destination,delayPenalty);
    }
    public int getTrafficDelay(Location destination){
       return trafficDelays.getOrDefault(destination,0);
    }

}
class RouteMap{
   private Set<Location> nodes;
   public RouteMap(){
       nodes=new HashSet<>();
   }
   public void addLocation(Location loc){
       nodes.add(loc);
   }
   public void printMap(){
       for(Location l:nodes){
           System.out.print(l.getName()+" connected with: ");
           if(l.getAdjacentLocations().isEmpty()){
               System.out.println("There is no connection.");
           }else{
               for(Map.Entry<Location,Integer> way:l.getAdjacentLocations().entrySet()){
                        Location target= way.getKey();
                        int distance= way.getValue();
                        System.out.print("["+target.getName()+"->"+distance+"km]");

               }
           }
           System.out.println();
       }

   }
   public void calculateShortestPath(Location startNode){
       startNode.setShortestDistance(0);
       PriorityQueue<Location> unvisitedNodes=new PriorityQueue<>();
       unvisitedNodes.add(startNode);
       while(!unvisitedNodes.isEmpty()){
          Location current=unvisitedNodes.poll();
          for(Map.Entry<Location,Integer> l:current.getAdjacentLocations().entrySet()){
              Location target=l.getKey();

              int TotalEdgeCost=l.getValue()+current.getTrafficDelay(target);
              int newDistance= current.getShortestDistance()+TotalEdgeCost;
              if(newDistance<target.getShortestDistance()){
                  target.setShortestDistance(newDistance);
                  target.setPreviousLocation(current);
                  System.out.println(target.getName()+" came from "+current.getName());
                  unvisitedNodes.add(target);
              }
          }
       }
   }
   public void printPath(Location targetNode){
       if(targetNode.getShortestDistance()==Integer.MAX_VALUE){
           return;
       }else{
           System.out.println("target: "+ targetNode.getName()+" Total Distance: "+targetNode.getShortestDistance());
           ArrayList<Location> list=new ArrayList<>();
           Location step=targetNode;
           while(step!=null) {
               list.add(step);
               step = step.getPreviousLocation();
           }
               Collections.reverse(list);
           System.out.print("Route:");
               for(Location l:list){
                   System.out.print(l.getName()+"->");
               }
               System.out.println("\n");
           }

   }
}
