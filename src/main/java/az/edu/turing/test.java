package az.edu.turing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class test {

    public static void main(String[] args) {

        List<String> list=new ArrayList<>();
        list.add("a");
        list.add("t");
        list.add("r");
        Collections.rotate(list,1);
        System.out.println(list);

    }
}
