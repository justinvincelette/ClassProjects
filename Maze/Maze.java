import java.awt.Color;
import java.util.HashMap;
import java.util.Iterator;
//import java.util.Random;


import javalib.worldimages.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javalib.colors.*;
import javalib.impworld.*;
import tester.*;

//represents the class MyPosn
class MyPosn extends Posn {
    //constructor for MyPosn
    MyPosn(int x, int y) {
        super(x, y);
    }
    //checks to see if this MyPosn is the same as 
    //the given
    public boolean equals(Object that) {
        if (that instanceof MyPosn) {
            MyPosn tthat = (MyPosn) that;
            return this.x == tthat.x &&
                    this.y == tthat.y;
        }
        else {
            return false;
        }
    }
    //creates a hashCode for this MyPosn
    public int hashCode() {
        return this.toString().hashCode();
    }
    //used in hashCode as well as
    //testing purposes
    public String toString() {
        return "(" + x + "," + y + ")";
    }
}

//represents the class Node
class Node {
    //declare variables
    MyPosn rep;
    ArrayList<Node> neighbor;
    //constructor for Node
    Node(MyPosn rep) {
        this.rep = rep;
        this.neighbor = new ArrayList<Node>();
    }
    //finds out the representative of this Node
    MyPosn find(HashMap<MyPosn, MyPosn> representative) {
        if (representative.get(this.rep).equals(this.rep)) {
            return this.rep;
        }
        else {
            return new Node(representative.get(this.rep)).find(representative);
        }
    }
    //checks if two nodes MyPosn are the same
    boolean sameNode(Node that) {
        return this.rep.equals(that.rep);
    }
}

//represents the class Edge
class Edge {
    //declare variables
    Node from;
    Node to;
    int weight;
    //constructor for Edge
    Edge(Node from, Node to, int weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }
}

//represents the class Graph
class Graph extends World {
    //static variables
    static int SIZE = 30;
    static int WIDTH = 20;
    static int HEIGHT = 20;
    //declare variables
    MyPosn current;

    ArrayList<ArrayList<Node>> allNodes = new ArrayList<ArrayList<Node>>(); //All Nodes

    HashMap<MyPosn, MyPosn> representative = new HashMap<MyPosn, MyPosn>();
    ArrayList<Edge> edgesInTree = new ArrayList<Edge>(); //All the edges in maze
    ArrayList<Edge> worklist = new ArrayList<Edge>();

    HashMap<MyPosn, Edge> cameFromEdge = new HashMap<MyPosn, Edge>();

    Node start; //the beginning Node with MyPosn(0,0)

    WorldImage answer = new CircleImage(new Posn(10, 10), 0, new Yellow()); //Will be the solution
    //will give answer it's value at the end of drawing of algorithm
    WorldImage answerHolder = new CircleImage(new Posn(10, 10), 0, new Yellow());
    //every path the user has gone to
    WorldImage visitedpath = new CircleImage(new Posn(10, 10), 0, new Yellow());
    boolean toggle; //turn the visitedpath on/off
    boolean b; //turn the bfs on/off
    boolean d; //turn the dfs on/off
    Deque<Node> alreadySeen = new Deque<Node>();
    WorldImage searching = new CircleImage(new Posn(10, 10), 0, new Yellow());

    //extra credit
    ArrayList<Posn> visited;
    int numberOfMoves = 0;
    int numberOfSearchMoves = 0;

