
import java.util.ArrayList;
import java.util.Scanner;

public class LinkState {
		
	public static void dijkstra(int weights[][], int src, ArrayList<Integer> destinations, int totalN) {
				
		int[] shortest_dist = new int[totalN]; //dist of shortest path to each vertex
		int[] next_hop = new int[totalN]; // what path is
		int[] hop_before = new int[totalN];
		boolean visited[] = new boolean[totalN];
		ArrayList<Integer> checked = new ArrayList<>(); //set of vertices already checked
		
		
		//Initialize
		for(int i = 0; i < totalN; i++) {
			shortest_dist[i] = 0;
			next_hop[i] = -1;
			visited[i] = false;
		}
		
		//Initialize
		shortest_dist[src] = 0;
		next_hop[src] = 0;
		visited[src] = true;
		checked.add(src + 1);  
		
		int link_dist;
		int temp_path;
		
		ArrayList<Integer> hops_from_src = new ArrayList<>();
		
		for(int n = 0; n < totalN; n++) {
			int current_node = checked.get(n);
			current_node = current_node -1;
			for(int i = 0; i < totalN; i++) {
				if(i != src && current_node != i) {
					link_dist = weights[current_node][i];
					if(link_dist != -1) {
						if(shortest_dist[i] != 0)
						{
							if(shortest_dist[i] != (link_dist + shortest_dist[current_node])) {
								shortest_dist[i] = Math.min(shortest_dist[i], (link_dist + shortest_dist[current_node]));
								if(shortest_dist[i] == (link_dist+shortest_dist[current_node])){ //path through current node
									if(hops_from_src.contains(current_node + 1)) {
										next_hop[i] = current_node +1;
									}
								} 
							}
						} else {
							if(n == 0) {
								hops_from_src.add(i+1);
								shortest_dist[i]= link_dist;
								next_hop[i] = i + 1;
							}
							else {
								shortest_dist[i] = link_dist + shortest_dist[current_node];

								if(hops_from_src.contains(current_node + 1)) {
									next_hop[i] = current_node + 1;
								} else {
									if(hops_from_src.contains(next_hop[current_node])){
										next_hop[i] = next_hop[current_node];
									}
									
								}
														
							}
						}
					}
				}
			}
			int temp_min = Integer.MAX_VALUE;
			int min_node = 0;
			for(int d = 0; d < shortest_dist.length; d++) {
				if (shortest_dist[d] != 0){
					if(shortest_dist[d] < temp_min) {
						if(checked.contains(d+1) == false) {
							temp_min = shortest_dist[d];
							min_node = d;
						}
					}
				} 
			}
			checked.add(min_node+1);

		}
		
		printForwardTable(totalN, src, shortest_dist, next_hop, destinations);			
	}


	private static void printForwardTable(int nodes, int src, int[] shortest_dist, int[] next_hop,  ArrayList<Integer> destinations) {
		for(int n = 0; n < nodes; n++) {
			if (next_hop[n] == -1) {
				shortest_dist[n] = -1;
			}
		}
		
		System.out.println("Forwarding Table for " + (src + 1));
		System.out.println("To     Cost     Next Hop");
		for(int i = 0; i < destinations.size(); i++) {
			System.out.println((destinations.get(i)) + "      " + shortest_dist[(destinations.get(i) - 1)] + "        " + next_hop[(destinations.get(i) - 1)]);;
			
		}
		
	}


	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		
		int totalNodes = in.nextInt();
		System.out.println("Total nodes: " + totalNodes);
		int[][] weights = new int[totalNodes][totalNodes];
		for (int i = 0; i < totalNodes; i++) {
			for(int j = 0; j < totalNodes; j++) {
				weights[i][j] = in.nextInt();
			}
		}
		
		ArrayList<Integer> destinations = new ArrayList<>();
		while (in.hasNext()) {
			destinations.add(in.nextInt());
		}
		
		boolean to_run = true;
		for(int n = 1; n < (totalNodes+1); n++) {
			to_run = true;
			for(int i = 0; i < destinations.size(); i++) {
				if(n == destinations.get(i)) { //does not run for gateway routers
					to_run = false;
					break;
				}
			}
			if (to_run == true) {
				dijkstra(weights, n-1, destinations, totalNodes);
			}
		}		
		
	}
}
