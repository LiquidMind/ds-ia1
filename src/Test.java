import java.util.HashSet;


public class Test {
  static HashSet<Integer> hs = new HashSet<Integer>();
  
  static void multicast(Integer a) {
    String out = "";
    
    System.out.println("One");
    for (Integer j : hs) {
      if (j.hashCode() == a.hashCode()) {
        // do nothing
      } else {
        out += "Two: " + j + "\n";
      }
    }
    System.out.print(out);
    System.out.println("Three");
  }
  
  public static void main(String[] args) {
    for (int i = 1; i <= 10; i++) {
      hs.add(i);
    } 

    Test.multicast(4);
  }

}