    //constructor for Graph
    Graph() {
        current = new MyPosn(0, 0);
        visited = new ArrayList<Posn>();
        toggle = false;
    }
    //initialize the Nodes into an IList of Nodes
    void initializeNodes() {
        ArrayList<ArrayList<Node>> result = new ArrayList<ArrayList<Node>>();
        for (int i = 0; i < HEIGHT; i++) {
            ArrayList<Node> row = new ArrayList<Node>();
            for (int j = 0; j < WIDTH; j++) {
                row.add(new Node(new MyPosn(j, i)));
            }
            result.add(row);
        }
        this.allNodes = result;
    }
    //sets all Nodes representative to itself
    void initializeReps() {
        representative = new HashMap<MyPosn, MyPosn>();
        for (ArrayList<Node> r : allNodes) {
            for (Node n : r) {
                representative.put(n.rep, n.rep);
            }
        }
    }
    //initializes the Edges in sorted of ascending weight, 
    //but (randomly) assigned weights
    void initializeEdges() {
        ArrayList<Edge> result = new ArrayList<Edge>();
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                if (i == HEIGHT - 1 && j == WIDTH - 1) {
                    //do nothing
                }
                else if (j == WIDTH - 1) {
                    result.add(new Edge(allNodes.get(i).get(j), 
                            allNodes.get(i + 1).get(j),
                            (int) (Math.random() * 100 + 1)));
                }
                else if (i == HEIGHT - 1) {
                    result.add(new Edge(allNodes.get(i).get(j),
                            allNodes.get(i).get(j + 1),
                            (int) (Math.random() * 100 + 1)));
                }
                else {
                    result.add(new Edge(allNodes.get(i).get(j),
                            allNodes.get(i + 1).get(j),
                            (int) (Math.random() * 100 + 1)));
                    result.add(new Edge(allNodes.get(i).get(j), 
                            allNodes.get(i).get(j + 1), 
                            (int) (Math.random() * 100 + 1)));
                }
            }
        }
        //sorts the worklist in ascending edge weight
        Collections.sort(result, new ByNumber());
        this.worklist = result;
    }















    //the algorithm to create the maze
    void kruskal() {
        int i = 0;
        ArrayList<Edge> result = new ArrayList<Edge>();
        while (result.size() != WIDTH * HEIGHT - 1) {
            Edge e = this.worklist.get(i);
            Node n1 = e.from;
            Node n2 = e.to;
            if (n1.find(representative).equals(n2.find(representative))) {
                //do nothing will discard
            }
            else {
                result.add(e);
                union(representative, n1, n2);
            }
            i++;
        } this.edgesInTree = result;

    }
    //changes the representative of the Node
    void union(HashMap<MyPosn, MyPosn> hm, Node n1, Node n2) {
        hm.put(n1.find(hm), n2.rep);
    }









    //draws out the maze
    WorldImage drawTheMaze() {
        WorldImage world = new CircleImage(new Posn(10, 10), 0, new Yellow());
        WorldImage right = new CircleImage(new Posn(10, 10), 0, new Yellow());
        WorldImage left = new CircleImage(new Posn(10, 10), 0, new Yellow());
        WorldImage up = new CircleImage(new Posn(10, 10), 0, new Yellow());
        WorldImage down = new CircleImage(new Posn(10, 10), 0, new Yellow());

        WorldImage blueROW = new CircleImage(new Posn(10, 10), 0, new Yellow());
        WorldImage blueCOLUMN = new CircleImage(new Posn(10, 10), 0, new Yellow());

        for (Edge e : edgesInTree) {
            if (e.from.rep.x + 1 == e.to.rep.x) {
                right = new OverlayImages(new LineImage(
                        new Posn(e.from.rep.x * SIZE + SIZE, 
                                e.from.rep.y * SIZE), 
                                new Posn(e.from.rep.x * SIZE + SIZE, 
                                        e.from.rep.y * SIZE + SIZE), new White()), right);
            }
            else if (e.from.rep.x - 1 == e.to.rep.x) {
                left = new OverlayImages(new LineImage(
                        new Posn(e.from.rep.x * SIZE, 
                                e.from.rep.y * SIZE), 
                                new Posn(e.from.rep.x * SIZE, 
                                        e.from.rep.y * SIZE + SIZE), new White()), left);
            }
            else if (e.from.rep.y + 1 == e.to.rep.y) {
                down = new OverlayImages(new LineImage(
                        new Posn(e.from.rep.x * SIZE, 
                                e.from.rep.y * SIZE + SIZE), 
                                new Posn(e.from.rep.x * SIZE + SIZE, 
                                        e.from.rep.y * SIZE + SIZE), new White()), down);
            }
            else if (e.from.rep.y - 1 == e.to.rep.y) {
                up = new OverlayImages(new LineImage(
                        new Posn(e.from.rep.x * SIZE, e.from.rep.y * SIZE),
                        new Posn(e.from.rep.x * SIZE + SIZE, e.from.rep.y * SIZE), 
                        new White()), up);
            } 
        }
        world = new OverlayImages(right, new OverlayImages(left, 
                new OverlayImages(down, new OverlayImages(up, world))));

        for (int i = 0; i < HEIGHT + 1; i++) {
            blueROW = new OverlayImages(new LineImage(new Posn(0, i * SIZE), 
                    new Posn(WIDTH * SIZE, i * SIZE), new Blue()), blueROW);
        } 

        for (int i = 0; i < WIDTH + 1; i++) {
            blueCOLUMN = new OverlayImages(new LineImage(new Posn(i * SIZE, 0), 
                    new Posn(i * SIZE, HEIGHT * SIZE), new Blue()), blueCOLUMN);
        } return new OverlayImages(blueROW, new OverlayImages(blueCOLUMN, world));
    }

    //draws out the maze with Player square and finish 
    public WorldImage makeImage() {
        return new OverlayImages(new TextImage(new Posn(SIZE * 2, HEIGHT * SIZE + SIZE / 2),
                "Manual: " + Integer.toString(numberOfMoves), Color.black),
                new OverlayImages(new TextImage(new Posn(SIZE * 8, HEIGHT * SIZE + SIZE / 2),
                        "Search Alg: " + Integer.toString(numberOfSearchMoves), Color.black),
                        new OverlayImages(visitedpath, 
                                new OverlayImages(searching, 
                                        new OverlayImages(answer, 
                                                new OverlayImages(new RectangleImage(
                                                        new Posn(WIDTH * SIZE - SIZE / 2,
                                                        HEIGHT * SIZE - SIZE / 2),
                                                        SIZE, SIZE, new Red()),
                                                        new OverlayImages(new RectangleImage(
                                                            new Posn(current.x * SIZE + SIZE / 2,
                                                                current.y * SIZE + SIZE / 2),
                                                                SIZE, SIZE, new Green()),
                                                                drawTheMaze())))))));
    }
    //reads in the key events to control the game play
    public void onKeyEvent(String ke) {
        MyPosn c = current;
        visited.add(current);

        if (ke.equals("up") && checkIfInEdge(new MyPosn(current.x, current.y - 1))) {
            c = new MyPosn(current.x, current.y - 1);
            visited.add(current);
            numberOfMoves++;
        }
        else if (ke.equals("down") && checkIfInEdge(new MyPosn(current.x, current.y + 1))) {
            c = new MyPosn(current.x, current.y + 1);
            visited.add(current);
            numberOfMoves++;
        }
        else if (ke.equals("left") && checkIfInEdge(new MyPosn(current.x - 1, current.y))) {
            c = new MyPosn(current.x - 1, current.y);
            visited.add(current);
            numberOfMoves++;
        }
        else if (ke.equals("right") && checkIfInEdge(new MyPosn(current.x + 1, current.y))) {
            c = new MyPosn(current.x + 1, current.y);
            visited.add(current);
            numberOfMoves++;
        }
        else if (ke.equals("r")) {
            current.x = 0;
            current.y = 0;
            initializeNodes();
            initializeReps();
            initializeEdges();
            kruskal();
            initializeNeighbor();
            visited = new ArrayList<Posn>();
            visitedpath = new CircleImage(new Posn(10, 10), 0, new Yellow());
            toggle = false;
            numberOfMoves = 0;
            numberOfSearchMoves = 0;
            b = false;
            d = false;
            answerHolder = new CircleImage(new Posn(10, 10), 0, new Yellow());
            answer = new CircleImage(new Posn(10, 10), 0, new Yellow());
            searching = new CircleImage(new Posn(10, 10), 0, new Yellow());
        }
        else if (ke.equals("t")) {
            if (toggle) {
                toggle = false;
            }
            else {
                toggle = true;
            }
            toggleView();
        }
        else if (ke.equals("b")) {
            if (b) {
                b = false;
                answer = new CircleImage(new Posn(10, 10), 0, new Yellow());
                searching = new CircleImage(new Posn(10, 10), 0, new Yellow());
                numberOfSearchMoves = 0;
            }
            else {
                bfs();
                b = true;
            }
        }
        else if (ke.equals("d")) {
            if (d) {
                d = false;
                answer = new CircleImage(new Posn(10, 10), 0, new Yellow());
                searching = new CircleImage(new Posn(10, 10), 0, new Yellow());
                numberOfSearchMoves = 0;
            }
            else {
                dfs();
                d = true;
            }
        }
        else {
            //do nothing
        }
        current = c;
    }
    //checks to see if the path the player wants to take is valid
    boolean checkIfInEdge(MyPosn goingTo) {
        for (Edge e : edgesInTree) {
            if ((e.from.rep.equals(current) && e.to.rep.equals(goingTo)) 
                    || (e.from.rep.equals(goingTo) && e.to.rep.equals(current))) {
                return true;
            }
        } return false;
    }
    //every tick creates a new image
    public void onTick() {
        makeImage();
        if (b) {
            convertAlreadySeen();
        }
        else if (d) {
            convertAlreadySeen();
        } 
    }
    //ends the game when the conditions are met
    public WorldEnd worldEnds() {
        if (current.equals(new MyPosn(WIDTH - 1, HEIGHT - 1))) {
            return new WorldEnd(true, new OverlayImages(this.makeImage(), 
                    new TextImage(new Posn(WIDTH * SIZE / 2, HEIGHT * SIZE / 2),
                            "YOU WIN!", Color.black)));
        } 
        else {
            return new WorldEnd(false, this.makeImage());
        }
    }
    //shows the visited path
    void toggleView() {
        if (toggle) {
            for (Posn p : visited) {
                visitedpath = new OverlayImages(new RectangleImage(new Posn(p.x * SIZE + SIZE / 2,
                        p.y * SIZE + SIZE / 2),
                        SIZE, SIZE, new Yellow()), visitedpath);
            }
        }
        else {
            visitedpath = new CircleImage(new Posn(10, 10), 0, new Yellow());
        }
    }









    //EFFECT: initializes the neighbors for each node
    void initializeNeighbor() {
        for (Edge e : edgesInTree) {
            e.from.neighbor.add(e.to);
            e.to.neighbor.add(e.from);
        }
        start = allNodes.get(0).get(0);
    }












    //Breath-First-Search
    void bfs() {
        searchHelp(new Queue<Node>());
    }
    //Depth-First-Search
    void dfs() {
        searchHelp(new Stack<Node>());
    }
    //EFFECT: searchworklist is changed as it goes
    //EFFECT: cameFromEdge is built up
    //EFFECT: alreadySeen is built up
    //Algorithm for different types of searches
    void searchHelp(ICollection<Node> searchworklist) {
        alreadySeen = new Deque<Node>();
        cameFromEdge = new HashMap<MyPosn, Edge>();

        // Initialize the worklist with the from Node
        searchworklist.add(start);
        // As long as the worklist isn't empty...
        while (!searchworklist.isEmpty()) {
            Node next = searchworklist.remove();
            if (next.rep.equals(new MyPosn(WIDTH - 1, HEIGHT - 1))) {
                reconstruct(cameFromEdge, next); // Success!
                break;
            }
            else if (alreadySeen.contains(next)) {
                // do nothing: we've already seen this one
            }
            else {
                // add all the neighbors of next to the worklist for further processing
                for (Node e : next.neighbor) {
                    searchworklist.add(e);
                    if (!alreadySeen.contains(e)) {
                        cameFromEdge.put(e.rep, new Edge(next, e, 1));
                    }
                }
                // add next to alreadySeen, since we're done with it
                alreadySeen.addAtTail(next);
            }
        }
    }
    //EFFECT: builds up the answerHolder WorldImage
    //Gets the shortest/final path by going backwards
    void reconstruct(HashMap<MyPosn, Edge> cfe, Node next) {
        Node current = next;
        answerHolder = new OverlayImages(new RectangleImage(new Posn(SIZE / 2, SIZE / 2), 
                SIZE, SIZE, Color.LIGHT_GRAY), answerHolder);
        while (!current.sameNode(start)) {
            answerHolder = new OverlayImages(new RectangleImage(
                    new Posn(current.rep.x * SIZE + SIZE / 2, current.rep.y * SIZE + SIZE / 2),
                    SIZE, SIZE, Color.LIGHT_GRAY),
                    answerHolder);
            current = cfe.get(current.rep).from;
        }
    }
    //EFFECT: converts alreadySeen into a worldImage
    void convertAlreadySeen() {
        if (!this.alreadySeen.isEmpty()) {
            Node n = this.alreadySeen.removeFromHead();
            int x = n.rep.x;
            int y = n.rep.y;
            numberOfSearchMoves++;
            searching = new OverlayImages(new RectangleImage(new Posn(x * SIZE + SIZE / 2,
                    y * SIZE + SIZE / 2), 
                    SIZE, SIZE,
                    Color.gray), searching);
        }
        else {
            answer = answerHolder;
        }
    }

}    






















//comparator class to sort
class ByNumber implements Comparator<Edge> {
    public int compare(Edge e1, Edge e2) {
        if (e1.weight < e2.weight) {
            return 1;
        } 
        else {
            return -1;
        }
    }
}

//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
// Represents a mutable collection of items
interface ICollection<T> {
    // Is this collection empty?
    boolean isEmpty();
    // EFFECT: adds the item to the collection
    void add(T item);
    // Returns the first item of the collection
    // EFFECT: removes that first item
    T remove();
}
//represents the class Stack
class Stack<T> implements ICollection<T> {
    //variable
    Deque<T> contents;
    //constructor
    Stack() {
        this.contents = new Deque<T>();
    }
    // Is this collection empty?
    public boolean isEmpty() {
        return this.contents.isEmpty();
    }
    // Returns the first item of the collection
    // EFFECT: removes that first item
    public T remove() {
        return this.contents.removeFromHead();
    }
    // EFFECT: adds the item to the collection
    public void add(T item) {
        this.contents.addAtHead(item);
    }
}
//represents the class Queue
class Queue<T> implements ICollection<T> {
    //variable
    Deque<T> contents;
    //constructor
    Queue() {
        this.contents = new Deque<T>();
    }
    // Is this collection empty?
    public boolean isEmpty() {
        return this.contents.isEmpty();
    }
    // Returns the first item of the collection
    // EFFECT: removes that first item
    public T remove() {
        return this.contents.removeFromHead();
    }
    // EFFECT: adds the item to the collection
    public void add(T item) {
        this.contents.addAtTail(item); // NOTE: Different from Stack!
    }
}
//represents the class Deque
class Deque<T> {
    //variables
    Sentinel<T> header;
    //constructor
    Deque() {
        this.header = new Sentinel<T>();
    }
    //contrsuctor
    Deque(Sentinel<T> head) {
        this.header = head;
    }
    //EFFECT: consumes a value of type T and inserts it at the front of the list.
    void addAtHead(T t) {
        this.header.next.add(t, true);
    }
    //EFFECT: consumes a value of type T and inserts it at the end of the list.
    void addAtTail(T t) {
        this.header.prev.add(t, false);
    }
    //EFFECT: removes the first node from this Deque.
    T removeFromHead() {
        return this.header.next.remove();
    }
    //is the Deque empty?
    boolean isEmpty() {
        return this.header.next.isEmpty();
    }
    //is this Node in this Deque
    boolean contains(T that) {
        return this.header.next.contains(that);
    }
}
// represents the abstract class ANode
abstract class ANode<T> {
    // variables
    ANode<T> next;
    ANode<T> prev;
    // constructor
    ANode() {
        //empty
    }
    // constructor
    ANode(ANode<T> next, ANode<T> prev) {
        this.next = next;
        this.prev = prev;
    }

    //EFFECT: adds the given T as a node at the head or tail
    abstract void add(T t, boolean athead);
    //EFFECT: removes the node from the head or tail
    abstract T remove();
    //is this empty?
    abstract boolean isEmpty();
    //is this the given Node
    abstract boolean contains(T that);
}
// represents the class Sentinel
class Sentinel<T> extends ANode<T> {
    // constructor
    Sentinel() {
        this.next = this;
        this.prev = this;
    }
    // constructor
    Sentinel(ANode<T> next, ANode<T> prev) {
        this.next = next;
        this.prev = prev;
    }

    //EFFECT: consumes a value of type T and inserts it at the front of the list.
    void add(T t, boolean athead) {
        new DNode<T>(t, this, this);
    }
    //removes the first/last node from this Deque.
    T remove() {
        throw new RuntimeException("Can't remove from empty list");
    }
    //is this empty?
    boolean isEmpty() {
        return true;
    }
    //is this the given Node
    boolean contains(T that) {
        return false;
    }
}
// represents the class DNode
class DNode<T> extends ANode<T> {
    // variable
    T data;
    // constructor
    DNode(T data) {
        this.data = data;
        this.next = null;
        this.prev = null;
    }
    // constructor
    DNode(T data, ANode<T> next, ANode<T> prev) {
        this.data = data;
        this.next = next;
        this.prev = prev;
        if (next == null) {
            new IllegalArgumentException("Invalid next node");
        }
        else {
            this.next.prev = this;
        }
        if (prev == null) {
            new IllegalArgumentException("Invalid prev node");
        }
        else {
            this.prev.next = this;
        }
    }

    //EFFECT: adds the given T as a node at the head or tail
    void add(T t, boolean athead) {
        if (athead) {
            new DNode<T>(t, this, this.prev);
        }
        else {
            new DNode<T>(t, this.next, this);
        }
    }
    //EFFECT: removes the node from the head or tail
    T remove() {
        this.prev.next = this.next;
        this.next.prev = this.prev;
        return this.data;
    }
    //is this empty?
    boolean isEmpty() {
        return false;
    }
    //is this the given Node
    boolean contains(T that) {
        Node tthis = (Node) this.data;
        Node tthat = (Node) that;
        if (tthis.sameNode(tthat)) {
            return true;
        }
        else {
            return this.next.contains(that);
        }
    }
}

//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

//represents an iterator for IList<T>
class IListIterator<T> implements Iterator<T> {
    // variable
    IList<T> items;
    // constructor
    IListIterator(IList<T> items) {
        this.items = items;
    }
    // checks to see if list has a next
    public boolean hasNext() {
        return this.items.isCons();
    }
    // gets the next element in the list
    public T next() {
        ConsList<T> itemsAsCons = this.items.asCons();
        T answer = itemsAsCons.first;
        this.items = itemsAsCons.rest;
        return answer;
    }
    // remove function not used
    public void remove() {
        // TODO Auto-generated method stub

    }
}




//interface IList<T>
interface IList<T> extends Iterable<T> {
    //checks to see if this IList is a ConsList
    boolean isCons();
    //sets this IList as a ConsList
    ConsList<T> asCons();
}
//represents the MtList<T> 
class MtList<T> implements IList<T> {
    //make an iterator out of this
    public Iterator<T> iterator() {
        return new IListIterator<T>(this);
    }
    //is this a Cons?
    public boolean isCons() {
        return false;
    }
    //this MtList is not a Cons, so throw exception
    public ConsList<T> asCons() {
        throw new RuntimeException("This list is not a Cons!");
    }
}
//represents the ConsList<T>
class ConsList<T> implements IList<T> {
    //variables
    T first;
    IList<T> rest;
    //constructor
    ConsList(T first, IList<T> rest) {
        this.first = first;
        this.rest = rest;
    }
    //make an iterator out of this
    public Iterator<T> iterator() {
        return new IListIterator<T>(this);
    }
    //is this a Cons?
    public boolean isCons() {
        return true;
    }
    //return as Cons
    public ConsList<T> asCons() {
        return this;
    }
}

//represents the class ExamplesMaze
class ExamplesMaze {
  // UNCOMMENT BELOW TO RUN GAME  
  boolean testRunGame(Tester t) {
      setup();
      this.graph.bigBang(Graph.WIDTH*Graph.SIZE,
                          Graph.HEIGHT*Graph.SIZE+Graph.SIZE, 0.01);
      return true;
    }
    
    MyPosn p1 = new MyPosn(3, 7);
    MyPosn p2 = new MyPosn(7, 3);
    MyPosn p3 = new MyPosn(7, 7);
    MyPosn p4 = new MyPosn(1, 1);
    MyPosn p5 = new MyPosn(3, 2);
    MyPosn p6 = new MyPosn(3, 3);
    MyPosn p7 = new MyPosn(3, 2);
    Node n1 = new Node(p1);
    Node n2 = new Node(p2);
    Node n3 = new Node(p3);
    Node n4 = new Node(p4);
    Node n5 = new Node(p5);
    Node n6 = new Node(p6);
    Node n7 = new Node(p7);
    Edge e1 = new Edge(n1, n2, 1);
    Edge e2 = new Edge(n2, n3, 1);
    Edge e3 = new Edge(n3, n4, 1);
    Edge e4 = new Edge(n4, n5, 1);
    HashMap<MyPosn, MyPosn> hm = new HashMap<MyPosn, MyPosn>();
    ArrayList<Edge> ed = new ArrayList<Edge>();

    Graph graph;
    void setup() {
        graph = new Graph();
        graph.initializeNodes();
        graph.initializeReps();
        graph.initializeEdges();
        graph.kruskal();
        graph.initializeNeighbor();
    }

    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    //setup for find to create hashmap
    void forfind() {
        hm = new HashMap<MyPosn, MyPosn>();
        hm.put(p1, p2);
        hm.put(p2, p2);
        hm.put(p3, p3);
        hm.put(p4, p5);
        hm.put(p5, p5);
        hm.put(p6, p6);
    }
    //setup to create arraylist of edges
    void foredge() {
        ed = new ArrayList<Edge>();
        e1 = new Edge(n1, n2, 1);
        e2 = new Edge(n2, n3, 1);
        e3 = new Edge(n3, n4, 1);
        e4 = new Edge(n4, n5, 1);
        ed.add(e1);
        ed.add(e2);
        ed.add(e3);
        ed.add(e4);
    }
    //test to check if MyPosn equal
    boolean testMyPosnEqual(Tester t) {
        return t.checkExpect(this.p1.equals(p1), true)
                && t.checkExpect(this.p1.equals(p2), false)
                && t.checkExpect(this.p2.equals(p3), false)
                && t.checkExpect(this.p3.equals(p1), false)
                && t.checkExpect(this.p4.equals(p4), true)
                && t.checkExpect(this.p5.equals(p6), false)
                && t.checkExpect(this.p5.equals(p7), true)
                && t.checkExpect(this.p5.equals(p4), false)
                && t.checkExpect(this.p7.equals(p7), true)
                && t.checkExpect(this.p7.equals(p5), true)
                && t.checkExpect(this.p7.equals(p4), false);
    }
    //test to MyPosn to String conversion
    boolean testMyPosnToString(Tester t) {
        return t.checkExpect(this.p1.toString(), "(3,7)")
                && t.checkExpect(this.p2.toString(), "(7,3)")
                && t.checkExpect(this.p3.toString(), "(7,7)")
                && t.checkExpect(this.p4.toString(), "(1,1)")
                && t.checkExpect(this.p5.toString(), "(3,2)")
                && t.checkExpect(this.p6.toString(), "(3,3)");
    }
    //test sameNode
    boolean testSameNode(Tester t) {
        return t.checkExpect(this.n1.sameNode(n1), true)
                && t.checkExpect(this.n2.sameNode(n1), false)
                && t.checkExpect(this.n2.sameNode(n3), false)
                && t.checkExpect(this.n3.sameNode(n1), false)
                && t.checkExpect(this.n4.sameNode(n1), false)
                && t.checkExpect(this.n5.sameNode(n6), false)
                && t.checkExpect(this.n6.sameNode(n5), false)
                && t.checkExpect(this.n4.sameNode(n4), true)
                && t.checkExpect(this.n4.sameNode(n2), false);
    }
    //test find
    boolean testfind(Tester t) {
        forfind();
        return t.checkExpect(n2.find(hm), p2) &&
                t.checkExpect(n1.find(hm), p2) &&
                t.checkExpect(n3.find(hm), p3) &&
                t.checkExpect(n4.find(hm), p5) &&
                t.checkExpect(n5.find(hm), p5) &&
                t.checkExpect(n6.find(hm), p6);
    }
    //test initializeNodes
    void testinitializeNodes(Tester t) {
        graph = new Graph();
        t.checkExpect(graph.allNodes.size(), 0);
        graph.initializeNodes();
        t.checkExpect(graph.allNodes.size(), Graph.HEIGHT);
        t.checkExpect(graph.allNodes.get(0).size(), Graph.WIDTH);
    }
    //test initialize Reps
    void testinitializeReps(Tester t) {
        graph = new Graph();
        t.checkExpect(graph.representative.size(), 0);
        graph.initializeNodes();
        graph.initializeReps();
        t.checkExpect(graph.representative.size(), Graph.WIDTH * Graph.HEIGHT);
    }
    //test initializeEdges
    void testinitializeEdges(Tester t) {
        graph = new Graph();
        t.checkExpect(graph.worklist.size(), 0);
        graph.initializeNodes();
        graph.initializeReps();
        graph.initializeEdges();
        t.checkExpect(graph.worklist.get(0).weight <= graph.worklist.get(1).weight, true);
        t.checkExpect(graph.worklist.get(1).weight <= graph.worklist.get(1).weight, true);
    }
    //test kurskal
    void testKruskal(Tester t) {
        graph = new Graph();
        t.checkExpect(graph.edgesInTree.size(), 0);
        t.checkExpect(graph.edgesInTree.isEmpty(), true);
        graph.initializeNodes();
        graph.initializeReps();
        graph.initializeEdges();
        graph.kruskal();
        t.checkExpect(graph.edgesInTree.size(), Graph.WIDTH * Graph.HEIGHT - 1);
        t.checkExpect(graph.edgesInTree.isEmpty(), false);
    }
    //test union
    void testUnion(Tester t) {
        graph = new Graph();
        forfind();
        t.checkExpect(n2.find(hm), p2);
        t.checkExpect(n1.find(hm), p2);
        t.checkExpect(n3.find(hm), p3);
        t.checkExpect(n4.find(hm), p5);
        t.checkExpect(n5.find(hm), p5);
        t.checkExpect(n6.find(hm), p6);
        t.checkExpect(n7.find(hm), p7);
        graph.union(hm, n2, n3);
        t.checkExpect(n1.find(hm), p3);
        t.checkExpect(n2.find(hm), p3);
        t.checkExpect(n3.find(hm), p3);
        t.checkExpect(n4.find(hm), p5);
        t.checkExpect(n5.find(hm), p5);
        t.checkExpect(n6.find(hm), p6);
        t.checkExpect(n7.find(hm), p7);
        graph.union(hm, n6, n7);
        t.checkExpect(n1.find(hm), p3);
        t.checkExpect(n2.find(hm), p3);
        t.checkExpect(n3.find(hm), p3);
        t.checkExpect(n4.find(hm), p5);
        t.checkExpect(n5.find(hm), p5);
        t.checkExpect(n6.find(hm), p7);
        t.checkExpect(n7.find(hm), p7);
        graph.union(hm, n3, n4);
        t.checkExpect(n1.find(hm), p5);
        t.checkExpect(n2.find(hm), p5);
        t.checkExpect(n3.find(hm), p5);
        t.checkExpect(n4.find(hm), p5);
        t.checkExpect(n5.find(hm), p5);
        t.checkExpect(n6.find(hm), p7);
        t.checkExpect(n7.find(hm), p7);
    }
    //test checkIfInEdge
    void testcheckIfInEdge(Tester t) {
        graph = new Graph();
        foredge();
        graph.current = new MyPosn(3, 7);
        graph.edgesInTree = ed;
        t.checkExpect(graph.checkIfInEdge(p1), false);
        t.checkExpect(graph.checkIfInEdge(p2), true); 
        graph.current = new MyPosn(7, 7);
        t.checkExpect(graph.checkIfInEdge(p3), false);
        t.checkExpect(graph.checkIfInEdge(p4), true);
        
    }
    //test initilizeNeighbor
    void testinitilizeNeighbor(Tester t) {
        graph = new Graph();
        graph.initializeNodes();
        graph.initializeReps();
        graph.initializeEdges();
        graph.kruskal();
        //before start.neighbor is null
        graph.initializeNeighbor();
        t.checkExpect(graph.start.neighbor.size() > 0, true);

    }
    //test searchHelp
    void testsearchHelp(Tester t) {
        graph = new Graph();
        graph.initializeNodes();
        graph.initializeReps();
        graph.initializeEdges();
        graph.kruskal();
        graph.initializeNeighbor();
        t.checkExpect(graph.alreadySeen.isEmpty(), true);
        graph.searchHelp(new Stack<Node>());
        t.checkExpect(graph.alreadySeen.isEmpty(), false);
    }

 // tests for Deques
    // create all data
    Sentinel<String> sentinel1 = new Sentinel<String>();

    Deque<String> deque1 = new Deque<String>();
    Sentinel<String> sentinel2 = new Sentinel<String>();
    ANode<String> abc = new DNode<String>("abc");
    ANode<String> bcd = new DNode<String>("bcd");
    ANode<String> cde = new DNode<String>("cde");
    ANode<String> def = new DNode<String>("def");
    Deque<String> deque2 = new Deque<String>();

    Sentinel<String> sentinel3 = new Sentinel<String>();
    ANode<String> shark = new DNode<String>("shark");
    ANode<String> dog = new DNode<String>("dog");
    ANode<String> cat = new DNode<String>("cat");
    ANode<String> monkey = new DNode<String>("monkey");
    Deque<String> deque3 = new Deque<String>();
    
    Sentinel<String> sentinel4 = new Sentinel<String>();
    ANode<String> banana = new DNode<String>("banana");
    Deque<String> deque4 = new Deque<String>();
    
    Sentinel<Node> sentinel5 = new Sentinel<Node>();
    ANode<Node> an1 = new DNode<Node>(n1);
    ANode<Node> an2 = new DNode<Node>(n2);
    Deque<Node> deque5 = new Deque<Node>();

    // method to clear the data
    void clear() {
        this.sentinel1 = new Sentinel<String>();
        this.deque1 = new Deque<String>();

        this.sentinel2 = new Sentinel<String>();
        this.abc = new DNode<String>("abc");
        this.bcd = new DNode<String>("bcd");
        this.cde = new DNode<String>("cde");
        this.def = new DNode<String>("def");
        this.deque2 = new Deque<String>();

        this.sentinel3 = new Sentinel<String>();
        this.shark = new DNode<String>("shark");
        this.dog = new DNode<String>("dog");
        this.cat = new DNode<String>("cat");
        this.monkey = new DNode<String>("monkey");
        this.deque3 = new Deque<String>();
        
        this.sentinel4 = new Sentinel<String>();
        this.banana = new DNode<String>("banana");
        this.deque4 = new Deque<String>();
        
        this.sentinel5 = new Sentinel<Node>();
        an1 = new DNode<Node>(n1);
        an2 = new DNode<Node>(n2);
        deque5 = new Deque<Node>();
    }
    // method to initialize the data
    void initialize() {
        this.deque1 = new Deque<String>(this.sentinel1);

        this.sentinel2 = new Sentinel<String>();
        this.abc = new DNode<String>("abc", this.bcd, this.sentinel2);
        this.bcd = new DNode<String>("bcd", this.cde, this.abc);
        this.cde = new DNode<String>("cde", this.def, this.bcd);
        this.def = new DNode<String>("def", this.sentinel2, this.cde);
        this.deque2 = new Deque<String>(this.sentinel2);

        this.sentinel3 = new Sentinel<String>();
        this.shark = new DNode<String>("shark", this.dog, this.sentinel3);
        this.dog = new DNode<String>("dog", this.cat, this.shark);
        this.cat = new DNode<String>("cat", this.monkey, this.dog);
        this.monkey = new DNode<String>("monkey", this.sentinel3, this.cat);
        this.deque3 = new Deque<String>(this.sentinel3);
        
        this.sentinel4 = new Sentinel<String>();
        this.banana = new DNode<String>("banana", this.sentinel4, this.sentinel4);
        this.deque4 = new Deque<String>(this.sentinel4);
        
        this.sentinel5 = new Sentinel<Node>();
        this.an1 = new DNode<Node>(n1, this.an2, this.sentinel5);
        this.an2 = new DNode<Node>(n2, this.sentinel5, this.an1);
        this.deque5 = new Deque<Node>(this.sentinel5);
    }
    // test add at head method
    boolean testAddAtHead(Tester t) {
        clear();
        initialize();
        deque1.addAtHead("abc");
        deque2.addAtHead("hello");
        return t.checkExpect(this.deque1.header.next, new DNode<String>("abc", 
                   this.sentinel1, this.sentinel1))
            && t.checkExpect(this.deque2.header.next, new DNode<String>("hello", 
                   this.abc, this.sentinel2));
    }
    // test add at tail method
    boolean testAddAtTail(Tester t) {
        clear();
        initialize();
        deque1.addAtTail("xyz");
        deque2.addAtTail("zzz");
        return t.checkExpect(this.deque1.header.prev, new DNode<String>("xyz", 
                   this.sentinel1, this.sentinel1))
            && t.checkExpect(this.deque2.header.prev, new DNode<String>("zzz", 
                   this.sentinel2, this.def));
    }
    // test remove from head method
    boolean testRemoveFromHead(Tester t) {
        clear();
        initialize();
        return t.checkExpect(this.deque2.removeFromHead(), "abc")
            && t.checkExpect(this.sentinel2.next, this.bcd)
            && t.checkExpect(this.deque3.removeFromHead(), "shark")
            && t.checkExpect(this.sentinel3.next, this.dog);
    }
    // test is Empty method
    boolean testisEmpty(Tester t) {
        clear();
        initialize();
        return t.checkExpect(this.deque1.isEmpty(), true)
            && t.checkExpect(this.deque2.isEmpty(), false)
            && t.checkExpect(this.deque3.isEmpty(), false)
            && t.checkExpect(this.deque4.isEmpty(), false);
    }
    // test contains method
    boolean testContains(Tester t) {
        clear();
        initialize();
        return t.checkExpect(this.deque1.contains("hi"), false)
            && t.checkExpect(this.deque5.contains(n1), true)
            && t.checkExpect(this.deque5.contains(n2), true)
            && t.checkExpect(this.deque5.contains(n3), false)
            && t.checkExpect(this.deque5.contains(n4), false);
    }
    
    // Tests for IListIterator
    IList<Node> empty = new MtList<Node>();
    IList<Node> l1 = new ConsList<Node>(n1, empty);
    IList<Node> l2 = new ConsList<Node>(n1, new ConsList<Node>(n2, empty));
    IListIterator<Node> emptyIterator = new IListIterator<Node>(empty);
    IListIterator<Node> iterator1 = new IListIterator<Node>(l1);
    IListIterator<Node> iterator2 = new IListIterator<Node>(l2);
    
    void initializeIterators() {
        l1 = new ConsList<Node>(n1, empty);
        l2 = new ConsList<Node>(n1, new ConsList<Node>(n2, empty));
        emptyIterator = new IListIterator<Node>(empty);
        iterator1 = new IListIterator<Node>(l1);
        iterator2 = new IListIterator<Node>(l2);
    }
    void testhasNext(Tester t) {
        initializeIterators();
        t.checkExpect(emptyIterator.hasNext(), false);
        t.checkExpect(iterator1.hasNext(), true);
        t.checkExpect(iterator2.hasNext(), true);
        iterator1.next();
        t.checkExpect(iterator1.hasNext(), false);
        iterator2.next();
        t.checkExpect(iterator2.hasNext(), true);
        iterator2.next();
        t.checkExpect(iterator2.hasNext(), false);    
    }
    void testNext(Tester t) {
        initializeIterators();
        t.checkExpect(iterator1.next(), n1);
        t.checkExpect(iterator2.next(), n1);
        t.checkExpect(iterator2.next(), n2);
    }
    
    // Tests for IList
    void testIsCons(Tester t) {
        initializeIterators();
        t.checkExpect(this.empty.isCons(), false);
        t.checkExpect(this.l1.isCons(), true);
        t.checkExpect(this.l2.isCons(), true);
    }
    void testAsCons(Tester t) {
        initializeIterators();
        t.checkExpect(this.l1.asCons(), this.l1);
        t.checkExpect(this.l2.asCons(), this.l2);
    }

}

